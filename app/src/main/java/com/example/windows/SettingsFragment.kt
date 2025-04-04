package com.example.windows

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.windows.data.RetrofitClient
import com.example.windows.databinding.FragmentSettingBinding
import retrofit2.Callback
import com.example.windows.libs.TokenManager
import retrofit2.Response
import androidx.navigation.fragment.findNavController
import retrofit2.Call
import com.example.windows.data.models.UserResponse

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        // binding.textView.text = "Настройки не существует в этом Мессенджере!"
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        RetrofitClient.create(requireContext(), view).getUser().enqueue(
            object :
                Callback<UserResponse> {
                override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                    if (response.isSuccessful) {
                        response.body()?.name.let { name ->
                            binding.nameProfile.text = name
                        }
                    }
                }

                override fun onFailure(p0: Call<UserResponse?>, p1: Throwable) {
                }
            })
        binding.switchThemes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setDarkTheme()
            } else {
                setLightTheme()
            }
        }
        binding.buttonRegister.setOnClickListener {
            RetrofitClient.create(requireContext(), view).logout().enqueue(
                object :
                    Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: Response<Void>) {
                        if (response.isSuccessful) {
                            TokenManager.clearToken(requireContext())
                            findNavController().navigate(R.id.action_settingsFragment_to_authFragment)
                        } else {
                        }
                    }

                    override fun onFailure(call: Call<Void?>, p1: Throwable) {
                    }
                })
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun setLightTheme(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }
    private fun setDarkTheme(){
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
    private fun getSavedThemeState() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
