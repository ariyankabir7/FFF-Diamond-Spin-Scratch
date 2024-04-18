package com.webnexa.fffdiamondspin

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.webnexa.fffdiamondspin.databinding.ActivityHomeBinding
import java.time.LocalDate


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    private lateinit var sharedPreferences: SharedPreferences
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
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



        sharedPreferences = getSharedPreferences("FFFDaimondSpin", Context.MODE_PRIVATE)
        sharedPreferences.edit()

        val currentDate = LocalDate.now()
        val storedDateStr = sharedPreferences.getString("date", "")

       // sharedPreferences.edit().clear().apply()

        if (storedDateStr != currentDate.toString()) {
            with(sharedPreferences.edit()) {
                putInt("dspinlimit", 10)
                putInt("sscratchlimit", 10)
                putInt("rspinlimit", 3)
                apply()


            }
        }

        binding.d.text = sharedPreferences.getString("diamond", "0")

        // Handle menu clicks
        binding.menu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }

        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.shared -> {
                    shareApp()
                    true
                }
                R.id.rate -> {
                    rateApp()
                    true
                }
                R.id.privacy -> {
                    openPrivacyPolicy()
                    true
                }
                R.id.exit -> {
                    showPopupDialog()
                    true
                }
                else -> false
            }
        }

        binding.rateus.setOnClickListener {
            rateApp()
        }

        binding.shareus.setOnClickListener {
            shareApp()
        }

        binding.daily.setOnClickListener {
            claimDailyBonus(currentDate.toString())
        }

        // Handle other button clicks
        binding.showDiamond.setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }
        binding.diamondspin.setOnClickListener {
            startActivity(Intent(this, DiamondSpinActivity::class.java))
        }
        binding.rankspin.setOnClickListener {
            startActivity(Intent(this, RankActivity::class.java))
        }
        binding.diamondscratch.setOnClickListener {
            startActivity(Intent(this, ScratchActivity::class.java))
        }
    }

    private fun shareApp() {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        val appLink = "https://play.google.com/store/apps/details?id=$packageName"
        val shareMessage = "This is FFF Diamond Spin Tool App:\n$appLink"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
        startActivity(Intent.createChooser(shareIntent, "Share app via"))
    }

    private fun rateApp() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
    }

    private fun openPrivacyPolicy() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com")))
    }

    private fun showPopupDialog() {
        AlertDialog.Builder(this, R.style.TransparentDialogTheme)
            .setView(R.layout.back_popup)
            .setCancelable(true)
            .create()
            .apply {
                show()
                findViewById<MaterialButton>(R.id.buttonCancel)?.setOnClickListener {
                    dismiss()
                }
                findViewById<MaterialButton>(R.id.buttonConfirm)?.setOnClickListener {
                    dismiss()
                    super.onBackPressed()
                    finish()
                }
            }
    }

    private fun claimDailyBonus(currentDateStr: String) {
        val storedDateStr = sharedPreferences.getString("date", "")
        if (storedDateStr != currentDateStr) {
            val editor = sharedPreferences.edit()
            editor.putString("date", currentDateStr)
            editor.putString("diamond", "25") // Assuming the daily bonus is always 25 diamonds
            editor.apply()

            val customLayout = LayoutInflater.from(this)
                .inflate(R.layout.diamond_popup, null)

            val textView: TextView = customLayout.findViewById(R.id.diamond_text)
            val image: ImageView = customLayout.findViewById(R.id.imageView)
            textView.text = "25"

            val builder = AlertDialog.Builder(this, R.style.TransparentDialogTheme)
            builder.setView(customLayout)
            builder.setCancelable(false)

            val alertDialog = builder.create()
            alertDialog.show()

            image.setOnClickListener {
                alertDialog.dismiss()
                binding.d.text = "25" // Update UI with the new diamond count
            }
        } else {
            Toast.makeText(
                this,
                "You have already claimed today's daily bonus!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onBackPressed() {

        showPopupDialog()
    }

    override fun onResume() {
        binding.d.text=sharedPreferences.getString("diamond","0")
        super.onResume()
    }
}