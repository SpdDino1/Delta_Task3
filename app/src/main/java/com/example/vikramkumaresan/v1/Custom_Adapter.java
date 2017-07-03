package com.example.vikramkumaresan.v1;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Custom_Adapter extends ArrayAdapter {
    Context ctx;
    ArrayList<String> names;
    ArrayList<String> paths;

    public Custom_Adapter(@NonNull Context context, ArrayList<String> names,ArrayList<String> paths) {
        super(context,R.layout.custom_row,names);
        ctx=context;
        this.names=names;
        this.paths=paths;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View custom_view = inflater.inflate(R.layout.custom_row,parent,false);

        ImageView img = (ImageView)custom_view.findViewById(R.id.img);
        TextView txt = (TextView)custom_view.findViewById(R.id.txt);

        Picasso.with(ctx).load(Uri.parse(paths.get(position))).into(img);
        txt.setText(names.get(position));

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Search_History.delete_row(position);
            }
        });
        return custom_view;
    }

}
