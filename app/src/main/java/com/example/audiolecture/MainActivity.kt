package com.example.audiolecture

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.core.app.ActivityCompat

class MainActivity : AppCompatActivity() {
    lateinit var mediarec : MediaRecorder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val btnPlay = findViewById<Button>(R.id.btnPlay)

        val path: String = getExternalFilesDir("").toString() + "/myrec.3gp"
        mediarec = MediaRecorder()
        btnStart.isEnabled = false
        btnStop.isEnabled = false
        //btnPlay.isEnabled = false


        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO,
                                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE),111)
        btnStart.isEnabled = true


        //start recording
        btnStart.setOnClickListener{
            mediarec.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediarec.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediarec.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            mediarec.setOutputFile(path)
            mediarec.prepare()
            mediarec.start()
            btnStart.isEnabled = false
            btnStop.isEnabled = true


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
            mediaplay.prepare()
            mediaplay.start()
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        val btnStart = findViewById<Button>(R.id.btnStart)
        val btnStop = findViewById<Button>(R.id.btnStop)
        val btnPlay = findViewById<Button>(R.id.btnPlay)


        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 111 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            btnStart.isEnabled = true
    }
}