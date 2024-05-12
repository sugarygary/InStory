package com.sugarygary.instory.ui.detail

import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.sugarygary.instory.R
import com.sugarygary.instory.data.repository.State
import com.sugarygary.instory.databinding.FragmentDetailBinding
import com.sugarygary.instory.ui.base.BaseFragment
import com.sugarygary.instory.util.glide
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(FragmentDetailBinding::inflate) {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel: DetailViewModel by viewModels()
    override fun initData() {
        viewModel.fetchStoryDetail(args.storyId)
    }

    override fun setupListeners() {
        with(binding) {
            materialToolbar2.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun setupObservers() {
        with(binding) {
            viewModel.storyDetail.observe(viewLifecycleOwner) {
                when (it) {
                    State.Empty -> {}
                    is State.Error -> {
                        Toast.makeText(
                            requireActivity(),
                            getString(R.string.error_fetching_data),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    State.Loading -> {
                        layoutSuccess.isGone = true
                        layoutLoading.isVisible = true
                    }

                    is State.Success -> {
                        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                        val dateTime = LocalDateTime.parse(it.data.createdAt, formatter)
                        val formattedDate = dateTime.format(
                            DateTimeFormatter.ofPattern(
                                "d MMMM yyyy",
                                Locale.getDefault()
                            )
                        )
                        tvTimestamps.text =
                            getString(R.string.uploaded_on_timestamps, formattedDate)
                        tvDetailName.text = it.data.name
                        tvDetailDescription.text = it.data.description
                        ivDetailPhoto.glide(it.data.photoUrl)
                        layoutLoading.isGone = true
                        layoutSuccess.isVisible = true
                    }
                }
            }
        }
    }
}