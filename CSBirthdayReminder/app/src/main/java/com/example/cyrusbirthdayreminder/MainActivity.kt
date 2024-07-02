package com.example.cyrusbirthdayreminder

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var tvHeader: TextView
    private lateinit var tvClear: TextView
    private lateinit var imgSetting: ImageView
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.listView)
        tvHeader = findViewById(R.id.tvHeader)
        tvClear = findViewById(R.id.tvClear)
        btnAdd = findViewById(R.id.btnAdd)
        imgSetting = findViewById(R.id.imgSetting)

        databaseHelper = DatabaseHelper(this)
        friendAdapter = FriendAdapter(this)

        listView.adapter = friendAdapter
        updateFriendList()

        btnAdd.setOnClickListener() {
            val intent = Intent(this, AddBirthdayActivity::class.java)

            startActivity(intent)
        }

        imgSetting.setOnClickListener(){
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)
        }


        tvClear.setOnClickListener() {

            dialogYesOrNo(
                this,
                "Reminder",
                "Are you sure want to delete all the Birthdays?",
                DialogInterface.OnClickListener { dialog, id ->
                    // do whatever you need to do when user presses "Yes"
                    databaseHelper.deleteAllUser()
                    updateFriendList()
                    val toast = Toast.makeText(
                        applicationContext,
                        "All Birthdays deleted :(",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                })
        }
    }

    fun dialogYesOrNo(
        activity: Activity,
        title: String,
        message: String,
        listener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(activity)
        builder.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
            dialog.dismiss()
            listener.onClick(dialog, id)
        })
        builder.setNegativeButton("No", null)
        val alert = builder.create()
        alert.setTitle(title)
        alert.setMessage(message)
        alert.show()
    }


    private fun updateFriendList() {
        val friends = databaseHelper.getAllFriends()
        friendAdapter.setFriendList(friends)
    }


}