package com.ivancaas.beersapp.util

import androidx.annotation.StringRes
import com.ivancaas.beersapp.App

object Strings {
    fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
        return App.instance.getString(stringRes, *formatArgs)
    }
}