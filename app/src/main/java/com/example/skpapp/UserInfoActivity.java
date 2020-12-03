package com.example.skpapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserInfoActivity extends AppCompatActivity {

    private TextInputLayout LayoutName, LayoutLastName;
    private TextInputEditText txtName, txtLastname;
    private TextView txtSelectPhoto;
    private Button btnContinue;
    private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        init();
    }

    private void init() {
        LayoutLastName = findViewById(R.id.txtLayoutLastNameUserInfo);
        LayoutName = findViewById(R.id.txtLayoutNameUserInfo);
    }
}