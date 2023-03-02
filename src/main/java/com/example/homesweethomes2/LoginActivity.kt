package com.example.homesweethomes2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class LoginActivity : AppCompatActivity(), FragmentNavigator {

    //variable to link xml edittext
    private lateinit var users: EditText
    private lateinit var pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //read the entered user text field
        users = findViewById(R.id.userLog)
        pass = findViewById(R.id.userPass)


        //transition to main frag for guest
        val guest = findViewById<Button>(R.id.guestBTN)
        guest.setOnClickListener {
            Navigator(HomeFragment(), false)
        }

        //transition to register sign up form
        val register = findViewById<ImageButton>(R.id.regBTN)
        register.setOnClickListener {
            Navigator(RegisterFragment(), false)
        }

        //transition to main for user
        val user = findViewById<Button>(R.id.loginBTN)
        user.setOnClickListener {
            validateLogIn()
        }

        //transition to forgot password
        val forgotPass = findViewById<ImageButton>(R.id.forgotBTN)
        forgotPass.setOnClickListener {
            Navigator(ForgotPasswordFragment(), false)
        }

        //transition to admin activity
        val admin = findViewById<ImageButton>(R.id.adminLoginBTN)
        admin.setOnClickListener {

            val intent = Intent(this,AdminLoginActivity::class.java)
            startActivity(intent)
        }
    }
    //Switch between fragment method
    override fun Navigator(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.onBoarding, fragment)
        if(addToStack)
        {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    // function/method to check user authentication
    private fun validateLogIn()
    {
        val warning = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_warning_24)
        warning?.setBounds(0,0, warning.intrinsicWidth, warning.intrinsicHeight)

        when
        {
            TextUtils.isEmpty(users.text.toString().trim())->{
                users.setError("Empty Entry, !Email is Username!", warning)
            }
            TextUtils.isEmpty(pass.text.toString().trim())->{
                pass.setError("Empty Entry", warning)
            }
            users.text.toString().isNotEmpty() &&
                    pass.text.toString().isNotEmpty()->
            {
                if(users.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                {
                    if(pass.text.toString().length >= 6)
                    {
                        //check for user in database
                        val email: String = users.text.toString().trim(){it <= ' '}
                        val password: String =pass.text.toString().trim(){it <= ' '}

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener {task ->
                                if(task.isSuccessful)
                                {
                                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()

                                    Navigator(HomeFragment(), false)
                                }
                                else
                                {
                                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                                        .addOnFailureListener { e->
                                            Toast.makeText(this,"Login Failed!, ${e.message}",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }
                    }
                    else
                    {
                        pass.setError("Password must be at least 6 characters", warning)
                    }
                }
                else
                {
                    users.setError("Please enter a valid email address", warning)
                }
            }
        }
    }
}