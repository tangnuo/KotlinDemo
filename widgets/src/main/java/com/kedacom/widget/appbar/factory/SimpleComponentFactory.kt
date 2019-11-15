package com.kedacom.widget.appbar.factory

import android.support.annotation.RestrictTo
import com.kedacom.widget.appbar.BarMode

/**
 * ComponentFactory简单工厂
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal object SimpleComponentFactory {

    fun createComponentFactory(type: BarMode) = when (type) {
        BarMode.NORMAL -> NativeBarComponentFactory()
        BarMode.MICRO -> MicroBarComponentFactory()
    }
}