package com.salwa.salwa.homepage.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.salwa.salwa.LoginActivity
import com.salwa.salwa.R
import com.salwa.salwa.databinding.ActivityChangePasswordBinding

class ChangePasswordActivity : AppCompatActivity() {

    private var binding: ActivityChangePasswordBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.title = "Change Password"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // VALIDASI FORM PASSWORD
        validatePasswordForm()

    }

    private fun validatePasswordForm() {
        binding?.saveBtn?.setOnClickListener {
            val email = binding?.emailEt?.text.toString().trim()
            val currentEmail = FirebaseAuth.getInstance().currentUser?.email

            if(email.isEmpty()) {
                binding?.emailEt?.error = "Email must be filled"
                return@setOnClickListener
            }
            else if(email != currentEmail) {
                binding?.emailEt?.error = "Email wrong"
                return@setOnClickListener
            }

            //KIRIMKAN EMAIL CHANGE PASSWORD KE PENGGUNA
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener {
                    if(it.isSuccessful) {
                        showSuccessDialog()
                    }
                    else {
                        Toast.makeText(this, "Failure to change password", Toast.LENGTH_SHORT).show()
                        Log.e("TAG", it.toString())
                    }
                }

        }
    }

    private fun showSuccessDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Cek Email Anda")
        dialog.setMessage("Email telah dikonfirmasi, silahkan cek email yang telah kami kirimkan pada email anda untuk mengubah Password")
        dialog.setIcon(R.drawable.ic_baseline_check_circle_24)
        dialog.setPositiveButton("YA"){ it,_ ->
            // sign out dari firebase autentikasi
            FirebaseAuth.getInstance().signOut()
            // go to login activity
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            it.dismiss()
            startActivity(intent)
            finish()
        }
        dialog.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}