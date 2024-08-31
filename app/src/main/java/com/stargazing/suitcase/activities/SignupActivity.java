package com.stargazing.suitcase.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stargazing.suitcase.database.AppDatabase;
import com.stargazing.suitcase.database.dao.UserDao;
import com.stargazing.suitcase.database.entities.User;
import com.stargazing.suitcase.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {

    private ActivitySignupBinding binding;
    private UserDao userDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDatabase();
        initListeners();
    }

    private void initListeners() {
        binding.btSingup.setOnClickListener(v -> {
            userSignup();
        });
    }

    private void userSignup() {
        var username = binding.etName.getText().toString();
        var email = binding.etEmail.getText().toString();
        var password = binding.etPassword.getText().toString();
        var confirmPassword = binding.etConfirmPassword.getText().toString();
        if (!username.isBlank() && !email.isBlank() && !password.isBlank() && !confirmPassword.isBlank()) {
            User existingUser = userDao.getUserByEmail(email);
            if (existingUser == null) {
                if (password.equals(confirmPassword)) {
                    userDao.addUser(new User(username, email, password));
                    finish();
                } else {
                    Toast.makeText(this, "Password and Confirm Password must be equal", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "User with this email already exist", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please fill all the fields first to signup", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDatabase() {
        userDao = AppDatabase.getInstance(this).userDao();
    }
}