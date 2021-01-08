package com.example.skpapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.skpapp.BottomSheetCallback;
import com.example.skpapp.BottomSheetDialog;
import com.example.skpapp.Model.Posts;
import com.example.skpapp.Model.User;
import com.example.skpapp.R;

import java.util.ArrayList;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.ViewHolder> {
    ArrayList<Posts> Listposts;
    ArrayList<User> listUser;
    private SharedPreferences userPref;
    int status;
    private FragmentManager fragmentManager;
    BottomSheetCallback callback;

    Context context;

    public AdapterPosts(ArrayList<Posts> Listposts, ArrayList<User> listUser, Context context, FragmentManager fragmentManager, BottomSheetCallback callback) {
        this.Listposts = Listposts;
        this.listUser = listUser;
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.callback = callback;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_posts_menu, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Posts posts = Listposts.get(position);
        User user = listUser.get(position);

        holder.txtDate.setText("Published : " + posts.getDate());
        holder.name.setText("Post By : " + user.getNim());
        holder.constraintLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (posts.getId_user().equals(userPref.getString("id", null))) {
                    Log.d("CRUDLOG", "User id null do something");
                    status = 1;
                } else {
                    status = 0;
                    Log.d("CRUDLOG", "User id null do something");
                }
                BottomSheetDialog bottomSheet = new BottomSheetDialog(posts.getId(), callback);
                bottomSheet.show(fragmentManager,
                        "ModalBottomSheet");
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {

        return Listposts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView, textView1, name, txtDate;
        Button button1, button2;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            userPref = context.getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            txtDate = itemView.findViewById(R.id.txtPostDate);
            name = itemView.findViewById(R.id.postby);
//            button1 = itemView.findViewById(R.id.editrwtrgs);
//            button2 = itemView.findViewById(R.id.delrwtrgs);
            constraintLayout = itemView.findViewById(R.id.consrwtrgs);
        }
    }
}
