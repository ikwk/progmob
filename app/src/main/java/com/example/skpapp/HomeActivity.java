package com.example.skpapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.skpapp.Utils.DataPart;
import com.example.skpapp.Utils.VolleyMultipartRequest;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements BottomSheetCallback {

    public RecyclerView recyclerView;
    private FragmentManager fragmentManager;
    private FloatingActionButton btnFab;
    int NEW_PDF_UPLOAD_FILE = 2;
    int REPLACE_PDF_UPLOAD_FILE = 3;
    String displayNamePDF;
    private byte[] fileData;
    private ProgressDialog dialog;
    private SharedPreferences userPref;
    GetData getData;
    BottomSheetCallback callback;

    String id_post_callback;
    boolean status_callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        callback = this;
//        recyclerView = new RecyclerView(getApplicationContext());
        recyclerView = findViewById(R.id.recycler2);
        getData = new GetData();
        getData.setmContext(getApplicationContext());
        getData.setRecyclerView(recyclerView);
        getData.setFragmentManager(getSupportFragmentManager(),callback);
        getData.getPosts();
        btnFab = findViewById(R.id.fab);
        btnFab.setOnClickListener(addButtonListener);
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        userPref = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);


    }

    private View.OnClickListener addButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "UPLOAD PDF", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType("application/pdf");
            startActivityForResult(i, NEW_PDF_UPLOAD_FILE);

        }
    };

    public void updatePdf() {
        Toast.makeText(getApplicationContext(), "UPLOAD PDF UNTUK UPDATE", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("application/pdf");
        startActivityForResult(i, REPLACE_PDF_UPLOAD_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);
            String path = myFile.getAbsolutePath();
            String displayName = null;

            if (uriString.startsWith("content://")) {
                Cursor cursor = null;
                try {
                    cursor = this.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                        Log.d("nameeeee>>>>  ", displayName);
                        fileData = getPDF(displayName, uri);
                        displayNamePDF = displayName;
                        if(requestCode==NEW_PDF_UPLOAD_FILE){
                            registrasi(Request.Method.POST, Constant.POSTS_CREATE, null);
                            getData.getPosts();
                        }else{
                            registrasi(Request.Method.POST, Constant.POSTS_UPDATE, id_post_callback);
                        }
                    }
                } finally {
                    cursor.close();
                }
            } else if (uriString.startsWith("file://")) {
                displayName = myFile.getName();
                Log.d("nameeeee>>>>  ", displayName);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);

    }

    private byte[] getPDF(String displayName, Uri uri) {
        InputStream iStream = null;
        byte[] inputData = null;
        try {
            iStream = getContentResolver().openInputStream(uri);
            inputData = getBytes(iStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputData;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    private void registrasi(int method, String url, String post_id) {
        dialog.setMessage("Menambahkan SKP");
        dialog.show();
        FirebaseMessaging.getInstance().unsubscribeFromTopic("allDevices");

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(method, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject object = new JSONObject(new String(response.data));
                            if (object.getBoolean("success")){
                                dialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Tambah SKP Sukses", Toast.LENGTH_SHORT).show();
                            } else{
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            dialog.dismiss();
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Tambah SKP Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        dialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> map = new HashMap<>();
                long filename = System.currentTimeMillis();
                map.put("file", new DataPart(filename + ".pdf", fileData));
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = userPref.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }

            //tambahkan parameter
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("id", post_id);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(HomeActivity.this);
        queue.add(volleyMultipartRequest);
    }

    @Override
    public void callbackId(String id_posts) {
        this.id_post_callback = id_posts;
    }

    @Override
    public void callbackStatus(boolean status) {
        this.status_callback = status;
        if(this.status_callback){
            Toast.makeText(getApplicationContext(),"HAPUS WKWKWKWK",Toast.LENGTH_LONG).show();
            registrasi(Request.Method.POST, Constant.POSTS_DELETE, id_post_callback);
        }else{
            Toast.makeText(getApplicationContext(),"UPDATE WKWKWKWK",Toast.LENGTH_LONG).show();
            updatePdf();
        }
        getData.getPosts();
    }
}