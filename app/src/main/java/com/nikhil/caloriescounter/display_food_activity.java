package com.nikhil.caloriescounter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import data.DatabaseHandler;
import data.customListviewAdapter;
import model.Food;
import util.Utils;

public class display_food_activity extends AppCompatActivity {
    private DatabaseHandler dba;
    private ArrayList<Food> dbFoods=new ArrayList<>();
    private customListviewAdapter foodAdapter;
    private ListView listView;
    private Food myFood;
    private TextView totalCals,totalFoods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_food_activity);

        listView=(ListView) findViewById(R.id.list);
        totalCals=(TextView) findViewById(R.id.totalAmountTextView);
        totalFoods=(TextView) findViewById(R.id.totalItemsTextView);

        refreshData();

    }

    private void refreshData() {
        dbFoods.clear();

        dba=new DatabaseHandler(getApplicationContext());

        ArrayList<Food> foodFromDB=dba.getFoods();

        int calsValue=dba.totalCalories();
        int totalItems=dba.getTotalItems();

        String formattedValue= Utils.formatNumber(calsValue);
        String formattedItems= Utils.formatNumber(totalItems);

        totalCals.setText("Total Calories: "+formattedValue);
        totalFoods.setText("Total Foods: "+formattedItems);

        for(int i=0;i<foodFromDB.size();i++){
            String name=foodFromDB.get(i).getFoodName();
            String dateText=foodFromDB.get(i).getRecordDate();
            int cals=foodFromDB.get(i).getCalories();;
            int foodId=foodFromDB.get(i).getFoodId();

            Log.v("Food IDs: ",String.valueOf(foodId));

            myFood=new Food();
            myFood.setFoodName(name);
            myFood.setCalories(cals);
            myFood.setRecordDate(dateText);
            myFood.setFoodId(foodId);

            dbFoods.add(myFood);
        }
        dba.close();

        //setup adapter
        foodAdapter=new customListviewAdapter(display_food_activity.this,R.layout.list_row,dbFoods);
        listView.setAdapter(foodAdapter);
        foodAdapter.notifyDataSetChanged();
    }
}