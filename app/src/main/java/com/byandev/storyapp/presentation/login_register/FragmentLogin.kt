package com.byandev.storyapp.presentation.login_register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.byandev.storyapp.R
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.LoginResult
import com.byandev.storyapp.databinding.FragmentLoginBinding
import com.byandev.storyapp.di.SharedPrefManager
import com.byandev.storyapp.di.UtilsConnect
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.Resources
import com.byandev.storyapp.utils.dialogLoading
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FragmentLogin : Fragment() {

    companion object {
        private const val TAG = "FragmentLogin"
    }

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPref: SharedPrefManager
    @Inject
    lateinit var utilsConnect: UtilsConnect
    private val sharedViewModel: SharedViewModel by viewModels()

    private var emailUser = ""
    private var passwordUser = ""
    private var isRemembered = false

    private lateinit var dialog: Dialog

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
        dialog = Dialog(requireContext())

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { requireActivity().finishAffinity() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)




        binding.apply {
            tvRegister.setOnClickListener {
                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRegister())
            }

            if (sharedPref.isRemember) {
                edtEmail.setText(sharedPref.email)
                edtPassword.setText(sharedPref.userKey)
                cbRemember.isChecked = sharedPref.isRemember

                emailUser = sharedPref.email
                passwordUser = sharedPref.userKey
                isRemembered = sharedPref.isRemember
            }
            edtEmail.doOnTextChanged { text, _, _, _ ->  emailUser = text.toString() }
            edtPassword.doOnTextChanged { text, _, _, _ -> passwordUser = text.toString() }

            cbRemember.setOnCheckedChangeListener { _, isChecked ->
                isRemembered = isChecked
            }

            btnLogin.setOnClickListener {
                if (emailUser.isNotEmpty() && passwordUser.length >= 6)
                    loginUser()
                else Toast.makeText(
                    requireContext(),
                    "Check your email & password",
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

        val signIn = ObjectAnimator.ofFloat(binding.tvSignIn, View.ALPHA, 1f).setDuration(500)
        val title = ObjectAnimator.ofFloat(binding.tvWelcome, View.ALPHA, 1f).setDuration(500)
        val desc = ObjectAnimator.ofFloat(binding.tvWelcome2, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)
        val signUp = ObjectAnimator.ofFloat(binding.tvRegister, View.ALPHA, 1f).setDuration(500)

        val together = AnimatorSet().apply {
            playTogether(login, signUp)
        }


        AnimatorSet().apply {
            playSequentially(title, desc, signIn, together)
            start()
        }

    }

    private fun loginUser() {
        val login = Login(email = emailUser, password = passwordUser)
        lifecycleScope.launch {
            dialogLoading(dialog)
            delay(5000)
            if (utilsConnect.isConnectedToInternet()) {
                sharedViewModel.loginUsers(login).observe(viewLifecycleOwner) {
                    when(it) {
                        is Resources.Loading -> {}
                        is Resources.Success -> {
                            dialog.dismiss()
                            loginSaved(it.data?.loginResult)
                            val nav = FragmentLoginDirections.actionFragmentLoginToFragmentHome()
                            findNavController().navigate(nav)
                            Toast.makeText(requireContext(), "${it.data?.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resources.Error -> {
                            dialog.dismiss()
                            Toast.makeText(requireContext(), "Error:${it.message}", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            } else {
                Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }

    private fun loginSaved(dataLogin: LoginResult?) {
        sharedPref.isLogin = true
        sharedPref.isRemember = isRemembered
        sharedPref.email = emailUser
        sharedPref.userKey = passwordUser
        sharedPref.userId = dataLogin?.userId.toString()
        sharedPref.userName = dataLogin?.name.toString()
        sharedPref.token = dataLogin?.token.toString()
    }

}