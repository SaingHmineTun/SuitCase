package com.stargazing.suitcase.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.stargazing.suitcase.database.converters.DateConverter;
import com.stargazing.suitcase.database.dao.ItemDao;
import com.stargazing.suitcase.database.dao.UserDao;
import com.stargazing.suitcase.database.entities.Item;
import com.stargazing.suitcase.database.entities.User;


@Database(entities = {User.class, Item.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract ItemDao itemDao();
    public abstract UserDao userDao();

    private static AppDatabase appDatabase;
    private static final String DATABASE_NAME = "suitcase";

    public static synchronized AppDatabase getInstance(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .build();
        }
        return appDatabase;
    }

}
