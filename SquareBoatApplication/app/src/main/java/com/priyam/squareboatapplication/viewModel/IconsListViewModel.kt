package com.priyam.squareboatapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priyam.squareboatapplication.repository.IconRepository
import com.priyam.squareboatapplication.responseBody.IconResponseBody
import com.priyam.squareboatapplication.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class IconsListViewModel @Inject constructor(private var iconRepository: IconRepository): ViewModel() {
    val iconResponse : MutableLiveData<Resource<IconResponseBody>> = MutableLiveData()

    fun getIcon( count: Int,identifier:String){
        viewModelScope.launch {
            iconResponse.postValue(Resource.Loading())
            val response = iconRepository.getIcon(count,identifier)
            iconResponse.postValue(response)
        }
    }

    fun searchIcon( query:String){
        viewModelScope.launch {
            iconResponse.postValue(Resource.Loading())
            val response = iconRepository.searchIcon(query)
            iconResponse.postValue(response)
        }
    }
}