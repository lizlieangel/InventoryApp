package com.example.lizlieholleza.inventoryapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract;

public class MainActivity extends AppCompatActivity {

    private static final int INV_LOADER = 0;

    InventoryCursorAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        ListView invListView = (ListView) findViewById(R.id.list_item);
        View emptyView = findViewById(R.id.empty_view);
        invListView.setEmptyView(emptyView);
        cursorAdapter = new InventoryCursorAdapter(this, null);
        invListView.setAdapter(cursorAdapter);
        invListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, EditorActivity.class);
                Uri currentPetUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, id);
                intent.setData(currentPetUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(INV_LOADER, null, this);
    }

    private void insertItem() {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INV_NAME, "Item 1");
        values.put(InventoryEntry.COLUMN_INV_PRICE, 20);
        values.put(InventoryEntry.COLUMN_INV_QTY_AVAILABLE, 50);
        values.put(InventoryEntry.COLUMN_INV_SUPPLIER, "SUPPLIER 1");
        Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_dummy_data:
                insertItem();
                return true;
            case R.id.delete_all_items:
                deleteAllItems();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
