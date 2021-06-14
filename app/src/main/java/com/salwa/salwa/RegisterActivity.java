package com.salwa.salwa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText etName, etEmail, etPassword;
    Button btnRegister;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.nameEt);
        etEmail = findViewById(R.id.emailEt);
        etPassword = findViewById(R.id.passwordEt);
        btnRegister = findViewById(R.id.registerBtn);

        progressBar = findViewById(R.id.progress_bar);

        // klik tombol mendaftar
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // validasi nama, email, dan password
                formValidation();
            }
        });

    }

    private void formValidation() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if(name.isEmpty()) {
            Toast.makeText(this, "Nama Lengkap tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if(!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        } else if(password.length() < 6) {
            Toast.makeText(this, "Kata Sandi tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // tampilkan progress bar untuk loading
        progressBar.setVisibility(View.VISIBLE);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            // simpan data pengguna ke database
                            saveUserData(name, email);
                        } else {
                            // sembunyikan progress bar untuk selesai loading
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Ups ada kendala ketika ingin  register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void saveUserData(String name, String email) {

        String uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        Map<String, Object> users = new HashMap<>();
        users.put("name", name);
        users.put("email", email);
        users.put("uid", uid);
        users.put("role", "user");

        FirebaseFirestore.getInstance().collection("users")
                .document(uid)
                .set(users)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        // sembunyikan progress bar untuk selesai loading
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(RegisterActivity.this, "Anda berhasil mendaftar", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // sembunyikan progress bar untuk selesai loading
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(RegisterActivity.this, "Anda tidak berhasil mendaftar", Toast.LENGTH_SHORT).show();
                });

    }

    public void goToLoginPage(View view) {
        // kembali ke home page
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}