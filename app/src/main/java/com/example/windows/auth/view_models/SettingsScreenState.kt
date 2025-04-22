package com.example.windows.auth.view_models

sealed class SettingsScreenState {
    object Loading : SettingsScreenState()
    data class Content(val userId: String, val userName: String, val userLogin: String, val userAvatar: String?) : SettingsScreenState()
    data class Error(
        val nameError: String? = null,
        val message: String? = null,
        val loginError: String? = null,
        val avatarError: String? = null
    ) : SettingsScreenState()
}