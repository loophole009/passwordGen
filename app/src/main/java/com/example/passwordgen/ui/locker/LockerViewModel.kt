package com.example.passwordgen.ui.locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordgen.models.Locker
import com.example.passwordgen.repository.LockerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LockerViewModel @Inject constructor(private val lockerRepository: LockerRepository) :
    ViewModel() {


    val lockersLiveData get() = lockerRepository.lockersLiveData
    val statusLiveData get() = lockerRepository.statusLiveData


    fun updateLocker(locker: Locker) {
        viewModelScope.launch {
            lockerRepository.updateLocker(locker)
        }
    }

    fun getAllLockers() {
        viewModelScope.launch {
            lockerRepository.getLockers()
        }
    }

    fun deleteLocker(locker: Locker) {
        viewModelScope.launch {
            lockerRepository.deleteLocker(locker)
        }
    }

}