package com.sugarygary.instory.ui.register

import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.FragmentRegisterBinding
import com.sugarygary.instory.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>(FragmentRegisterBinding::inflate) {
    private val viewModel: RegisterViewModel by viewModels()
    override fun setupListeners() {
        with(binding) {
            btnRegister.setOnClickListener {
                if (edRegisterName.text.isNullOrEmpty() || edRegisterName.error != null) {
                    edRegisterName.error =
                        getString(R.string.required_field)
                    edRegisterName.requestFocus()
                    return@setOnClickListener
                }
                if (edRegisterEmail.text.isNullOrEmpty() || edRegisterEmail.error != null) {
                    edRegisterEmail.error =
                        getString(R.string.email_invalid)
                    edRegisterEmail.requestFocus()
                    return@setOnClickListener
                }
                if (edRegisterPassword.text.isNullOrEmpty() || edRegisterPassword.error != null) {
                    edRegisterPassword.error =
                        getString(R.string.password_invalid)
                    edRegisterPassword.requestFocus()
                    return@setOnClickListener
                }
                viewModel.register(
                    edRegisterName.text.toString(),
                    edRegisterEmail.text.toString(),
                    edRegisterPassword.text.toString()
                )
            }
            tvToLogin.setOnClickListener {
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
        }
    }

    override fun setupObservers() {
        viewModel.registerResponse.observe(viewLifecycleOwner) { response ->
            with(binding) {
                when (response) {
                    is State.Error -> {
                        progressBar.isInvisible = true
                        btnRegister.isVisible = true
                        if (response.error == "Email is already taken") {
                            edRegisterEmail.error = getString(R.string.email_taken_error)
                            edRegisterEmail.requestFocus()
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.error_register_general),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    State.Loading -> {
                        progressBar.isVisible = true
                        btnRegister.isInvisible = true
                    }

                    is State.Success -> {
                        progressBar.isInvisible = true
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.success_register_message),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                    }

                    State.Empty -> {}
                }
            }
        }
    }
}