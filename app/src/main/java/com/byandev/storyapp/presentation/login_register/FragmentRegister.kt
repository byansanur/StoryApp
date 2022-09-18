package com.byandev.storyapp.presentation.login_register

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.databinding.FragmentRegisterBinding
import com.byandev.storyapp.di.SharedPrefManager
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.dialogLoading
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentRegister : Fragment() {

    companion object {
        private const val TAG = "FragmentRegister"
    }

    private var _binding : FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefManager

    private val sharedViewModel: SharedViewModel by viewModels()

    private var nameUser = ""
    private var emailUser = ""
    private var passwordUser = ""

    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog = Dialog(requireContext())

        binding.apply {
            edtName.doOnTextChanged { text, _, _, _ -> nameUser = text.toString() }
            edtEmail.doOnTextChanged { text, _, _, _ -> emailUser = text.toString() }
            edtPassword.doOnTextChanged { text, _, _, _ -> passwordUser = text.toString() }

            tvLogin.setOnClickListener { findNavController().navigateUp() }
            btnRegister.setOnClickListener {
                if (nameUser.isNotEmpty() && emailUser.isNotEmpty() && passwordUser.length >= 6)
                    registerNewUser()
                else Toast.makeText(
                    requireContext(),
                    "Complete field for register",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun registerNewUser() {


        val register = Register(name = nameUser, email = emailUser, password = passwordUser)

        lifecycleScope.launch {
            dialogLoading(dialog)
            delay(5000)
            sharedViewModel.registerUsers(register).observe(viewLifecycleOwner) {
                when(it) {
                    is Resources.Loading -> Log.e(TAG, "registerNewUser: loading")
                    is Resources.Success -> {
                        dialog.dismiss()
                        Log.e(TAG, "registerNewUser: success ${Gson().toJson(it.data)}")
                        sharedPref.userKey = passwordUser
                        sharedPref.email = emailUser
                        Toast.makeText(requireContext(), "${it.data?.message}", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigateUp()
                    }
                    is Resources.Error -> {
                        dialog.dismiss()
                        Log.e(TAG, "registerNewUser: error cause ${it.message}")
                        Toast.makeText(requireContext(), "Error:${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}