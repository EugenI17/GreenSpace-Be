package ro.upt.greenspace.service

import org.springframework.core.io.ByteArrayResource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.util.UriComponentsBuilder
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import ro.upt.greenspace.dto.PlantDetailsDto
import ro.upt.greenspace.entity.Plant
import ro.upt.greenspace.repository.PlantRepository

@Service
class PlantService(private val webClient: WebClient.Builder) {

    @Autowired
    private lateinit var plantRepository: PlantRepository

    private val plantNetApiUrl = "https://my-api.plantnet.org/v2/identify/all"
    @Value("\${tokens.plant-net}")
    private val plantNetApiKey = ""

    private val chatGptApiUrl = "https://api.openai.com/v1/chat/completions"
    @Value("\${tokens.open-ai}")
    private val chatGptApiKey = ""

    fun identifyPlant(image: ByteArray): String {
        val uri = UriComponentsBuilder.fromHttpUrl(plantNetApiUrl)
            .queryParam("api-key", plantNetApiKey)
            .build()
            .toUri()

        val body = org.springframework.util.LinkedMultiValueMap<String, Any>().apply {
            this.add("images", object : ByteArrayResource(image) {
                override fun getFilename(): String = "plant_image.png"
            })
        }

        val response = webClient.build()
            .method(HttpMethod.POST)
            .uri(uri)
            .header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
            .bodyValue(body)
            .retrieve()
            .bodyToMono(String::class.java)
            .block()

        val responseMap: Map<String, Any> = ObjectMapper().readValue(response, Map::class.java) as Map<String, Any>

        val results = responseMap["results"] as? List<Map<String, Any>>
            ?: throw IllegalStateException("No results found in the response")

        val bestResult = results.maxByOrNull { (it["score"] as? Number)?.toDouble() ?: 0.0 }
            ?: throw IllegalStateException("Failed to determine the best result")

        val species = bestResult["species"] as? Map<String, Any>
            ?: throw IllegalStateException("Species information is missing")

        return species["scientificNameWithoutAuthor"] as? String
            ?: throw IllegalStateException("Scientific name is missing")

    }


    fun getPlantDetails(plantType: String): PlantDetailsDto {

        val previousPlants = plantRepository.findAll().map { plant ->
            PlantDetailsDto(
                type = plant.type,
                family = plant.family,
                water = plant.water,
                light = plant.light,
                suggestion = null // We'll compute this dynamically later
            )
        }

        val prompt = """
                Provide detailed care instructions for the plant "$plantType".please be explicit with the water like in this example: "2 cups of water every week" but keep the water and light indications short max 6 words". 
                Format the response as a JSON object with the following fields:
                {
                    "type": "The plant type",
                    "family": "The plant family",
                    "water": "Watering instructions",
                    "light": "Light requirements",
                    "suggestion": "Placement suggestion based on the following existing plants"
                }.
                Suggest where this plant should be placed based on the following existing plants if there are any, else put "N/A".:
                ${previousPlants.joinToString(separator = "\n") { "- ${it.type}: Light=${it.light}, Water=${it.water}" }}
            """.trimIndent()

        val requestPayload = mapOf(
            "model" to "gpt-4",
            "messages" to listOf(
                mapOf("role" to "system", "content" to "You are a plant care assistant."),
                mapOf("role" to "user", "content" to prompt)
            ),
            "max_tokens" to 150,
            "temperature" to 0.7
        )

        val response = webClient.build()
            .post()
            .uri(chatGptApiUrl)
            .header("Authorization", chatGptApiKey)
            .header("Content-Type", "application/json")
            .bodyValue(requestPayload)
            .retrieve()
            .bodyToMono(Map::class.java)
            .block()

        val choices = response?.get("choices") as? List<Map<String, Any>>
            ?: throw IllegalStateException("No choices returned in the response: $response")
        val message = choices.firstOrNull()?.get("message") as? Map<String, Any>
            ?: throw IllegalStateException("No message found in choices: $response")
        val content = message["content"] as? String
            ?: throw IllegalStateException("No content found in the message: $response")

        val cleanContent = content
            .replace("```json", "")
            .replace("```", "")
            .trim()

        return try {
            val objectMapper = ObjectMapper().registerModule(com.fasterxml.jackson.module.kotlin.KotlinModule())
            objectMapper.readValue(cleanContent, PlantDetailsDto::class.java)
        } catch (e: Exception) {
            throw IllegalStateException("Error mapping JSON to PlantDetailsDto: $content", e)
        }
    }

    fun addDetails(plant: Plant): Plant {
        val plantType = identifyPlant(plant.image)
        val plantDetails: PlantDetailsDto = getPlantDetails(plantType)
        plantDetails.addToPlant(plant)
        return plant
    }

}