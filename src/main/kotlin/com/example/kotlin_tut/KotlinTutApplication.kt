package com.example.kotlin_tut

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.support.beans

@SpringBootApplication
class KotlinTutApplication

fun main(args: Array<String>) {
    runApplication<KotlinTutApplication>(*args) {
        this.addInitializers(beans)
    }
}

val beans = beans {
//    bean {
//        CommandLineRunner {
//            val repo = ref<MessageRepo>()
//            val mess = Message()
//            mess.text = "repo text"
//            val t = Tag(name = "repo tag")
//            t.mess = mess
//            mess.tags.add(t)
//
//            repo.save(mess)
//        }
//    }
}
