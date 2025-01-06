package ro.upt.greenspace.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.EqualsAndHashCode
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter
import ro.upt.greenspace.dto.PlantDetailsDto

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
data class Plant (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val name: String,

    @ManyToOne
    @JsonIgnore
    val home: Home,

    var type: String,

    var family: String,

    var water: String,

    var light: String,

    @Lob
    val image: ByteArray,

    var suggestion: String? = null
    )