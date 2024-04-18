package com.webnexa.fffdiamondspin

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.webnexa.fffdiamondspin.databinding.ActivityRankBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream

class RankActivity : AppCompatActivity() {
    lateinit var binding: ActivityRankBinding
    var isSpinning = false
    lateinit var anglelist: List<Float>
    lateinit var anglevalue: List<Int>
    private lateinit var sharedPreferences: SharedPreferences
    private val WRITE_EXTERNAL_STORAGE_REQUEST = 123
    lateinit var loadvalue:String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRankBinding.inflate(layoutInflater)
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

        binding.d.text = sharedPreferences.getString("diamond", "0")

        binding.included.h1st.text = "Rank"
        binding.included.back.setOnClickListener { finish() }
        anglelist = listOf(120f, 190f, 240f, 0f)
        anglevalue =
            listOf(R.drawable.diamond1, R.drawable.platinium, R.drawable.gold, R.drawable.bronze)



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
                            }
                        }

                        val customLayout = LayoutInflater.from(this@RankActivity)
                            .inflate(R.layout.rank_popup, null)

                        val image: ImageView = customLayout.findViewById(R.id.rankImage)
                        val imageView: ImageView = customLayout.findViewById(R.id.imageView)

                        Glide
                            .with(this@RankActivity)
                            .load(anglevalue[randomIndex])
                            .centerCrop()
                            .placeholder(R.drawable.loading_image)
                            .into(image);
                        // Create AlertDialog with custom layout
                        val builder =
                            AlertDialog.Builder(this@RankActivity, R.style.TransparentDialogTheme)
                        builder.setView(customLayout)
                        builder.setCancelable(false)

                        val alertDialog = builder.create()
                        alertDialog.show()

                        imageView.setOnClickListener {
                            alertDialog.dismiss()

                            val fileName = "Rank_Image.jpg" // Provide your desired file name
                            // Call saveImageToGallery inside a coroutine
                            GlobalScope.launch {
                                saveImageToGallery(image, fileName)
                            }
                        }

                    }
                }
                timer.start()
            }
        }

    }

    private suspend fun saveImageToGallery(imageView: ImageView, fileName: String) {
        withContext(Dispatchers.IO) {

            val drawable = imageView.drawable
            val bitmap = (drawable as BitmapDrawable).bitmap

            // Save the bitmap to the device's storage
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            var outputStream: OutputStream? = null
            try {
                outputStream = resolver.openOutputStream(imageUri!!)
                if (outputStream != null) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                }
                outputStream?.flush()

                // Notify the media scanner to refresh
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(File(imageUri.path))))
                } else {
                    MediaStore.Images.Media
                        .getBitmap(contentResolver, imageUri)
                }

                // Show success message
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RankActivity, "Image saved successfully", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()

                // Show failure message
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RankActivity, "Failed to save image", Toast.LENGTH_SHORT).show()
                }
            } finally {
                outputStream?.close()
            }
        }
    }

}
