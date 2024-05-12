package com.sugarygary.instory.ui.login

import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.FragmentLoginBinding
import com.sugarygary.instory.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(FragmentLoginBinding::inflate) {
    private val viewModel: LoginViewModel by viewModels()
    override fun setupListeners() {
        with(binding) {
            btnLogin.setOnClickListener {
                if (edLoginEmail.text.isNullOrEmpty() || edLoginEmail.error != null) {
                    edLoginEmail.error =
                        getString(R.string.email_invalid)
                    edLoginEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edLoginPassword.text.isNullOrEmpty() || edLoginPassword.error != null) {
                    edLoginPassword.error =
                        getString(R.string.password_invalid)
                    edLoginPassword.requestFocus()
                    return@setOnClickListener
                }

                viewModel.login(
                    edLoginEmail.text.toString(),
                    edLoginPassword.text.toString()
                )
            }
            tvToRegister.setOnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }
    }

    override fun setupObservers() {
        viewModel.loginResponse.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response) {
                    is State.Error -> {
                        progressBar.isInvisible = true
                        btnLogin.isVisible = true
                        when (response.error) {
                            "User not found" -> {
                                edLoginEmail.error = getString(R.string.email_not_found_message)
                                edLoginEmail.requestFocus()
                            }
                            "Invalid password" -> {
                                edLoginPassword.error = getString(R.string.invalid_password)
                                edLoginPassword.requestFocus()
                            }
                            else -> {
                                Toast.makeText(
                                    requireActivity(),
                                    getString(R.string.error_login_general),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }

                    State.Loading -> {
                        progressBar.isVisible = true
                        btnLogin.isInvisible = true
                    }

                    is State.Success -> {
                        progressBar.isInvisible = true
                        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                    }

                    State.Empty -> {}
                }
            }
        }
    }
}