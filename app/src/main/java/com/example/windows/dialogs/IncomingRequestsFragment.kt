package com.example.windows.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.windows.auth.view_models.RequestsViewModel
import com.example.windows.contacts.ui.adapters.IncomingRequestsAdapter
import com.example.windows.data.RetrofitClient
import kotlin.getValue
import com.example.windows.databinding.FragmentContactsIncomingRequestsBinding

class IncomingRequestsFragment : Fragment() {
    private var _binding: FragmentContactsIncomingRequestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var incomingRequestsAdapter: IncomingRequestsAdapter
    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }
    private val viewModel by viewModels<RequestsViewModel> {
        RequestsViewModel.getViewModelFactory(apiService, requireContext(), view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsIncomingRequestsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        incomingRequestsAdapter = IncomingRequestsAdapter(
            accept = { request -> viewModel.acceptRequest(request.requestId) },
            decline = { request -> viewModel.declineRequest(request.requestId) }
        )
        binding.rvSearchResults.adapter = incomingRequestsAdapter
        viewModel.incomingRequests.observe(viewLifecycleOwner) { requests ->
            incomingRequestsAdapter.setIncomingRequests(requests)
        }
        viewModel.inRequestResponse()
    }
}