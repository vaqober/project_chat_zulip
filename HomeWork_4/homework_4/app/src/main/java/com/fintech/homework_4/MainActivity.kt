package com.fintech.homework_4

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fintech.homework_4.databinding.ActivityMainBinding
import com.fintech.homework_4.fragments.MainFragment

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