package com.example.windows.auth.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.windows.auth.AuthPagerAdapter
import com.example.windows.databinding.AuthFragmentBinding
import com.google.android.material.tabs.TabLayoutMediator

class AuthFragment : Fragment() {
    private var _binding: AuthFragmentBinding? = null
    private val binding get() = _binding!!
    lateinit var tabsMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = AuthFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        val isInternetConnected = capabilities != null

        childFragmentManager.setFragmentResultListener("request", viewLifecycleOwner) { key, bundle ->
            val result = bundle.getBoolean("result")
            binding.overlay.isVisible = result
            binding.progressBar.isVisible = result
        }

        if (capabilities != null) {
            val connectionType = when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "cellular"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "wifi"
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "ethernet"
                else -> "no internet"
            }
        }

        binding.viewPager.adapter = AuthPagerAdapter(this)
        tabsMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) {
            tab, position ->
            when (position){
                0 -> tab.text = "Войти"
                1 -> tab.text = "Регистрация"
            }
        }
        tabsMediator.attach()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        tabsMediator.detach()
    }
}