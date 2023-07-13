package com.baymax104.chatapp.service

import com.baymax104.chatapp.base.ioc.PathMapping
import com.baymax104.chatapp.base.ioc.Service
import com.baymax104.chatapp.base.thread.ThreadMap
import com.baymax104.chatapp.dto.Response
import com.baymax104.chatapp.dto.UserDTO
import com.baymax104.chatapp.entity.User
import com.baymax104.chatapp.repository.database
import com.baymax104.chatapp.repository.users
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*

@Service
class UserService {

    fun login(map: Map<String, String>): Response<UserDTO> {
        val account = map["account"]!!
        val password = map["password"]!!
        val user = database.users.find { it.account eq account }
            ?: return Response("error", "1000", "用户不存在")

        if (user.password != password) {
            return Response("error", "1001", "用户密码错误")
        }

        val userDTO = UserDTO(user)
        return Response("success", "200", body = userDTO)
    }

    fun register(map: Map<String, String>): Response<Any> {
        val account = map["account"]!!
        val password = map["password"]!!

        val find = database.users.find { it.account eq account }
        if (find != null) {
            return Response("error", "1002", "用户已存在")
        }
        val size = database.users.count()
        val user = User {
            this.account = account
            this.password = password
            username = "用户${1000 + size}"
        }
        val i = database.users.add(user)
        if (i != 1) {
            return Response("error", "2001", "用户注册失败")
        }

        return Response("success", "200", body = Unit)
    }

    @PathMapping("/online")
    fun getOnline(): Response<List<UserDTO>> {
        val ids = ThreadMap.keys
        val users = database.users
            .filter { it.id inList ids }
            .map { UserDTO(it) }
        return Response("success", "200", body = users)
    }

    @PathMapping("/update")
    fun updateInfo(newUser: UserDTO): Response<Any> {
        val user = database.users.find { it.id eq newUser.id }
            ?: return Response("error", "2002", "用户信息修改失败")
        user.apply {
            username = newUser.username
            age = newUser.age
            gender = newUser.gender
        }
        user.flushChanges()
        return Response("success", "200", body = Unit)
    }

}