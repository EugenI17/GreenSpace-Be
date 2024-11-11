package ro.upt.greenspace.dto

import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
class PlantDto (
    val id: Long? = null,
    val name: String,
    val room: Long? = null,
)
