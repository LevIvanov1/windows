package com.example.windows.contacts.ui.adapters

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.example.windows.data.models.UserSearchResponse
import com.example.windows.databinding.ItemUserSearchBinding

class UserSearchAdapter(private val onClick: (UserSearchResponse) -> Unit) : RecyclerView.Adapter<UserSearchAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: ItemUserSearchBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var contacts = mutableListOf<UserSearchResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserSearchAdapter.ViewHolder {
        val binding = ItemUserSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserSearchAdapter.ViewHolder, position: Int) {
        with(holder.binding) {
            userName.text = contacts[position].name
            userLogin.text = contacts[position].login
            add.setOnClickListener {
                onClick(contacts[position])
            }
        }
    }

    override fun getItemCount() = contacts.size

    fun setContacts(newContacts: List<UserSearchResponse>) {
        contacts = newContacts.toMutableList()
        notifyDataSetChanged()
    }
}