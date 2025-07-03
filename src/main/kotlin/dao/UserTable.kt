package dao

import org.jetbrains.exposed.dao.id.LongIdTable

object UserTable : LongIdTable("users") {
    val name   = varchar("name", 100)
    val age    = integer("age")
    val gender = varchar("gender", 10)
}