package com.jin.repository

import dao.UserTable
import models.User
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepo(db: Database) {
    init {
        // Khi khởi động tạo bảng nếu chưa có
        transaction(db) { SchemaUtils.create(UserTable) }
    }

    private val database = db

    fun all(): List<User> = transaction(database) {
        UserTable.selectAll().map { row ->
            User(
                id = row[UserTable.id].value,
                name = row[UserTable.name],
                age = row[UserTable.age],
                gender = row[UserTable.gender]
            )
        }
    }

    fun findById(id: Long): User? = transaction(database) {
        UserTable.select { UserTable.id eq id }
            .map { row ->
                User(
                    id = row[UserTable.id].value,
                    name = row[UserTable.name],
                    age = row[UserTable.age],
                    gender = row[UserTable.gender]
                )
            }.singleOrNull()
    }

    fun create(user: User): User = transaction(database) {
        val inserted = UserTable.insertAndGetId {
            it[name] = user.name
            it[age] = user.age
            it[gender] = user.gender
        }
        user.copy(id = inserted.value)
    }

    fun update(id: Long, user: User): Boolean = transaction(database) {
        UserTable.update({ UserTable.id eq id }) {
            it[name] = user.name
            it[age] = user.age
            it[gender] = user.gender
        } > 0
    }

    fun delete(id: Long): Boolean = transaction(database) {
        UserTable.deleteWhere { UserTable.id eq id } > 0
    }
}