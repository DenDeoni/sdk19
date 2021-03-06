/*
 * MIT License
 *
 * Copyright (c) 2020 Petrus Nguyễn Thái Học
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.test.sdk19.viewbindingdelegate

import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Used to implement [ViewBinding] property delegate for the [Dialog] of [DialogFragment].
 *
 * @param fragment the [DialogFragment].
 * @param rootId the root id of custom view of the [Dialog].
 * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] eg: `T::bind` static method can be used.
 * @param viewBindingClass if viewBindingBind is not provided, Kotlin Reflection will be used to get `T::bind` static method.
 */
class DialogFragmentViewBindingDelegate<T : ViewBinding, DF> private constructor(
    private val fragment: DF,
    @IdRes private val rootId: Int,
    viewBindingBind: ((View) -> T)? = null,
    viewBindingClass: Class<T>? = null
) : ReadOnlyProperty<DialogFragment, T> where DF : DialogFragment, DF : ViewBindingDialogFragment {
    private var binding: T? = null
    private val bind = viewBindingBind ?: { view: View ->
        @Suppress("UNCHECKED_CAST")
        GetBindMethod(viewBindingClass!!)(null, view) as T
    }

    init {
        ensureMainThread()
        require(viewBindingBind != null || viewBindingClass != null) {
            "Both viewBindingBind and viewBindingClazz are null. Please provide at least one."
        }

        fragment.lifecycle.addObserver(FragmentLifecycleObserver())
    }

    override fun getValue(thisRef: DialogFragment, property: KProperty<*>): T {
        return binding ?: bind(thisRef.requireDialog().findViewById(rootId))
                .also { binding = it }
    }

    private inner class FragmentLifecycleObserver : DefaultLifecycleObserver {
        override fun onCreate(owner: LifecycleOwner) {
            fragment.onDestroyViewLiveData.observe(fragment) { listeners ->
                (listeners ?: return@observe) += {
                    log { "$fragment::onDestroyView" }

                    MainHandler.post {
                        binding = null
                        log { "$fragment MainHandler.post { binding = null }" }
                    }
                }
            }

            log { "$fragment::onCreate" }
        }

        override fun onDestroy(owner: LifecycleOwner) {
            fragment.lifecycle.removeObserver(this)
            binding = null

            log { "$fragment::onDestroy" }
        }
    }

    companion object Factory {
        /**
         * Create [DialogFragmentViewBindingDelegate] from [viewBindingBind] lambda function.
         *
         * @param viewBindingBind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] eg: `T::bind` static method can be used.
         */
        public fun <T : ViewBinding, DF> from(
            fragment: DF,
            @IdRes rootId: Int,
            viewBindingBind: (View) -> T
        ): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment =
            DialogFragmentViewBindingDelegate(
                fragment = fragment,
                viewBindingBind = viewBindingBind,
                rootId = rootId,
            )

        /**
         * Create [DialogFragmentViewBindingDelegate] from [viewBindingClass] class.
         *
         * @param viewBindingClass Kotlin Reflection will be used to get `T::bind` static method from this class.
         */
        public fun <T : ViewBinding, DF> from(
            fragment: DF,
            @IdRes rootId: Int,
            viewBindingClass: Class<T>
        ): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment =
            DialogFragmentViewBindingDelegate(
                fragment = fragment,
                viewBindingClass = viewBindingClass,
                rootId = rootId,
            )
    }
}
