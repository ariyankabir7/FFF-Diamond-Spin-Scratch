package com.webnexa.fffdiamondspin

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.webnexa.fffdiamondspin.databinding.ActivityRedeemBinding

class RedeemActivity : AppCompatActivity() {
    lateinit var binding: ActivityRedeemBinding
    private lateinit var sharedPreferences: SharedPreferences
    var d = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRedeemBinding.inflate(layoutInflater)
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
        binding.include.h1st.text = "Redeem"
        binding.include.h2st.text = ""

        sharedPreferences = getSharedPreferences("FFFDaimondSpin", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        binding.d.text = sharedPreferences.getString("diamond", "0")

        binding.d499.setOnClickListener {
            if (binding.d.text.toString().toInt() >= 499) {
                d = 499
                showAlert()

            } else {
                Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()

            }
        }
        binding.d799.setOnClickListener {
            if (binding.d.text.toString().toInt() >= 799) {
                d = 799
                showAlert()
            } else {
                Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()

            }
        }
        binding.d999.setOnClickListener {
            if (binding.d.text.toString().toInt() >= 999) {
                d = 999
                showAlert()
            } else {
                Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()

            }
        }
        binding.d1499.setOnClickListener {
            if (binding.d.text.toString().toInt() >= 1499) {
                d = 1499
                showAlert()
            } else {
                Toast.makeText(this, "Insufficient Diamonds !", Toast.LENGTH_SHORT).show()

            }
        }
    }

    fun showAlert() {

        sharedPreferences = getSharedPreferences("FFFDaimondSpin", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        val customLayout = LayoutInflater.from(this)
            .inflate(R.layout.diamond_popup, null)

        val textView: TextView =
            customLayout.findViewById(R.id.diamond_text)
        val image: ImageView = customLayout.findViewById(R.id.imageView)
        textView.text = d.toString()

        // Create AlertDialog with custom layout
        val builder = AlertDialog.Builder(
            this, R.style.TransparentDialogTheme
        )
        builder.setView(customLayout)
        builder.setCancelable(false)

        val alertDialog = builder.create()
        alertDialog.show()

        image.setOnClickListener {
            alertDialog.dismiss()


            val currentText = textView.text.toString()
            val currentNumber = currentText.toIntOrNull()
                ?: 0 // Convert current text to an integer, default to 0 if conversion fails
            val diamondCount = sharedPreferences.getString("diamond", "0")
                ?.toIntOrNull() ?: 0


            val sub = diamondCount - currentNumber
            binding.d.text = sub.toString()

            editor.putString("diamond", sub.toString())
            editor.apply()
        }
    }
}
