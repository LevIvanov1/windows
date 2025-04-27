package com.example.windows.contacts.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.windows.R
import com.example.windows.data.RequestResponse
import com.example.windows.databinding.ItemOutgoingRequestBinding

class OutgoingRequestsAdapter(val decline: (RequestResponse) -> Unit): RecyclerView.Adapter<OutgoingRequestsAdapter.ViewHolder>()  {
    inner class ViewHolder(val binding: ItemOutgoingRequestBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var requests = mutableListOf<RequestResponse>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutgoingRequestsAdapter.ViewHolder {
        val binding = ItemOutgoingRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OutgoingRequestsAdapter.ViewHolder, position: Int) {
        with(holder.binding) {
            userName.text = requests[position].name
            userLogin.text = requests[position].login
            Glide.with(holder.itemView).load(requests[position].avatar).placeholder(R.drawable.contacts_foreground).into(userAvatar)
            decline.setOnClickListener {
                decline(requests[position])
            }
        }
    }

    override fun getItemCount() = requests.size

    fun setIncomingRequests(newContacts: List<RequestResponse>) {
        requests = newContacts.toMutableList()
        notifyDataSetChanged()
    }
}