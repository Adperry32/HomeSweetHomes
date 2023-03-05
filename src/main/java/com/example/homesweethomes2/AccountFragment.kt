package com.example.homesweethomes2

import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homesweethomes2.databinding.FragmentAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.StorageReference


class AccountFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var userData: UserData
    private lateinit var fetch: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private lateinit var storageRef: StorageReference
    private lateinit var image: Uri
    private lateinit var _uid: String
    private lateinit var bind: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_account, container, false)
        bind = view?.let{ FragmentAccountBinding.bind(it)}!!

        //initialization
        user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()
        fetch = FirebaseDatabase.getInstance().getReference("Users")
        auth = FirebaseAuth.getInstance()
        _uid = auth.currentUser?.uid.toString()

        getUserData()

        return view
    }

    private fun getUserData() {
        fetch.child(_uid).addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                userData = snapshot.getValue(UserData::class.java)!!

                bind.acctFname.setText(userData.firstName)
                bind.acctLname.setText(userData.lastName)
                bind.acctEmail.setText(userData.userEmail)
                bind.acctAge.setText(userData.age.toString())
                bind.acctPass.setText(userData.userPassword)
                bind.acctKid.setText(userData.children.toString())
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error Loading User Info",Toast.LENGTH_SHORT).show()
            }
        })
    }

}