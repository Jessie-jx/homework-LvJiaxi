package jessie.cs175.hw4_translator.fragments

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import jessie.cs175.hw4_translator.R
import jessie.cs175.hw4_translator.model.User
import jessie.cs175.hw4_translator.util.InputValidation

class RegisterFragment:Fragment() {
    private lateinit var textInputLayoutName: TextInputLayout
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout
    private lateinit var textInputLayoutConfirmPassword: TextInputLayout

    private lateinit var textInputEditTextName: TextInputEditText
    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText
    private lateinit var textInputEditTextConfirmPassword: TextInputEditText

    private lateinit var appCompatButtonRegister: AppCompatButton
    private lateinit var appCompatTextViewLoginLink: AppCompatTextView
    private lateinit var inputValidation: InputValidation
    private lateinit var registerLinearView: LinearLayoutCompat

    var user_uri: Uri = Uri.parse("content://jessie.cs175.hw4_translator.db.provider/UserTable")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater.inflate(R.layout.activity_register, container, false)
        textInputLayoutName = view.findViewById<View>(R.id.textInputLayoutName) as TextInputLayout
        textInputLayoutEmail = view.findViewById<View>(R.id.textInputLayoutEmailRegister) as TextInputLayout
        textInputLayoutPassword = view.findViewById<View>(R.id.textInputLayoutPasswordRegister) as TextInputLayout
        textInputLayoutConfirmPassword = view.findViewById<View>(R.id.textInputLayoutConfirmPassword) as TextInputLayout

        textInputEditTextName = view.findViewById<View>(R.id.textInputEditTextName) as TextInputEditText
        textInputEditTextEmail = view.findViewById<View>(R.id.textInputEditTextEmailRegister) as TextInputEditText
        textInputEditTextPassword = view.findViewById<View>(R.id.textInputEditTextPasswordRegister) as TextInputEditText
        textInputEditTextConfirmPassword = view.findViewById<View>(R.id.textInputEditTextConfirmPassword) as TextInputEditText

        appCompatButtonRegister = view.findViewById<View>(R.id.appCompatButtonRegister) as AppCompatButton

        appCompatTextViewLoginLink = view.findViewById<View>(R.id.appCompatTextViewLoginLink) as AppCompatTextView
        registerLinearView = view.findViewById<View>(R.id.RegisterLinearView) as LinearLayoutCompat

        appCompatButtonRegister!!.setOnClickListener {
            postDataToSQLite()
        }
        appCompatTextViewLoginLink!!.setOnClickListener{
            fragmentManager?.popBackStack()
        }
        inputValidation = InputValidation(activity as Context)

        return view
    }

    private fun postDataToSQLite() {
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(
                R.string.error_message_name
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(
                R.string.error_message_email
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(
                R.string.error_message_email
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(
                R.string.error_message_password
            ))) {
            return
        }
        if (!inputValidation!!.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.error_password_match))) {
            return
        }

        if (!checkUser(textInputEditTextEmail!!.text.toString().trim())) {

            var user = User(name = textInputEditTextName!!.text.toString().trim(),
                email = textInputEditTextEmail!!.text.toString().trim(),
                password = textInputEditTextPassword!!.text.toString().trim())

            addUser(user)

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(registerLinearView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show()
            emptyInputEditText()


        } else {
            // Snack Bar to show error message that record already exists
            Snackbar.make(registerLinearView, getString(R.string.error_email_exists), Snackbar.LENGTH_LONG).show()
        }



    }

    private fun checkUser(email: String): Boolean {
        val cursor: Cursor? =requireActivity().contentResolver.query(
            user_uri,
            arrayOf("user_id"),
            "user_email=?",
            arrayOf(email),
            null
        )

        val cursorCount = cursor?.count
        cursor?.close()

        if (cursorCount!! > 0)
            return true

        return false
    }
    private fun addUser(user: User){
        val values= ContentValues()
        values.put("user_name",user.name)
        values.put("user_email",user.email)
        values.put("user_password",user.password)

        requireActivity().contentResolver.insert(user_uri,values)
    }

    private fun emptyInputEditText() {
        textInputEditTextName!!.text = null
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
        textInputEditTextConfirmPassword!!.text = null
    }
}