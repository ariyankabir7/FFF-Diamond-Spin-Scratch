package com.webnexa.fffdiamondspin

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.anupkumarpanwar.scratchview.ScratchView
import com.webnexa.fffdiamondspin.databinding.ActivityScratchBinding


class ScratchActivity : AppCompatActivity() {
    lateinit var binding: ActivityScratchBinding
    var isScratching = false
    private lateinit var anglevalue:List<String>
    private lateinit var sharedPreferences: SharedPreferences
    var slimit=0
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityScratchBinding.inflate(layoutInflater)
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
        val editor = sharedPreferences.edit()

        binding.d.text = sharedPreferences.getString("diamond", "0")
        slimit = sharedPreferences.getInt("sscratchlimit", 0)

        binding.include.back.setOnClickListener { finish() }
        binding.include.h1st.text = "Scratch"
        binding.include.h2st.text = "& Win"

        anglevalue = listOf("12", "14", "7", "0")

        binding.linearLayout3.setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }

        binding.scratchView.setRevealListener(object : ScratchView.IRevealListener {

            override fun onRevealed(p0: ScratchView?) {

            }

            override fun onRevealPercentChangedListener(p0: ScratchView?, percent: Float) {
                if(slimit>0){

                    if (percent > 0.32 && !isScratching) {
                        isScratching = true
                        Handler().postDelayed({
                            binding.scratchView.reveal()

                            isScratching = false

                            val customLayout = LayoutInflater.from(this@ScratchActivity)
                                .inflate(R.layout.diamond_popup, null)

                            val textView: TextView = customLayout.findViewById(R.id.diamond_text)
                            val image: ImageView = customLayout.findViewById(R.id.imageView)
                            textView.text = anglevalue.random()

                            // Create AlertDialog with custom layout
                            val builder = AlertDialog.Builder(this@ScratchActivity, R.style.TransparentDialogTheme)
                            builder.setView(customLayout)
                            builder.setCancelable(false)

                            val alertDialog = builder.create()
                            alertDialog.show()

                            image.setOnClickListener {
                                alertDialog.dismiss()
                                binding.scratchView.mask()
                                slimit -= 1

                                val currentText = textView.text.toString()
                                val currentNumber = currentText.toIntOrNull() ?: 0 // Convert current text to an integer, default to 0 if conversion fails
                                val diamondCount = sharedPreferences.getString("diamond", "0")
                                    ?.toIntOrNull() ?: 0


                                val sum = currentNumber + diamondCount
                                binding.d.text = sum.toString()

                                editor.putString("diamond", sum.toString())
                                editor.putInt("dspinlimit", slimit)
                                editor.apply()
                            }


                        }, 1500)

                    }
                }else{
                    Toast.makeText(this@ScratchActivity, "Today Limit Ends !", Toast.LENGTH_SHORT).show()
                }


            }
        })
    }
}