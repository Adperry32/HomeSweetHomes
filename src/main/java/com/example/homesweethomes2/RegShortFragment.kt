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
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference

class RegShortFragment : Fragment(), FragmentNavigator {
    // variable to find register edit text
    private lateinit var f_Name: EditText
    private lateinit var l_Name: EditText
    private lateinit var pass2: EditText
    private lateinit var passCon2: EditText
    private lateinit var _2email: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_reg_short, container, false)

        f_Name = view.findViewById(R.id.firstName2)
        l_Name = view.findViewById(R.id.lastName2)
        pass2 = view.findViewById(R.id.pass2)
        passCon2 = view.findViewById(R.id.conPass)
        _2email = view.findViewById(R.id.email2)

        view.findViewById<Button>(R.id.qr_Submit).setOnClickListener {
            registerUser()
        }
        view.findViewById<Button>(R.id.qr_cancel).setOnClickListener {
            Navigator(HomeFragment(), false)
        }

        return view
    }

    override fun Navigator(fragment: Fragment, addToStack: Boolean) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.homeFragment, fragment)
        if(addToStack)
        {
            transaction!!.addToBackStack(null)
        }
        transaction!!.commit()
    }

    private fun registerUser()
    {
        //initialize variables
        val firstName = f_Name.text.toString()
        val lastName = l_Name.text.toString()
        val email = _2email.text.toString()
        val _password = pass2.text.toString()
        val _userType = 0


        val warning = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        warning?.setBounds(0,0,warning.intrinsicWidth, warning.intrinsicHeight)

        //Check if field is empty
        when {
            TextUtils.isEmpty(f_Name.text.toString().trim()) -> {
                f_Name.setError("Please enter First Name", warning)
            }
            TextUtils.isEmpty(l_Name.text.toString().trim()) -> {
                l_Name.setError("Please enter Last Name", warning)
            }
            TextUtils.isEmpty(_2email.text.toString().trim()) -> {
                _2email.setError("Please enter a valid email address", warning)
            }
            TextUtils.isEmpty(pass2.text.toString().trim()) -> {
                pass2.setError("Must enter password", warning)
            }
            TextUtils.isEmpty(passCon2.text.toString().trim()) -> {
                passCon2.setError("Must enter password", warning)
            }
            _2email.text.toString().isNotEmpty()&&
                    pass2.text.toString().isNotEmpty()&&
                    passCon2.text.toString().isNotEmpty()->
            {
                if(_2email.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                {
                    if(pass2.text.toString().length >= 6)
                    {
                        if(pass2.text.toString() == passCon2.text.toString())
                        {
                            val _email: String = _2email.text.toString().trim(){it <= ' '}
                            val password: String = pass2.text.toString().trim(){it <= ' '}

                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(_email,password)
                                .addOnCompleteListener{ task ->
                                    if(task.isSuccessful)
                                    {

                                        //Create firebase instance and set variables
                                        auth = FirebaseAuth.getInstance()
                                        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
                                        val _uid = auth.currentUser?.uid
                                        val userProfile = UserData(firstName,lastName,email,_password,_userType)

                                        //add userData if uid is not NUll
                                        if(_uid != null)
                                        {
                                            databaseRef.child(_uid).setValue(userProfile).addOnCompleteListener{
                                                if(it.isSuccessful)
                                                {
                                                    Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show()
                                                    Navigator(HomeFragment(), false)
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else
                        {
                            passCon2.setError("Passwords Did Not Match", warning)
                        }
                    }
                    else
                    {
                        pass2.setError("Password must be at least 6 characters", warning)
                    }
                }
                else
                {
                    _2email.setError("Please enter a valid email address", warning)
                }
            }
        }
    }




}