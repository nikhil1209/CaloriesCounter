package com.nikhil.caloriescounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import data.DatabaseHandler;
import model.Food;

public class MainActivity extends AppCompatActivity {
    private EditText foodName,foodCals;
    private Button submitButton;
    private DatabaseHandler dba;
    private TextView skp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dba = new DatabaseHandler(MainActivity.this);
        foodName = (EditText) findViewById(R.id.foodEdittext);
        foodCals = (EditText) findViewById(R.id.caloriesEdittext);
        submitButton = (Button) findViewById(R.id.submitButton);
        skp=(TextView) findViewById(R.id.skip);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDatatoDB();
            }
        });

        skp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,display_food_activity.class));
            }
        });


    }
    private void saveDatatoDB(){
        Food food=new Food();
        String name=foodName.getText().toString().trim();
        String calsString=foodCals.getText().toString();

        int cals=Integer.parseInt(calsString);

        if(name.equals("")||calsString.equals("")){
            Toast.makeText(getApplicationContext(),"No empty fields allowed",Toast.LENGTH_LONG).show();
        }else{
            food.setFoodName(name);
            food.setCalories(cals);

            dba.addFood(food);
            dba.close();

            //clear the form
            foodName.setText("");
            foodCals.setText("");

            //to next screen
            startActivity(new Intent(MainActivity.this,display_food_activity.class));
        }
    }
}
