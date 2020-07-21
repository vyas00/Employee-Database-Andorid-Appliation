package com.example.android.fragmentlifecycle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private String TAG = "DatabaseHandler";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "employeesManager.db";
    private static final String TABLE_EMPLOYEES = "employees";
    private static final String KEY_ID = "id";
    private static final String EMPLOYEE_NAME = "name";
    private static final String EMPLOYEE_ID_NO = "id_number";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EMPLOYEES_TABLE = "CREATE TABLE " + TABLE_EMPLOYEES + " (" + EMPLOYEE_ID_NO + " INTEGER," + EMPLOYEE_NAME + " TEXT" + ")";
        db.execSQL(CREATE_EMPLOYEES_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMPLOYEES);


        onCreate(db);
    }


    void addEmployee(Employee employee) {

        Log.d(TAG, "Employee is : " + employee.geteId() + "  " + employee.geteName());

        SQLiteDatabase db = this.getWritableDatabase();

        Log.d(TAG, "Employee is : " + employee.geteId() + "  " + employee.geteName());
        ContentValues values = new ContentValues();
        values.put(EMPLOYEE_ID_NO, employee.geteId());
        values.put(EMPLOYEE_NAME, employee.geteName());

        // Inserting Row
        db.insert(TABLE_EMPLOYEES, null, values);

        db.close();
    }


 public  Employee getEmployee(int his_id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EMPLOYEES, new String[]{EMPLOYEE_ID_NO, EMPLOYEE_NAME}, EMPLOYEE_ID_NO + "=?", new String[]{Integer.toString(his_id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Employee employee = new Employee(Integer.parseInt(cursor.getString(0)), cursor.getString(1));

        return employee;
    }

    public ArrayList<Employee> getAllEmployees() {
        ArrayList<Employee> employeeList = new ArrayList<Employee>();

        String selectQuery = "SELECT  * FROM " + TABLE_EMPLOYEES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst() && cursor!=null) {
            do {
                Employee employee = new Employee();
                employee.seteId(Integer.parseInt(cursor.getString(0)));
                employee.seteName(cursor.getString(1));

                employeeList.add(employee);
            } while (cursor.moveToNext());
        }
cursor.close();
        return employeeList;

    }

    public void updateEmployee(Employee employee, int his_id) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EMPLOYEE_ID_NO, employee.geteId());
        values.put(EMPLOYEE_NAME, employee.geteName());
        db.update(TABLE_EMPLOYEES, values, EMPLOYEE_ID_NO + " = ?", new String[]{Integer.toString(his_id)});
        db.close();
    }

    public void deleteEmployee( int his_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_EMPLOYEES, EMPLOYEE_ID_NO + " = ?", new String[]{Integer.toString(his_id)});
        db.close();
    }


    public void deleteTableEmployee() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_EMPLOYEES);
    }


    public int getEmployeesCount() {
        int count = 0;
        String countQuery = "SELECT  * FROM " + TABLE_EMPLOYEES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if (cursor != null && !cursor.isClosed()) {
            count = cursor.getCount();
            cursor.close();
        }
        return count;
    }

}
