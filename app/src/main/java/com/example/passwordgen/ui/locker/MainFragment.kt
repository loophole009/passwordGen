package com.example.passwordgen.ui.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.passwordgen.R
import com.example.passwordgen.databinding.FragmentMainBinding
import com.example.passwordgen.models.Locker
import com.example.passwordgen.util.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val lockerViewModel by viewModels<LockerViewModel>()

    private lateinit var adapter: LockerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        adapter = LockerAdapter(::onLockerClicked)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeContainer.setOnRefreshListener {
            lockerViewModel.getAllLockers()
            bindObservers()
        }
        lockerViewModel.getAllLockers()
        binding.lockerList.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        binding.lockerList.adapter = adapter
        binding.addLocker.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_lockerFragment)
        }
        bindObservers()
    }

    private fun bindObservers() {
        lockerViewModel.lockersLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    adapter.submitList(it.data)
                    binding.swipeContainer.isRefreshing = false
                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_SHORT)
                        .show()
                }

                is NetworkResult.Loading -> {

                }
            }
        })
    }

    private fun onLockerClicked(locker: Locker) {
        val bundle = Bundle()
        bundle.putString("locker", Gson().toJson(locker))
        findNavController().navigate(R.id.action_mainFragment_to_lockerFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}