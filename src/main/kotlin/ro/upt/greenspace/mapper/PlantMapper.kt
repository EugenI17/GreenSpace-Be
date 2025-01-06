package ro.upt.greenspace.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.upt.greenspace.dto.PlantDto
import ro.upt.greenspace.entity.Plant
import ro.upt.greenspace.repository.HomeRepository

@Service
class PlantMapper() {

    @Autowired
    private lateinit var roomRepository: HomeRepository

    fun toEntity(plantDto: PlantDto, image: ByteArray): Plant {
        val homeId = plantDto.home ?: throw IllegalArgumentException("Home id is required")
        return Plant(
            name = plantDto.name,
            home = roomRepository.findById(homeId).orElseThrow { IllegalArgumentException("Room not found") },
            type = plantDto.type?: "",
            family = plantDto.family?: "",
            water = plantDto.water?: "",
            light = plantDto.light?: "",
            image = image,
            suggestion = plantDto.suggestion?: ""

        )
    }

    fun toDto(plant: Plant): PlantDto {
        return PlantDto(
            id = plant.id,
            name = plant.name,
            home = plant.home.id,
            type = plant.type,
            family = plant.family,
            water = plant.water,
            light = plant.light,
            image = plant.image,
            suggestion = plant.suggestion
        )
    }
}