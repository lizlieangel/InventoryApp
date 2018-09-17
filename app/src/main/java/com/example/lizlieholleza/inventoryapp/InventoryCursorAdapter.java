package com.example.lizlieholleza.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract;

import org.w3c.dom.Text;

public class InventoryCursorAdapter extends CursorAdapter {

    public InventoryCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in the list_item
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView) findViewById(R.id.image);
        TextView nameTextView = (TextView) findViewById(R.id.name);
        TextView quantityTextView = (TextView) findViewById(R.id.quantity);
        TextView priceTextView = (TextView) findViewById(R.id.price);

        int imageIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_PICTURE);
        int nameIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_NAME);
        int quantityIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_QTY_AVAILABLE);
        int priceIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_PRICE);

        int image = cursor.getInt(imageIndex);
        String name = cursor.getString(nameIndex);
        int quantity = cursor.getInt(quantityIndex);
        int price = cursor.getInt(priceIndex);

        imageView.setImageResource(image);
        nameTextView.setText(name);
        quantityTextView.setText(quantity);
        priceTextView.setText(price);

    }
}
