package com.priyam.squareboatapplication.di.main

import androidx.lifecycle.ViewModel
import com.priyam.squareboatapplication.di.ViewModelKey
import com.priyam.squareboatapplication.viewModel.MainActivityViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class MainViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: MainActivityViewModel): ViewModel

}
