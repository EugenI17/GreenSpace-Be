package ro.upt.greenspace.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.upt.greenspace.entity.Room

interface RoomRepository : JpaRepository<Room, Long>