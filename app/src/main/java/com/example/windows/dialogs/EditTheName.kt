package com.example.windows.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.example.windows.databinding.FragmentEditTheNameBinding


class EditTheName : DialogFragment() {
    private var _binding: FragmentEditTheNameBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditTheNameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        binding.cancelButton.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.okButton.setOnClickListener{
            setFragmentResult("requestKey", bundleOf("resultKey" to binding.enterName.text.toString()))
            findNavController().navigateUp()
        }
    }
}