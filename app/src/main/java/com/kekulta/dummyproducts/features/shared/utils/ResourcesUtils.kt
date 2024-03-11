package com.kekulta.dummyproducts.features.shared.utils

import android.app.Activity
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.google.android.material.color.MaterialColors

@ColorInt
fun View.getMaterialColor(@AttrRes resId: Int): Int {
    return MaterialColors.getColor(this, resId)
}

fun View.getMaterialColorStateList(@AttrRes resId: Int): ColorStateList {
    return ColorStateList.valueOf(getMaterialColor(resId))
}

fun View.getDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, context.theme)
}

@ColorInt
fun Fragment.getMaterialColor(@AttrRes resId: Int): Int {
    return MaterialColors.getColor(requireContext(), resId, Color.RED)
}

fun Fragment.getMaterialColorStateList(@AttrRes resId: Int): ColorStateList {
    return ColorStateList.valueOf(getMaterialColor(resId))
}

fun Fragment.getDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, requireContext().theme)
}

@ColorInt
fun Activity.getMaterialColor(@AttrRes resId: Int): Int {
    return MaterialColors.getColor(this, resId, Color.RED)
}

fun Activity.getMaterialColorStateList(@AttrRes resId: Int): ColorStateList {
    return ColorStateList.valueOf(getMaterialColor(resId))
}

fun Activity.getDrawable(@DrawableRes resId: Int): Drawable? {
    return ResourcesCompat.getDrawable(resources, resId, theme)
}
