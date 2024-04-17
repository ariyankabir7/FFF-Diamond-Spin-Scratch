package com.webnexa.fffdiamondspin

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
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
import java.util.Date


class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
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


        val sharedPreferences = getSharedPreferences("FFFDaimondSpin", MODE_PRIVATE)
        val myEdit = sharedPreferences.edit()




        binding.menu.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.shared -> {

                    val shareIntent = Intent(Intent.ACTION_SEND)
                    shareIntent.type = "text/plain"
                    val appLink = "https://play.google.com/store/apps/details?id=$packageName"
                    val shareMessage =
                        "This is FFF Diamond Spin Tool App :\n$appLink"
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(shareIntent, "Share app via"))
                    true
                }

                R.id.rate -> {

                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                        )
                    )
                    true
                }

                R.id.privacy -> {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.google.com")
                        )
                    )
                    true
                }

                R.id.exit -> {
                    showPpopupDialog()
                    true
                }

                else -> {
                    false
                }
            }
        }
        binding.rateus.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
        binding.shareus.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val appLink = "https://play.google.com/store/apps/details?id=$packageName"
            val shareMessage =
                "This is FFF Diamond Spin Tool App :\n$appLink"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share app via"))
        }
        binding.daily.setOnClickListener {

            val currentDate = LocalDate.now()

            val storedDateStr = sharedPreferences.getString("date", "")

            val storedDate = if (storedDateStr?.isNotEmpty() == true) {
                LocalDate.parse(storedDateStr)
            } else {
                null
            }

            if (storedDate != null && storedDate == currentDate) {

                Toast.makeText(this, "You have already claimed today daily Bonus!", Toast.LENGTH_SHORT).show()
            } else {
                val editor = sharedPreferences.edit()
                editor.putString("date", currentDate.toString())
                editor.apply()
                Toast.makeText(this, "You have claimed today daily Bonus!", Toast.LENGTH_SHORT).show()
            }
        }
        binding.showDiamond.setOnClickListener {
            val intent = Intent(this, RedeemActivity::class.java)
            startActivity(intent)
        }
        binding.diamondspin.setOnClickListener {
            val intent = Intent(this, DiamondSpinActivity::class.java)
            startActivity(intent)
        }
        binding.rankspin.setOnClickListener {
            val intent = Intent(this, RankActivity::class.java)
            startActivity(intent)
        }
        binding.diamondscratch.setOnClickListener {
            val intent = Intent(this, ScratchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showPpopupDialog() {
        AlertDialog.Builder(this, R.style.TransparentDialogTheme).setView(R.layout.back_popup)
            .setCancelable(true).create().apply {
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

    override fun onBackPressed() {
        showPpopupDialog()

    }

}