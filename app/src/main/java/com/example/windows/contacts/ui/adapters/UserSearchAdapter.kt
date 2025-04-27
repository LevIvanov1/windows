package com.example.windows.contacts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windows.R
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
            Glide.with(holder.itemView).load(contacts[position].avatar).placeholder(R.drawable.contacts_foreground).into(userAvatar)
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