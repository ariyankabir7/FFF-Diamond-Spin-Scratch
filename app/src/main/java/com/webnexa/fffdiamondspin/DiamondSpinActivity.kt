package com.webnexa.fffdiamondspin

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.webnexa.fffdiamondspin.databinding.ActivityDiamondSpinBinding

class DiamondSpinActivity : AppCompatActivity() {
    lateinit var binding: ActivityDiamondSpinBinding
    private var currentAngle = 0f
    private var isSpinning = false
    lateinit var anglelist: List<Float>
    lateinit var anglevalue: List<String>
    var dslimit = 0
    private lateinit var sharedPreferences: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDiamondSpinBinding.inflate(layoutInflater)
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

        sharedPreferences = getSharedPreferences("FFFDaimondSpin", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        dslimit = sharedPreferences.getInt("dspinlimit", 0)

        binding.d.text = sharedPreferences.getString("diamond", "0")


        binding.included.back.setOnClickListener {
            finish()
        }
        binding.linearLayout3.setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }
        anglelist = listOf(120f, 190f, 240f, 0f)
        anglevalue = listOf("12", "14", "7", "0")

        binding.spinBtn.setOnClickListener {

            if (dslimit > 0) {
                if (!isSpinning) {
                    isSpinning = true


                    val timer = object : CountDownTimer(3000, 15) {
                        override fun onTick(millisUntilFinished: Long) {
                            binding.ivDiamondSpin.rotation += 25f
                        }

                        override fun onFinish() {
                            isSpinning = false
                            var randomIndex = anglelist.indices.random()
                            var randomAngle = anglelist[randomIndex]
                            if (binding.ivDiamondSpin.rotation < randomAngle) {
                                binding.ivDiamondSpin.rotation = randomAngle
                            } else {
                                randomIndex = anglelist.indices.random()
                                randomAngle = anglelist[randomIndex]
                                if (binding.ivDiamondSpin.rotation < randomAngle) {
                                    binding.ivDiamondSpin.rotation = randomAngle
                                } else {
                                    randomIndex = anglelist.indices.random()
                                    randomAngle = anglelist[randomIndex]
                                    binding.ivDiamondSpin.rotation = randomAngle


                                    val customLayout = LayoutInflater.from(this@DiamondSpinActivity)
                                        .inflate(R.layout.diamond_popup, null)

                                    val textView: TextView =
                                        customLayout.findViewById(R.id.diamond_text)
                                    val image: ImageView = customLayout.findViewById(R.id.imageView)
                                    textView.text = anglevalue[randomIndex]

                                    // Create AlertDialog with custom layout
                                    val builder = AlertDialog.Builder(
                                        this@DiamondSpinActivity,
                                        R.style.TransparentDialogTheme
                                    )
                                    builder.setView(customLayout)
                                    builder.setCancelable(false)

                                    val alertDialog = builder.create()
                                    alertDialog.show()

                                    image.setOnClickListener {
                                        alertDialog.dismiss()
                                        dslimit -= 1

                                        val currentText = textView.text.toString()
                                        val currentNumber = currentText.toIntOrNull() ?: 0 // Convert current text to an integer, default to 0 if conversion fails
                                        val diamondCount = sharedPreferences.getString("diamond", "0")
                                                ?.toIntOrNull() ?: 0


                                        val sum = currentNumber + diamondCount
                                        binding.d.text = sum.toString()

                                        editor.putString("diamond", sum.toString())
                                        editor.putInt("dspinlimit", dslimit)
                                        editor.apply()
                                    }
                                }
                            }

                        }
                    }
                    timer.start()
                }
            } else {
                Toast.makeText(this, "Todays Limit Ends !", Toast.LENGTH_SHORT).show()
            }


        }
    }

}