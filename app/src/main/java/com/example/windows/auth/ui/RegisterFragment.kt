package com.example.windows.auth.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.windows.R
import com.example.windows.data.RetrofitClient
import com.example.windows.data.models.LoginResponse
import com.example.windows.data.models.RegisterRequest
import com.example.windows.data.models.errors.AuthErrorBody422
import com.example.windows.databinding.RegisterFragmentBinding
import com.example.windows.libs.TokenManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.errLogin.isVisible = false
        binding.errPassword.isVisible = false
        binding.errName.isVisible = false

        binding.buttonRegister.setOnClickListener{
            binding.errLogin.isVisible = false
            binding.errPassword.isVisible = false
            binding.errName.isVisible = false
            RetrofitClient.create(requireContext(), view).register(RegisterRequest(binding.inputLogin.text.toString(), binding.inputPassword.text.toString(),
                binding.inputName.text.toString())).enqueue(
                object :
                    Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            response.body()?.token?.let { token ->
                                TokenManager.saveToken(requireContext(), token)
                                findNavController().navigate(R.id.action_authFragment_to_chatsFragment2)
                            }
                        } else if (response.code() == 422) {
                            val gson = Gson()
                            val type = object : TypeToken<AuthErrorBody422>() {}.type
                            var errorResponse: AuthErrorBody422? = gson.fromJson(response.errorBody()!!.charStream(), type)
                            if (errorResponse == null)
                                return

                            if (errorResponse.errors.Login != null) {
                                binding.errLogin.text = errorResponse.errors.Login!!.first()
                                binding.errLogin.isVisible = true
                            }
                            if (errorResponse.errors.Password != null) {
                                binding.errPassword.text = errorResponse.errors.Password!!.first()
                                binding.errPassword.isVisible = true
                            }
                            if (errorResponse.errors.Name != null) {
                                binding.errName.text = errorResponse.errors.Name!!.first()
                                binding.errName.isVisible = true
                            }
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    }
                })
        }
    }
}