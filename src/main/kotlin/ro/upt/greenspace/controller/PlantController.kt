package ro.upt.greenspace.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.upt.greenspace.dto.PlantDto
import ro.upt.greenspace.dto.RoomDto
import ro.upt.greenspace.entity.Plant
import ro.upt.greenspace.mapper.PlantMapper
import ro.upt.greenspace.mapper.RoomMapper
import ro.upt.greenspace.repository.PlantRepository
import ro.upt.greenspace.repository.RoomRepository

@RestController
@RequestMapping("/api/v1/plants")
class PlantController {

    @Autowired
    private lateinit var plantMapper: PlantMapper

    @Autowired
    private lateinit var plantRepository: PlantRepository

    @GetMapping
    fun getAll(): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(plantRepository.findAll())
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @GetMapping("/{plantId}")
    fun getOne(@PathVariable plantId: Long): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(plantRepository.findById(plantId))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @PostMapping
    fun create(@RequestBody plant: PlantDto): ResponseEntity<Any> =
        try {
            val plantEntity = plantMapper.toEntity(plant)
            ResponseEntity.ok(plantRepository.save(plantEntity))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @DeleteMapping("/{plantId}")
    fun delete(@PathVariable plantId: Long): ResponseEntity<Any> =
        try {
            plantRepository.deleteById(plantId)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
}