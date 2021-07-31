package com.mnsh.hom583kursinfo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //bu metod navigation shu activityda boshlanadi va bizga homeFragmentni ochib beradi
    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.my_navigation_host).navigateUp()
    }
}