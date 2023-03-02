package com.example.homesweethomes2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordFragment : Fragment() {

    private lateinit var _forgotEmail: EditText

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_forgot_password, container, false)
        val navy = activity as FragmentNavigator

        //Read in edit text
        _forgotEmail = view.findViewById(R.id.forgotEmail)

        view.findViewById<Button>(R.id.cancelForgot).setOnClickListener {
            val intent = Intent(this@ForgotPasswordFragment.requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }

        view.findViewById<Button>(R.id.passwordLinkBTN).setOnClickListener {
            validateEntry()
        }

        return view
    }
    private fun validateEntry()
    {
        //Add warning Icon with message
        val warning = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        warning?.setBounds(0,0,warning.intrinsicWidth, warning.intrinsicHeight)

        when {
            TextUtils.isEmpty(_forgotEmail.text.toString().trim()) -> {
                _forgotEmail.setError("Please enter a valid email address", warning)
            }
            _forgotEmail.text.toString().isNotEmpty()->
            {
                if(_forgotEmail.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                {
                    val navy = activity as FragmentNavigator
                    val email: String = _forgotEmail.text.toString().trim(){it <= ' '}
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful)
                            {
                                Toast.makeText(context, "Email sent successfully to reset password", Toast.LENGTH_LONG).show()
                                //Switch back to Login Screen
                                //Sign user out of account
                                Firebase.auth.signOut()

                                //Switch back to Login screen
                                val intent = Intent(this@ForgotPasswordFragment.requireContext(),LoginActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(context, "Email Sent Successful",Toast.LENGTH_SHORT).show()
                            }
                            else
                            {
                                Toast.makeText(context,task.exception!!.toString(), Toast.LENGTH_LONG).show()
                            }
                        }
                }
                else
                {
                    _forgotEmail.setError("Please use email format", warning )
                }
            }
        }
    }
}