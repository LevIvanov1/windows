package com.example.windows.auth.view_models

import android.content.Context
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.windows.data.ApiService
import com.example.windows.data.models.AuthScreenState
import com.example.windows.data.models.LoginRequest
import com.example.windows.data.models.LoginResponse
import com.example.windows.data.models.RegisterRequest
import com.example.windows.data.models.errors.AuthErrorBody422
import com.example.windows.libs.HandleOperators
import com.example.windows.libs.TokenManager
import com.google.gson.Gson
import kotlinx.coroutines.launch

class AuthViewModel(val apiService: ApiService, val context: Context, val view: View?) : ViewModel() {
    private val _authState = MutableLiveData<AuthScreenState>(AuthScreenState.Content)
    val authState: MutableLiveData<AuthScreenState> get() = _authState

    fun renderState(state: AuthScreenState) {
        _authState.postValue(state)
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService, context: Context, view: View?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    AuthViewModel(
                        apiService = apiService,
                        context = context,
                        view = view
                    )
                }
            }
    }

    fun login(context: Context, login: String, password: String) {
        renderState(AuthScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.login(LoginRequest(login, password))
            }

            when (response.code()) {
                200 -> {
                    with(response.body() as LoginResponse) {
                        TokenManager.saveToken(context, token)
                        renderState(AuthScreenState.Navigate)
                    }
                }

                400 -> {
                    val errorText = response.errorBody()?.string()
                    renderState(
                        AuthScreenState.Error(
                            errorMessage = errorText
                        )
                    )
                }

                422 -> {
                    val error = Gson().fromJson(response.errorBody()?.string(), AuthErrorBody422::class.java)
                    renderState(
                        AuthScreenState.Error(
                            loginError = error.errors.Login?.firstOrNull(),
                            passwordError = error.errors.Password?.firstOrNull()
                        )
                    )
                }

                999 -> {
                }
            }
        }
    }

    fun register(context: Context, login: String, password: String, name: String) {
        renderState(AuthScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.register(RegisterRequest(login, password, name))
            }

            when (response.code()) {
                200 -> {
                    with(response.body() as LoginResponse) {
                        TokenManager.saveToken(context, token)
                        renderState(AuthScreenState.Navigate)
                    }
                }

                422 -> {
                    val error = Gson().fromJson(response.errorBody()?.string(), AuthErrorBody422::class.java)
                    renderState(
                        AuthScreenState.Error(
                            loginError = error.errors.Login?.firstOrNull(),
                            passwordError = error.errors.Password?.firstOrNull(),
                            nameError = error.errors.Name?.firstOrNull()
                        )
                    )
                }

                999 -> {
                }
            }
        }
    }
}