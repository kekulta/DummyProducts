package com.kekulta.dummyproducts.features.shared.utils

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
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
