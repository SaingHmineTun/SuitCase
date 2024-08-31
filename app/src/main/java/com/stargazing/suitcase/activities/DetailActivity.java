package com.stargazing.suitcase.activities;

import static com.stargazing.suitcase.utils.Utility.decode;
import static com.stargazing.suitcase.utils.Utility.encode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stargazing.suitcase.database.AppDatabase;
import com.stargazing.suitcase.database.dao.ItemDao;
import com.stargazing.suitcase.database.entities.Item;
import com.stargazing.suitcase.utils.PrefManager;
import com.stargazing.suitcase.databinding.ActivityDetailBinding;

import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private Item item;
    private ItemDao itemDao;
    private static final int SELECT_IMAGE_REQUEST_CODE = 1234;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initDatabase();
        initListeners();

    }

    private void initDatabase() {
        itemDao = AppDatabase.getInstance(this).itemDao();
    }


    private void initListeners() {

        binding.ivBack.setOnClickListener(v -> {
            finish();
        });

        binding.ivShare.setOnClickListener(v -> {
            if (item != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                String message = String.format(Locale.ENGLISH, """
                    Name : %s
                    Quantity : %d
                    Price : %.2f
                    """, item.getDescription(), item.getQuantity(), item.getPrice());
                sendIntent.putExtra(Intent.EXTRA_TEXT, message);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            } else {
                Toast.makeText(this, "Cannot share newly created item!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.ivItem.setOnClickListener(v -> {
            var intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_IMAGE_REQUEST_CODE);
        });

        binding.btExit.setOnClickListener(v -> {
            if (item == null) {
                finish();
            } else {
                itemDao.deleteItem(item);
                finish();
            }
        });

        binding.btSave.setOnClickListener(v -> {
            try {
                var desc = binding.etDesc.getText().toString();
                var quantity = Integer.parseInt(binding.etQuantity.getText().toString());
                var price = Double.parseDouble(binding.etPrice.getText().toString());
                var finish = binding.cbFinish.isChecked();
                if (item == null) {
                    // Add new item
                    var item = new Item(desc, quantity, price, encode(this, imageUri), finish, PrefManager.getUser(this));
                    itemDao.addItem(item);
                    finish();
                } else {
                    // Update item
                    Item item;
                    if (imageUri == null)
                        item = new Item(desc, quantity, price, this.item.getImage(), finish, PrefManager.getUser(this));
                    else
                        item = new Item(desc, quantity, price, encode(this, imageUri), finish, PrefManager.getUser(this));

                    if (!this.item.equals(item)) {
                        var newItem = this.item.update(item);
                        itemDao.updateItem(newItem);
                        this.finish();
                    } else {
                        Toast.makeText(this, "You have modified none! It cannot be updated!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Cannot save item! Please resolve the issue first", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initUi() {
        if (getIntent() != null && getIntent().getSerializableExtra("item") != null) {
            // Update item
            item = (Item) getIntent().getSerializableExtra("item");
            binding.ivItem.setImageBitmap(decode(item.getImage()));
            binding.etDesc.setText(item.getDescription());
            binding.etQuantity.setText(String.valueOf(item.getQuantity()));
            binding.etPrice.setText(String.valueOf(item.getPrice()));
            binding.cbFinish.setChecked(item.isFinish());

            binding.ivShare.setVisibility(View.VISIBLE);

            binding.btSave.setText("Update");
            binding.btExit.setText("Delete");

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_IMAGE_REQUEST_CODE) {
            imageUri = data.getData();
            binding.ivItem.setImageURI(imageUri);
        }
    }

}