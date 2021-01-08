package com.example.skpapp.Dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.skpapp.Model.Posts;

import java.util.List;

@Dao
public interface PostDao {
    @Transaction
    @Query("SELECT * FROM posts ORDER BY ID")
    List<Posts> loadAllPosts();

    @Insert
    void insertPendaftaran(Posts posts);

    @Query("DELETE FROM posts")
    void deleteAll();

}
