package ro.upt.greenspace.entity

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Getter
import lombok.NoArgsConstructor
import lombok.Setter

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
data class Home (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    val name: String,

    val city: String,

    @OneToMany(mappedBy = "home", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var plants: MutableList<Plant> = mutableListOf()
)
