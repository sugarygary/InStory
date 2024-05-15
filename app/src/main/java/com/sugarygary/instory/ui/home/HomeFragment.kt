package com.sugarygary.instory.ui.home

import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SnapHelper
import com.sugarygary.instory.R
import com.sugarygary.instory.databinding.FragmentHomeBinding
import com.sugarygary.instory.ui.base.BaseFragment
import com.sugarygary.instory.util.StorySnapHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::inflate) {
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var storyAdapter: StoryAdapter
    private var isFirstBackPressed = false
    private fun openStoryDetail(id: String) {
        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment(id))
    }

    override fun setupUI() {
        //Matikan snapHelper jika mengganggu
        val snapHelper: SnapHelper = StorySnapHelper()
        storyAdapter = StoryAdapter(::openStoryDetail)
        val layoutManager =
            LinearLayoutManager(requireActivity())
        with(binding) {
            snapHelper.attachToRecyclerView(rvStories)
            rvStories.layoutManager = layoutManager
            rvStories.adapter = storyAdapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    storyAdapter.retry()
                }
            )
        }
    }

    override fun setupListeners() {
        with(binding) {
            requireActivity().onBackPressedDispatcher.addCallback(this@HomeFragment,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (isFirstBackPressed) {
                            if (!findNavController().popBackStack()) {
                                requireActivity().finish()
                            }
                        } else {
                            isFirstBackPressed = true
                            Toast.makeText(
                                requireActivity(),
                                getString(R.string.press_back_again_to_exit_app), Toast.LENGTH_SHORT
                            ).show()
                            lifecycleScope.launch {
                                delay(2000)
                                isFirstBackPressed = false
                            }
                        }
                    }
                })
            refreshLayout.setOnRefreshListener {
                storyAdapter.refresh()
                refreshLayout.isRefreshing = false
            }
            materialToolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_logout -> {
                        lifecycleScope.launch(Dispatchers.Main) {
                            viewModel.logout()
                            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
                        }
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_language -> {
                        requireActivity().startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                        return@setOnMenuItemClickListener true
                    }

                    R.id.action_maps -> {
                        findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment())
                        return@setOnMenuItemClickListener true
                    }
                }
                false
            }
            materialToolbar.setOnClickListener {
                if ((rvStories.adapter?.itemCount ?: 0) > 0) {
                    rvStories.smoothScrollToPosition(0)
                }
                refreshLayout.isRefreshing = true
                storyAdapter.refresh()
                refreshLayout.isRefreshing = false
            }
            floatingActionButton.setOnClickListener {
                val extras = FragmentNavigatorExtras(floatingActionButton to "transform_add")
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToAddFragment(), extras
                )
            }
        }
    }

    override fun setupObservers() {
        viewModel.stories.observe(viewLifecycleOwner) {
            storyAdapter.submitData(viewLifecycleOwner.lifecycle, it)
        }
    }
}