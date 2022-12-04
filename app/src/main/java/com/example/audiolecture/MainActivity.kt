package com.example.audiolecture

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.visualizer.amplitude.AudioRecordView
import java.io.IOException
import java.lang.Math.random
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.scheduleAtFixedRate
import kotlin.random.Random.Default.nextInt

class MainActivity : AppCompatActivity() {
    private lateinit var mediarec : MediaRecorder
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button
    private lateinit var btnPlay: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById<Button>(R.id.btnStart)
        btnStop = findViewById<Button>(R.id.btnStop)
        btnPlay = findViewById<Button>(R.id.btnPlay)

        //disable the buttons
        btnStart.isEnabled = false
        btnStop.isEnabled = false

        var date = SimpleDateFormat("yyyy-MM-dd").format(Date())
        var filename = "record_$date"+"_"+(0..100).random()+".3gp"
        var path: String = getExternalFilesDir("").toString() + "/$filename"
        mediarec = MediaRecorder()


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        btnStart.isEnabled = true


        //start recording
        btnStart.setOnClickListener{
            mediarec.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediarec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediarec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediarec.setOutputFile(path)
            try {
                mediarec.prepare()
            } catch (e: IOException) {
                Toast.makeText(
                    this,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
            mediarec.start()
            btnStart.isEnabled = false
            btnStop.isEnabled = true

            val audioRecordView: AudioRecordView = findViewById(R.id.audioRecordView)
            Thread {
                while (btnStop.isEnabled){
                    audioRecordView.update(mediarec.maxAmplitude)
                    Thread.sleep(30)
                }
            }.start()
        }


        //stop recording
        btnStop.setOnClickListener{
            btnStop.isEnabled = false
            btnStart.isEnabled = true
            mediarec.stop()
        }

        //play recording
        btnPlay.setOnClickListener {
            var mediaplay = MediaPlayer()
            mediaplay.setDataSource(path)
            try {
                mediaplay.prepare()
            } catch (e: IOException) {
                Toast.makeText(
                    this,
                    e.toString(),
                    Toast.LENGTH_LONG
                ).show()
            }
            mediaplay.start()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            btnStart.isEnabled = true
    }
}