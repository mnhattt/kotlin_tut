package com.example.kotlin_tut

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
import javax.persistence.AttributeOverride
import javax.persistence.CollectionTable
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Embeddable
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class Message {
    @Id
    @GeneratedValue
    var id: Long? = null

    @NotNull
    @Size(min = 5, max = 200, message
    = "About Me must be between 10 and 200 characterssss")
    var text: String? = null

    @ElementCollection
    @CollectionTable(name = "TAG")
//    @GenericGenerator(name = "sequence_gen", strategy = "sequence")
    var tags: List<Tag>? = null
}

data class MessageDTO(val text: String, val tags: List<String>) {
    fun toEntity(): Message {
        val message = Message()
        val tags = tags.map { Tag(it) }
        message.tags = tags

        return message
    }
}

@Embeddable
class Tag(var name: String? = null) {
}

interface MessageRepo : CrudRepository<Message, Long> {}

@RestController
class Controller {

    @Autowired
    lateinit var repo: MessageRepo

    @GetMapping
    fun index(): Iterable<Message> {
        return repo.findAll()
    }

    @PostMapping
    @ResponseBody
    fun post(@RequestBody messageDTO: MessageDTO): Message {
        val mess = messageDTO.toEntity()
        repo.save(mess)
        return mess
//        return message
    }
}