package com.example.Kokodi.exception

class UserLoginNotFoundException(login: String) :
    RuntimeException("User with login $login not found!")