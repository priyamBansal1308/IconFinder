package com.priyam.squareboatapplication.di.IconsList

import androidx.lifecycle.ViewModel
import com.priyam.squareboatapplication.di.ViewModelKey
import com.priyam.squareboatapplication.viewModel.IconsListViewModel
import com.priyam.squareboatapplication.viewModel.MainActivityViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class IconsListViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(IconsListViewModel::class)
    abstract fun bindMainViewModel(iconsListViewModel: IconsListViewModel): ViewModel

}
