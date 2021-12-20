package com.omisoft.myapplication.mvvm.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputLayout
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.model.storage.preferences.AppPreferencesImpl
import com.omisoft.myapplication.mvvm.ui.auth.AuthViewModel
import com.omisoft.myapplication.mvvm.ui.draft.countries.fragment.AlbumsListFragment


class AuthFragment : Fragment() {

    private lateinit var viewModel: AuthViewModel
    private lateinit var progress: ProgressBar
    private lateinit var overlay: FrameLayout
    private lateinit var loginField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var saveCredentialsCheckBox: AppCompatCheckBox
    private var titleText: AppCompatTextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        viewModel.setSharedPreferences(AppPreferencesImpl.getInstance(requireContext()))
        viewModel.fetchStoredData()

        requireActivity().supportFragmentManager.setFragmentResult(
            MainActivity.NAVIGATION_EVENT,
            bundleOf(MainActivity.NAVIGATION_EVENT_DATA_KEY to "AuthFragment Created")
        )

        buttonLogin = view.findViewById(R.id.button_login)
        loginField = view.findViewById(R.id.input_layout_login)
        passwordField = view.findViewById(R.id.input_layout_password)
        overlay = view.findViewById(R.id.overlay_container)
        progress = view.findViewById(R.id.progress)
        titleText = view.findViewById(R.id.title_text)
        saveCredentialsCheckBox = view.findViewById(R.id.save_credentials_check_box)

        initListeners()
        subscribeOnLiveData()
    }

    private fun initListeners() {
        loginField.editText?.addTextChangedListener {
            viewModel.setUpdatedEmail(it.toString())
        }
        passwordField.editText?.addTextChangedListener {
            viewModel.setUpdatedPassword(it.toString())
        }
        buttonLogin.setOnClickListener {
            val emailText = loginField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            viewModel.onLoginClicked(emailText, passwordText)
        }
        saveCredentialsCheckBox.setOnCheckedChangeListener { _, selected ->
            viewModel.setSaveCredentialsSelected(selected)
        }
    }

    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(viewLifecycleOwner, {
            (activity as MainActivity).openFragment(AlbumsListFragment(), doClearBackStack = true)
        })
        viewModel.isLoginFailedLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Something went wrong. Please, retry!", Toast.LENGTH_LONG).show()
        })
        viewModel.showProgressLiveData.observe(viewLifecycleOwner, {
            showProgress()
        })
        viewModel.hideProgressLiveData.observe(viewLifecycleOwner, {
            hideProgress()
        })
        viewModel.titleLiveData.observe(viewLifecycleOwner, { title ->
            titleText?.text = title
        })
        viewModel.saveCredentialsCheckedLiveData.observe(viewLifecycleOwner, { isSelected ->
            saveCredentialsCheckBox.isChecked = isSelected
        })
        viewModel.emailLiveData.observe(viewLifecycleOwner, { email ->
            loginField.editText?.setText(email)
            loginField.editText?.setSelection(email.length)
        })
        viewModel.passwordLiveData.observe(viewLifecycleOwner, { password ->
            passwordField.editText?.setText(password)
            passwordField.editText?.setSelection(password.length)
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