package com.webnexa.fffdiamondspin

import android.app.AlertDialog
import android.content.Intent
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
import com.bumptech.glide.Glide
import com.webnexa.fffdiamondspin.databinding.ActivityHomeBinding
import com.webnexa.fffdiamondspin.databinding.ActivityRankBinding

class RankActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankBinding
   var isSpinning=false
    lateinit var anglelist: List<Float>
    lateinit var anglevalue: List<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityRankBinding.inflate(layoutInflater)
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

        binding.included.h1st.text="Rank"
        binding.included.back.setOnClickListener { finish() }
        anglelist = listOf(120f, 190f, 240f, 0f)
        anglevalue = listOf(R.drawable.diamond1, R.drawable.platinium,R.drawable.gold,R.drawable.bronze)



        binding.linearLayout3.setOnClickListener {
            startActivity(Intent(this, RedeemActivity::class.java))
        }
        binding.spinBtn.setOnClickListener {

            if (!isSpinning) {
                isSpinning = true


                val timer = object : CountDownTimer(3000, 15) {
                    override fun onTick(millisUntilFinished: Long) {
                        binding.ivDiamondSpin.rotation += 19f
                    }

                    override fun onFinish() {
                        isSpinning = false
                        var randomIndex = anglelist.indices.random()
                        var randomAngle = anglelist[randomIndex]
                        if (binding.ivDiamondSpin.rotation<randomAngle){
                            binding.ivDiamondSpin.rotation = randomAngle
                        }else{
                            randomIndex = anglelist.indices.random()
                            randomAngle = anglelist[randomIndex]
                            if (binding.ivDiamondSpin.rotation<randomAngle){
                                binding.ivDiamondSpin.rotation = randomAngle
                            }else{
                                randomIndex = anglelist.indices.random()
                                randomAngle = anglelist[randomIndex]
                                binding.ivDiamondSpin.rotation = randomAngle
                            }
                        }

                        val customLayout = LayoutInflater.from(this@RankActivity)
                            .inflate(R.layout.rank_popup, null)

                        val image: ImageView = customLayout.findViewById(R.id.rankImage)
                        val imageView: ImageView = customLayout.findViewById(R.id.imageView)

                        Glide
                            .with(this@RankActivity)
                            .load( anglevalue[randomIndex])
                            .centerCrop()
                            .placeholder(R.drawable.loading_image)
                            .into(image);
                        // Create AlertDialog with custom layout
                        val builder = AlertDialog.Builder(this@RankActivity, R.style.TransparentDialogTheme)
                        builder.setView(customLayout)
                        builder.setCancelable(false)

                        val alertDialog = builder.create()
                        alertDialog.show()

                        imageView.setOnClickListener {
                            alertDialog.dismiss()
                        }

                    }
                }
                timer.start()
            }
        }

    }
}