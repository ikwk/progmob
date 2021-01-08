package com.example.skpapp.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.skpapp.Model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Transaction
    @Query("SELECT * FROM users ORDER BY idNya desc")
    List<User> loadAllUsers();

    @Insert
    void insertUser(User user);

    @Query("DELETE FROM users")
    void deleteAll();
}
