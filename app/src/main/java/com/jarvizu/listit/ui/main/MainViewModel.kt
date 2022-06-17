package com.jarvizu.listit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.kittinunf.result.success
import com.jarvizu.listit.api.ApiHelper
import com.jarvizu.listit.data.CommentObject
import com.jarvizu.listit.data.ListObject
import com.jarvizu.listit.data.RedditResponse
import com.jarvizu.listit.data.local.MainRepository
import com.jarvizu.listit.data.local.Resource
import com.jarvizu.listit.data.local.Status
import com.skydoves.whatif.whatIf
import com.skydoves.whatif.whatIfNotNull
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Objects
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _repo = MutableLiveData<Resource<ListObject>>()

    val repo: MutableLiveData<Resource<ListObject>>
        get() = _repo

    private val _comments = MutableLiveData<Resource<CommentObject>>()

    val comments: MutableLiveData<Resource<CommentObject>>
        get() = _comments

    init {
        Timber.d("MainViewModel Created:")
        getList()
    }

    fun getList() = viewModelScope.launch {
        mainRepository.getList().whatIfNotNull {
            Timber.d("Get Success$it")
            _repo.postValue(Resource.success(it))
            if (Objects.isNull(it)) {
                _repo.postValue(Resource.error())
            }
        }
    }

    fun getComments(post : String) = viewModelScope.launch {
        try {
            mainRepository.getComments(post).whatIfNotNull {
                Timber.d("Get Success$it")
                _comments.postValue(Resource.success(it))
                if (Objects.isNull(it)) {
                    _comments.postValue(Resource.error())
                }
            }
        } catch (exception : Exception) {
            Timber.e(exception)
        }
    }
}