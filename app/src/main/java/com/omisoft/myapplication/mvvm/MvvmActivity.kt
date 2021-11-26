package com.omisoft.myapplication.mvvm

import android.content.Intent
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.omisoft.myapplication.R
import com.omisoft.myapplication.success.SuccessActivity


class MvvmActivity : AppCompatActivity() {
    private lateinit var progress: ProgressBar
    private lateinit var overlay: FrameLayout
    private lateinit var viewModel: MvvmViewModel
    private var titleText: AppCompatTextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mvp)
        viewModel = ViewModelProvider(this)[MvvmViewModel::class.java]

        val buttonLogin: AppCompatButton = findViewById(R.id.button_login)
        val loginField: TextInputLayout = findViewById(R.id.input_layout_login)
        val passwordField: TextInputLayout = findViewById(R.id.input_layout_password)
        overlay = findViewById(R.id.overlay_container)
        progress = findViewById(R.id.progress)
        titleText = findViewById(R.id.title_text)

        buttonLogin.setOnClickListener {
            val emailText = loginField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            viewModel.onLoginClicked(emailText, passwordText)
        }

        subscribeOnLiveData()
    }

    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(this, {
            val intent = Intent(this, SuccessActivity::class.java)
            startActivity(intent)
        })
        viewModel.isLoginFailedLiveData.observe(this, {
            Toast.makeText(this, "Something went wrong. Please, retry!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(this, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(this, {
            hideProgress()
        })
        viewModel.titleLiveData.observe(this, { title ->
            titleText?.text = title
        })
    }

    private fun hideProgress() {
        progress.isVisible = false
        overlay.isVisible = false
    }

    private fun showProgress() {
        progress.isVisible = true
        overlay.isVisible = true
    }
}