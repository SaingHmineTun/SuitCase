package com.stargazing.suitcase.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.stargazing.suitcase.database.entities.User;


@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void addUser(User user);

    @Update
    void updateUser(User user);

    @Delete
    void deleteUser(User user);

    @Query("SELECT * FROM user WHERE email = :email")
    User getUserByEmail(String email);
}
