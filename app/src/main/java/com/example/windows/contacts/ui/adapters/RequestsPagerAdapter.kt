package com.example.windows.contacts.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.windows.dialogs.IncomingRequestsFragment
import com.example.windows.dialogs.OutgoingRequestsFragment

class RequestsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0-> IncomingRequestsFragment()
            1-> OutgoingRequestsFragment()
            else-> throw IllegalArgumentException("Invalid position")
        }
    }
}