package com.example.Kokodi.exception

class UserAlreadyExistException(login: String) :
    RuntimeException("User with login $login already exist!") {
}
