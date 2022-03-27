package com.example.myfirestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myfirestorage.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(){
    private lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("myDB")
        auth = FirebaseAuth.getInstance()

        val myRV: RecyclerView = findViewById(R.id.recyclerPerson)
        myRV.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        myRV.setHasFixedSize(true)

        readData(object : ReadCallBack {
                override fun onCallBack(list: MutableList<Person>) {
                val adapter = PersonAdapter(list)

                myRV.adapter = adapter

            }
        })

        if (auth.currentUser != null) {
            binding.textView.text = auth.currentUser!!.email
        }


        binding.bntRegister.setOnClickListener(){
            register("bait2073.202201@gmail.com", "123456")
        }

        binding.btnLogin.setOnClickListener(){
            login("bait2073.202201@gmail.com", "123456")
        }

        binding.btnSignOut.setOnClickListener(){
            signOut()
            binding.textView.text = "Signed out"
        }

        binding.btnAdd.setOnClickListener(){
            val person = Person("johny", "1321156")
            add(person)
        }

        binding.btnDelete.setOnClickListener(){
            val name = "John"
            delete(name)
        }

        binding.btnSearch.setOnClickListener(){
            val name = binding.tfName.text.toString()
            search(name);
        }

    }

    fun register(email: String, psw: String) {

        auth.createUserWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser
                binding.textView.text = "Registered > ${user!!.email}"
             }
            .addOnFailureListener { e ->
                binding.textView.text = e.message
            }
    }

    fun login(email: String, psw: String) {
        auth.signInWithEmailAndPassword(email, psw)
            .addOnSuccessListener {
                val user = auth.currentUser
                binding.textView.text = user!!.email
            }
            .addOnFailureListener { e ->
                binding.textView.text = e.message
            }
    }

    fun signOut() {
        Firebase.auth.signOut()

    }


    fun add(person: Person) {
//        "rules": {
//            ".read": "auth != null",
//            ".write": "auth != null",
//        }

        database.child("person").child(person.name).setValue(person)
            .addOnSuccessListener {
                binding.textView.text = "Added > ${person.name}"
            }
            .addOnFailureListener { e ->
                binding.textView.text = e.message
            }


    }

    fun delete(name:String) {

        database.child("person").child(name).removeValue()
            .addOnSuccessListener {
                binding.textView.text = "Deleted > ${name}"
            }
            .addOnFailureListener { e->
                binding.textView.text = e.message
            }
    }


    fun search(name:String){
        database.child("person").child(name).get()
            .addOnSuccessListener { result ->

                if (result!=null){
                     val pName = result.child("name").value
                     val pTel = result.child("tel").value

                    binding.textView.text = "search > ${pName}, ${pTel}"
                }else{
                    binding.textView.text = "search > null"
                }
        }.addOnFailureListener{ e->
                binding.textView.text = "search > ${e.message}"
        }

    }

    fun readData(callback: ReadCallBack){
        val ref = database.child("person")

        val refListener = object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    val PersonList = mutableListOf<Person>()

                    for (c in p0.children) {

                            val name =  c.child("name").value.toString()
                            val tel =  c.child("tel").value.toString()

                            val person = Person(name, tel)
                            PersonList.add(person)

                    }
                    callback.onCallBack(PersonList)
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        }

        ref.addValueEventListener(refListener)

    }

    interface ReadCallBack{
        fun onCallBack(list: MutableList<Person> )
    }

}