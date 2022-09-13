package com.byandev.storyapp.presentation.login_register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.R
import com.byandev.storyapp.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentLogin : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRegister())
        }


        binding.edtPassword.onTextChanged { t -> Log.e("TAG", "onViewCreated:textChange $t") }
        binding.edtPassword.onValidate = { validate ->
            Log.e("TAG", "onViewCreated:validate ${validate.toString()}")
        }

    }

}