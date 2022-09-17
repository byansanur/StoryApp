package com.byandev.storyapp.presentation.home

import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.byandev.storyapp.R
import com.byandev.storyapp.adapter.AdapterStoryLocationPaging
import com.byandev.storyapp.adapter.AdapterStoryPaging
import com.byandev.storyapp.adapter.FooterStateAdapter
import com.byandev.storyapp.data.model.Story
import com.byandev.storyapp.databinding.FragmentHomeBinding
import com.byandev.storyapp.di.SharedPrefManager
import com.byandev.storyapp.di.UtilsConnect
import com.byandev.storyapp.presentation.SharedViewModel
import com.byandev.storyapp.utils.dialogLoading
import com.byandev.storyapp.utils.handlingError
import com.byandev.storyapp.utils.textAsBitmap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

@AndroidEntryPoint
class FragmentHome : Fragment(), AdapterStoryPaging.StoryClickListener,
    AdapterStoryLocationPaging.StoryLocationClickListener {

    companion object {
        private const val TAG = "FragmentHome"
    }

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var sharedPrefManager: SharedPrefManager

    @Inject
    lateinit var utilsConnect: UtilsConnect

    private val sharedViewModel: SharedViewModel by viewModels()

    private lateinit var adapterStoryPaging: AdapterStoryPaging
    private lateinit var adapterStoryLocationPaging: AdapterStoryLocationPaging

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.story_app_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.fragmentAccount -> {
                findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentAccount2())
                true
            }
            else -> false
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if (sharedPrefManager.isIntroSp) {
            findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentIntroPermission())
        } else {
            if (!sharedPrefManager.isLogin) findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentLogin())
            else setViewHomeScreen()
        }

    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setViewHomeScreen() {
//        setupMenuHost()
        dialog = Dialog(requireContext())
        adapterStoryPaging = AdapterStoryPaging(this, sharedPrefManager)
        adapterStoryLocationPaging = AdapterStoryLocationPaging(this, sharedPrefManager)

        setupRecyclerViewOther()
        setupRecyclerViewLocation()

        binding.floatingAddStory.setOnClickListener {
            findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentAddStories())
        }
    }

    private fun setupRecyclerViewLocation() {
        binding.apply {
            rvStoryBasedLocation.adapter = adapterStoryLocationPaging.withLoadStateFooter(
                footer = FooterStateAdapter {
                    adapterStoryLocationPaging.retry()
                }
            )
            rvStoryBasedLocation.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            )
            rvStoryBasedLocation.setHasFixedSize(false)
            rvStoryBasedLocation.isNestedScrollingEnabled = true
        }
        callStoryNearby()
    }

    private fun setupRecyclerViewOther() {

        binding.apply {
            rvStoryOther.adapter = adapterStoryPaging.withLoadStateFooter(
                footer = FooterStateAdapter {
                    adapterStoryPaging.retry()
                }
            )
            rvStoryOther.layoutManager = LinearLayoutManager(requireContext())
            rvStoryOther.setHasFixedSize(false)
            rvStoryOther.isNestedScrollingEnabled = true
        }
        callStoryOther()
    }

    private fun callStoryNearby() {
        lifecycleScope.launchWhenCreated {
            dialogLoading(dialog)
            delay(2000)
            dialog.dismiss()
            sharedViewModel.getListStory(1)
                .distinctUntilChanged()
                .collectLatest { valueData ->
                    adapterStoryLocationPaging.submitData(valueData)
                }
            addLoadAdapterLocation()
        }
    }

    private fun callStoryOther() {
        lifecycleScope.launchWhenCreated {
            dialogLoading(dialog)
            delay(2000)

            sharedViewModel.getListStory(0)
                .distinctUntilChanged()
                .collectLatest { valueData ->
                    adapterStoryPaging.submitData(valueData)
                }
            dialog.dismiss()
        }
        addLoadAdapterOther()
    }

    private fun addLoadAdapterLocation() {
        if (utilsConnect.isConnectedToInternet()) {
            adapterStoryLocationPaging.addLoadStateListener { loadState ->
                when(loadState.source.refresh) {
                    is LoadState.Loading -> {
                        Log.e(TAG, "addLoadAdapterLocation: loading state")
                    }
                    is LoadState.NotLoading -> {
                        Log.e(TAG, "addLoadAdapterLocation: not loading state")
                        dialog.dismiss()
                        binding.rvStoryBasedLocation.isVisible = adapterStoryLocationPaging.itemCount > 1
                    }
                    is LoadState.Error -> {
                        binding.rvStoryBasedLocation.isVisible = false

                        val e = loadState.refresh as LoadState.Error
                        val msgErr = when (e.error) {
                            is UnknownHostException -> {
                                "Unknown Host"
                            }
                            is SocketTimeoutException -> {
                                "Request Timeout"
                            }
                            is Exception -> {
                                handlingError(e.error)
                            }
                            else -> "Error ${e.error.message}"
                        }
                        Log.e(TAG, "addLoadAdapterLocation: error state $msgErr")
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun addLoadAdapterOther() {
        if (utilsConnect.isConnectedToInternet()) {
            adapterStoryPaging.addLoadStateListener {
                when(it.source.refresh) {
                    is LoadState.Loading -> {
                        Log.e(TAG, "addLoadAdapterOther: loading state")
                    }
                    is LoadState.NotLoading -> {
                        Log.e(TAG, "addLoadAdapterOther: not loading state")
                        dialog.dismiss()
                        binding.rvStoryOther.isVisible = adapterStoryLocationPaging.itemCount > 1
                    }
                    is LoadState.Error -> {
                        binding.rvStoryBasedLocation.isVisible = false
                        val e = it.refresh as LoadState.Error
                        val msgErr = when (e.error) {
                            is UnknownHostException -> {
                                "Unknown Host"
                            }
                            is SocketTimeoutException -> {
                                "Request Timeout"
                            }
                            is Exception -> {
                                handlingError(e.error)
                            }
                            else -> "Error ${e.error.message}"
                        }
                        Log.e(TAG, "addLoadAdapterOther: error state $msgErr")
                    }
                }
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
    }

    private fun setupMenuHost() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.story_app_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.fragmentAccount -> {
                        findNavController().navigate(FragmentHomeDirections.actionFragmentHomeToFragmentAccount2())
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun storyClicked(story: Story) {
        TODO("Not yet implemented")
    }

    override fun storyLocationClicked(story: Story) {
        TODO("Not yet implemented")
    }
}