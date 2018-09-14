package com.example.lizlieholleza.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "inventory.db";
    private static final  int DATABASE_VERSION = 1;

    public InventoryHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORIES_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + "(" +
                InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                InventoryEntry.COLUMN_INV_NAME + " TEXT NOT NULL," +
                InventoryEntry.COLUMN_INV_PRICE + " INTEGER NOT NULL DEFAULT 0," +
                InventoryEntry.COLUMN_INV_QTY_AVAILABLE + " INTEGER NOT NULL," +
                InventoryEntry.COLUMN_INV_SUPPLIER + " TEXT DEFAULT 'COMMON'," +
                InventoryEntry.COLUMN_INV_PICTURE + "TEXT NOT NULL);";\
        db.execSQL(SQL_CREATE_INVENTORIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
