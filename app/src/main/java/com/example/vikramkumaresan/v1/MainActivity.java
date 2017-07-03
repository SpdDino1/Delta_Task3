package com.example.vikramkumaresan.v1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

//Fix menu checkbox

public class MainActivity extends AppCompatActivity {
    public static Intent enter_name;
    int ENTER_POKEMON_NAME=1;

    static TextView name;
    static TextView weight;
    static TextView height;
    static TextView type;
    static ImageView img;

    public static ArrayList<String> pokemon_names;
    public static ArrayList<String> pic_paths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name=(TextView)findViewById(R.id.name);
        type=(TextView)findViewById(R.id.type);
        height=(TextView)findViewById(R.id.height);
        weight=(TextView)findViewById(R.id.weight);
        img=(ImageView)findViewById(R.id.img);
        pokemon_names=new ArrayList<String>();
        pic_paths=new ArrayList<String>();

        //Storage Retrieval Mech

        SharedPreferences pref = getSharedPreferences("names",MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("paths",MODE_PRIVATE);

        if(pref.getInt("size",0)!=0){
            for (int i=0;i<pref.getInt("size",0);i++){
                pokemon_names.add(pref.getString(""+i,null));
                pic_paths.add(pref2.getString(""+i,null));
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("Search History")){
            Intent search = new Intent(this,Search_History.class);
            startActivity(search);
        }
        return true;
    }

    public void search(View view){
        enter_name = new Intent(this,Pokemon_Name_Dialog.class);
        startActivityForResult(enter_name,ENTER_POKEMON_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK && requestCode==ENTER_POKEMON_NAME){
            Access_API obj = new Access_API(enter_name.getStringExtra("Name"),getApplicationContext());
            Intent loadScreen = new Intent(this,Loading.class);
            startActivity(loadScreen);
            obj.execute();
        }
    }

    public static void Async_Complete(final ArrayList<String> api_data, final Context ctx){
        //Updating UI
        name.setText(api_data.get(0));
        type.setText(api_data.get(1));
        height.setText(api_data.get(2));
        weight.setText(api_data.get(3));
        Picasso.with(ctx).load(api_data.get(4)).into(img, new Callback() {
            //Storing Mech for pic+name
            @Override
            public void onSuccess() {
                //ImgViev to Bitmap, then storage mech
                try {
                    File pic_file = File.createTempFile(api_data.get(0),".png",ctx.getExternalFilesDir("Poke_Pics"));
                    BitmapDrawable draw = (BitmapDrawable)img.getDrawable();
                    Bitmap pic = draw.getBitmap();

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    pic.compress(Bitmap.CompressFormat.PNG,0,stream);

                    FileOutputStream outstream = new FileOutputStream(pic_file);
                    outstream.write(stream.toByteArray());
                    outstream.flush();
                    outstream.close();
                    stream.close();

                    Uri path = FileProvider.getUriForFile(ctx,BuildConfig.APPLICATION_ID + ".provider",pic_file);
                    pokemon_names.add(api_data.get(0));
                    pic_paths.add(path.toString());

                    Loading.ctx.finish();

                } catch (IOException e) {
                    Log.d("TAG","File Creation Error = "+e.toString());
                }
            }

            @Override
            public void onError() {

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        //Storage Mech
        if(pokemon_names.size()!=0){
            SharedPreferences pref = getSharedPreferences("names",MODE_PRIVATE);
            SharedPreferences pref2 = getSharedPreferences("paths",MODE_PRIVATE);
            SharedPreferences.Editor edit = pref.edit();
            SharedPreferences.Editor edit2 = pref2.edit();

            edit.putInt("size",pokemon_names.size());
            edit2.putInt("size",pic_paths.size());

            for(int i=0;i<pokemon_names.size();i++){
                edit.putString(""+i,pokemon_names.get(i));
                edit2.putString(""+i,pic_paths.get(i));
            }
            edit.apply();
            edit2.apply();
        }
    }

    public static void update_data(int position){
        pokemon_names.remove(position);
        pic_paths.remove(position);
    }
}
