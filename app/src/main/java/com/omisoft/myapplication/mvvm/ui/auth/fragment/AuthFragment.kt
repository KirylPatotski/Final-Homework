package com.omisoft.myapplication.mvvm.ui.auth.fragment

import android.content.Context
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
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.omisoft.myapplication.MainActivity
import com.omisoft.myapplication.R
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthService
import com.omisoft.myapplication.mvvm.data.network.service.auth.NetworkAuthServiceImpl
import com.omisoft.myapplication.mvvm.data.storage.LocalStorageModel
import com.omisoft.myapplication.mvvm.data.storage.UserStorage
import com.omisoft.myapplication.mvvm.data.storage.preferences.AppPreferencesImpl
import com.omisoft.myapplication.mvvm.ui.auth.AuthViewModel
import com.omisoft.myapplication.mvvm.ui.auth.AuthViewModelFactory
import com.omisoft.myapplication.mvvm.ui.music.fragment.MusicFragment
import java.io.File


class AuthFragment : Fragment() {

    private lateinit var progress: ProgressBar
    private lateinit var overlay: FrameLayout
    private lateinit var loginField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var saveCredentialsCheckBox: AppCompatCheckBox
    private var titleText: AppCompatTextView? = null

    private val viewModel by viewModels<AuthViewModel> {
        AuthViewModelFactory(
            NetworkAuthServiceImpl() as NetworkAuthService,
            LocalStorageModel() as UserStorage,
            AppPreferencesImpl.getInstance(requireContext())
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchStoredData()

        buttonLogin = view.findViewById(R.id.button_login)
        loginField = view.findViewById(R.id.input_layout_login)
        passwordField = view.findViewById(R.id.input_layout_password)
        overlay = view.findViewById(R.id.overlay_container)
        progress = view.findViewById(R.id.progress)
        titleText = view.findViewById(R.id.title_text)
        saveCredentialsCheckBox = view.findViewById(R.id.save_credentials_check_box)

        if (loginField.editText?.text?.isBlank() == true) {
            ContextCompat.getColorStateList(requireContext(), R.color.auth_input_layout_stroke_color_default)?.let {
                loginField.setBoxStrokeColorStateList(it)
            }
        } else {
            ContextCompat.getColorStateList(requireContext(), R.color.auth_input_layout_stroke_color)?.let { colorList ->
                loginField.setBoxStrokeColorStateList(colorList)
            }
        }

        initListeners()
        subscribeOnLiveData()
    }

    private fun initListeners() {
        loginField.editText?.addTextChangedListener {
            it?.let {
                viewModel.setUpdatedEmail(it.toString())
                if (it.isBlank()) {
                    ContextCompat.getColorStateList(requireContext(), R.color.auth_input_layout_stroke_color_default)?.let { colorList ->
                        loginField.setBoxStrokeColorStateList(colorList)
                    }
                } else {
                    ContextCompat.getColorStateList(requireContext(), R.color.auth_input_layout_stroke_color)?.let { colorList ->
                        loginField.setBoxStrokeColorStateList(colorList)
                    }
                }
            }
        }
        passwordField.editText?.addTextChangedListener {
            viewModel.setUpdatedPassword(it.toString())
        }
        buttonLogin.setOnClickListener {
            val emailText = loginField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            viewModel.onLoginClicked(emailText, passwordText)
            viewModel.saveCredentialsToFile(
                emailText,
                passwordText,
                File(requireActivity().getDir("credentials", Context.MODE_PRIVATE).absolutePath + "/" + "credentials.txt")
            )
        }
        saveCredentialsCheckBox.setOnCheckedChangeListener { _, selected ->
            viewModel.setSaveCredentialsSelected(selected)
        }
    }

    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(viewLifecycleOwner, {
            (activity as MainActivity).openFragment(MusicFragment(), doClearBackStack = true, tag = "MusicFragment")
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