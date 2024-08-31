package com.stargazing.suitcase.adapters;


import com.stargazing.suitcase.database.entities.Item;

public interface ItemClickListener {
    void onItemClicked(Item item);
    void onFinishItemClicked(Item item);
}
