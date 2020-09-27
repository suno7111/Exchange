package com.example.exchangetest.page

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.exchangetest.databinding.ActivityExchangeBinding

class ExchangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(binding.mainBody.id, ExchangeFragment()).commit()
    }
}