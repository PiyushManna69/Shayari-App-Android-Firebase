package com.infinisync.shayariapp.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.infinisync.shayariapp.model.ShayariModel;
import java.util.List;

@Dao
public interface ShayariDao {

    @Query("SELECT * FROM shayari")
    List<ShayariModel> getAllShayari();

    @Query("SELECT * FROM shayari WHERE shayariId = :shayariId")
    ShayariModel getShayariById(String shayariId);

    @Insert
    void insert(ShayariModel shayari);

    @Query("DELETE FROM shayari WHERE shayariId = :shayariId")
    void deleteById(String shayariId);
}
