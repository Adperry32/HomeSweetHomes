package com.example.homesweethomes2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), FragmentNavigator {

    private lateinit var openToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val drawer : DrawerLayout = findViewById(R.id.main)
        val nav_View : NavigationView = findViewById(R.id.nav)

        openToggle = ActionBarDrawerToggle(this,drawer,R.string.open, R.string.close)
        drawer.addDrawerListener(openToggle)
        openToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        nav_View.setNavigationItemSelectedListener {

            when(it.itemId)
            {
                R.id.home -> Toast.makeText(applicationContext,"Clicked Home", Toast.LENGTH_SHORT).show()
                R.id.message -> Toast.makeText(applicationContext,"Clicked Message", Toast.LENGTH_SHORT).show()
                R.id.search -> Toast.makeText(applicationContext,"Clicked Search", Toast.LENGTH_SHORT).show()
                R.id.views -> Toast.makeText(applicationContext,"Clicked Recently Viewed", Toast.LENGTH_SHORT).show()
                R.id.favorites -> Toast.makeText(applicationContext,"Clicked Favorite", Toast.LENGTH_SHORT).show()
                R.id.login -> Toast.makeText(applicationContext,"Clicked Login", Toast.LENGTH_SHORT).show()
                R.id.share -> Toast.makeText(applicationContext,"Clicked Share", Toast.LENGTH_SHORT).show()
                R.id.rate -> Toast.makeText(applicationContext,"Clicked Rate Us", Toast.LENGTH_SHORT).show()
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(openToggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
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