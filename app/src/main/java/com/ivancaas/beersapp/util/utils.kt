package com.ivancaas.beersapp.util

infix fun <T> Boolean.then(param: T): T? = if (this) param else null
