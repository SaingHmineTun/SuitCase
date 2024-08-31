package com.stargazing.suitcase.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stargazing.suitcase.database.entities.Item;

import java.util.List;


@Dao
public interface ItemDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addItem(Item item);

    @Update
    void updateItem(Item item);

    @Delete
    void deleteItem(Item item);

    @Query("SELECT * FROM item WHERE userEmail = :userEmail AND finish = 0")
    List<Item> getTodoItemsByUserEmail(String userEmail);

    @Query("SELECT * FROM item WHERE userEmail = :userEmail AND finish = 1")
    List<Item> getFinishedItemsByUserEmail(String userEmail);

    @Query("DELETE FROM item;")
    void clearAll();
}
