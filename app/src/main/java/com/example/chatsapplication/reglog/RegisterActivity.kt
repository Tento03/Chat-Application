package com.example.chatsapplication.reglog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.chatsapplication.databinding.ActivityRegisterBinding
import com.example.chatsapplication.message.UserActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener(){
            var intent= Intent(this, LoginActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener(){
            val nama=binding.etName.text.toString()
            val email=binding.etEmail.text.toString()
            val pass=binding.etPassword.text.toString()
            val repass=binding.etConfirmPassword.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.etEmail.error="Harus sesuai format"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.etEmail.error="Jangan Kosong"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.etPassword.error="Jangan Kosong"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (pass.length <8){
                binding.etPassword.error="Panjang password harus 8"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }
            if (repass!=pass){
                binding.etConfirmPassword.error="Harus sama dengan password"
                binding.etConfirmPassword.requestFocus()
                return@setOnClickListener
            }
            if (nama.isEmpty()){
                binding.etName.error="Jangan Kosong"
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            registerUser(nama,email,pass)
        }
    }

    private fun registerUser(nama:String,email: String, pass: String) {
        var firebaseAuth=FirebaseAuth.getInstance()
        val databaseReference=FirebaseDatabase.getInstance().getReference("user")
        firebaseAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(){
            if (it.isSuccessful){
                val user= hashMapOf<String,Any>(
                    "id" to firebaseAuth.currentUser!!.uid,
                    "name" to nama,
                    "email" to email,
                    "password" to pass
                )
                databaseReference.child(firebaseAuth.currentUser!!.uid).setValue(user)
                Toast.makeText(this,"Berhasil Register", Toast.LENGTH_SHORT).show()
                var intent=Intent(this, UserActivity::class.java).also {
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Gagal Register", Toast.LENGTH_SHORT).show()
            }
        }
    }
}