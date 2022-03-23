package com.homework.app.mvvw.ui.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.textfield.TextInputLayout
import com.homework.app.R
import com.homework.app.mvvw.AppPreferencesImpl
import com.homework.app.mvvw.ui.main.MainActivity
import com.homework.app.mvvw.ui.music.fragment.MusicFragment
import java.io.File

class LoginFragment : Fragment() {


    private lateinit var overlay: FrameLayout
    private lateinit var emailField: TextInputLayout
    private lateinit var passwordField: TextInputLayout
    private lateinit var confirmField: TextInputLayout
    private lateinit var buttonLogin: AppCompatButton
    private lateinit var saveCredentialsCheckBox: AppCompatCheckBox
    private var titleText: AppCompatTextView? = null

    private val viewModel by viewModels<AuthVariables> {AuthViewModelFactory(LoginValid() as LoginValid, AppPreferencesImpl.getInstance(requireContext())) }

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        buttonLogin = view.findViewById(R.id.button_login)
        emailField = view.findViewById(R.id.input_layout_login)
        passwordField = view.findViewById(R.id.input_layout_password)
        confirmField = view.findViewById(R.id.input_confirm_password)
        overlay = view.findViewById(R.id.overlay_container)
        titleText = view.findViewById(R.id.title_text)
        saveCredentialsCheckBox = view.findViewById(R.id.save_credentials_check_box)

        viewModel.fetchStoredData()

        setListeners()
        subscribeOnLiveData()
    }

    private fun setListeners() {

        setColors(emailField)
        setColors(confirmField)
        setColors(passwordField)


        emailField.editText?.addTextChangedListener {
            it?.let {
                viewModel.setUpdatedEmail(it.toString())
            }
        }

        passwordField.editText?.addTextChangedListener {
            viewModel.setUpdatedPassword(it.toString())
        }


        buttonLogin.setOnClickListener {
            val emailText = emailField.editText?.text.toString()
            val passwordText = passwordField.editText?.text.toString()
            viewModel.onLoginClicked(emailText, passwordText)
            viewModel.writeFile(emailText,passwordText,File(requireActivity().getDir("credentials",Context.MODE_PRIVATE).absolutePath + "/" + "credentials.txt"))
        }

        saveCredentialsCheckBox.setOnCheckedChangeListener { _, selected ->
            viewModel.setSaveCredentialsSelected(selected)
        }
    }


    private fun subscribeOnLiveData() {
        viewModel.isLoginSuccessLiveData.observe(viewLifecycleOwner, {
            (activity as MainActivity).openFragment(MusicFragment(),doClearBackStack = true, tag = "MusicFragment")
        })
        viewModel.isLoginFailedLiveData.observe(viewLifecycleOwner, {
            Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show()
        })
        viewModel.saveCredentialsCheckedLiveData.observe(viewLifecycleOwner, { isSelected ->
            saveCredentialsCheckBox.isChecked = isSelected
        })
        viewModel.emailLiveData.observe(viewLifecycleOwner, { email ->
            emailField.editText?.setText(email)
            emailField.editText?.setSelection(email.length)
        })
        viewModel.passwordLiveData.observe(viewLifecycleOwner, { password ->
            passwordField.editText?.setText(password)
            passwordField.editText?.setSelection(password.length)
        })
    }

    fun setColors(field: TextInputLayout){
        if (field.editText?.text?.isBlank() == true) {
            ContextCompat.getColorStateList(requireContext(), R.color.white)?.let {
                field.setBoxStrokeColorStateList(it)
            }
        } else {
            ContextCompat.getColorStateList(requireContext(), R.color.white)?.let { colorList ->
                field.setBoxStrokeColorStateList(colorList)
            }
        }

    }

} 

