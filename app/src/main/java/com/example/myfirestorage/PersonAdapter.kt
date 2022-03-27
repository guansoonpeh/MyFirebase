package com.example.myfirestorage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class PersonAdapter(private val personList:List<Person>) : RecyclerView.Adapter<PersonAdapter.myViewHolder>() {

    class myViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvTel: TextView = view.findViewById(R.id.tvTel)
        val imgPhoto : ImageView = view.findViewById(R.id.imgPhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val personView =  LayoutInflater.from(parent.context)
            .inflate(R.layout.person_view, parent, false)

        return myViewHolder(personView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        val currentRec  = personList[position]

        holder.tvName.text = currentRec.name
        holder.tvTel.text = currentRec.tel
        holder.imgPhoto.setImageURI(null)


    }

    override fun getItemCount(): Int {
        return personList.size
    }


}