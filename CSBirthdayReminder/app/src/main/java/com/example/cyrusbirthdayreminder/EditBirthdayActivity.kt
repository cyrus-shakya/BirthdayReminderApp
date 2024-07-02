package com.example.cyrusbirthdayreminder

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditBirthdayActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var friendAdapter: FriendAdapter
    private lateinit var etName: TextInputEditText
    private lateinit var etBirthday: TextInputEditText
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        var friendId: Long = 1;

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_birthday)

        etName = findViewById(R.id.etName)
        etBirthday = findViewById(R.id.etBirthday)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        databaseHelper = DatabaseHelper(this)
        friendAdapter = FriendAdapter(this)

        if (intent.getStringExtra("selectedName") != null && intent.getStringExtra("selectedBirthday") != null) {
            etName.setText(intent.getStringExtra("selectedName").toString())
            etBirthday.setText(intent.getStringExtra("selectedBirthday").toString())

            friendId = intent.getLongExtra("selectedId", 1)

//            val toast = Toast.makeText(applicationContext, "$friendId", Toast.LENGTH_SHORT)
//            toast.show()
        }


        btnEdit.setOnClickListener() {
            var name = etName.text.toString().trim()
            var birthday = etBirthday.text.toString().trim()

            if (name.isNotEmpty() && birthday.isNotEmpty()) {
                val newFriend = Friend(id= friendId, name = name, birthday = birthday)
                databaseHelper.updateUser(newFriend)
                updateFriendList()
                val toast = Toast.makeText(
                    applicationContext,
                    "Birthday for $name updated :)",
                    Toast.LENGTH_SHORT
                )
                toast.show()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else {
                val toast =
                    Toast.makeText(applicationContext, "Fields cannot be empty", Toast.LENGTH_SHORT)
                toast.show()
                clearInputFields()
            }
        }

        btnDelete.setOnClickListener(){

            dialogYesOrNo(
                this,
                "Reminder",
                "Are you sure want to delete this Birthday?",
                DialogInterface.OnClickListener { dialog, id ->
                    // do whatever you need to do when user presses "Yes"
                    var name = etName.text.toString().trim()
                    var birthday = etBirthday.text.toString().trim()

                    val newFriend = Friend(id= friendId, name = name, birthday = birthday)
                    databaseHelper.deleteUser(newFriend)
                    updateFriendList()
                    val toast = Toast.makeText(
                        applicationContext,
                        "Birthday for $name deleted :(",
                        Toast.LENGTH_SHORT
                    )
                    toast.show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                })

        }

        updateFriendList()

        etBirthday.setOnClickListener() {
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

}
