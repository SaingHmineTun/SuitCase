package com.stargazing.suitcase.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stargazing.suitcase.database.AppDatabase;
import com.stargazing.suitcase.database.dao.UserDao;
import com.stargazing.suitcase.database.entities.User;
import com.stargazing.suitcase.utils.PrefManager;
import com.stargazing.suitcase.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
    }

    private void initUi() {
        String userEmail = PrefManager.getUser(this);
        if (userEmail != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
            initDatabase();
            initListener();
        }
    }

    private void initDatabase() {
        userDao = AppDatabase.getInstance(this).userDao();
    }

    private void initListener() {
        binding.btSignin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            if (!email.isEmpty() && !password.isEmpty()) {
                User user = userDao.getUserByEmail(email);
                if (user != null) {
                    if (user.getPassword().equals(password)) {
                        loginSuccess(user);
                    } else {
                        Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Cannot find user with provided email", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter email and password to login!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btSingup.setOnClickListener(v -> {
            Intent intent = new Intent(this, SignupActivity.class);
            startActivity(intent);
        });

    }

    private void loginSuccess(User user) {
        PrefManager.setUser(this, user.getEmail());
        var intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }
}