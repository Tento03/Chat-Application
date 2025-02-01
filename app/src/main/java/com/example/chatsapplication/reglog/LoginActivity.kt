package com.example.chatsapplication.reglog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.chatsapplication.databinding.ActivityLoginBinding
import com.example.chatsapplication.message.UserActivity
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var firebaseAuth=FirebaseAuth.getInstance()
        binding.forgotPasswordTextview.setOnClickListener(){
            var intent=Intent(this, ResetPasswordActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        binding.backToRegisterTextview.setOnClickListener(){
            var intent=Intent(this, RegisterActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }

        binding.loginButtonLogin.setOnClickListener(){
            var email=binding.emailEdittextLogin.text.toString()
            var pass=binding.passwordEdittextLogin.text.toString()

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEdittextLogin.error="Harus sesuai format"
                binding.emailEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (email.isEmpty()){
                binding.emailEdittextLogin.error="Jangan Kosong"
                binding.emailEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (pass.isEmpty()){
                binding.passwordEdittextLogin.error="Jangan Kosong"
                binding.passwordEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            if (pass.length <8){
                binding.passwordEdittextLogin.error="Panjang password harus 8"
                binding.passwordEdittextLogin.requestFocus()
                return@setOnClickListener
            }
            loginUser(email,pass)
        }
    }

    private fun loginUser(email: String, pass: String) {
        var firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(){
            if (it.isSuccessful){
                Toast.makeText(this,"Berhasil Login",Toast.LENGTH_SHORT).show()
                var intent=Intent(this, UserActivity::class.java).also {
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                }
                startActivity(intent)
            }
            else{
                Toast.makeText(this,"Gagal Login",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        val firebaseAuth=FirebaseAuth.getInstance()
        val user= firebaseAuth.currentUser?.uid
        if (user!=null){
            var intent=Intent(this,UserActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
    }
}