package com.example.homesweethomes2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment

class HomeActivity : AppCompatActivity(), FragmentNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        //Switch to Quick reg
        //findViewById<ImageButton>(R.id.regBTN).setOnClickListener {
            //Navigator(RegShortFragment(), false)
       // }

        //switch to Account profile
        //findViewById<Button>(R.id.accBTN).setOnClickListener {
           // Navigator(AccountFragment(), false)
       // }

    }

    override fun Navigator(fragment: Fragment, addToStack: Boolean) {
        val transaction = supportFragmentManager
            .beginTransaction()
            .replace(R.id.main, fragment)
        if(addToStack)
        {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }
}