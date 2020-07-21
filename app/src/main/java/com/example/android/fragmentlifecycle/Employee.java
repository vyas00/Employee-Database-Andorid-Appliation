package com.example.android.fragmentlifecycle;


import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;


public class Employee implements Serializable, Comparable<Employee> {
    private int eId;

    private String eName;

    public Employee(){}
    public Employee (int Id, String Name)
    {
        eId=Id;
        eName=Name;

    }

    @Override
    public String toString() {
        return "Employee{" +
                "eId='" + eId + '\'' +
                ", eName='" + eName + '\'' +
                '}';
    }

    public void seteId(int eId) {
        this.eId = eId;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public int geteId()
    {
        return eId;
    }

    public String geteName()
    {
        return eName;
    }


    public static final Comparator<Employee> IdComparator = new Comparator<Employee>(){

        @Override
        public int compare(Employee e1, Employee e2) {
            return e1.eId - e2.eId;
        }

    };


    public static final Comparator<Employee> NameComparator = new Comparator<Employee>(){

        @Override
        public int compare(Employee e1, Employee e2) {
            return e1.eName.compareTo(e2.eName);
        }

    };


    @Override
    public int compareTo(Employee e) {
        return this.eId - e.eId;
    }

}
