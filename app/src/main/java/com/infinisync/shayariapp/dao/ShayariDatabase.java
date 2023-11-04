package com.infinisync.shayariapp.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import com.infinisync.shayariapp.model.ShayariModel;

@Database(entities = {ShayariModel.class}, version = 1)
public abstract class ShayariDatabase extends RoomDatabase {
    public abstract ShayariDao shayariDao();
}
