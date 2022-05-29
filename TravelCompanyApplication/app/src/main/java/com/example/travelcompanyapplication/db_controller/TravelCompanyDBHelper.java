package com.example.travelcompanyapplication.db_controller;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class TravelCompanyDBHelper extends SQLiteOpenHelper {
    private TravelCompanyDBFiller travelCompanyDBFiller;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_HOTEL_ENTRIES = "CREATE TABLE " + TravelCompanyContract.HotelEntry.TABLE_NAME + " (" +
            TravelCompanyContract.HotelEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            TravelCompanyContract.HotelEntry.CITY + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.HotelEntry.HOTEL_NAME + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.HotelEntry.MAP_LINK + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.HotelEntry.COST + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.HotelEntry.RATING + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.HotelEntry.IMAGE_URL + TEXT_TYPE + " )";

    private static final String SQL_CREATE_FLIGHT_ENTRIES = "CREATE TABLE " + TravelCompanyContract.FlightEntry.TABLE_NAME + " (" +
            TravelCompanyContract.FlightEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            TravelCompanyContract.FlightEntry.DEPARTURE_CITY + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.FlightEntry.ARRIVAL_CITY + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.FlightEntry.DEPARTURE_DATE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.FlightEntry.ARRIVAL_DATE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.FlightEntry.AIRLINE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.FlightEntry.COST + TEXT_TYPE + " )";

    private static final String SQL_CREATE_GUIDE_ENTRIES = "CREATE TABLE " + TravelCompanyContract.GuideEntry.TABLE_NAME + " (" +
            TravelCompanyContract.GuideEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            TravelCompanyContract.GuideEntry.CITY_TITLE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.GuideEntry.DESCRIPTION + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.GuideEntry.LINK + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ACCOUNT_HOTEL_ENTRIES = "CREATE TABLE " + TravelCompanyContract.AccountHotelEntry.TABLE_NAME + " (" +
            TravelCompanyContract.AccountHotelEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            TravelCompanyContract.AccountHotelEntry.HOTEL_ID + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.AccountHotelEntry.DEPARTURE_DATE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.AccountHotelEntry.ARRIVAL_DATE + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.AccountHotelEntry.ADULTS_NUMBER + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.AccountHotelEntry.KIDS_NUMBER + TEXT_TYPE + " )";

    private static final String SQL_CREATE_ACCOUNT_FLIGHT_ENTRIES = "CREATE TABLE " + TravelCompanyContract.AccountFlightEntry.TABLE_NAME + " (" +
            TravelCompanyContract.AccountFlightEntry._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
            TravelCompanyContract.AccountFlightEntry.FLIGHT_ID + TEXT_TYPE + COMMA_SEP +
            TravelCompanyContract.AccountFlightEntry.TICKETS_NUMBER + TEXT_TYPE + " )";

    private static final String SQL_DELETE_HOTEL_ENTRIES = "DROP TABLE IF EXISTS " + TravelCompanyContract.HotelEntry.TABLE_NAME;
    private static final String SQL_DELETE_FLIGHT_ENTRIES = "DROP TABLE IF EXISTS " + TravelCompanyContract.FlightEntry.TABLE_NAME;
    private static final String SQL_DELETE_GUIDE_ENTRIES = "DROP TABLE IF EXISTS " + TravelCompanyContract.GuideEntry.TABLE_NAME;
    private static final String SQL_DELETE_ACCOUNT_HOTEL_ENTRIES = "DROP TABLE IF EXISTS " + TravelCompanyContract.AccountHotelEntry.TABLE_NAME;
    private static final String SQL_DELETE_ACCOUNT_FLIGHT_ENTRIES = "DROP TABLE IF EXISTS " + TravelCompanyContract.AccountFlightEntry.TABLE_NAME;
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "TravelCompany.db";

    public TravelCompanyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        if (!(dataBaseExists(context))) {
            travelCompanyDBFiller = new TravelCompanyDBFiller(this);
            travelCompanyDBFiller.fillDB();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_HOTEL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_FLIGHT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_GUIDE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_HOTEL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_CREATE_ACCOUNT_FLIGHT_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DELETE_HOTEL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_FLIGHT_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_GUIDE_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCOUNT_HOTEL_ENTRIES);
        sqLiteDatabase.execSQL(SQL_DELETE_ACCOUNT_FLIGHT_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        onUpgrade(sqLiteDatabase, oldVersion, newVersion);
    }

    private Boolean dataBaseExists(Context context) {
        try {
            SQLiteDatabase checkDataBase = SQLiteDatabase.openDatabase(context.getDatabasePath(DATABASE_NAME).getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
            checkDataBase.close();
        } catch (SQLiteException e) {
            return false;
        }
        return true;
    }

    public ArrayList<ArrayList<String>> getHotelRecords(String selection, String[] selectionArgs) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                TravelCompanyContract.HotelEntry._ID,
                TravelCompanyContract.HotelEntry.CITY,
                TravelCompanyContract.HotelEntry.HOTEL_NAME,
                TravelCompanyContract.HotelEntry.MAP_LINK,
                TravelCompanyContract.HotelEntry.COST,
                TravelCompanyContract.HotelEntry.RATING,
                TravelCompanyContract.HotelEntry.IMAGE_URL
        };
        String sortOrder = TravelCompanyContract.HotelEntry.CITY + " ASC";
        Cursor cursor = database.query(
                TravelCompanyContract.HotelEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry._ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.CITY)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.HOTEL_NAME)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.MAP_LINK)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.COST)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.RATING)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.HotelEntry.IMAGE_URL)));
            table.add(row);
            cursor.moveToNext();
        }
        return table;
    }

    public ArrayList<ArrayList<String>> getFlightRecords(String selection, String[] selectionArgs) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                TravelCompanyContract.FlightEntry._ID,
                TravelCompanyContract.FlightEntry.DEPARTURE_CITY,
                TravelCompanyContract.FlightEntry.ARRIVAL_CITY,
                TravelCompanyContract.FlightEntry.DEPARTURE_DATE,
                TravelCompanyContract.FlightEntry.ARRIVAL_DATE,
                TravelCompanyContract.FlightEntry.AIRLINE,
                TravelCompanyContract.FlightEntry.COST
        };
        String sortOrder = TravelCompanyContract.FlightEntry.DEPARTURE_DATE + " ASC";
        Cursor cursor = database.query(
                TravelCompanyContract.FlightEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry._ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.DEPARTURE_CITY)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.ARRIVAL_CITY)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.DEPARTURE_DATE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.ARRIVAL_DATE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.AIRLINE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.FlightEntry.COST)));
            table.add(row);
            cursor.moveToNext();
        }
        return table;
    }

    public ArrayList<ArrayList<String>> getGuideRecords(String selection, String[] selectionArgs) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                TravelCompanyContract.GuideEntry._ID,
                TravelCompanyContract.GuideEntry.CITY_TITLE,
                TravelCompanyContract.GuideEntry.DESCRIPTION,
                TravelCompanyContract.GuideEntry.LINK
        };
        String sortOrder = TravelCompanyContract.GuideEntry.CITY_TITLE + " ASC";
        Cursor cursor = database.query(
                TravelCompanyContract.GuideEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.GuideEntry._ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.GuideEntry.CITY_TITLE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.GuideEntry.DESCRIPTION)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.GuideEntry.LINK)));
            table.add(row);
            cursor.moveToNext();
        }
        return table;
    }

    public ArrayList<ArrayList<String>> getAccountHotelRecords(String selection, String[] selectionArgs) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                TravelCompanyContract.AccountHotelEntry._ID,
                TravelCompanyContract.AccountHotelEntry.DEPARTURE_DATE,
                TravelCompanyContract.AccountHotelEntry.ARRIVAL_DATE,
                TravelCompanyContract.AccountHotelEntry.HOTEL_ID,
                TravelCompanyContract.AccountHotelEntry.ADULTS_NUMBER,
                TravelCompanyContract.AccountHotelEntry.KIDS_NUMBER
        };
        String sortOrder = TravelCompanyContract.AccountHotelEntry.HOTEL_ID + " ASC";
        Cursor cursor = database.query(
                TravelCompanyContract.AccountHotelEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry._ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry.DEPARTURE_DATE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry.ARRIVAL_DATE)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry.HOTEL_ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry.ADULTS_NUMBER)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountHotelEntry.KIDS_NUMBER)));
            table.add(row);
            cursor.moveToNext();
        }
        return table;
    }

    public ArrayList<ArrayList<String>> getAccountFlightRecords(String selection, String[] selectionArgs) {
        SQLiteDatabase database = getReadableDatabase();
        String[] projection = {
                TravelCompanyContract.AccountFlightEntry._ID,
                TravelCompanyContract.AccountFlightEntry.FLIGHT_ID,
                TravelCompanyContract.AccountFlightEntry.TICKETS_NUMBER
        };
        String sortOrder = TravelCompanyContract.AccountFlightEntry.FLIGHT_ID + " ASC";
        Cursor cursor = database.query(
                TravelCompanyContract.AccountFlightEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        cursor.moveToFirst();
        ArrayList<ArrayList<String>> table = new ArrayList<>();
        while (!cursor.isAfterLast()) {
            ArrayList<String> row = new ArrayList<>();
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountFlightEntry._ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountFlightEntry.FLIGHT_ID)));
            row.add(cursor.getString(cursor.getColumnIndexOrThrow(TravelCompanyContract.AccountFlightEntry.TICKETS_NUMBER)));
            table.add(row);
            cursor.moveToNext();
        }
        return table;
    }
}

