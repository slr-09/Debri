package com.example.debri_lize.view.auth

import com.example.debri_lize.response.Result

interface LoginView {
    fun onLoginSuccess(code:Int, result : Result?)
    fun onLoginFailure(code:Int, message : String)
}