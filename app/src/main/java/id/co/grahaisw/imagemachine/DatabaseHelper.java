package id.co.grahaisw.imagemachine;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import id.co.grahaisw.imagemachine.Model.Machine;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "image_machine_db";

    private static final String MACH_TABLE = "machine";
    private static final String MACH_ID = "machine_id";
    private static final String MACH_NAME = "machine_name";
    private static final String MACH_TYPE = "machine_type";
    private static final String MACH_CODE = "machine_code";
    private static final String MACH_LAST_MT = "machine_last_mt";

    private static final String MAIM_TABLE = "machine_image";
    private static final String MAIM_ID = "machine_image_id";
    private static final String MAIM_URI = "machine_image_blob";

    private static final String CREATE_MACHINE_TABLE =
            "CREATE TABLE " + MACH_TABLE + "("
                    + MACH_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MACH_NAME + " TEXT,"
                    + MACH_TYPE + " TEXT,"
                    + MACH_CODE + " INTEGER,"
                    + MACH_LAST_MT + " DATE"
                    + ")";

    private static final String CREATE_MACHINE_IMAGE_TABLE =
            "CREATE TABLE " + MAIM_TABLE + "("
                    + MAIM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + MACH_ID + " INTEGER,"
                    + MAIM_URI + " TEXT"
                    + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL( CREATE_MACHINE_TABLE );
        sqLiteDatabase.execSQL( CREATE_MACHINE_IMAGE_TABLE );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MACH_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MAIM_TABLE);
    }

    @SuppressLint("SimpleDateFormat")
    public void insertMachine(String name, String type, String code, String last_mt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MACH_NAME, name);
        values.put(MACH_TYPE, type);
        values.put(MACH_CODE, code);
        values.put(MACH_LAST_MT, last_mt);

        db.insert(MACH_TABLE, null, values);

        db.close();
    }

    public void insertMachineImage(int id, ArrayList<Uri> uriArrayList) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(int x = 0;x < uriArrayList.size();x++) {
            ContentValues values = new ContentValues();
            values.put(MACH_ID, id);
            values.put(MAIM_URI, uriArrayList.get(x).toString());
            db.insert(MAIM_TABLE, null, values);
        }

        db.close();
    }

    public List<Machine> getMachineAll(String sort) {
        List<Machine> machineList = new ArrayList<>();
        String selectQuery;
        if(sort.equals("name")) {
            selectQuery = "SELECT * FROM " + MACH_TABLE + " ORDER BY " + MACH_NAME + " ASC";
        }else{
            selectQuery = "SELECT * FROM " + MACH_TABLE + " ORDER BY " + MACH_TYPE + " ASC";
        }

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MACH_ID));
                String name = cursor.getString(cursor.getColumnIndex(MACH_NAME));
                String type = cursor.getString(cursor.getColumnIndex(MACH_TYPE));
                int code = cursor.getInt(cursor.getColumnIndex(MACH_CODE));
                String last_mt = cursor.getString(cursor.getColumnIndex(MACH_LAST_MT));
                Machine machine = new Machine(id, name, type, code, last_mt);
                machineList.add(machine);
            } while (cursor.moveToNext());
        }

        db.close();

        return machineList;
    }

    public ArrayList<Uri> getMachineImageAll(int machine_id) {
        ArrayList<Uri> uriArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + MAIM_TABLE + " WHERE " + MACH_ID + " == " + machine_id;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String uri = cursor.getString(cursor.getColumnIndex(MAIM_URI));
                uriArrayList.add(Uri.parse(uri));
            } while (cursor.moveToNext());
        }

        db.close();

        return uriArrayList;
    }

    public Machine getMachineById(int machine_id) {
        Machine machine = new Machine();
        String selectQuery = "SELECT * FROM " + MACH_TABLE + " WHERE " + MACH_ID + " == " + machine_id;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MACH_ID));
                String name = cursor.getString(cursor.getColumnIndex(MACH_NAME));
                String type = cursor.getString(cursor.getColumnIndex(MACH_TYPE));
                int code = cursor.getInt(cursor.getColumnIndex(MACH_CODE));
                String last_mt = cursor.getString(cursor.getColumnIndex(MACH_LAST_MT));
                machine = new Machine(id, name, type, code, last_mt);
            } while (cursor.moveToNext());
        }

        db.close();

        return machine;
    }

    public int getMachineIdRecent() {
        int id = 0;
        String selectQuery = "SELECT * FROM " + MACH_TABLE + " ORDER BY " + MACH_ID + " DESC LIMIT 1";

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(MACH_ID));
            } while (cursor.moveToNext());
        }

        db.close();

        return id;
    }

    public int getMachineCount() {
        String countQuery = "SELECT * FROM " + MACH_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int getMachineIdByCode(int machine_code) {
        int id = 0;
        String selectQuery = "SELECT * FROM " + MACH_TABLE + " WHERE " + MACH_CODE + " == " + machine_code;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(MACH_ID));
            } while (cursor.moveToNext());
        }

        db.close();

        return id;
    }

    public int getMachineIdByCodeAndId(int machine_id, int machine_code) {
        int id = 0;
        String selectQuery = "SELECT * FROM " + MACH_TABLE + " WHERE " + MACH_CODE + " == " + machine_code + " AND " + MACH_ID + " != " + machine_id;

        SQLiteDatabase db = this.getWritableDatabase();
        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndex(MACH_ID));
            } while (cursor.moveToNext());
        }

        db.close();

        return id;
    }

    public int getImageMachineCountById(int machine_id) {
        String countQuery = "SELECT * FROM " + MAIM_TABLE + " WHERE " + MACH_ID + " == " + machine_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    @SuppressLint("SimpleDateFormat")
    public void updateMachine(int id, String name, String type, String code, String last_mt) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MACH_NAME, name);
        values.put(MACH_TYPE, type);
        values.put(MACH_CODE, code);
        values.put(MACH_LAST_MT, last_mt);

        db.update(MACH_TABLE, values, MACH_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public void deleteMachine(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ MACH_TABLE + " WHERE " + MACH_ID + " == " + id);
        db.close();
    }

    public void deleteMachineImage(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+ MAIM_TABLE + " WHERE " + MACH_ID + " == " + id);
        db.close();
    }
}
