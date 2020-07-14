package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Food;

public class DatabaseHandler extends SQLiteOpenHelper {
    public final ArrayList<Food> foodList=new ArrayList<>();

    public DatabaseHandler(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Constants.TABLE_NAME+"("+Constants.KEY_ID+" INTEGER PRIMARY KEY, "+
                Constants.FOOD_NAME+" TEXT,"+Constants.FOOD_CALORIES+" INT, "+Constants.DATE_NAME+" LONG);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Constants.TABLE_NAME);
        onCreate(db);
    }

    //get total items saved
    public int getTotalItems(){
        int totalItems=0;
        SQLiteDatabase dba=this.getReadableDatabase();
        Cursor cursor=dba.rawQuery("SELECT * FROM "+Constants.TABLE_NAME,null);
        totalItems=cursor.getCount();
        cursor.close();
        return totalItems;
    }

    //get total cals consumed
    public int totalCalories(){
        int cals=0;

        SQLiteDatabase dba=this.getReadableDatabase();
        Cursor cursor=dba.rawQuery("SELECT SUM( "+Constants.FOOD_CALORIES
        +") FROM "+Constants.TABLE_NAME,null);
        if(cursor.moveToFirst()){
            cals=cursor.getInt(0);
        }
        cursor.close();
        dba.close();
        return cals;
    }

    // delete food item
    public void deleteFood(int id){
        SQLiteDatabase dba=this.getWritableDatabase();
        dba.delete(Constants.TABLE_NAME,Constants.KEY_ID+"=?"
        ,new String[]{String.valueOf(id)});
        dba.close();
    }

    // add food item to db
    public void addFood(Food food){
        SQLiteDatabase dba=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(Constants.FOOD_NAME,food.getFoodName());
        values.put(Constants.FOOD_CALORIES,food.getCalories());
        values.put(Constants.DATE_NAME,System.currentTimeMillis());

        dba.insert(Constants.TABLE_NAME,null,values);
        Log.v("Added Food Item ","Yesss!!");

        dba.close();
    }

    //Get All foods
    public ArrayList<Food> getFoods(){
        foodList.clear();
        SQLiteDatabase dba=this.getReadableDatabase();

        Cursor cursor=dba.query(Constants.TABLE_NAME,new String[]{Constants.KEY_ID,Constants.FOOD_NAME
        ,Constants.FOOD_CALORIES,Constants.DATE_NAME},null,null,null,
                null,Constants.DATE_NAME+" DESC");
        //loop through
        if(cursor.moveToFirst()){
            do{
                Food food=new Food();
                food.setFoodId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));
                food.setFoodName(cursor.getString(cursor.getColumnIndex(Constants.FOOD_NAME)));
                food.setCalories(cursor.getInt(cursor.getColumnIndex(Constants.FOOD_CALORIES)));

                DateFormat dateFormat=DateFormat.getDateInstance();
                String date=dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                food.setRecordDate(date);

                foodList.add(food);

            }while(cursor.moveToNext());
        }
        cursor.close();
        dba.close();

        return foodList;
    }
}
