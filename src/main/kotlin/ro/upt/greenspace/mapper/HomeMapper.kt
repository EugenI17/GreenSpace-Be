package ro.upt.greenspace.mapper

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import ro.upt.greenspace.dto.HomeDto
import ro.upt.greenspace.entity.Home
import ro.upt.greenspace.repository.PlantRepository

@Service
class HomeMapper {

    @Autowired
    private lateinit var plantRepository: PlantRepository

    fun toEntity(homeDto: HomeDto): Home {
        val plants = homeDto.plants?.map { plantId ->
            plantRepository.findById(plantId).orElseThrow { IllegalArgumentException("Plant not found for ID: $plantId") }
        }?.toMutableList()
        return Home(
            name = homeDto.name,
            city = homeDto.city,
            plants = plants?: mutableListOf()
        )
    }

    fun toDto(home: Home): HomeDto {
        return HomeDto(
            id = home.id,
            name = home.name,
            city = home.city,
            plants = home.plants.map { it.id!! }
        )
    }
}