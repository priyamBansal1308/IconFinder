package com.priyam.squareboatapplication.di


import com.priyam.squareboatapplication.di.IconsList.IconsListModule
import com.priyam.squareboatapplication.di.IconsList.IconsListViewModelModule
import com.priyam.squareboatapplication.view.activity.MainActivity
import com.priyam.squareboatapplication.di.main.MainModule
import com.priyam.squareboatapplication.di.main.MainViewModelModule
import com.priyam.squareboatapplication.view.activity.IconListActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {
    @ContributesAndroidInjector(
            modules = [MainViewModelModule::class,
            MainModule::class]
    )
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(
        modules = [IconsListViewModelModule::class,
            IconsListModule::class]
    )
    abstract fun contributeIconListActivity(): IconListActivity
}