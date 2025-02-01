package com.example.chatsapplication.message

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.chatsapplication.databinding.ActivityProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_IMG=1
    }
    lateinit var binding: ActivityProfileBinding
    private var imageUri:Uri?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgBack.setOnClickListener(){
            onBackPressed()
        }
        binding.btnSave.setOnClickListener(){
            uploadProfile()
        }
        binding.userImage.setOnClickListener(){
            openGallery()
        }
    }

    private fun openGallery() {
        Intent(Intent.ACTION_GET_CONTENT).also {
            it.type="image/*"
            startActivityForResult(it, REQUEST_IMG)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode== REQUEST_IMG && resultCode== RESULT_OK){
            data?.data.let {
                imageUri=it
                binding.userImage.setImageURI(it)
            }
        }
    }

    private fun uploadProfile() {
        updateUsername()
    }

    private fun updatePfp(){
        var firebaseAuth=FirebaseAuth.getInstance()
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
        var firebaseStorage=FirebaseStorage.getInstance().getReference("user")
        var uid= firebaseAuth.currentUser!!.uid

        imageUri?.let {
            firebaseStorage.child(uid).putFile(it).addOnSuccessListener {  task->
                task.metadata?.reference?.downloadUrl?.addOnSuccessListener(){ uri ->
                    var userImage= hashMapOf<String,Any>(
                        "image" to uri.toString()
                    )
                    firebaseDatabase.child(uid).setValue(userImage).addOnSuccessListener(){
                        Toast.makeText(this,"Gambar updated",Toast.LENGTH_SHORT).show()
                        var intent=Intent(this,UserActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }

    private fun updateUsername() {
        var firebaseAuth=FirebaseAuth.getInstance()
        var firebaseDatabase=FirebaseDatabase.getInstance().getReference("user")
        var uid= firebaseAuth.currentUser!!.uid
        var nama=binding.etUserName.text.toString()
        var image=imageUri.toString()

        var userName= hashMapOf<String,Any>(
            "name" to nama,
            "image" to image
        )
        firebaseDatabase.child(uid).updateChildren(userName)
            .addOnSuccessListener(){
            Toast.makeText(this,"Profile updated",Toast.LENGTH_SHORT).show()
            var intent=Intent(this,UserActivity::class.java)
            startActivity(intent)
        }
            .addOnFailureListener(){
                Toast.makeText(this,"Failed to update profile",Toast.LENGTH_SHORT).show()
            }
    }
}