package com.example.skpapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.skpapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialog extends BottomSheetDialogFragment {
    String id_post;
    BottomSheetCallback callback;


    public BottomSheetDialog(String id_post, BottomSheetCallback callback) {
        this.id_post = id_post;
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
            ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.crud_buttons,
                container, false);

        Button updateBtn = v.findViewById(R.id.algo_button);
        Button deleteBtn = v.findViewById(R.id.course_button);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        id_post, Toast.LENGTH_SHORT)
                        .show();
                dismiss();
                callback.callbackId(id_post);
                callback.callbackStatus(false);
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        id_post, Toast.LENGTH_SHORT)
                        .show();
                dismiss();
                callback.callbackId(id_post);
                callback.callbackStatus(true);
            }
        });
        return v;
    }
}
