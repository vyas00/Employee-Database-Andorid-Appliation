package com.example.android.fragmentlifecycle;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class EmployeeAdapter extends ArrayAdapter<Employee> {

    private String TAG="EmployeeAdapter";

    private Context context;

    public EmployeeAdapter(Activity context,ArrayList<Employee> employees) {
        super(context, 0, employees);

    }


    public View getView(int position, View convertView, ViewGroup parent) {


        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_list, parent, false);
        }


        Employee currentEmploye =getItem(position);


        int id=currentEmploye.geteId();
        String name=currentEmploye.geteName();


        TextView idView = (TextView) listItemView.findViewById(R.id.tv_id);
        idView.setText(Integer.toString(id));


        TextView nameView = (TextView) listItemView.findViewById(R.id.tv_name);
        nameView.setText(name);


        return listItemView;
        }


    }
