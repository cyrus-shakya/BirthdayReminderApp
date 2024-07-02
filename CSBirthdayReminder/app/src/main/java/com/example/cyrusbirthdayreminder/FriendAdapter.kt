package com.example.cyrusbirthdayreminder

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity

class FriendAdapter(private val context: Context) : BaseAdapter() {
    private var friendList = listOf<Friend>()

    fun setFriendList(friends: List<Friend>) {
        friendList = friends
        notifyDataSetChanged()
    }

    override fun getCount(): Int = friendList.size

    override fun getItem(position: Int): Friend = friendList[position]

    override fun getItemId(position: Int): Long = position.toLong()


    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder

        val view = LayoutInflater.from(context).inflate(R.layout.item_friends, parent, false)
        viewHolder = ViewHolder(view)
        view.tag = viewHolder

        val friend = getItem(position)

        viewHolder.tvName.text = friend.name
        viewHolder.tvBirthday.text = friend.birthday

        viewHolder.btnUpdate.setOnClickListener {
            // Handle button click event
            val intent = Intent(context, EditBirthdayActivity::class.java)

//            val a = friend.id
//            val toast = Toast.makeText(context, "$a", Toast.LENGTH_SHORT)
//            toast.show()

            intent.putExtra("selectedId", friend.id)
            intent.putExtra("selectedName", friend.name)
            intent.putExtra("selectedBirthday", friend.birthday)
            context.startActivity(intent)
        }


        return view
    }

    private class ViewHolder(view: View) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvBirthday: TextView = view.findViewById(R.id.tvBirthday)
        val btnUpdate: ImageButton = view.findViewById(R.id.btnUpdate)
    }




}