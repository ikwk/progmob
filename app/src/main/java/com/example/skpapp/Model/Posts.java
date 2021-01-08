package com.example.skpapp.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class Posts {

    @PrimaryKey(autoGenerate = false)
    @NonNull
    private String id;

    @ColumnInfo(name = "id_user")
    private String id_user;

    @ColumnInfo(name = "file")
    private String file;

    @ColumnInfo(name = "created_at")
    private String date;


    //id
    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    //id_user
    public String getId_user(){
        return id_user;
    }

    public void setId_user(String id_user){
        this.id_user= id_user;
    }

    //file
    public String getFile(){
        return file;
    }

    public void setFile(String file){
        this.file = file;
    }

    //date
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}

