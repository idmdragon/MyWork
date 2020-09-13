package com.example.mywork

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CompanySignupActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var database: FirebaseDatabase
    private lateinit var db_ref: DatabaseReference

    private lateinit var nama_perusahaan:EditText
    private lateinit var email_perusahaan:EditText
    private lateinit var nomor_perusahaan:EditText
    private lateinit var alamat_perusahaan:EditText
    private lateinit var nama_pengaju:EditText
    private lateinit var email_pengaju:EditText
    private lateinit var nomor_pengaju:EditText
    private lateinit var jabatan_pengaju:EditText
    private lateinit var pass_pengaju:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_company_signup)

        auth= FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        db_ref = database.getReference("SEEKERS")


        nama_perusahaan= findViewById<EditText>(R.id.nama_perusahaan)
        email_perusahaan= findViewById<EditText>(R.id.email_perusahaan)
        nomor_perusahaan= findViewById<EditText>(R.id.no_perusahaan)
        alamat_perusahaan= findViewById<EditText>(R.id.alamat_perushaan)

        nama_pengaju= findViewById<EditText>(R.id.nama_pengaju)
        email_pengaju= findViewById<EditText>(R.id.email_pengaju)
        nomor_pengaju= findViewById<EditText>(R.id.no_pengaju)
        jabatan_pengaju= findViewById<EditText>(R.id.jabatan_pengaju)
        pass_pengaju= findViewById<EditText>(R.id.pass_pengaju)

        val kirim_btn= findViewById<Button>(R.id.kirim)
        kirim_btn.setOnClickListener(){
            if(validateInput()){
                val credential= EmailAuthProvider.getCredential(email_pengaju.text.toString(),pass_pengaju.text.toString())
                    user.reauthenticate(credential).addOnCompleteListener(this){task ->
                        if(task.isSuccessful){
                            auth.createUserWithEmailAndPassword(email_perusahaan.text.toString(),pass_pengaju.text.toString())
                                .addOnCompleteListener(this){ task ->
                                    if(task.isSuccessful){
                                        user = auth.currentUser!!
                                        val profileUpdates = UserProfileChangeRequest.Builder()
                                            .setDisplayName(nama_perusahaan.text.toString())
                                            .build()
                                        user?.updateProfile(profileUpdates)
                                        val company = CompanyModel(nomor_perusahaan.text.toString(),alamat_perusahaan.text.toString())
                                        db_ref.child(user.uid).setValue(company)
                                        Toast.makeText(applicationContext, "Akun Perusahaan berhasil ditambah", Toast.LENGTH_LONG).show()
                                        startActivity(Intent(this, ProfileActivity::class.java))
                                        finish()
                                    }else{
                                        Toast.makeText(applicationContext, "Pendaftaran akun Perusahaan gagal", Toast.LENGTH_LONG).show()
                                    }
                                }
                        }else{
                            Toast.makeText(applicationContext, "Akun Pengaju Tidak Sesuai", Toast.LENGTH_SHORT).show()
                        }

                    }

            }
        }


    }


    override fun onStart() {
        super.onStart()
        user = FirebaseAuth.getInstance().currentUser!!
    }

    fun validateInput():Boolean{
        var valid= true
        if(nama_perusahaan.text.toString().isEmpty()){
            nama_perusahaan.error="Tolong Masukkan Nama Perusahaan"
            valid=false
        }
        if(!isEmailValid(email_perusahaan)){
            valid= false
        }
        if(nomor_perusahaan.text.toString().isEmpty()){
            nomor_perusahaan.error="Tolong Masukkan Nomor Perusahaan"
            valid= false
        }
        if(alamat_perusahaan.text.toString().isEmpty()){
            alamat_perusahaan.error= "Tolong Masukkan Alamat Perusahaan"
            valid= false
        }
        if(!isNameValid())
            valid= false
        if(!isEmailValid(email_pengaju))
            valid=false
        if(nomor_pengaju.text.toString().isEmpty()){
            nomor_pengaju.error="Masukkan Nomor Anda"
            valid=false
        }
        if(jabatan_pengaju.text.toString().isEmpty()){
            jabatan_pengaju.error="Masukkan Jabatan Anda di Perusahaan"+nama_perusahaan.text
            valid=false
        }
        return valid
    }

    fun isNameValid():Boolean{
        val name= nama_pengaju.text.toString()
        val regex= Regex("[A-Za-z\\s]+")
        if(name.length<3){
            nama_pengaju.error="Tolong Masukkan Minimal 3 karakter"
            return false
        }
        if(!name.matches(regex)){
            nama_pengaju.error= "Tolong Masukkan Nama yang Valid"
            return false
        }
        return true
    }
    fun isEmailValid(email:EditText):Boolean{
        val text_email= email.text.toString()
        val regex= Regex("^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})")
        if(text_email.isEmpty()){
            email.error="Tolong Masukkan Email"
            return false
        }
        else if(!text_email.matches(regex)){
            email.error= "Masukkan Email yang Valid"
            return false
        }
        return true
    }
}