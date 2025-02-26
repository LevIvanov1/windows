package com.example.windows

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import android.view.View
import android.view.ViewGroup
import com.example.windows.databinding.ActivityMainBinding

// RootActivity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
         binding.textView.text = "Welcome to Messenger!"

        //  if (savedInstanceState == null) {
           // supportFragmentManager.beginTransaction().add(R.id.fragment_container_view, ChatsFragment()).commit() }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }
}

