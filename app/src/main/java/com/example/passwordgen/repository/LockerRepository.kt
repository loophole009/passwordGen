package com.example.passwordgen.repository

import com.example.passwordgen.db.LockerDatabase
import com.example.passwordgen.models.Locker
import com.example.passwordgen.util.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class LockerRepository @Inject constructor(private val lockerDatabase: LockerDatabase) {


    private val _lockersFlow = MutableStateFlow<NetworkResult<List<Locker>>>(NetworkResult.Loading())
    val lockersFlow get() = _lockersFlow

    private val _statusFlow = MutableStateFlow<NetworkResult<Pair<Boolean, String>>>(NetworkResult.Loading())
    val statusFlow get() = _statusFlow

    suspend fun getLockers() {
        _lockersFlow.emit(NetworkResult.Loading())
        val lockers = lockerDatabase.getLockerDao().getLockers()
        _lockersFlow.emit(NetworkResult.Success(lockers))
    }

    suspend fun updateLocker(locker: Locker) {
        if (locker.password == lockerDatabase.getLockerDao().getLockerPassword(locker.website)) {
            _statusFlow.emit(NetworkResult.Error("password not updated"))
        } else if (locker.website.isNotEmpty() && locker.username.isNotEmpty() && locker.password.isNotEmpty()) {
            lockerDatabase.getLockerDao().insertLocker(locker)
            handleResponse(locker, "Locker Created")
        } else {
            _statusFlow.emit(NetworkResult.Error("all input data required"))
        }
    }

    suspend fun deleteLocker(locker: Locker) {
        if (locker.website.isNotEmpty()) {
            lockerDatabase.getLockerDao().deleteLocker(locker)
            handleResponse(locker, "Note Deleted")
        } else {
            _statusFlow.emit(NetworkResult.Error("website data required"))
        }
    }

    private suspend fun handleResponse(locker: Locker, message: String) {
        if (locker.website.isNotEmpty() && locker.username.isNotEmpty() && locker.password.isNotEmpty()) {
            _statusFlow.emit(NetworkResult.Success(Pair(true, message)))
        } else {
            _statusFlow.emit(NetworkResult.Success(Pair(false, "Something went wrong")))
        }
    }

}
