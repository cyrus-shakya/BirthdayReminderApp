package com.example.cyrusbirthdayreminder

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBirthdayActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var etName: TextInputEditText
    private lateinit var etBirthday: TextInputEditText
    private lateinit var btnAdd: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_birthday)

        etName = findViewById(R.id.etName)
        etBirthday = findViewById(R.id.etBirthday)
        btnAdd = findViewById(R.id.btnAdd)

        databaseHelper = DatabaseHelper(this)
        friendAdapter = FriendAdapter(this)

        etBirthday.setOnClickListener(){
            var calendar = Calendar.getInstance()
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale.CANADA)
                    etBirthday.setText(sdf.format(calendar.time).toString())
                }

            DatePickerDialog(
                this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnAdd.setOnClickListener(){
            var name = etName.text.toString().trim()
            var birthday = etBirthday.text.toString().trim()

            if (name.isNotEmpty() && birthday.isNotEmpty()) {
                val newFriend = Friend(name = name, birthday = birthday)
                databaseHelper.insertFriend(newFriend)
                updateFriendList()
                clearInputFields()
                val toast = Toast.makeText(applicationContext, "Birthday for $name added", Toast.LENGTH_SHORT)
                toast.show()

                val intent = Intent(this, MainActivity::class.java)
                //passing intent data into ActivityB
                startActivity(intent)
            }
            else
            {
                val toast = Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT)
                toast.show()

                clearInputFields()
            }
        }
        updateFriendList()
    }

    private fun updateFriendList() {
        val friends = databaseHelper.getAllFriends()
        friendAdapter.setFriendList(friends)
    }

    private fun clearInputFields() {
        etName.text?.clear()
        etBirthday.text?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        databaseHelper.close()
    }
}