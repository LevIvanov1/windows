package com.example.windows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.windows.auth.utils.RequestsDialogFragment
import com.example.windows.contacts.ui.adapters.ContactsAdapter
import com.example.windows.contacts.ui.adapters.ContactsScreenState
import com.example.windows.data.RetrofitClient
import com.example.windows.databinding.FragmentContactsBinding
import com.example.windows.dialogs.UserSearchDialogFragment

class ContactsFragment : Fragment() {
    private var _binding: FragmentContactsBinding? = null
    private val binding get() = _binding!!
    private lateinit var contactsAdapter: ContactsAdapter
    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }
    private val viewModel by viewModels<ContactsViewModel> {
        ContactsViewModel.getViewModelFactory(apiService, requireContext(), view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentContactsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            render(contacts)
        }

        contactsAdapter = ContactsAdapter(
            onClick = { contact -> viewModel.deleteContact(contact.id) }
        )
        binding.setContacts.adapter = contactsAdapter

        binding.SearchButton.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                contactsAdapter.filter.filter(newText)
                return true
            }
        })

        binding.PlusContact.setOnClickListener{
            UserSearchDialogFragment().show(childFragmentManager, "ConfirmationDialog")
        }

        binding.SearchContact.setOnClickListener{
            RequestsDialogFragment().show(childFragmentManager, "ConfirmationDialog")
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: ContactsScreenState) {
        when (state) {
            is ContactsScreenState.Loading -> showLoading(state)
            is ContactsScreenState.Content -> showContent(state)
            is ContactsScreenState.Error -> showError(state)
        }
    }

    private fun hideAll() {
        binding.overlay.isVisible = false
        binding.progressBar.isVisible = false
        binding.setContacts.isVisible = false
    }

    private fun showLoading(state: ContactsScreenState.Loading) {
        hideAll()
        binding.overlay.isVisible = true
        binding.progressBar.isVisible = true
    }

    private fun showContent(state: ContactsScreenState.Content) {
        hideAll()
        binding.setContacts.isVisible = true
        contactsAdapter.setContacts(state.contacts)
    }

    private fun showError(state: ContactsScreenState.Error) {
        hideAll()
    }
}