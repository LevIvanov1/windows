package com.example.windows

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.windows.auth.view_models.SettingsScreenState
import com.example.windows.auth.view_models.SettingsViewModel
import com.example.windows.data.RetrofitClient
import com.example.windows.databinding.FragmentSettingBinding
import com.example.windows.libs.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val apiService by lazy {
        RetrofitClient.create(requireContext(), view)
    }

    private val viewModel by viewModels<SettingsViewModel> {
        SettingsViewModel.getViewModelFactory(apiService, requireContext(), view)
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri: Uri? ->
        if (uri != null) {
            viewModel.updateAvatar(uri, requireContext())

            binding.avatar.setImageURI(uri)
        } else {
            Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT).show()
        }
    }

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

        viewModel.settingsData.observe(viewLifecycleOwner) {
                settingsData -> render(settingsData)
        }

        binding.buttonSwitchAvatar.setOnClickListener {
            launchImagePicker()
        }

        binding.switchThemes.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                setLightTheme()
            } else {
                setDarkTheme()
            }
        }

        binding.buttonRegister.setOnClickListener {
            RetrofitClient.create(requireContext(), view).logout().enqueue(
                object :
                    Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            TokenManager.clearToken(requireContext())
                            findNavController().navigate(com.example.windows.R.id.action_settingsFragment_to_authFragment)
                        } else {
                        }
                    }
                    override fun onFailure(p0: Call<Unit?>, p1: Throwable) {
                    }
                })

        }

    setFragmentResultListener("requestKey") { key, bundle ->
        val result = bundle.getString("resultKey")
        viewModel.rename(result.toString())
    }

    binding.buttonEditName.setOnClickListener {
        findNavController().navigate(R.id.action_settingsFragment_to_editTheName)
    }
        binding.avatar.setOnClickListener {
            launchImagePicker()
        }
}

    private fun launchImagePicker() {
        pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun render(state: SettingsScreenState) {
        when (state) {
            is SettingsScreenState.Content-> showContent(state)
            is SettingsScreenState.Error -> return
            is SettingsScreenState.Loading -> showLoading(state)
        }
    }

    private fun showContent(state: SettingsScreenState.Content) {
        binding.nameLogin.text = getString(R.string.user, state.userLogin)
        binding.nameProfile.text = getString(R.string.name, state.userName)
    }

    private fun showLoading(state: SettingsScreenState.Loading) {
        viewModel.getUser()
        viewModel.processSettings()
    }
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

