package com.example.windows.auth.utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.windows.contacts.ui.adapters.RequestsPagerAdapter
import com.example.windows.databinding.FragmentContactsRequestsDialogBinding
import com.google.android.material.tabs.TabLayoutMediator

class RequestsDialogFragment : DialogFragment() {
    private var _binding: FragmentContactsRequestsDialogBinding? = null
    private val binding get() = _binding!!
    lateinit var tabsMediator: TabLayoutMediator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsRequestsDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        binding.viewPager.adapter = RequestsPagerAdapter(this)
        tabsMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Входящие"
                1 -> tab.text = "Исходящие"
            }
        }

        tabsMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabsMediator.detach()
    }
}