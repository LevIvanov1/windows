package com.example.windows.auth.view_models

import android.content.Context
import android.net.Uri
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.windows.data.ApiService
import com.example.windows.libs.HandleOperators
import com.example.windows.libs.UpdateNameRequest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class SettingsViewModel(val apiService: ApiService, val context: Context, val view: View?) : ViewModel() {
    private val _settingsData = MutableLiveData<SettingsScreenState>(SettingsScreenState.Loading)
    val settingsData: LiveData<SettingsScreenState> get() = _settingsData

    fun renderState(state: SettingsScreenState) {
        _settingsData.postValue(state)
    }

    fun processSettings() {
        renderState(SettingsScreenState.Loading)
        viewModelScope.launch {
            val userResponse = apiService.getUser()
            if (userResponse.isSuccessful) {
                val user = userResponse.body()!!
                renderState(SettingsScreenState.Content(userId = user.id,
                    userName = user.name, userLogin = user.login, userAvatar = user.avatar))
            }
        }
    }

    companion object {
        fun getViewModelFactory(apiService: ApiService, context: Context, view: View?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SettingsViewModel(
                        apiService = apiService,
                        context = context,
                        view = view
                    )
                }
            }
    }

    fun getUser() {
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.getUser()
            }
            when (response.code()) {
                200 -> {
                    val user = response.body()!!
                    _settingsData.value = SettingsScreenState.Content(userId = user.id,
                        userName = user.name, userLogin = user.login, userAvatar = user.avatar)
                }
                -1 -> {
                }
            }
        }
    }

    fun rename(result: String) {
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.userUpdateName(UpdateNameRequest(result))
            }
            when (response.code()) {
                200 -> {
                }
                -1 -> {
                }
            }
        }
    }
    fun updateAvatar(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val file = File(context.cacheDir, "avatar.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("avatar", file.name, requestFile)
                val response = apiService.updateUserAvatar(body)

                if (response.isSuccessful) {
                    getUser()
                } else {
                    println("Error updating avatar: ${response.code()}")
                }

            } catch (e: Exception) {
                println("Exception while updating avatar: ${e.message}")
            }
        }
    }
}
