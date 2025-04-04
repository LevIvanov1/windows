package com.example.windows

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.windows.databinding.ActivityMainBinding
import com.example.windows.libs.TokenManager

// RootActivity
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // binding.textView.text = "Welcome to Messenger!"

        //  if (savedInstanceState == null) {
           // supportFragmentManager.beginTransaction().add(R.id.fragment_container_view, ChatsFragment()).commit() }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.FragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        if (savedInstanceState == null && TokenManager.getToken(this) != null){
            navController.navigate(R.id.action_authFragment_to_chatsFragment2)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.ChatFragment) {
                binding.bottomNavigationView.isVisible = false
            } else if (destination.id == R.id.authFragment) {
                binding.bottomNavigationView.isVisible = false
            } else {
                binding.bottomNavigationView.isVisible = true
            }
        }
    }
}

