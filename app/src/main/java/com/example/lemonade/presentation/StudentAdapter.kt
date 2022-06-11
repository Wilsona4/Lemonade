package com.example.lemonade.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import coil.load
import com.example.lemonade.R
import com.example.lemonade.data.remote.model.Student

class StudentAdapter(private val context: Activity, private val studentList: List<Student>) :
    BaseAdapter() {

    private lateinit var profile: ImageView
    private lateinit var name: TextView
    private lateinit var age: TextView
    private lateinit var course: TextView
    private lateinit var regNo: TextView

    override fun getCount(): Int {
        return studentList.size
    }

    override fun getItem(p0: Int): Any {
        return studentList[p0]
    }

    override fun getItemId(p0: Int): Long {
        return studentList[p0].id.toLong()
    }

    @SuppressLint("SetTextI18n")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var convertView = p1
        convertView = if (p0 % 2 == 0) {
            LayoutInflater.from(context).inflate(R.layout.list_ltr_item, p2, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.list_rtl_item, p2, false)
        }

        profile = convertView.findViewById(R.id.profile_pic_iv)
        name = convertView.findViewById(R.id.tv_name)
        age = convertView.findViewById(R.id.tv_age)
        course = convertView.findViewById(R.id.tv_course)
        regNo = convertView.findViewById(R.id.tv_number)

        val current = studentList[p0]

        profile.load(current.avatar)
        name.text = current.name
        age.text = "${current.age} years"
        course.text = current.department
        regNo.text = current.profileId

        return convertView
    }
}