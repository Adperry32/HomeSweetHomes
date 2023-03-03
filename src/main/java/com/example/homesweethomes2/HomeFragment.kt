package com.example.homesweethomes2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class HomeFragment : Fragment(),FragmentNavigator {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
     val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<ImageButton>(R.id.regBTN).setOnClickListener {
            Navigator(RegShortFragment(), false)
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


}