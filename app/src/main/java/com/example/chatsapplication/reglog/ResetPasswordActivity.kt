package com.example.chatsapplication.reglog

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.chatsapplication.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityResetPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backToLoginTextview.setOnClickListener(){
            var intent= Intent(this, LoginActivity::class.java).also {
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startActivity(intent)
        }
        binding.sendResetButton.setOnClickListener(){
            var email=binding.emailEdittextForgotPassword.text.toString()
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                binding.emailEdittextForgotPassword.error="Harus sesuai format"
                binding.emailEdittextForgotPassword.requestFocus()
                return@setOnClickListener
            }
            resetPassword(email)
        }
    }

    private fun resetPassword(email: String) {
        var firebaseAuth=FirebaseAuth.getInstance()
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(){
                if (it.isSuccessful){
                    Toast.makeText(this,"Email dikirim", Toast.LENGTH_SHORT).show()
                    binding.emailEdittextForgotPassword.text.clear()
                    var intent= Intent(this, LoginActivity::class.java).also {
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    }
                    startActivity(intent)
                }
               else{
                    Toast.makeText(this,"Gagal Ganti Pass", Toast.LENGTH_SHORT).show()
                }
            }
    }
}