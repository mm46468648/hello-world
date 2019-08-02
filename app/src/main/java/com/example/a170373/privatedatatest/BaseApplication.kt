package com.example.a170373.privatedatatest

import android.app.Application
import android.content.Context
import kotlin.properties.Delegates

class BaseApplication:Application() {
    companion object {
        // 单例不会是null   所以使用notNull委托
        var instance: BaseApplication by Delegates.notNull()

    }
    override fun onCreate() {
        super.onCreate()

        instance = this
    }
}