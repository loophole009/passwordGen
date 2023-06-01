package com.example.passwordgen.repository

import androidx.lifecycle.MutableLiveData
import com.example.passwordgen.db.LockerDatabase
import com.example.passwordgen.models.Locker
import com.example.passwordgen.util.NetworkResult
import javax.inject.Inject


class LockerRepository @Inject constructor(private val lockerDatabase: LockerDatabase) {


    private val _lockersLiveData = MutableLiveData<NetworkResult<List<Locker>>>()
    val lockersLiveData get() = _lockersLiveData

    private val _statusLiveData = MutableLiveData<NetworkResult<Pair<Boolean, String>>>()
    val statusLiveData get() = _statusLiveData

    suspend fun getLockers() {
        _lockersLiveData.postValue(NetworkResult.Loading())
        val lockers = lockerDatabase.getLockerDao().getLockers()
        _lockersLiveData.postValue(NetworkResult.Success(lockers))
    }


    suspend fun updateLocker(locker: Locker) {
        _statusLiveData.postValue(NetworkResult.Loading())
        if (locker.password == lockerDatabase.getLockerDao().getLockerPassword(locker.website)) {
            _statusLiveData.postValue(NetworkResult.Error("password not updated"))
        } else if (locker.website.isNotEmpty() && locker.username.isNotEmpty() && locker.password.isNotEmpty()) {
            lockerDatabase.getLockerDao().insertLocker(locker)
            handleResponse(locker, "Locker Created")
        } else {
            _statusLiveData.postValue(NetworkResult.Error("all input data required"))
        }
    }

    suspend fun deleteLocker(locker: Locker) {
        _statusLiveData.postValue(NetworkResult.Loading())
        if (locker.website.isNotEmpty()) {
            lockerDatabase.getLockerDao().deleteLocker(locker)
            handleResponse(locker, "Note Deleted")
        } else {
            _statusLiveData.postValue(NetworkResult.Error("website data required"))
        }
    }

    private fun handleResponse(locker: Locker, message: String) {
        if (locker.website.isNotEmpty() && locker.username.isNotEmpty() && locker.password.isNotEmpty()) {
            _statusLiveData.postValue(NetworkResult.Success(Pair(true, message)))
        } else {
            _statusLiveData.postValue(NetworkResult.Success(Pair(false, "Something went wrong")))
        }
    }
}
