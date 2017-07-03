package com.example.vikramkumaresan.v1;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class Search_History extends AppCompatActivity {
    static ArrayAdapter adapt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search__history);

        ListView list = (ListView)findViewById(R.id.list);
        adapt = new Custom_Adapter(this,MainActivity.pokemon_names,MainActivity.pic_paths);
        list.setAdapter(adapt);

        Toast.makeText(this,"Touch a Pokemon to Remove",Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getTitle().toString().equals("Pokedex")){
            finish();
        }
        return true;
    }

    public static void delete_row(int position){
        MainActivity.pokemon_names.remove(position);
        MainActivity.pic_paths.remove(position);
        adapt.notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences pref = getSharedPreferences("names",MODE_PRIVATE);
        SharedPreferences pref2 = getSharedPreferences("paths",MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        SharedPreferences.Editor edit2 = pref2.edit();

        edit.putInt("size",MainActivity.pokemon_names.size());
        edit2.putInt("size",MainActivity.pic_paths.size());

        for(int i=0;i<MainActivity.pokemon_names.size();i++){
            edit.putString(""+i,MainActivity.pokemon_names.get(i));
            edit2.putString(""+i,MainActivity.pic_paths.get(i));
        }
        edit.apply();
        edit2.apply();
    }

    public void clear_all(View view){
        MainActivity.pokemon_names.clear();
        MainActivity.pic_paths.clear();
        adapt.notifyDataSetChanged();
    }
}
