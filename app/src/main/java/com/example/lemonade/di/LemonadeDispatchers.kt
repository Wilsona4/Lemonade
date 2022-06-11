package com.example.lemonade.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val dispatcher: LemonadeDispatchers)

enum class LemonadeDispatchers {
    DEFAULT, IO, MAIN, UNCONFINED
}