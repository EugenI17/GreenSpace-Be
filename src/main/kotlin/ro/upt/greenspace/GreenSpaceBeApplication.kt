package ro.upt.greenspace

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GreenSpaceBeApplication

fun main(args: Array<String>) {
    runApplication<GreenSpaceBeApplication>(*args)
}
