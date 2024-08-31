package com.stargazing.suitcase.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.stargazing.suitcase.database.AppDatabase;
import com.stargazing.suitcase.database.dao.UserDao;
import com.stargazing.suitcase.database.entities.User;
import com.stargazing.suitcase.utils.PrefManager;
import com.stargazing.suitcase.databinding.ActivityProfileBinding;
import com.stargazing.suitcase.databinding.DialogChangePasswordBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private UserDao userDao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initDatabase();
        initUi();
        initListeners();
    }

    private void initUi() {
        binding.tvEmail.setText(user.getEmail());
        binding.tvName.setText(user.getUsername());
    }

    private void initDatabase() {
        userDao = AppDatabase.getInstance(this).userDao();
        user = userDao.getUserByEmail(PrefManager.getUser(this));
    }

    private void initListeners() {
        binding.btLogout.setOnClickListener(v -> {
            showLogoutDialog();
        });

        binding.btChangePassword.setOnClickListener(v -> {
            showChangePasswordDialog();
        });

    }

    private AlertDialog changePasswordDialog;

    private void showChangePasswordDialog() {
        if (changePasswordDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            var dialogBinding = DialogChangePasswordBinding.inflate(getLayoutInflater());
            changePasswordDialog = builder.setView(dialogBinding.getRoot())
                    .setPositiveButton("Confirm", (dialog1, which) -> {
                        String oldPassword = dialogBinding.etOldPassword.getText().toString();
                        String newPassword = dialogBinding.etNewPassword.getText().toString();
                        if (!oldPassword.isBlank() && !newPassword.isBlank()) {
                            if (oldPassword.equals(user.getPassword())) {
                                user.setPassword(newPassword);
                                userDao.updateUser(user);
                                logout();
                            } else {
                                Toast.makeText(this, "Old password does not match", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Password fields must not be empty", Toast.LENGTH_SHORT).show();
                        }

                    })
                    .setNegativeButton("Cancel", ((dialog1, which) -> {

                    })).create();


        }
        changePasswordDialog.show();
    }

    private void logout() {
        PrefManager.clear(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private AlertDialog logoutDialog;

    private void showLogoutDialog() {
        if (logoutDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            logoutDialog = builder.setTitle("Logout")
                    .setMessage("Are you sure you want to logout?")
                    .setPositiveButton("YES", (dialog, which) -> {
                        logout();
                        dialog.cancel();
                    })
                    .setNegativeButton("NO", (dialog, which) -> {
                        dialog.cancel();
                    }).create();
        }
        logoutDialog.show();
    }
}