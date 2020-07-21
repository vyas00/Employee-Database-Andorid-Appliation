package com.example.android.fragmentlifecycle;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class OneFragment extends Fragment {

private String TAG="OneFragment";

private int eCount=0;

    public OneFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private ArrayList<Employee> employeeList = new ArrayList<Employee>();
    private EditText etName;
    private EditText etId;
    private Button btnAdd;
    private Button btnDisplay;
    private TextView tvCount;
    private  Button btnClearDatabase;
    private Button btnUpdateEmployee;

   /* private  Button btnDeleteThisEmployee;*/

    private String currentAddedTime;
    private String currentDisplayTime;



    private int hisCurrrentId=-1;
    private  boolean clickOnAddButton=true;
    private int lastEmployeeAdded=0;


    DatabaseHandler db;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFragmentOne = inflater.inflate(R.layout.fragment_one, container, false);


        etId = viewFragmentOne.findViewById(R.id.et_id);
        etName = viewFragmentOne.findViewById(R.id.et_name);
        btnAdd=viewFragmentOne.findViewById(R.id.btn_add);
        btnDisplay=viewFragmentOne.findViewById(R.id.btn_display);
        tvCount=viewFragmentOne.findViewById(R.id.tv_ecount);
        btnClearDatabase=viewFragmentOne.findViewById(R.id.btn_delete);
        btnUpdateEmployee=viewFragmentOne.findViewById(R.id.btn_update);

        /*btnDeleteThisEmployee=viewFragmentOne.findViewById(R.id.btn_deleteThisEmployee);*/



           db= new DatabaseHandler(getActivity());



            Bundle clickedEmployee=getArguments();

        if(clickedEmployee!=null)
        {        Employee cureentEmployee= (Employee) clickedEmployee.getSerializable("clickedEmployee");
            etName.setText(cureentEmployee.geteName());
            etId.setText(Integer.toString(cureentEmployee.geteId()));
            hisCurrrentId= (int) clickedEmployee.getSerializable("hisId");
            clickOnAddButton=false;
        }

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (clickOnAddButton) {

                    Boolean uniqueId = true;

                    if (etName.getText().length() != 0 && etId.getText().length() == 0) {
                        Toast.makeText(getActivity(), "Enter the employee ID ", Toast.LENGTH_LONG).show();
                    } else if (etName.getText().length() == 0 && etId.getText().length() != 0) {
                        Toast.makeText(getActivity(), "Enter the employee Name ", Toast.LENGTH_LONG).show();
                    } else if (etName.getText().length() == 0 && etId.getText().length() == 0) {
                        Toast.makeText(getActivity(), "Enter the employee Id and Employee Name ", Toast.LENGTH_LONG).show();
                    } else {

                        String name = etName.getText().toString();
                        int id = Integer.parseInt(etId.getText().toString());

                        Log.d(TAG, "if entered ");

                        if (db.getEmployeesCount() > 0) {
                            employeeList = db.getAllEmployees();
                            for (int i = 0; i < employeeList.size(); i++) {
                                if (employeeList.get(i).geteId() == id) {
                                    uniqueId = false;
                                    break;
                                }
                            }

                        }

                        if (uniqueId) {
                            currentAddedTime = getTimeStamp(System.currentTimeMillis());
                            PrefManager.setLastAddedTime(currentAddedTime);

                            lastEmployeeAdded=lastEmployeeAdded+1;
                            PrefManager.setEmployeesAdded(lastEmployeeAdded);

                            eCount = eCount + 1;
                            tvCount.setText(Integer.toString(eCount));

                            db.addEmployee(new Employee(id, name));
                            /*        Log.d(TAG, "he is : " + employeeList.get(0).geteId() + "  " + employeeList.get(0).geteName());*/

                            Toast.makeText(getActivity(), "Employee " + name + " with ID " + id + " added successfully!", Toast.LENGTH_LONG).show();

                            etId.setText("");
                            etName.setText("");

                        } else {
                            Toast.makeText(getActivity(), "Sorry, ID is already taken !", Toast.LENGTH_LONG).show();
                        }
                    }

                }
                else{
                    Toast.makeText(getActivity(), "You can only update current Employee ! ", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getEmployeesCount()>0) {

                    currentDisplayTime=getTimeStamp(System.currentTimeMillis());
                    PrefManager.setLastDisplayTime(currentDisplayTime);

                  /*  employeeList=db.getAllEmployees();*/   //How to send a complex employee list from bundle !!
                    Bundle bundleList = new Bundle();
                  /*  bundleList.putSerializable("list", (Serializable) employeeList);*/
                    bundleList.putSerializable("lastEmployeeCount", (Serializable) lastEmployeeAdded);

                    TwoFragment two = new TwoFragment();
                    two.setArguments(bundleList);
                    getFragmentManager().beginTransaction().replace(R.id.frame_container, two).addToBackStack(TAG).commit();

                }
                else {

                    Toast.makeText(getActivity(), "No Employees yet !", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnClearDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteTableEmployee();
                Toast.makeText(getActivity(), "Employees Database has been deleted ! ", Toast.LENGTH_LONG).show();
            }
        });

        btnUpdateEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (etName.getText().length() != 0 && etId.getText().length() != 0) {

                    Boolean uniqueId = true;

                     String name = etName.getText().toString();
                      int id = Integer.parseInt(etId.getText().toString());

                    Log.d(TAG, "if entered ");

                    if (db.getEmployeesCount() > 0) {
                        employeeList = db.getAllEmployees();
                        for (int i = 0; i < employeeList.size(); i++) {
                            if (employeeList.get(i).geteId() == id && id!=hisCurrrentId) {
                                uniqueId = false;
                                break;
                            }
                        }

                    }

                    if(uniqueId){

                    Employee updateEmploye = new Employee();
                    updateEmploye.seteId(Integer.parseInt(etId.getText().toString()));
                    updateEmploye.seteName(etName.getText().toString());
                    db.updateEmployee(updateEmploye, hisCurrrentId);

                    hisCurrrentId=-1;

                    clickOnAddButton = true;
                    etName.setText(""); etId.setText("");

                    Toast.makeText(getActivity(), "Employees details updated ! ", Toast.LENGTH_LONG).show();}
                    else{ Toast.makeText(getActivity(), "This ID is already taken, enter other ID ! ", Toast.LENGTH_LONG).show();}
                }
               else if (etName.getText().length() != 0 && etId.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter the employe Id ", Toast.LENGTH_LONG).show();
                }
                else if (etName.getText().length() == 0 && etId.getText().length() == 0) {
                    Toast.makeText(getActivity(), "No Employee to update ", Toast.LENGTH_LONG).show();
                }
                else if (etName.getText().length() == 0 && etId.getText().length() != 0) {
                    Toast.makeText(getActivity(), "Enter the employee Name ", Toast.LENGTH_LONG).show();
                }


            }
        });

       /* btnDeleteThisEmployee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etName.getText().length() != 0 && etId.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter the employee ID ", Toast.LENGTH_LONG).show();
                } else if (etName.getText().length() == 0 && etId.getText().length() != 0) {
                    Toast.makeText(getActivity(), "Enter the employee Name ", Toast.LENGTH_LONG).show();
                } else if (etName.getText().length() == 0 && etId.getText().length() == 0) {
                    Toast.makeText(getActivity(), "Enter the employee Id and Employee Name ", Toast.LENGTH_LONG).show();
                }
else{
                String name = etName.getText().toString();
                int id = Integer.parseInt(etId.getText().toString());


                if (hisCurrrentId != -1) {
                    String currentName=db.getEmployee(hisCurrrentId).geteName();
                    if(id==hisCurrrentId && name.equals(currentName)) {
                        db.deleteEmployee(hisCurrrentId);
                        Toast.makeText(getActivity(), "This employee has been deleted successfully ", Toast.LENGTH_LONG).show();
                    }
                   else if(id!=hisCurrrentId && name.equals(currentName)){
                        Toast.makeText(getActivity(), "This id does not match with the selected ID, can't delete ", Toast.LENGTH_LONG).show();
                    }
                    else if(id==hisCurrrentId && name.equals(currentName)==false){
                        Toast.makeText(getActivity(), "This Name does not match with the selected Name, can't delete ", Toast.LENGTH_LONG).show();
                    }
                    else{Toast.makeText(getActivity(), "This Name & ID does not match with the selected Name & ID, can't delete ", Toast.LENGTH_LONG).show();}

                } else {
                    Toast.makeText(getActivity(), "No employee to delete ", Toast.LENGTH_LONG).show();
                }
            }
            }
        });*/



        return viewFragmentOne;
    }



    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);

        return date;
    }

}