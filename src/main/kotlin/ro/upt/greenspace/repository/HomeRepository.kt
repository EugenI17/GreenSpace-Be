package ro.upt.greenspace.repository

import org.springframework.data.jpa.repository.JpaRepository
import ro.upt.greenspace.entity.Home

interface HomeRepository : JpaRepository<Home, Long>