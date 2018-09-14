package com.example.lizlieholleza.inventoryapp.data;

import android.net.Uri;
import android.provider.BaseColumns;

import java.net.URI;

public class InventoryContract {
    private InventoryContract() {}

    public static final String CONTENT_AUTHORITY = "com.exanple.android.inventoryapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_INVENTORY = "inventory";

    public static final class InventoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_INVENTORY);
        public final static String TABLE_NAME = "inventories";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_INV_NAME = "name";
        public final static String COLUMN_INV_PRICE = "price";
        public final static String COLUMN_INV_QTY_AVAILABLE = "qty_available";
        public final static String COLUMN_INV_SUPPLIER = "supplier";
        public final static String COLUMN_INV_PICTURE = "picture";

    }
}
