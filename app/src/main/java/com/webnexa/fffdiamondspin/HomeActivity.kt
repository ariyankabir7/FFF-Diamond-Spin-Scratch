package com.webnexa.fffdiamondspin

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.webnexa.fffdiamondspin.databinding.ActivityHomeBinding
import com.webnexa.fffdiamondspin.databinding.ActivityMainBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
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
        binding.diamondspin.setOnClickListener {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
        binding.rankspin.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val appLink = "https://play.google.com/store/apps/details?id=$packageName"
            val shareMessage =
                "This is H4X Sensi Tool App :\n$appLink"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage)
            startActivity(Intent.createChooser(shareIntent, "Share app via"))
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

}