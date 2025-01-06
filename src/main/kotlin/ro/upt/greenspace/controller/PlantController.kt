package ro.upt.greenspace.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import ro.upt.greenspace.dto.PlantDto
import ro.upt.greenspace.mapper.PlantMapper
import ro.upt.greenspace.repository.PlantRepository
import ro.upt.greenspace.service.PlantService

@RestController
@RequestMapping("/api/v1/plants")
class PlantController {

    @Autowired
    private lateinit var plantMapper: PlantMapper

    @Autowired
    private lateinit var plantRepository: PlantRepository

    @Autowired
    private lateinit var plantService: PlantService

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

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(@RequestPart plant: PlantDto, @RequestPart image: MultipartFile): ResponseEntity<Any> =
        try {
            val plantEntity = plantMapper.toEntity(plant, image.bytes)
            val completePlant = plantService.addDetails(plantEntity)
            ResponseEntity.ok(plantRepository.save(completePlant))
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