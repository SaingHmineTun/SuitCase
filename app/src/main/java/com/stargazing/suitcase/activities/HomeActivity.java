package com.stargazing.suitcase.activities;

import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.stargazing.suitcase.utils.PrefManager;
import com.stargazing.suitcase.adapters.FragmentLifeCycle;
import com.stargazing.suitcase.adapters.ViewPagerAdapter;
import com.stargazing.suitcase.custom.ShakeDetector;
import com.stargazing.suitcase.database.AppDatabase;
import com.stargazing.suitcase.database.dao.ItemDao;
import com.stargazing.suitcase.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private ViewPagerAdapter pagerAdapter;
    public String userEmail;
    public ItemDao itemDao;
    private SensorManager sensorManager;
    private ShakeDetector shakeDetector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListeners();
        initShakeSensor();
    }


    private void initShakeSensor() {

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        shakeDetector = new ShakeDetector(this::showClearAllItemsDialog);
    }

    private AlertDialog clearItemsDialog;

    private void showClearAllItemsDialog() {
        if (clearItemsDialog == null) {
            clearItemsDialog = new AlertDialog.Builder(this)
                    .setTitle("Clear items")
                    .setMessage("Are you sure to clear all finished and unfinished items")
                    .setPositiveButton("YES", (dialog, which) -> {
                        itemDao.clearAll();
                        pagerAdapter.refreshDataSetChanged();
                        dialog.cancel();
                    })
                    .setNegativeButton("NO", ((dialog, which) -> {
                        dialog.cancel();
                    }))
                    .create();
        }
        if (!clearItemsDialog.isShowing()) {
            clearItemsDialog.show();
        }
    }

    private void initListeners() {
        binding.ivUser.setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });
        binding.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int currentPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                FragmentLifeCycle fragmentToShow = (FragmentLifeCycle) pagerAdapter.getItem(position);
                fragmentToShow.onResumeFragment();
                FragmentLifeCycle fragmentToHide = (FragmentLifeCycle) pagerAdapter.getItem(currentPosition);
                fragmentToHide.onPauseFragment();
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (shakeDetector != null)
            shakeDetector.stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shakeDetector != null)
            shakeDetector.start(sensorManager, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void initUi() {
        userEmail = PrefManager.getUser(this);
        itemDao = AppDatabase.getInstance(this).itemDao();
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        binding.viewPager.setAdapter(pagerAdapter);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

}
