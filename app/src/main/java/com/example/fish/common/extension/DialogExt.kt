package com.example.fish.common.extension

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.view.Window
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager

@RequiresApi(api = Build.VERSION_CODES.M)
fun Dialog.setWhiteNavigationBarBottomSheetDialog() {
    val window: Window? = window
    if (window != null) {
        val metrics = DisplayMetrics()
        window.windowManager.defaultDisplay.getMetrics(metrics)
        val dimDrawable = GradientDrawable()
        val navigationBarDrawable = GradientDrawable()
        navigationBarDrawable.shape = GradientDrawable.RECTANGLE
        navigationBarDrawable.setColor(Color.WHITE)
        val layers: Array<Drawable> =
            arrayOf(dimDrawable, navigationBarDrawable)
        val windowBackground = LayerDrawable(layers)
        windowBackground.setLayerInsetTop(1, metrics.heightPixels)
        window.setBackgroundDrawable(windowBackground)
    }
}

fun DialogFragment.showIfNotAdded(fragmentManager: FragmentManager, tag: String?) {
    if (!isAdded) {
        show(fragmentManager, tag)
    }
}