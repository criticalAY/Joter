package com.uchi.joter.ui

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.uchi.joter.BuildConfig
import com.uchi.joter.R

class SettingsActivity : AppCompatActivity() {
    private lateinit var actionHome: ImageView
    private var tapCount = 0
    private lateinit var about:TextView
    private lateinit var versionText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val toolbar = findViewById<Toolbar>(R.id.setting_note_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        about = findViewById(R.id.about)
        versionText = findViewById(R.id.version_text)

        actionHome = findViewById(R.id.action_home)
        actionHome.setOnClickListener {
                val mIntent = Intent(this, AddNoteActivity::class.java)
                mIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(mIntent)
        }

        versionText.text = BuildConfig.VERSION_NAME

        about.setOnClickListener {
//            tapCount++
//            if(tapCount==20){
//                Toast.makeText(this, getString(R.string.ester_egg), Toast.LENGTH_SHORT).show()
//                tapCount=0
//            }
//            else{}
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.setTitle(resources.getString(R.string.app_title))
                alertDialog.setMessage(resources.getString(R.string.about_message))
                alertDialog.setPositiveButton(R.string.ok) { dialogInterface, _ ->
                    dialogInterface.dismiss()
                }
                alertDialog.create().show()

        }




    }
}