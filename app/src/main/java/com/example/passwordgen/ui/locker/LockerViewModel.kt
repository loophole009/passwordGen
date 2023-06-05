package com.example.passwordgen.ui.locker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.passwordgen.models.Locker
import com.example.passwordgen.repository.LockerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

@HiltViewModel
class LockerViewModel @Inject constructor(private val lockerRepository: LockerRepository) :
    ViewModel() {


    val lockersFlow get() = lockerRepository.lockersFlow
    val statusFlow get() = lockerRepository.statusFlow

    val algorithm = "AES/CBC/PKCS5Padding"
    val key = SecretKeySpec("1234567890123456".toByteArray(), "AES")
    val iv = IvParameterSpec(ByteArray(16))

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