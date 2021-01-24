package com.priyam.squareboatapplication.di

import android.app.Application


import com.priyam.squareboatapplication.BaseApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Component(
        modules = [AndroidInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class,
        ViewModelFactoryModule::class
        ]
)
@Singleton
interface AppComponent:AndroidInjector<BaseApplication> {

    @Component.Builder
    interface Builder{
        @BindsInstance
        fun application(application:Application): Builder
        fun build(): AppComponent
    }
}