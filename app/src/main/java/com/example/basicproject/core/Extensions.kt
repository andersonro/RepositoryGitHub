package com.example.basicproject.core

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.example.basicproject.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout

var TextInputLayout.text: String
    get() = editText?.text?.toString() ?: ""
    set(value) {
        editText?.setText(value)
    }

fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun Context.createDialog(block: MaterialAlertDialogBuilder.() -> Unit = {}): AlertDialog {
    val builder = MaterialAlertDialogBuilder(this)
    builder.setPositiveButton(android.R.string.ok, null)
    block(builder)
    return builder.create()
}

fun Context.createProgressDialog(): AlertDialog {
    return createDialog {
        val padding =
            this@createProgressDialog.resources.getDimensionPixelOffset(R.dimen.dimen_default)
        val progressBar = ProgressBar(this@createProgressDialog)
        progressBar.setPadding(padding, padding, padding, padding)
        setView(progressBar)

        setPositiveButton(null, null)
        setCancelable(false)
    }
}

fun Context.hasInternet(context: Context?): Boolean {
    val connectivityManager =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        val network = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    } else {
        @Suppress("DEPRECATION")
        val networkInfo = connectivityManager.activeNetworkInfo ?: return false
        return networkInfo.isConnected
    }
}

fun Context.saveSharedPrefUser(user: String) {
    val sharedPref =
        getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
            ?: return
    with(sharedPref.edit()) {
        putString(getString(R.string.shared_preferences_key_user), user)
        apply()
    }
}

fun Context.getSharedPrefUser(): String {
    val sharedPref =
        getSharedPreferences(getString(R.string.shared_preferences_name), Context.MODE_PRIVATE)
            ?: return ""
    return sharedPref.getString(getString(R.string.shared_preferences_key_user), "").toString()
}