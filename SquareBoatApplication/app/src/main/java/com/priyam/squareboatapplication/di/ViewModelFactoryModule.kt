package com.priyam.squareboatapplication.di

import androidx.lifecycle.ViewModelProvider
import com.priyam.squareboatapplication.viewModel.ViewModelProviderFactory

import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelFactoryModule {
    @Binds
    abstract fun bindViewModelFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}