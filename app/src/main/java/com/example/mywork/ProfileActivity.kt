package com.example.mywork

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue

class ProfileActivity : AppCompatActivity(){
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var db_ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val txt_nama = findViewById<TextView>(R.id.text_profile_nama)
        val txt_telp = findViewById<TextView>(R.id.text_profile_telp)
        val txt_birth = findViewById<TextView>(R.id.text_profile_birth)
        val txt_email = findViewById<TextView>(R.id.text_profile_email)

        user = FirebaseAuth.getInstance().currentUser!!
        database = FirebaseDatabase.getInstance()
        db_ref = database.getReference("SEEKERS")

        txt_nama.text = user.displayName
        txt_email.text = user.email

        db_ref.child(user.uid).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val post = dataSnapshot.getValue<SeekersModel>()
                txt_telp.text = post?.telp
                txt_birth.text = post?.birth
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("cancelled", "loadPost:onCancelled", databaseError.toException())
            }
        })

        val btn_signout = findViewById<Button>(R.id.btn_profile_signout)
        btn_signout.setOnClickListener(){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}