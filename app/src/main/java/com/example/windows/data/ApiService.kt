package com.example.windows.data

import com.example.windows.data.models.LoginRequest
import com.example.windows.data.models.LoginResponse
import com.example.windows.data.models.RegisterRequest
import com.example.windows.data.models.UserResponse
import com.example.windows.data.models.UserSearchResponse
import com.example.windows.dialogs.ContactRequest
import com.example.windows.dialogs.UpdateNameRequest
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<LoginResponse>

    @POST("/auth/logout")
    suspend fun logout(): Response<Unit>

    @GET("/users/user")
    suspend fun getUser(): Response<UserResponse>

    @GET("/users/search")
    suspend fun userSearch(@Query("search") search : String): Response<List<UserSearchResponse>>

    @PATCH("/users/update/name")
    suspend fun userUpdateName(@Body request: UpdateNameRequest): Response<Unit>

    @GET("/contacts")
    suspend fun getContacts(): Response<List<ContactsResponse>>

    @POST("/contacts/add")
    suspend fun addContact(@Body request: ContactRequest): Response<Unit>

    @POST("/contacts/accept")
    suspend fun acceptRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @POST("/contacts/decline")
    suspend fun declineRequest(@Body request: AcceptDeclineRequest): Response<Unit>

    @POST("/contacts/delete")
    suspend fun deleteContact(@Body request: ContactRequest): Response<Unit>

    @GET("contacts/in_requests")
    suspend fun getContactsInRequest(): Response<List<RequestResponse>>

    @GET("contacts/out_requests")
    suspend fun getContactsOutRequest(): Response<List<RequestResponse>>

    @Multipart
    @PATCH("/users/update/avatar")
    suspend fun updateAvatar(@Part file: MultipartBody.Part): Response<String>
}