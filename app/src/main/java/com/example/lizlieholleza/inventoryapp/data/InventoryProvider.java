package com.example.lizlieholleza.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;

public class InventoryProvider extends ContentProvider {

    public static final String LOG_TAG = InventoryProvider.class.getSimpleName();

    private InventoryHelper inventoryHelper;
    private static final int INV = 100;
    private static final int INV_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY, INV);
        uriMatcher.addURI(InventoryContract.CONTENT_AUTHORITY, InventoryContract.PATH_INVENTORY + "/#", INV_ID);
    }

    @Override
    public boolean onCreate() {
        // initialize database helper object to gain access to the database
        inventoryHelper = new InventoryHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = inventoryHelper.getReadableDatabase();
        Cursor cursor;
        int match = uriMatcher.match(uri);
        switch (match) {
            case INV:
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case INV_ID:
                selection = InventoryEntry._ID + "=?";
                selectionArgs = new String[] {
                        String.valueOf(ContentUris.parseId(uri))
                };
                cursor = database.query(InventoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknow URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri,ContentValues contentValues) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case INV:
                return insertInventory(uri, contentValues);
                break;
            default:
                throw new IllegalArgumentException("Insertion is not support for " + uri);
        }
    }

    private Uri insertInventory(Uri uri, ContentValues contentValues) {
        String name = contentValues.getAsString(InventoryEntry.COLUMN_INV_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Please insert the name for this item");
        }

        Integer price = contentValues.getAsInteger(InventoryEntry.COLUMN_INV_PRICE);
        if(price != null && price < 0) {
            throw new IllegalArgumentException("Please input a valid price");
        }

        Integer quantity = contentValues.getAsInteger(InventoryEntry.COLUMN_INV_QTY_AVAILABLE);
        if(quantity != null && quantity < 0) {
            throw new IllegalArgumentException("Please input a valid quantity");
        }

        String picture = contentValues.getAsString(InventoryEntry.COLUMN_INV_PICTURE);
        if(picture == null) {
            throw new IllegalArgumentException("Please add a picture to the item");
        }

        SQLiteDatabase database =  inventoryHelper.getWritableDatabase();

        long id = database.insert(InventoryEntry.TABLE_NAME, null, contentValues);

        if(id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
