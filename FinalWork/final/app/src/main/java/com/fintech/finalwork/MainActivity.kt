package com.fintech.finalwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.fintech.finalwork.databinding.ActivityMainBinding
import com.fintech.finalwork.presentation.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.mainFragmentContainer, MainFragment.newInstance(), null)
                .commitAllowingStateLoss()
        }
    }
}