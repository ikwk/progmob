package com.example.skpapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.skpapp.Adapter.AdapterPosts;
import com.example.skpapp.Database.RoomDB;
import com.example.skpapp.Model.Posts;
import com.example.skpapp.Model.User;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class GetData {
    public RecyclerView recyclerView;
    AdapterPosts adapter1;
    AdapterPosts adapter2;
    public static ArrayList<Posts> Listposts = new ArrayList<>();
    public static ArrayList<Posts> Listpostsbackup = new ArrayList<>();
    private ArrayList<User> userListBackup;
    private ArrayList<User> listUser;
    RoomDB database;
    private Context mContext;
    private SharedPreferences userPref;
    private FragmentManager fragmentManager;
    BottomSheetCallback callback;

    public void setFragmentManager(FragmentManager fragmentManager, BottomSheetCallback callback) {
        this.fragmentManager = fragmentManager;
        this.callback = callback;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        this.recyclerView = recyclerView;
    }

    public void getPosts() {
        Listposts = new ArrayList<>();
        listUser = new ArrayList<>();
        Listpostsbackup = new ArrayList<>();
        userListBackup = new ArrayList<>();
        database = RoomDB.getInstance(mContext);
        userPref = mContext.getSharedPreferences("user", Context.MODE_PRIVATE);
        StringRequest request = new StringRequest(Request.Method.GET, Constant.POSTS, response -> {
            try {
                JSONObject object1 = new JSONObject(response);
                if (object1.getBoolean("success")) {
                    database.postDao().deleteAll();
                    database.userDao().deleteAll();
                    JSONArray array = new JSONArray(object1.getString("post"));
                    for (int i=0; i<array.length(); i++){
                        JSONObject daftar = array.getJSONObject(i);
                        JSONObject userObject = daftar.getJSONObject("user");

                        User user = new User();
                        user.setIdNya(i);
                        user.setId(userObject.getInt("id"));
                        user.setNim(userObject.getString("nim"));
                        listUser.add(user);
                        database.userDao().insertUser(user);

                        Posts posts = new Posts();
                        posts.setId(daftar.getString("id"));
                        posts.setId_user(daftar.getString("user_id"));
                        posts.setDate(daftar.getString("created_at"));
                        posts.setFile(daftar.getString("file"));

                        Listposts.add(posts);
                        database.postDao().insertPendaftaran(posts);
                    }
                    adapter1 = new AdapterPosts(Listposts, listUser,mContext, fragmentManager, callback);
                    recyclerView.setAdapter(adapter1);
                }else {
                    Toast.makeText(mContext, "Get", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(mContext, "Get Data Failed", Toast.LENGTH_SHORT).show();
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error instanceof NetworkError || error instanceof AuthFailureError || error instanceof NoConnectionError || error instanceof TimeoutError) {
                    Listpostsbackup = (ArrayList<Posts>) database.postDao().loadAllPosts();
                    userListBackup = (ArrayList<User>) database.userDao().loadAllUsers();
                    adapter2 = new AdapterPosts(Listpostsbackup,userListBackup, mContext, fragmentManager, callback);
                    recyclerView.setAdapter(adapter2);
                }
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization", "Bearer "+token);
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(mContext);
        queue.add(request);
    }
}
