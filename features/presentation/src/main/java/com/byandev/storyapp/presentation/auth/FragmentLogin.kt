package com.byandev.storyapp.presentation.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.byandev.storyapp.common.SharedPrefManager
import com.byandev.storyapp.common.UtilsConnect
import com.byandev.storyapp.common.base.BaseFragment
import com.byandev.storyapp.data.model.Login
import com.byandev.storyapp.data.model.LoginResult
import com.byandev.storyapp.presentation.PresentationViewModel
import com.byandev.storyapp.presentation.R
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.presentation.databinding.FragmentLoginBinding
import com.byandev.storyapp.presentation.dialogLoading
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.sharedViewModel

class FragmentLogin : BaseFragment<FragmentLoginBinding>() {

    companion object {
        private const val TAG = "FragmentLogin"
    }

    private lateinit var sharedPref : SharedPrefManager
    private lateinit var utilsConnect : UtilsConnect
    private val sharedViewModels: SharedViewModel by viewModels()
    private val presentationViewModel: PresentationViewModel by sharedViewModel()

    private var emailUser = ""
    private var passwordUser = ""
    private var isRemembered = false

    private lateinit var dialog: Dialog

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        sharedPref = SharedPrefManager(requireContext())
        utilsConnect = UtilsConnect(requireContext())
        observeNavigation(sharedViewModels)
        dialog = Dialog(requireContext())

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() { requireActivity().finishAffinity() }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, onBackPressedCallback)




        binding.apply {
            tvRegister.setOnClickListener {
//                findNavController().navigate(FragmentLoginDirections.actionFragmentLoginToFragmentRegister())
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
                presentationViewModel.loginUsers(login).map { result ->
                    dialog.dismiss()
                    if (result.data?.error == false) {
                        loginSaved(result.data?.loginResult)
                        sharedViewModels.navigateToHomeFromLogin()
                        Toast.makeText(requireContext(), "${result.data?.message}", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(requireContext(), "Error:${result.message}", Toast.LENGTH_SHORT)
                            .show()
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