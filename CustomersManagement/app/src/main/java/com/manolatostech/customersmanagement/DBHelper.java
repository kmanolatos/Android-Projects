package com.manolatostech.customersmanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "CustomersDB.db";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create TABLE IF NOT EXISTS customers " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, name text, surname text, phone1 text, phone2 text, city text, address text, email text, debt real, guaranteeCount int, taskCount int)"
        );

        db.execSQL(
                "create TABLE IF NOT EXISTS customersGuarantee " +
                        "(id INTEGER, guaranteeRow int, date text, details text)"
        );


        db.execSQL(
                "create TABLE IF NOT EXISTS customersDeletedGuarantee " +
                        "(id INTEGER, guaranteeRow int, name text, date text, details text)"
        );

        db.execSQL(
                "create TABLE IF NOT EXISTS customersCreditDebts " +
                        "(id INTEGER, creditDebtRow int, name text, surname text, date text, oldDebt real, creditDebt real, currentDebt real, customerId int)"
        );

        db.execSQL(
                "create TABLE IF NOT EXISTS customersTask " +
                        "(id INTEGER, taskRow int, date text, time text, details text)"
        );

        db.execSQL(
                "create TABLE IF NOT EXISTS customersPendingTask " +
                        "(id INTEGER, taskRow int, name text, date text, time text, phone1 text, phone2 text, city text, address text, details text)"
        );

        db.execSQL(
                "create TABLE IF NOT EXISTS personalNotes " +
                        "(id INTEGER PRIMARY KEY AUTOINCREMENT, note text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean insertCustomer(String name, String surname, String phone1, String phone2, String city, String address, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("phone1", phone1);
        contentValues.put("phone2", phone2);
        contentValues.put("city", city);
        contentValues.put("address", address);
        contentValues.put("email", email);
        contentValues.put("debt", 0.00);
        contentValues.put("guaranteeCount", 0);
        contentValues.put("taskCount", 0);
        db.insert("customers", null, contentValues);
        return true;
    }

    public void insertCustomerGuarantee(CustomersGuaranteeModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", model.id);
        contentValues.put("guaranteeRow", model.guaranteeRow);
        contentValues.put("date", model.date);
        contentValues.put("details", model.details);
        db.insert("customersGuarantee", null, contentValues);
    }

    public void insertCustomerDeletedGuarantee(CustomersGuaranteeExtendedModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", model.id);
        contentValues.put("guaranteeRow", model.guaranteeRow);
        contentValues.put("name", model.name);
        contentValues.put("date", model.date);
        contentValues.put("details", model.details);
        db.insert("customersDeletedGuarantee", null, contentValues);
    }

    public void insertCustomerPendingTask(CustomersTasksExtendedModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", model.id);
        contentValues.put("taskRow", model.taskRow);
        contentValues.put("name", model.name);
        contentValues.put("phone1", model.phone1);
        contentValues.put("phone2", model.phone2);
        contentValues.put("city", model.city);
        contentValues.put("address", model.address);
        contentValues.put("date", model.date);
        contentValues.put("time", model.time);
        contentValues.put("details", model.details);
        db.insert("customersPendingTask", null, contentValues);
    }

    public void insertCustomerTask(CustomersTasksModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", model.id);
        contentValues.put("taskRow", model.taskRow);
        contentValues.put("date", model.date);
        contentValues.put("time", model.time);
        contentValues.put("details", model.details);
        db.insert("customersTask", null, contentValues);
    }

    public void insertCustomerCreditDebt(CustomersCreditDebtModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", model.id);
        contentValues.put("creditDebtRow", model.creditDebtRow);
        contentValues.put("name", model.name);
        contentValues.put("surname", model.surname);
        contentValues.put("date", model.date);
        contentValues.put("oldDebt", model.oldDebt);
        contentValues.put("creditDebt", model.creditDebt);
        contentValues.put("currentDebt", model.currentDebt);
        contentValues.put("customerId", model.customerId);
        db.insert("customersCreditDebts", null, contentValues);
    }

    public boolean insertPersonalNote(String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", note);
        db.insert("personalNotes", null, contentValues);
        return true;
    }


    public int deleteCustomer(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customers",
                "id = ? ",
                new String[]{String.valueOf(id)});
    }

    public int deleteCustomerGuarantee(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersGuarantee",
                "id = ? ",
                new String[]{String.valueOf(id)});
    }

    public int deleteCustomerCreditDebt(int id, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (row == -1)
            return db.delete("customersCreditDebts", "customerId = ?", new String[]{Integer.toString(id)});
        else
            return db.delete("customersCreditDebts", "customerId = ? and creditDebtRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public int deletePersonalNotes(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("personalNotes",
                "id = ? ",
                new String[]{String.valueOf(id)});
    }

    public int deleteCustomerGuaranteeRow(int id, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersGuarantee", "id = ? and guaranteeRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public int deleteCustomerDeletedGuaranteeRow(int id, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersDeletedGuarantee", "id = ? and guaranteeRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public int deleteCustomerPendingTaskRow(int id, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersPendingTask", "id = ? and taskRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }


    public int deleteCustomerTaskRow(int id, int row) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersTask", "id = ? and taskRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public int deleteCustomerTask(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("customersTask",
                "id = ? ",
                new String[]{String.valueOf(id)});
    }

    public void updateCustomerGuaranteeRows(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersGuarantee where id = " + id + " order by guaranteeRow", null);
        res.moveToFirst();
        int rowCount = 1;
        while (res.isAfterLast() == false) {
            if (res.getInt(res.getColumnIndex("guaranteeRow")) != rowCount) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("guaranteeRow", rowCount);
                db.update("customersGuarantee", contentValues, "id = " + id + " and guaranteeRow = " + res.getInt(res.getColumnIndex("guaranteeRow")), null);
            }
            rowCount++;
            res.moveToNext();
        }
    }

    public void updateCustomerDeletedGuaranteeRows(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersDeletedGuarantee where id = " + id + " order by guaranteeRow", null);
        res.moveToFirst();
        int rowCount = 1;
        while (res.isAfterLast() == false) {
            if (res.getInt(res.getColumnIndex("guaranteeRow")) != rowCount) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("guaranteeRow", rowCount);
                db.update("customersDeletedGuarantee", contentValues, "id = " + id + " and guaranteeRow = " + res.getInt(res.getColumnIndex("guaranteeRow")), null);
            }
            rowCount++;
            res.moveToNext();
        }
    }

    public void updateCustomerPendingTaskRows(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersPendingTask where id = " + id + " order by taskRow", null);
        res.moveToFirst();
        int rowCount = 1;
        while (res.isAfterLast() == false) {
            if (res.getInt(res.getColumnIndex("taskRow")) != rowCount) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("taskRow", rowCount);
                db.update("customersPendingTask", contentValues, "id = " + id + " and taskRow = " + res.getInt(res.getColumnIndex("taskRow")), null);
            }
            rowCount++;
            res.moveToNext();
        }
    }

    public void updateCustomerTaskRows(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersTask where id = " + id + " order by taskRow", null);
        res.moveToFirst();
        int rowCount = 1;
        while (res.isAfterLast() == false) {
            if (res.getInt(res.getColumnIndex("taskRow")) != rowCount) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("taskRow", rowCount);
                db.update("customersTask", contentValues, "id = " + id + " and taskRow = " + res.getInt(res.getColumnIndex("taskRow")), null);
            }
            rowCount++;
            res.moveToNext();
        }
    }

    public void updateCustomers(CustomersModel model) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", model.name);
        contentValues.put("surname", model.surname);
        contentValues.put("phone1", model.phone1);
        contentValues.put("phone2", model.phone2);
        contentValues.put("city", model.city);
        contentValues.put("address", model.address);
        contentValues.put("email", model.email);
        contentValues.put("debt", model.debt);
        contentValues.put("guaranteeCount", model.guaranteeCount);
        contentValues.put("taskCount", model.taskCount);
        db.update("customers", contentValues, "id = ? ", new String[]{Integer.toString(model.id)});
    }

    public void updateCustomersGuarantee(int id, int row, CustomersGuaranteeModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", model.date);
        contentValues.put("details", model.details);
        db.update("customersGuarantee", contentValues, "id = ? and guaranteeRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public void updateCustomersTask(int id, int row, CustomersTasksModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date", model.date);
        contentValues.put("time", model.time);
        contentValues.put("details", model.details);
        db.update("customersTask", contentValues, "id = ? and taskRow = ?", new String[]{Integer.toString(id), Integer.toString(row)});
    }

    public void updatePersonalNotes(int id, PersonalNotesModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note", model.note);
        db.update("personalNotes", contentValues, "id = ?", new String[]{Integer.toString(id)});
    }

    public ArrayList<CustomersModel> getCustomers() {
        ArrayList<CustomersModel> model = new ArrayList<CustomersModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customers", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersModel temp = new CustomersModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.name = res.getString(res.getColumnIndex("name"));
            temp.surname = res.getString(res.getColumnIndex("surname"));
            temp.phone1 = res.getString(res.getColumnIndex("phone1"));
            temp.phone2 = res.getString(res.getColumnIndex("phone2"));
            temp.city = res.getString(res.getColumnIndex("city"));
            temp.address = res.getString(res.getColumnIndex("address"));
            temp.email = res.getString(res.getColumnIndex("email"));
            temp.debt = res.getDouble(res.getColumnIndex("debt"));
            temp.guaranteeCount = res.getInt(res.getColumnIndex("guaranteeCount"));
            temp.taskCount = res.getInt(res.getColumnIndex("taskCount"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersCreditDebtModel> getCustomersCreditDebt(int id) {
        ArrayList<CustomersCreditDebtModel> model = new ArrayList<CustomersCreditDebtModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersCreditDebts where customerId = " + id, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersCreditDebtModel temp = new CustomersCreditDebtModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.creditDebtRow = res.getInt(res.getColumnIndex("creditDebtRow"));
            temp.name = res.getString(res.getColumnIndex("name"));
            temp.surname = res.getString(res.getColumnIndex("surname"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.oldDebt = res.getDouble(res.getColumnIndex("oldDebt"));
            temp.creditDebt = res.getDouble(res.getColumnIndex("creditDebt"));
            temp.currentDebt = res.getDouble(res.getColumnIndex("currentDebt"));
            temp.customerId = res.getInt(res.getColumnIndex("customerId"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersCreditDebtModel> getAllCustomersCreditDebt() {
        ArrayList<CustomersCreditDebtModel> model = new ArrayList<CustomersCreditDebtModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersCreditDebts", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersCreditDebtModel temp = new CustomersCreditDebtModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.creditDebtRow = res.getInt(res.getColumnIndex("creditDebtRow"));
            temp.name = res.getString(res.getColumnIndex("name"));
            temp.surname = res.getString(res.getColumnIndex("surname"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.oldDebt = res.getDouble(res.getColumnIndex("oldDebt"));
            temp.creditDebt = res.getDouble(res.getColumnIndex("creditDebt"));
            temp.currentDebt = res.getDouble(res.getColumnIndex("currentDebt"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public CustomersModel getCustomerById(int id) {
        CustomersModel model = new CustomersModel();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customers where id = " + id, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            model.id = res.getInt(res.getColumnIndex("id"));
            model.name = res.getString(res.getColumnIndex("name"));
            model.surname = res.getString(res.getColumnIndex("surname"));
            model.phone1 = res.getString(res.getColumnIndex("phone1"));
            model.phone2 = res.getString(res.getColumnIndex("phone2"));
            model.city = res.getString(res.getColumnIndex("city"));
            model.address = res.getString(res.getColumnIndex("address"));
            model.email = res.getString(res.getColumnIndex("email"));
            model.debt = res.getDouble(res.getColumnIndex("debt"));
            model.guaranteeCount = res.getInt(res.getColumnIndex("guaranteeCount"));
            model.taskCount = res.getInt(res.getColumnIndex("taskCount"));
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersGuaranteeModel> getCustomersGuaranteeById(int id) {
        ArrayList<CustomersGuaranteeModel> model = new ArrayList<CustomersGuaranteeModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersGuarantee where id = " + id + " order by guaranteeRow", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersGuaranteeModel temp = new CustomersGuaranteeModel();
            temp.guaranteeRow = res.getInt(res.getColumnIndex("guaranteeRow"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.details = res.getString(res.getColumnIndex("details"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersGuaranteeExtendedModel> getCustomerDeletedGuarantees() {
        ArrayList<CustomersGuaranteeExtendedModel> model = new ArrayList<CustomersGuaranteeExtendedModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersDeletedGuarantee", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersGuaranteeExtendedModel temp = new CustomersGuaranteeExtendedModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.guaranteeRow = res.getInt(res.getColumnIndex("guaranteeRow"));
            temp.name = res.getString(res.getColumnIndex("name"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.details = res.getString(res.getColumnIndex("details"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersTasksExtendedModel> getCustomerPendingTasks() {
        ArrayList<CustomersTasksExtendedModel> model = new ArrayList<CustomersTasksExtendedModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersPendingTask", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersTasksExtendedModel temp = new CustomersTasksExtendedModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.taskRow = res.getInt(res.getColumnIndex("taskRow"));
            temp.name = res.getString(res.getColumnIndex("name"));
            temp.phone1 = res.getString(res.getColumnIndex("phone1"));
            temp.phone2 = res.getString(res.getColumnIndex("phone2"));
            temp.city = res.getString(res.getColumnIndex("city"));
            temp.address = res.getString(res.getColumnIndex("address"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.time = res.getString(res.getColumnIndex("time"));
            temp.details = res.getString(res.getColumnIndex("details"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public ArrayList<CustomersTasksModel> getCustomersTaskById(int id) {
        ArrayList<CustomersTasksModel> model = new ArrayList<CustomersTasksModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersTask where id = " + id + " order by taskRow", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            CustomersTasksModel temp = new CustomersTasksModel();
            temp.taskRow = res.getInt(res.getColumnIndex("taskRow"));
            temp.date = res.getString(res.getColumnIndex("date"));
            temp.time = res.getString(res.getColumnIndex("time"));
            temp.details = res.getString(res.getColumnIndex("details"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public int getMaxCustomerId() {
        int maxId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customers order by id desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            maxId = res.getInt(res.getColumnIndex("id"));
            break;
        }
        return maxId;
    }

    public int getMaxCustomerCreditDebtId() {
        int maxId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersCreditDebts order by id desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            maxId = res.getInt(res.getColumnIndex("id"));
            break;
        }
        return maxId;
    }

    public int getMaxCustomerCreditDebtRow(int id) {
        int maxRow = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersCreditDebts where customerId = " + id + " order by creditDebtRow desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            maxRow = res.getInt(res.getColumnIndex("creditDebtRow"));
            break;
        }
        return maxRow;
    }

    public int getMaxPersonalNoteId() {
        int maxId = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from personalNotes order by id desc", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            maxId = res.getInt(res.getColumnIndex("id"));
            break;
        }
        return maxId;
    }

    public int getCustomerDeletedGuaranteesRows() {
        int rows = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersDeletedGuarantee", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            rows++;
            res.moveToNext();
        }
        return rows;
    }

    public int getCustomerPendingTaskRows() {
        int rows = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersPendingTask", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            rows++;
            res.moveToNext();
        }
        return rows;
    }

    public ArrayList<PersonalNotesModel> getPersonalNotes() {
        ArrayList<PersonalNotesModel> model = new ArrayList<PersonalNotesModel>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from personalNotes", null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            PersonalNotesModel temp = new PersonalNotesModel();
            temp.id = res.getInt(res.getColumnIndex("id"));
            temp.note = res.getString(res.getColumnIndex("note"));
            model.add(temp);
            res.moveToNext();
        }
        return model;
    }

    public int checkIfCustomerPendingTaskExist(int id, int row) {
        int found = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from customersPendingTask where id = " + id + " and taskRow = " + row, null);
        res.moveToFirst();
        while (res.isAfterLast() == false) {
            found = 1;
            break;
        }
        return found;
    }
}