package com.example.android.fragmentlifecycle;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;


public class TwoFragment extends Fragment {

    private String TAG="TwoFragment";

    public TwoFragment() { }



    private ArrayList<Employee> employeeList =new ArrayList<Employee>();

    private EmployeeAdapter eAdapter;
    private ListView employeeListView;
    private Button btnSortId;
    private Button btnSortName;
    private TextView tvLastSoted;
    private TextView tvLastEmployeeCount;
    private int lastEmployeeCount;
    private TextView tvTotalEmployeesCount;


    public static final String ID_SORT_ASCENDING = "ID(ascending)" ;
    public static final String ID_SORT_DESCENDING = "ID(descending)" ;
    public static final String NAME_SORT_ASCENDING = "Name(ascending)" ;
    public static final String NAME_SORT_DESCENDING = "Name(descending)" ;



    boolean flag_sortId=true;
    boolean flag_sortName=true;

    DatabaseHandler db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    public static final Comparator<Employee> NameComparator = new Comparator<Employee>(){

        @Override
        public int compare(Employee e1, Employee e2) {
            return e1.geteName().compareTo(e2.geteName());
        }

    };

    public static final Comparator<Employee> IdComparator = new Comparator<Employee>(){

        @Override
        public int compare(Employee e1, Employee e2) {
            return e1.geteId() - e2.geteId();
        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewFragmentTwo= inflater.inflate(R.layout.fragment_two,container,false);




        btnSortId =viewFragmentTwo.findViewById(R.id.btn_idSort);
        btnSortName =viewFragmentTwo.findViewById(R.id.btn_nameSort);
        tvLastSoted=viewFragmentTwo.findViewById(R.id.tv_lastSorting);
        tvLastEmployeeCount=viewFragmentTwo.findViewById(R.id.tv_lastcount);
        tvTotalEmployeesCount=viewFragmentTwo.findViewById(R.id.tv_totalEmployeesCount);

        tvLastSoted.setText(PrefManager.getLastSorting());


        Bundle listbundle=getArguments();
        if(listbundle!=null) {
            /*employeeList = (ArrayList<Employee>) listbundle.getSerializable("list");*/      //How to receive the employee list from bundle!!
            lastEmployeeCount= (int) listbundle.getSerializable("lastEmployeeCount");
        }


          db= new DatabaseHandler(getActivity());
          employeeList=db.getAllEmployees();

      if(lastEmployeeCount==0) { tvLastEmployeeCount.setText(Integer.toString(PrefManager.getLastEmployeesCount())); }
      else{tvLastEmployeeCount.setText(Integer.toString(lastEmployeeCount));}


        tvTotalEmployeesCount.setText("Total Employees Count: "+Integer.toString(employeeList.size()));

        SetListByLastSorting(PrefManager.getLastSorting(), employeeList);

        eAdapter = new EmployeeAdapter(getActivity(),employeeList);

         employeeListView= viewFragmentTwo.findViewById(R.id.list_employee);
         employeeListView.setAdapter(eAdapter);


        btnSortId.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {

                if(flag_sortId)
                {
                    Collections.sort(employeeList, IdComparator);
                    Toast.makeText(getActivity(), "IDs sorted in ascending order ", Toast.LENGTH_LONG).show();
                    flag_sortId=false;

                    PrefManager.setLastSorting(ID_SORT_ASCENDING);
                    tvLastSoted.setText(ID_SORT_ASCENDING);

                }
                else {
                    Collections.sort(employeeList, Collections.reverseOrder(IdComparator));
                    Toast.makeText(getActivity(), "IDs sorted in descending order ", Toast.LENGTH_LONG).show();
                    flag_sortId=true;


                    PrefManager.setLastSorting(ID_SORT_DESCENDING);
                    tvLastSoted.setText(ID_SORT_DESCENDING);
                }

                eAdapter = new EmployeeAdapter(getActivity(),employeeList);
                employeeListView.setAdapter(eAdapter);


            }
        });

        btnSortName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag_sortName)
                {
                    Collections.sort(employeeList,NameComparator);
                    Toast.makeText(getActivity(), "Names sorted in ascending order ", Toast.LENGTH_LONG).show();
                    flag_sortName=false;


                    PrefManager.setLastSorting(NAME_SORT_ASCENDING);
                    tvLastSoted.setText(NAME_SORT_ASCENDING);
                }
                else {
                    Collections.sort(employeeList, Collections.reverseOrder(NameComparator));
                    Toast.makeText(getActivity(), "Names sorted in descending order ", Toast.LENGTH_LONG).show();
                    flag_sortName=true;


                    PrefManager.setLastSorting(NAME_SORT_DESCENDING);
                    tvLastSoted.setText(NAME_SORT_DESCENDING);
                }

                eAdapter = new EmployeeAdapter(getActivity(),employeeList);
                employeeListView.setAdapter(eAdapter);


            }
        });

        employeeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {


                final Employee clickedEmployee= employeeList.get(position);
                final Bundle bundleEmployee = new Bundle();
                bundleEmployee.putSerializable("clickedEmployee", (Serializable) clickedEmployee);
                bundleEmployee.putSerializable(("hisId"),(Serializable) clickedEmployee.geteId());

                AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(
                        getActivity());
                alertDialog2.setTitle("Select your choice:");


                alertDialog2.setMessage("Hi there! Please select any one of the following: ");

                alertDialog2.setPositiveButton("DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                db.deleteEmployee(clickedEmployee.geteId());


                                employeeList=db.getAllEmployees();
                                tvTotalEmployeesCount.setText("Total Employees Count: "+Integer.toString(employeeList.size()));
                                eAdapter = new EmployeeAdapter(getActivity(),employeeList);
                                employeeListView.setAdapter(eAdapter);

                                PrefManager.setLastDisplayTime(getTimeStamp(System.currentTimeMillis()));
                                Toast.makeText(getActivity(), "This employee has been deleted successfully ", Toast.LENGTH_LONG).show();

                            }
                        });
                alertDialog2.setNeutralButton("CANCEL",
                        new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();

                            }
                        });

                alertDialog2.setNegativeButton("UPDATE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                OneFragment one = new OneFragment();
                                one.setArguments(bundleEmployee);
                                getFragmentManager().beginTransaction().replace(R.id.frame_container, one).addToBackStack(TAG).commit();

                            }
                        });
                alertDialog2.show();


            }
        });



        return viewFragmentTwo;
    }

    
    public void SetListByLastSorting(String lastsorting, ArrayList<Employee> employeelist)
    {

        if(lastsorting.equals(ID_SORT_ASCENDING))
        {
            Collections.sort(employeelist, IdComparator);
        }
        else if(lastsorting.equals(ID_SORT_DESCENDING))
        {
            Collections.sort(employeelist, Collections.reverseOrder(IdComparator));
        }
        else  if(lastsorting.equals(NAME_SORT_ASCENDING))
        {
            Collections.sort(employeelist,NameComparator);
        }
        else if(lastsorting.equals(NAME_SORT_DESCENDING))
        {
            Collections.sort(employeelist, Collections.reverseOrder(NameComparator));
        }

    }



    public String getTimeStamp(long timeinMillies) {
        String date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        date = formatter.format(new Date(timeinMillies));
        System.out.println("Today is " + date);

        return date;
    }
}