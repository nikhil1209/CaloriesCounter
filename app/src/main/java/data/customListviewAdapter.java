package data;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nikhil.caloriescounter.R;
import com.nikhil.caloriescounter.food_item_details;

import java.util.ArrayList;

import model.Food;

public class customListviewAdapter extends ArrayAdapter<Food> {
    private int layoutResource;
    private Activity activity;
    private ArrayList<Food> foodlist=new ArrayList<>();

    public customListviewAdapter(@NonNull Activity act, int resource, ArrayList<Food> data) {
        super(act, resource, data);
        layoutResource=resource;
        activity=act;
        foodlist=data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return foodlist.size();
    }

    @Nullable
    @Override
    public Food getItem(int position) {
        return foodlist.get(position);
    }

    @Override
    public int getPosition(@Nullable Food item) {
        return super.getPosition(item);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View row=convertView;
        ViewHolder holder=null;

        if(row==null||(row.getTag()==null)){
            LayoutInflater inflater=LayoutInflater.from(activity);
            row=inflater.inflate(layoutResource,null);

            holder=new ViewHolder();

            holder.foodName=(TextView) row.findViewById(R.id.name);
            holder.foodCalories=(TextView) row.findViewById(R.id.calories);
            holder.foodDate=(TextView) row.findViewById(R.id.dateText);

            row.setTag(holder);
        }else {
            holder=(ViewHolder) row.getTag();
        }

        holder.food=getItem(position);
        holder.foodName.setText(holder.food.getFoodName());
        holder.foodCalories.setText(String.valueOf(holder.food.getCalories()));
        holder.foodDate.setText(holder.food.getRecordDate());

        final ViewHolder finalHolder = holder;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(activity, food_item_details.class);

                Bundle mBundle=new Bundle();
                mBundle.putSerializable("userObj", finalHolder.food);
                i.putExtras(mBundle);

                activity.startActivity(i);
            }
        });



        return row;
    }
    public class ViewHolder{
        Food food;
        TextView foodName;
        TextView foodCalories;
        TextView foodDate;
    }
}
