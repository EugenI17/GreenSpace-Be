package ro.upt.greenspace.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.upt.greenspace.dto.PlantDto
import ro.upt.greenspace.entity.Plant
import ro.upt.greenspace.repository.RoomRepository

@Service
class PlantMapper() {

    @Autowired
    private lateinit var roomRepository: RoomRepository

    fun toEntity(plantDto: PlantDto): Plant {
        val roomId = plantDto.room ?: throw IllegalArgumentException("Room id is required")
        return Plant(
            name = plantDto.name,
            room = roomRepository.findById(roomId).orElseThrow { IllegalArgumentException("Room not found") }

        )
    }

    fun toDto(plant: Plant): PlantDto {
        return PlantDto(
            id = plant.id,
            name = plant.name,
            room = plant.room.id
        )
    }
}