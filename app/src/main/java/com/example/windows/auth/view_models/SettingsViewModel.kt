package com.example.windows.auth.view_models
import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.windows.data.ApiService
import com.example.windows.data.models.SettingsScreenState
import com.example.windows.data.models.UserResponse
import com.example.windows.dialogs.UpdateNameRequest
import com.example.windows.libs.HandleOperators
import com.example.windows.libs.TokenManager
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class SettingsViewModel(val apiService: ApiService, val context: Context, val view: View?) : ViewModel() {
    private val _settingsData = MutableLiveData<SettingsScreenState>(SettingsScreenState.Loading)
    val settingsData: LiveData<SettingsScreenState> get() = _settingsData
    private lateinit var user: UserResponse

    init {
        getUser()
    }

    fun renderState(state: SettingsScreenState) {
        _settingsData.postValue(state)
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService, context: Context, view: View?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                SettingsViewModel(
                    apiService = apiService, context = context, view = view
                )
            }
        }
    }

    fun getUser() {
        renderState(SettingsScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.getUser()
            }
            when (response.code()) {
                200 -> {
                    user = response.body()!!
                    renderState(
                        SettingsScreenState.Content(
                            userId = user.id, userName = user.name, userLogin = user.login, userAvatar = user.avatar
                        )
                    )
                }

                999 -> {

                }
            }
        }
    }

    fun logout(context: Context) {
        renderState(SettingsScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.logout()
            }
            when (response.code()) {
                200 -> {
                    TokenManager.clearToken(context)
                    renderState(SettingsScreenState.Navigate)
                }

                999 -> {

                }
            }
        }
    }

    fun rename(result: String) {
        renderState(SettingsScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.userUpdateName(UpdateNameRequest(result))
            }
            when (response.code()) {
                200 -> {
                    user = user.copy(name = result)
                    renderState(
                        SettingsScreenState.Content(
                            userId = user.id, userName = user.name, userLogin = user.login, userAvatar = user.avatar
                        )
                    )
                }

                999 -> {
                }
            }
        }
    }

    fun updateAvatar(filePart: MultipartBody.Part) {
        renderState(SettingsScreenState.Loading)
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.updateAvatar(filePart)
            }
            when (response.code()) {
                200 -> {
                    user = user.copy(avatar = response.body()!!)
                    renderState(
                        SettingsScreenState.Content(
                            userId = user.id, userName = user.name, userLogin = user.login, userAvatar = user.avatar
                        )
                    )
                }

                999 -> {
                }
            }
        }
    }
}