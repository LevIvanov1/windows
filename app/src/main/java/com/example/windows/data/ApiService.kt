package com.example.windows.data

import com.example.windows.data.models.LoginRequest
import com.example.windows.data.models.LoginResponse
import com.example.windows.data.models.RegisterRequest
import com.example.windows.data.models.UserResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<LoginResponse>

    @POST("auth/logout")
    fun logout(): Call<Void>

    @GET("users/user")
    fun getUser(): Call<UserResponse>
}