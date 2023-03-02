package com.example.homesweethomes2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdminLoginActivity : AppCompatActivity(), FragmentNavigator {
    private lateinit var _admin: EditText
    private lateinit var _adminPass: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var dataRefUser: DatabaseReference
    private lateinit var dataRefAdmin: DatabaseReference
    private lateinit var uType: UserData
    private lateinit var uid: String
    private lateinit var type: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        //Variable initialization
        _admin = findViewById(R.id.adminUser)
        _adminPass = findViewById(R.id.adminPass)
        dataRefAdmin = FirebaseDatabase.getInstance().getReference("Admin")
        auth = FirebaseAuth.getInstance()
        uid = auth.currentUser?.uid.toString()


        //transition to main admin platform
        val admin = findViewById<Button>(R.id.enterAdmin)
        admin.setOnClickListener {
            validateLogIn()
        }
        //transition back to user login activity
        val back = findViewById<ImageButton>(R.id.backBTN)
        back.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

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

    private fun validateLogIn()
    {
        val warning = AppCompatResources.getDrawable(this, R.drawable.ic_baseline_warning_24)
        warning?.setBounds(0,0, warning.intrinsicWidth, warning.intrinsicHeight)

        when
        {
            TextUtils.isEmpty(_admin.text.toString().trim())->{
                _admin.setError("Empty Entry, !Email is Username!", warning)
            }
            TextUtils.isEmpty(_adminPass.text.toString().trim())->{
                _adminPass.setError("Empty Entry", warning)
            }
            _admin.text.toString().isNotEmpty() &&
                    _adminPass.text.toString().isNotEmpty()->
            {
                if(_admin.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                {
                    if(_adminPass.text.toString().length >= 8)
                    {
                        //check for user in database
                        val email: String = _admin.text.toString().trim(){it <= ' '}
                        val password: String =_adminPass.text.toString().trim(){it <= ' '}

                        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener {task ->
                                if(task.isSuccessful)
                                {
                                    readData()

                                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                                        Navigator(HomeFragment(), false)

                                    /*else
                                    {
                                        Toast.makeText(this,"None Administrative Member - Return to UserLogin", Toast.LENGTH_LONG).show()
                                        val intent = Intent(this,AdminLoginActivity::class.java)
                                        startActivity(intent)
                                    }*/

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
                        _adminPass.setError("Password Incorrect", warning)
                    }
                }
                else
                {
                    _admin.setError("Please enter a valid email address", warning)
                }
            }
        }
    }

    private fun readData()
    {
       dataRefAdmin.child(uid).addValueEventListener(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               uType = snapshot.getValue(UserData::class.java)!!

               if(uType != null)
               {
                   _admin.setText(uType.userEmail)
                   _adminPass.setText(uType.userPassword)
                   uType.userType?.let { type.setText(it) }
               }
           }

           override fun onCancelled(error: DatabaseError) {
               Toast.makeText(this@AdminLoginActivity,"Error Loading User Info",Toast.LENGTH_SHORT).show()
           }
       })

    }

}
