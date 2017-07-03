package com.example.vikramkumaresan.v1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Pokemon_Name_Dialog extends AppCompatActivity {
    EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon__name__dialog);

        name = (EditText)findViewById(R.id.name);
    }

    public void give_name(View view){
        MainActivity.enter_name.putExtra("Name",name.getText().toString());
        setResult(RESULT_OK);
        finish();
    }
}
