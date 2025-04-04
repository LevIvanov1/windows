package com.example.windows.auth
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.windows.auth.ui.LoginFragment
import com.example.windows.auth.ui.RegisterFragment

class AuthPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
          0 -> LoginFragment()
          1 -> RegisterFragment()
            else -> throw IllegalArgumentException("Invalid")
        }
    }
}