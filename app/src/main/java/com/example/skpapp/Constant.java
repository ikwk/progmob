package com.example.skpapp;

public class Constant {
    public static final String URL = "http://192.168.43.200";

//    public static final String URL = "http://10.0.2.2:8000";
    public static final String HOME = URL+"/api";
    public static final String LOGIN = HOME+"/login";
    public static final String REGISTER = HOME+"/register";
    public static final String SAVE_USER_INFO = HOME+"/save_user_info";
    public static final String SPINNER_PRODI = HOME+"/prodi";

//    untuk postingan
    public static final String POSTS_CREATE = HOME+"/posts/create";
    public static final String POSTS = HOME+"/posts";
    public static final String POSTS_UPDATE = HOME+"/posts/update";
    public static final String POSTS_DELETE = HOME+"/posts/delete";

}