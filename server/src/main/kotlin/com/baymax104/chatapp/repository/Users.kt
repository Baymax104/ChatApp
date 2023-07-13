package com.baymax104.chatapp.repository

import com.baymax104.chatapp.entity.User
import org.ktorm.database.Database
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar

/**
 * User Binding
 */
object Users : Table<User>("users") {
    val id = int("id").primaryKey().bindTo { it.id }
    val account = varchar("account").bindTo { it.account }
    val password = varchar("password").bindTo { it.password }
    val username = varchar("username").bindTo { it.username }
    val gender = varchar("gender").bindTo { it.gender }
    val age = varchar("age").bindTo { it.age }
}

val Database.users get() = sequenceOf(Users)