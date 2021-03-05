package com.test.lifehackstudio.viewbindingdelegate

import android.app.Dialog
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MainThread
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding(): FragmentViewBindingDelegate<T> =
    FragmentViewBindingDelegate(
        fragment = this,
        viewBindingClass = T::class.java
    )

/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DialogFragment].
 *
 * @param bind a lambda function that creates a [ViewBinding] instance from root view of the [Dialog] of this [DialogFragment],
 *        eg: `T::bind` static method can be used.
 */

@MainThread
fun <DF, T : ViewBinding> DF.dialogFragmentViewBinding(
    @IdRes rootId: Int,
    bind: (View) -> T
): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment {
    return DialogFragmentViewBindingDelegate.from(
        fragment = this,
        viewBindingBind = bind,
        rootId = rootId,
    )
}
/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DialogFragment].
 */
@MainThread
inline fun <DF, reified T : ViewBinding> DF.dialogFragmentViewBinding(
    @IdRes rootId: Int
): DialogFragmentViewBindingDelegate<T, DF> where DF : DialogFragment, DF : ViewBindingDialogFragment {
    return DialogFragmentViewBindingDelegate.from(
        fragment = this,
        viewBindingClass = T::class.java,
        rootId = rootId,
    )
}
/**
 * Create [ViewBinding] property delegate for the [Dialog] of this [DefaultViewBindingDialogFragment].
 */
@MainThread
inline fun <reified T : ViewBinding> DefaultViewBindingDialogFragment.dialogFragmentViewBinding(
    @IdRes rootId: Int
): DialogFragmentViewBindingDelegate<T, DefaultViewBindingDialogFragment> =
    dialogFragmentViewBinding<DefaultViewBindingDialogFragment, T>(rootId)
