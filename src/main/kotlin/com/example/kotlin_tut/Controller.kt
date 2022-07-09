package com.example.kotlin_tut

import com.fasterxml.jackson.annotation.JsonBackReference
import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonManagedReference
import com.fasterxml.jackson.annotation.ObjectIdGenerators
import org.hibernate.annotations.GenericGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestController
import javax.annotation.Resource
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Message {
    @Id
    @GeneratedValue
    var id: Long? = null

    @NotNull
    @Size(
        min = 5, max = 200, message = "About Me must be between 10 and 200 characterssss"
    )
    var text: String? = null

//    @ElementCollection
//    @CollectionTable(name = "TAG")
//    var tags: List<Tag>? = null


    @OneToMany(mappedBy = "mess", cascade = [CascadeType.PERSIST], fetch = FetchType.LAZY)
//    @JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    var tags: MutableList<Tag> = mutableListOf()

}

data class MessageDTO(val text: String, val tags: List<Tag>?) {
    fun toEntity(): Message {
        val message = Message()
        message.text = text
        if (tags != null) {
            for (t in tags){
                t.mess = message
            }
        message.tags = tags as MutableList<Tag>
        }

        return message
    }
}

//@Embeddable
//class Tag(var name: String? = null) {}

@Entity
class Tag(var name: String? = null) {
    @Id
    @GeneratedValue
    var id: Long? = null

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
//    @JsonIdentityInfo(
//        generator = ObjectIdGenerators.PropertyGenerator::class, property = "id")
    var mess: Message? = null
}

interface MessageRepo : CrudRepository<Message, Long> {}
interface TagRepo : CrudRepository<Tag, Long> {}

@RestController
class Controller {

    @Autowired
    lateinit var repo: MessageRepo

    @Autowired
    lateinit var tagRepo: TagRepo

    @GetMapping
    fun index(): Iterable<Message> {
        return repo.findAll()
    }

    @GetMapping("/t")
    fun tag(): Iterable<Tag> {
        return tagRepo.findAll()
    }

//    POST http://localhost:8080/
//    Content-Type: application/json
//

//    {
//        "text": "3333",
//        "tags": [
//        {
//            "name": "12121"
//        },
//        {
//            "name": "33333"
//        }
//        ]
//    }

//    @PostMapping
//    @ResponseBody
//    fun post(@RequestBody messageDTO: MessageDTO): Message {
//        val mess = messageDTO.toEntity()
//        repo.save(mess)
//        return mess
////        return message
//    }

    @PostMapping("/v3")
    @ResponseBody
    fun postt(@RequestBody messageDTO: MessageDTO): Message {
        return repo.save(messageDTO.toEntity())
    }
}