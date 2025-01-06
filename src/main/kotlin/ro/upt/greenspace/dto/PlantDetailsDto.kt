package ro.upt.greenspace.dto

import com.fasterxml.jackson.annotation.JsonProperty
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import ro.upt.greenspace.entity.Plant

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
data class PlantDetailsDto(
    @JsonProperty("type") val type: String?,
    @JsonProperty("family") val family: String?,
    @JsonProperty("water") val water: String?,
    @JsonProperty("light") val light: String?,
    @JsonProperty("suggestion") val suggestion: String?
) {
    fun addToPlant(plant: Plant) {
        plant.type = type ?: plant.type
        plant.family = family ?: plant.family
        plant.water = water ?: plant.water
        plant.light = light ?: plant.light
        plant.suggestion = suggestion ?: plant.suggestion
    }
}