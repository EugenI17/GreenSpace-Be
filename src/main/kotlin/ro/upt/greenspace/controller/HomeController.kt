package ro.upt.greenspace.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.upt.greenspace.dto.HomeDto
import ro.upt.greenspace.mapper.HomeMapper
import ro.upt.greenspace.repository.HomeRepository

@RestController
@RequestMapping("/api/v1/homes")
class HomeController {

    @Autowired
    private lateinit var homeMapper: HomeMapper

    @Autowired
    private lateinit var homeRepository: HomeRepository

    @GetMapping
    fun getAll(): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(homeRepository.findAll())
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @GetMapping("/{homeId}")
    fun getOne(@PathVariable homeId: Long): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(homeRepository.findById(homeId))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @PostMapping
    fun create(@RequestBody home: HomeDto): ResponseEntity<Any> =
        try {
            val homeEntity = homeMapper.toEntity(home)
            ResponseEntity.ok(homeRepository.save(homeEntity))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @DeleteMapping("/{homeId}")
    fun delete(@PathVariable homeId: Long): ResponseEntity<Any> =
        try {
            homeRepository.deleteById(homeId)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
}

