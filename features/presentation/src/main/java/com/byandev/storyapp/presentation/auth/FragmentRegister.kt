package com.byandev.storyapp.presentation.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.airbnb.lottie.utils.Utils
import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.UtilsConnect
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.data.model.Register
import com.byandev.storyapp.presentation.PresentationViewModel
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.presentation.databinding.FragmentIntroPermissionBinding
import com.byandev.storyapp.presentation.databinding.FragmentRegisterBinding
import com.byandev.storyapp.presentation.dialogLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class FragmentRegister : BaseFragment<FragmentRegisterBinding>() {

    companion object {
        private const val TAG = "FragmentRegister"
    }


    private lateinit var sharedPref : SharedPrefManager
    private lateinit var utilsConnect : UtilsConnect

    private val presentationViewModel: PresentationViewModel by viewModels()

    private var nameUser = ""
    private var emailUser = ""
    private var passwordUser = ""

    private lateinit var dialog: Dialog

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentRegisterBinding {
        return FragmentRegisterBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        sharedPref = SharedPrefManager(requireContext())
        utilsConnect = UtilsConnect(requireContext())
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

        playAnimation()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imgApp, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val signIn = ObjectAnimator.ofFloat(binding.tvLogin, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvWelcome2, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.tvSignUp, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signUp)
        }


        AnimatorSet().apply {
            playSequentially(title, desc, signIn, together)
            start()
        }

    }

    private fun registerNewUser() {


        val register = Register(name = nameUser, email = emailUser, password = passwordUser)

        lifecycleScope.launch {
            dialogLoading(dialog)
            delay(5000)
            if (utilsConnect.isConnectedToInternet()) {
                presentationViewModel.registerUsers(register).map { result ->
                    dialog.dismiss()
                    if (result.data?.error == false) {
                        sharedPref.userKey = passwordUser
                        sharedPref.email = emailUser
                        Toast.makeText(requireContext(), "${result.data?.message}", Toast.LENGTH_SHORT)
                            .show()
                        findNavController().navigateUp()
                    } else {
                        Log.e(TAG, "registerNewUser: error cause ${result.message}")
                        Toast.makeText(requireContext(), "Error:${result.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()
            }
        }
    }

}