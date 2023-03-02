package com.example.homesweethomes2

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.FirebaseDatabaseKtxRegistrar
import com.google.firebase.storage.StorageReference


class RegisterFragment : Fragment() {

    // variable to find register edit text
    private lateinit var fName: EditText
    private lateinit var lName: EditText
    private lateinit var _pass: EditText
    private lateinit var _passCon: EditText
    private lateinit var _email: EditText
    private lateinit var age: EditText
    private lateinit var _kids: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef : DatabaseReference
    private lateinit var storageRef : StorageReference


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        //find views by Id
        fName = view.findViewById(R.id.firstName)
        lName = view.findViewById(R.id.lastName)
        _pass = view.findViewById(R.id.password)
        _passCon = view.findViewById(R.id.confirmPass)
        _email = view.findViewById(R.id.email)
        age = view.findViewById(R.id.DOB)
        _kids = view.findViewById(R.id.kids)


        // transition back to activity from fragment
        view.findViewById<Button>(R.id.cancelBTN).setOnClickListener {
            val intent = Intent(this@RegisterFragment.requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }

        //transition to main page and add new user to database
        view.findViewById<Button>(R.id.regSubmitBTN).setOnClickListener {
            registerForm()
        }
        return view
    }

    private fun registerForm()
    {
        //initialize variables
        val firstName = fName.text.toString()
        val lastName = lName.text.toString()
        val email = _email.text.toString()
        val _age = age.text.toString().toInt()
        val _password = _pass.text.toString()
        val _kid = _kids.text.toString().toInt()
        val _userType = 0


        val warning = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_baseline_warning_24)
        warning?.setBounds(0,0,warning.intrinsicWidth, warning.intrinsicHeight)

        //Check if field is empty
        when {
            TextUtils.isEmpty(fName.text.toString().trim()) -> {
                fName.setError("Please enter First Name", warning)
            }
            TextUtils.isEmpty(lName.text.toString().trim()) -> {
                lName.setError("Please enter Last Name", warning)
            }
            TextUtils.isEmpty(_email.text.toString().trim()) -> {
                _email.setError("Please enter a valid email address", warning)
            }
            TextUtils.isEmpty(_pass.text.toString().trim()) -> {
                _pass.setError("Must enter password", warning)
            }
            TextUtils.isEmpty(_passCon.text.toString().trim()) -> {
                _passCon.setError("Must enter password", warning)
            }
            _email.text.toString().isNotEmpty()&&
                    _pass.text.toString().isNotEmpty()&&
                    _passCon.text.toString().isNotEmpty()->
            {
                if(_email.text.toString().matches(Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")))
                {
                    if(_pass.text.toString().length >= 6)
                    {
                        if(_pass.text.toString() == _passCon.text.toString())
                        {
                            val email: String = _email.text.toString().trim(){it <= ' '}
                            val password: String = _pass.text.toString().trim(){it <= ' '}

                            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                                .addOnCompleteListener{ task ->
                                    if(task.isSuccessful)
                                    {
                                        val firstName = fName.text.toString()
                                        val lastName = lName.text.toString()
                                        val __email = _email.text.toString()
                                        val _password = _pass.text.toString()
                                        val _ages = age.text.toString().toInt()
                                        val kid = _kids.text.toString().toInt()
                                        val user = _userType

                                        //Create firebase instance and set variables
                                        auth = FirebaseAuth.getInstance()
                                        databaseRef = FirebaseDatabase.getInstance().getReference("Users")
                                        val _uid = auth.currentUser?.uid
                                        val userProfile = UserData(firstName,lastName,__email,_password,_ages,kid,user)

                                        //add userData if uid is not NUll
                                        if(_uid != null)
                                        {
                                            databaseRef.child(_uid).setValue(userProfile).addOnCompleteListener{
                                                if(it.isSuccessful)
                                                {
                                                  Toast.makeText(context, "Successful",Toast.LENGTH_SHORT).show()
                                                    val intent = Intent(this@RegisterFragment.requireContext(),LoginActivity::class.java)
                                                    startActivity(intent)
                                                }
                                                else
                                                {
                                                    Toast.makeText(context, "Error",Toast.LENGTH_SHORT).show()
                                                }
                                            }
                                        }
                                    }
                                }
                        }
                        else
                        {
                            _passCon.setError("Passwords Did Not Match", warning)
                        }
                    }
                    else
                    {
                        _pass.setError("Password must be at least 6 characters", warning)
                    }
                }
                else
                {
                   _email.setError("Please enter a valid email address", warning)
                }
            }
        }
    }
}