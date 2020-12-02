package com.example.skpapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.skpapp.R;

public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;


    public ViewPagerAdapter(Context context) {
        this.context = context;
    }

    private int images[] = {
            R.drawable.unud1,
            R.drawable.unud2,
            R.drawable.unud3,
    };


    private String titles[] = {
            "Coba",
            "cobaaa2",
            "Coba3"
    };

    private String descs[] = {
            "lorem ipsum dolor contraint spaces dolor ipsum loremters termainal",
            "lorem ipsum dolor contraint spaces dolor ipsum loremters termainal",
            "lorem ipsum dolor contraint spaces dolor ipsum loremters termainal"
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.view_pager,container,false);

        ImageView imageView = v.findViewById(R.id.imgViewPager);
        TextView txtTitle = v.findViewById(R.id.txtTitleViewPager);
        TextView txtDesc = v.findViewById(R.id.txtDescViewPager);

        imageView.setImageResource(images[position]);
        txtTitle.setText(titles[position]);
        txtDesc.setText(descs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
