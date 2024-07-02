package com.example.cyrusbirthdayreminder

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1){
    companion object {
        private const val DATABASE_NAME = "my-database"
        private const val TABLE_NAME = "Friend"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_BIRTHDAY = "birthday"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_NAME TEXT, $COLUMN_BIRTHDAY TEXT)"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertFriend(friend: Friend) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, friend.name)
            put(COLUMN_BIRTHDAY, friend.birthday)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    @SuppressLint("Range")
    fun getAllFriends(): List<Friend> {
        val friends = mutableListOf<Friend>()
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getLong(cursor.getColumnIndex(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                val birthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY))
                val friend = Friend(id, name, birthday)
                friends.add(friend)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return friends
    }

    @SuppressLint("Range")
    fun getFriendById(friendId: Long): Friend? {
        val db = readableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?"
        val cursor = db.rawQuery(selectQuery, arrayOf(friendId.toString()))
        var friend: Friend? = null
        if (cursor.moveToFirst()) {
            val name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
            val birthday = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTHDAY))
            friend = Friend(friendId, name, birthday)
        }
        cursor.close()
        db.close()
        return friend
    }

    fun updateUser(friend: Friend) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME, friend.name)
            put(COLUMN_BIRTHDAY, friend.birthday)
        }
        db.update(TABLE_NAME, values, "$COLUMN_ID = ?", arrayOf(friend.id.toString()))
        db.close()
    }

    fun deleteUser(friend: Friend) {
        val db = writableDatabase
        db.delete(TABLE_NAME, "$COLUMN_ID = ?", arrayOf(friend.id.toString()))
        db.close()
    }

    fun deleteAllUser() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null,null)
        db.close()
    }



}