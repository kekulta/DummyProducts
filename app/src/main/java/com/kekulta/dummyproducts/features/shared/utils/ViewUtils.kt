package com.kekulta.dummyproducts.features.shared.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.Dimension
import androidx.core.view.allViews
import androidx.fragment.app.Fragment

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun <V : View, T> V.applyOrGone(param: T?, block: V.(param: T) -> Unit) {
    if (param == null) {
        gone()
        return
    }

    block(param)
    show()
}

fun <V : View, T> V.applyOrHide(param: T?, block: V.(param: T) -> Unit) {
    if (param == null) {
        hide()
        return
    }

    block(param)
    show()
}

@SuppressLint("ClickableViewAccessibility")
fun View.disableInteractions() {
    setOnTouchListener { _, _ -> true }
    allViews.forEach { it.setOnTouchListener { _, _ -> true } }
}

fun View.enableInteractions() {
    setOnTouchListener(null)
    allViews.forEach { it.setOnTouchListener(null) }
}

fun View.setPadding(@Dimension(unit = Dimension.DP) padding: Int) {
    setPadding(dip(padding), dip(padding), dip(padding), dip(padding))
}

fun Activity.setNavBarColor(@AttrRes color: Int) {
    window.navigationBarColor = getMaterialColor(color)
}

fun Fragment.setNavBarColor(@AttrRes color: Int) {
    requireActivity().setNavBarColor(color)
}