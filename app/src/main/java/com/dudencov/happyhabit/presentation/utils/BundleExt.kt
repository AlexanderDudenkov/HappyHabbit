package com.dudencov.happyhabit.presentation.utils

import android.os.Build
import android.os.Bundle
import android.os.Parcelable

fun <T : Parcelable> Bundle.getParcelableExt(key: String?, clazz: Class<T>): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getParcelable(key, clazz)
    } else {
        @Suppress("DEPRECATION")
        getParcelable(key) as? T?
    }
}