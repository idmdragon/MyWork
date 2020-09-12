package com.example.mywork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var db_ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        db_ref = database.getReference("SEEKERS")

        val in_nama = findViewById<EditText>(R.id.input_signup_nama)
        val in_telp = findViewById<EditText>(R.id.input_signup_phone)
        val in_birth = findViewById<EditText>(R.id.input_signup_birth)
        val in_email = findViewById<EditText>(R.id.input_signup_email)
        val in_pass = findViewById<EditText>(R.id.input_signup_pass)

        val btn_simpan = findViewById<Button>(R.id.btn_signup_simpan)
        btn_simpan.setOnClickListener(){
            Log.d("klik", "btn_simpan clicked")
            if (in_nama.text == null || in_telp.text == null || in_birth.text == null || in_email.text == null || in_pass.text == null){
                Toast.makeText(applicationContext, "Harap lengkapi formulir", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(in_email.text.toString(), in_pass.text.toString())
                    .addOnCompleteListener(this){ task ->
                        if(task.isSuccessful){
                            user = auth.currentUser!!
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(in_nama.text.toString())
                                .build()
                            user?.updateProfile(profileUpdates)
                            val seeker = SeekersModel(in_telp.text.toString(), in_birth.text.toString())
                            db_ref.child(user.uid).setValue(seeker)
                            Toast.makeText(applicationContext, "Akun berhasil ditambah", Toast.LENGTH_LONG).show()
                            startActivity(Intent(this, ProfileActivity::class.java))
                        }else{
                            Toast.makeText(applicationContext, "Pendaftaran akun gagal", Toast.LENGTH_LONG).show()
                        }
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val userCurr = FirebaseAuth.getInstance().currentUser
        if(userCurr!=null){
            startActivity(Intent(this, ProfileActivity::class.java))
        }
    }
}