package com.samplewp.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.google.android.material.snackbar.Snackbar
import com.samplewp.R
import kotlinx.android.synthetic.main.activity_main.*


abstract class BaseActivity<out V : ViewDataBinding> : AppCompatActivity() {


    abstract fun getContentView(): Int

    private var dialog: AlertDialog? = null
    fun updateTitle(title: String) {
        actionBar?.title = title
        supportActionBar?.title = title
    }

    fun showActionBarBackButton(isShow: Boolean) {
        supportActionBar?.setDisplayShowHomeEnabled(isShow)
        supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(menuItem)
    }


    fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val nw = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }

    fun showSnackBar(message: String) {
        val mySnackBar = Snackbar.make(
            sr_pull,
            message, Snackbar.LENGTH_SHORT
        )
        mySnackBar.setAction(getString(R.string.ok)) { mySnackBar.dismiss() }
        mySnackBar.show()
    }

    fun showProgress() {
        if (dialog == null)
            dialog =
                AlertDialog.Builder(this).setCancelable(false).setView(R.layout.progress_layout)
                    .create()
        dialog!!.show()
    }

    fun hideProgress() {
        if (dialog != null && dialog!!.isShowing)
            dialog!!.hide()
    }
}