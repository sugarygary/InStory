package com.sugarygary.instory.ui.onboarding

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.FragmentOnboardingBinding
import com.sugarygary.instory.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment :
    BaseFragment<FragmentOnboardingBinding>(FragmentOnboardingBinding::inflate) {
    private val viewModel: OnboardingViewModel by viewModels()
    override fun initData() {
        viewModel.checkAuthStatus()
    }

    override fun setupListeners() {
        with(binding) {
            btnToLogin.setOnClickListener {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToLoginFragment())
            }
            btnToRegister.setOnClickListener {
                findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToRegisterFragment())
            }
        }
    }

    override fun setupObservers() {
        viewModel.authStatus.observe(viewLifecycleOwner) { status ->
            with(binding) {
                when (status) {
                    is State.Success -> {
                        layoutOnboarding.isGone = true
                        findNavController().navigate(OnboardingFragmentDirections.actionOnboardingFragmentToHomeFragment())
                    }

                    is State.Error -> {
                        layoutOnboarding.isVisible = true
                        motionLayout.setTransition(R.id.onboarding_start_end)
                        motionLayout.transitionToEnd()
                    }

                    State.Empty -> {
                        layoutOnboarding.isVisible = true
                        motionLayout.setTransition(R.id.onboarding_start_end)
                        motionLayout.transitionToEnd()
                    }

                    State.Loading -> {}
                }
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onResume() {
        super.onResume()
        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}