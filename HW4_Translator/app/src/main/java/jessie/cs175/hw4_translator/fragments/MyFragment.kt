package jessie.cs175.hw4_translator.fragments

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import jessie.cs175.hw4_translator.R
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import jessie.cs175.hw4_translator.db.AppPreferences
import jessie.cs175.hw4_translator.util.InputValidation


class MyFragment : Fragment() {
    private lateinit var textInputLayoutEmail: TextInputLayout
    private lateinit var textInputLayoutPassword: TextInputLayout

    private lateinit var textInputEditTextEmail: TextInputEditText
    private lateinit var textInputEditTextPassword: TextInputEditText

    private lateinit var appCompatButtonLogin: AppCompatButton

    private lateinit var textViewLinkRegister: AppCompatTextView
    private lateinit var inputValidation: InputValidation
    private lateinit var loginLinearView: LinearLayoutCompat

    private var userNameText: String? = null
    private var userEmailText: String? = null
    private var userPasswordText: String? = null
    var user_uri: Uri = Uri.parse("content://jessie.cs175.hw4_translator.db.provider/UserTable")


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.layout_my, container, false)
        textInputLayoutEmail = view.findViewById<View>(R.id.textInputLayoutEmail) as TextInputLayout
        textInputLayoutPassword =
            view.findViewById<View>(R.id.textInputLayoutPassword) as TextInputLayout

        textInputEditTextEmail =
            view.findViewById<View>(R.id.textInputEditTextEmail) as TextInputEditText
        textInputEditTextPassword =
            view.findViewById<View>(R.id.textInputEditTextPassword) as TextInputEditText

        appCompatButtonLogin = view.findViewById<View>(R.id.appCompatButtonLogin) as AppCompatButton

        textViewLinkRegister =
            view.findViewById<View>(R.id.textViewLinkRegister) as AppCompatTextView
        loginLinearView = view.findViewById<View>(R.id.LoginLinearView) as LinearLayoutCompat
        appCompatButtonLogin!!.setOnClickListener {
            verifyFromSQLite()
        }
        textViewLinkRegister!!.setOnClickListener {
            // Navigate to RegisterActivity
//            val intentRegister = Intent(
//                activity?.applicationContext, RegisterActivity::class.java
//            )
//            startActivity(intentRegister)
            val trans = fragmentManager?.beginTransaction()

            // We use the "root frame" defined in "root_fragment.xml" as the reference to replace fragment
            if (trans != null) {
                trans.addToBackStack(null)
                trans.replace(R.id.root_frame, RegisterFragment())
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                trans.commit()
            }
        }
        inputValidation = InputValidation(activity as Context)

        return view
    }

    private fun verifyFromSQLite() {
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation!!.isInputEditTextEmail(
                textInputEditTextEmail!!,
                textInputLayoutEmail!!,
                getString(R.string.error_message_email)
            )
        ) {
            return
        }
        if (!inputValidation!!.isInputEditTextFilled(
                textInputEditTextPassword!!,
                textInputLayoutPassword!!,
                getString(R.string.error_message_password)
            )
        ) {
            return
        }

        if (checkUser(
                textInputEditTextEmail!!.text.toString().trim { it <= ' ' },
                textInputEditTextPassword!!.text.toString().trim { it <= ' ' })
        ) {
            val cursor: Cursor? = requireActivity().contentResolver.query(
                user_uri,
                arrayOf("user_id", "user_name"),
                "user_email=? AND user_password=?",
                arrayOf(
                    textInputEditTextEmail!!.text.toString().trim { it <= ' ' },
                    textInputEditTextPassword!!.text.toString().trim { it <= ' ' }),
                null
            )
            cursor?.moveToFirst()

            userEmailText = textInputEditTextEmail!!.text.toString().trim { it <= ' ' }
            userNameText = cursor?.getString(1)
            userPasswordText = textInputEditTextPassword!!.text.toString().trim { it <= ' ' }

            AppPreferences.isLogin = true
            AppPreferences.username = userNameText.toString()
            AppPreferences.email = userEmailText.toString()
            AppPreferences.password = userPasswordText.toString()

            val trans = fragmentManager?.beginTransaction()

            // We use the "root frame" defined in "root_fragment.xml" as the reference to replace fragment
            if (trans != null) {
                trans.addToBackStack(null)
                trans.replace(
                    R.id.root_frame,
                    AccountFragment.newInstance(userNameText, userEmailText,AppPreferences.headImages)
                )
                trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                trans.commit()
            }

        } else {

            // Snack Bar to show success message that record is wrong
            Snackbar.make(
                loginLinearView,
                getString(R.string.error_valid_email_password),
                Snackbar.LENGTH_LONG
            ).show()
        }

    }

    private fun emptyInputEditText() {
        textInputEditTextEmail!!.text = null
        textInputEditTextPassword!!.text = null
    }

    private fun checkUser(email: String, password: String): Boolean {
        val cursor: Cursor? = requireActivity().contentResolver.query(
            user_uri,
            arrayOf("user_id", "user_name"),
            "user_email=? AND user_password=?",
            arrayOf(email, password),
            null
        )
        cursor?.moveToFirst()

//        userEmailText = email
//        userNameText = cursor?.getString(1)
//        userPasswordText=password

        val cursorCount = cursor?.count
        cursor?.close()

        if (cursorCount!! > 0)
            return true

        return false
    }


}