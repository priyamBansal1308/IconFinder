package com.priyam.squareboatapplication.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.priyam.squareboatapplication.repository.IconSetRepository
import com.priyam.squareboatapplication.responseBody.IconSetResponseBody
import com.priyam.squareboatapplication.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainActivityViewModel @Inject constructor(private val iconSetRepository: IconSetRepository):ViewModel(){
val iconSetResponse : MutableLiveData<Resource<IconSetResponseBody>> = MutableLiveData()

    fun getIconSet(  count: String, isPremium: Boolean ){
        viewModelScope.launch {
         iconSetResponse.postValue(Resource.Loading())
         val response = iconSetRepository.getIconSet(count, isPremium)
         iconSetResponse.postValue(response)
        }
    }
}
