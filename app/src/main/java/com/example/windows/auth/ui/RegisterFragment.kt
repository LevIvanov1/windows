package com.example.windows.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.windows.R
import com.example.windows.auth.view_models.AuthViewModel
import com.example.windows.data.RetrofitClient
import com.example.windows.data.models.AuthScreenState
import com.example.windows.databinding.RegisterFragmentBinding

class RegisterFragment : Fragment() {
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!
    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }
    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModel.getViewModelFactory(apiService, requireContext(), view)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.authState.observe(viewLifecycleOwner) { authState ->
            render(authState)
        }

        binding.buttonRegister.setOnClickListener{
            viewModel.register(login = binding.inputLogin.text.toString(), password = binding.inputPassword.text.toString(), name = binding.inputName.text.toString(), context = requireContext())
        }
    }

    private fun render(state: AuthScreenState) {
        when (state) {
            is AuthScreenState.Loading -> showLoading(state)
            is AuthScreenState.Content -> showContent(state)
            is AuthScreenState.Error -> showError(state)
            is AuthScreenState.Navigate -> showNavigate(state)
        }
    }

    private fun hideAll() {
        binding.errLogin.isVisible = false
        binding.errPassword.isVisible = false
        binding.errName.isVisible = false
        setFragmentResult("request", bundleOf("result" to false))
    }

    private fun showLoading(state: AuthScreenState.Loading) {
        hideAll()
        setFragmentResult("request", bundleOf("result" to true))
    }

    private fun showContent(state: AuthScreenState.Content) {
        hideAll()
    }

    private fun showError(state: AuthScreenState.Error) {
        hideAll()

        if (state.loginError != null){
            binding.errLogin.isVisible = true
            binding.errLogin.text = state.loginError
        }

        if (state.passwordError != null){
            binding.errPassword.isVisible = true
            binding.errPassword.text = state.passwordError
        }

        if (state.nameError != null){
            binding.errName.isVisible = true
            binding.errName.text = state.nameError
        }
    }

    private fun showNavigate(state: AuthScreenState){
        findNavController().navigate(R.id.action_authFragment_to_chatsFragment2)
    }
}