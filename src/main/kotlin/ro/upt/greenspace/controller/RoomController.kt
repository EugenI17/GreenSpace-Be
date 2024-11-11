package ro.upt.greenspace.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ro.upt.greenspace.dto.RoomDto
import ro.upt.greenspace.entity.Room
import ro.upt.greenspace.mapper.RoomMapper
import ro.upt.greenspace.repository.RoomRepository

@RestController
@RequestMapping("/api/v1/rooms")
class RoomController {

    @Autowired
    private lateinit var roomMapper: RoomMapper

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @GetMapping
    fun getAll(): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(roomRepository.findAll())
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @GetMapping("/{roomId}")
    fun getOne(@PathVariable roomId: Long): ResponseEntity<Any> =
        try {
            ResponseEntity.ok(roomRepository.findById(roomId))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @PostMapping
    fun create(@RequestBody room: RoomDto): ResponseEntity<Any> =
        try {
            val roomEntity = roomMapper.toEntity(room)
            ResponseEntity.ok(roomRepository.save(roomEntity))
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }

    @DeleteMapping("/{roomId}")
    fun delete(@PathVariable roomId: Long): ResponseEntity<Any> =
        try {
            roomRepository.deleteById(roomId)
            ResponseEntity.ok().build()
        } catch (e: Exception) {
            ResponseEntity.badRequest().body(e.message)
        }
}

