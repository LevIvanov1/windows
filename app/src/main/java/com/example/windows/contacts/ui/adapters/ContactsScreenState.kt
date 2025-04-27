package com.example.windows.contacts.ui.adapters

import com.example.windows.data.ContactsResponse

sealed class ContactsScreenState {
    data object Loading : ContactsScreenState()
    data class Content(val contacts: List<ContactsResponse>) : ContactsScreenState()
    data class Error(val loginError: String?, val nameError: String?, val passwordError: String?) : ContactsScreenState()
}