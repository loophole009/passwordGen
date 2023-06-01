package com.example.passwordgen.ui.locker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.passwordgen.R
import com.example.passwordgen.databinding.FragmentLockerBinding
import com.example.passwordgen.models.Locker
import com.example.passwordgen.util.NetworkResult
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Date

@AndroidEntryPoint
class LockerFragment : Fragment() {

    private var _binding: FragmentLockerBinding? = null
    private val binding get() = _binding!!
    private val lockerViewModel by viewModels<LockerViewModel>()
    private var locker: Locker? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLockerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialData()
        bindHandlers()
        bindObservers()
    }

    private fun bindObservers() {
        lockerViewModel.statusLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is NetworkResult.Success -> {
                    findNavController().popBackStack()
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

    @SuppressLint("SimpleDateFormat")
    private fun bindHandlers() {
        binding.apply {

            btnSubmit.setOnClickListener {
                val website = txtWebsite.text.toString()
                val username = txtUsername.text.toString()
                val password = txtPassword.text.toString()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val input = Locker(website, username, password, currentDate)
                lockerViewModel.updateLocker(input)
            }

            btnDelete.setOnClickListener {
                val website = txtWebsite.text.toString()
                val username = txtUsername.text.toString()
                val password = txtPassword.text.toString()
                val sdf = SimpleDateFormat("yyyy-MM-dd")
                val currentDate = sdf.format(Date())
                val input = Locker(website, username, password, currentDate)
                lockerViewModel.deleteLocker(input)
            }
        }
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setInitialData() {
        val jsonLocker = arguments?.getString("locker")
        if (jsonLocker != null) {
            locker = Gson().fromJson<Locker>(jsonLocker, Locker::class.java)
            locker?.let {
                binding.txtWebsite.setText(it.website)
                binding.txtUsername.setText(it.username)
                binding.txtPassword.setText(it.password)
            }
        } else {
            binding.addEditText.text = resources.getString(R.string.add_locker)
            binding.btnSubmit.text = resources.getString(R.string.txt_submit)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}