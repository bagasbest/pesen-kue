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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.salwa.salwa.homepage.HomeActivity;

public class LoginActivity extends AppCompatActivity {


    Button btnLogin;
    EditText etEmail, etPassword;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.emailEt);
        etPassword = findViewById(R.id.passwordEt);
        btnLogin = findViewById(R.id.loginBtn);

        progressBar = findViewById(R.id.progress_bar);

        // auto login jika sudah pernah login sebelumnya
        autoLogin();

        // klik login
        btnLogin.setOnClickListener(view -> {
            // validasi kolom email dan password
            validateForm();
        });


    }

    private void autoLogin() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }
    }

    private void validateForm() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Kata Sandi tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        } else if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Email tidak valid", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.length() < 6) {
            Toast.makeText(this, "Kata Sandi tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // tampilkan progress bar untuk loading
        progressBar.setVisibility(View.VISIBLE);
        // lakukan login
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // sembunyikan progress bar untuk selesai loading
                            progressBar.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            finish();
                        } else {
                            // sembunyikan progress bar untuk selesai loading
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, "Ups ada kendala ketika ingin  login", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void goToRegisterPage(View view) {
        // menuju login page
        startActivity(new Intent(this, RegisterActivity.class));
    }
}