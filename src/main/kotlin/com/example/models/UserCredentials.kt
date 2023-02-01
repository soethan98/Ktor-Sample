package com.example.models

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
class UserCredentials(val username:String,
                      private val password:String){

    fun hashPassword(): String = BCrypt.hashpw(this.password,BCrypt.gensalt())

    fun isValidCredentials():Boolean = username.length >= 3 && password.length >=6

}