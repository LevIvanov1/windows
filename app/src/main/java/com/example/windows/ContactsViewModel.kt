package com.example.windows

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
import com.example.windows.data.ContactsResponse
import com.example.windows.libs.ContactRequest
import com.example.windows.libs.HandleOperators
import kotlinx.coroutines.launch

class ContactsViewModel(val apiService: ApiService, val context: Context, val view: View?): ViewModel() {
    private val _contacts = MutableLiveData<List<ContactsResponse>>()
    val contacts: LiveData<List<ContactsResponse>> get() = _contacts

    companion object {
        fun getViewModelFactory(apiService: ApiService, context: Context, view: View?): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ContactsViewModel(
                        apiService = apiService,
                        context = context,
                        view = view
                    )
                }
            }}

    fun getContacts() {
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.getContacts()
            }
            when (response.code()) {
                200 -> {
                    _contacts.value = response.body()
                }
                -1 -> {
                }
            }
        }
    }

    fun deleteContact(userId : String) {
        viewModelScope.launch {
            val response = HandleOperators.handleRequest {
                apiService.deleteContact(ContactRequest(userId))
            }
            when (response.code()) {
                200 -> {
                    getContacts()
                }
                -1 -> {
                }
            }
        }
    }
}