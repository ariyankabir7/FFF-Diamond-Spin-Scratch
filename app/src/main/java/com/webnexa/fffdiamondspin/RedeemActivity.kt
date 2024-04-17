package com.webnexa.fffdiamondspin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.webnexa.fffdiamondspin.databinding.ActivityRedeemBinding
import com.webnexa.fffdiamondspin.databinding.ActivityScratchBinding

class RedeemActivity : AppCompatActivity() {
    lateinit var binding: ActivityRedeemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityRedeemBinding.inflate(layoutInflater)
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
        window.statusBarColor = resources.getColor(R.color.app_color)

        binding.include.back.setOnClickListener { finish() }
        binding.include.h1st.text="Redeem"
        binding.include.h2st.text=""

        binding.d499.setOnClickListener {
            Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()
        }
        binding.d799.setOnClickListener {
            Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()
        }
        binding.d999.setOnClickListener {
            Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()
        }
        binding.d1499.setOnClickListener {
            Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()
        }
    }
}