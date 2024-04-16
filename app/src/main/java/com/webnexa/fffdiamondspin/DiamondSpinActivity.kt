package com.webnexa.fffdiamondspin

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.webnexa.fffdiamondspin.databinding.ActivityDiamondSpinBinding
import com.webnexa.fffdiamondspin.databinding.ActivityHomeBinding

class DiamondSpinActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiamondSpinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityDiamondSpinBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            val insetsController = ViewCompat.getWindowInsetsController(v)
            insetsController?.isAppearanceLightStatusBars = false
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.included.back.setOnClickListener {
            finish()
        }
    }
}