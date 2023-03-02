package com.example.homesweethomes2

import androidx.fragment.app.Fragment

interface FragmentNavigator {
    fun Navigator(fragment: Fragment, addToStack: Boolean)
}