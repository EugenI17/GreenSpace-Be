package ro.upt.greenspace.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.upt.greenspace.dto.RoomDto
import ro.upt.greenspace.entity.Plant
import ro.upt.greenspace.entity.Room
import ro.upt.greenspace.repository.PlantRepository

@Service
class RoomMapper {

    @Autowired
    private lateinit var plantRepository: PlantRepository

    fun toEntity(roomDto: RoomDto): Room {
        val plants = roomDto.plants.map { plantId ->
            plantRepository.findById(plantId).orElseThrow { IllegalArgumentException("Plant not found for ID: $plantId") }
        }.toMutableList()
        return Room(
            name = roomDto.name,
            plants = plants
        )
    }

    fun toDto(room: Room): RoomDto {
        return RoomDto(
            id = room.id,
            name = room.name,
            plants = room.plants.map { it.id!! }
        )
    }
}