package com.example.lizlieholleza.inventoryapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INV_LOADER = 0;
    private Uri currentInvUri;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private boolean invHasChanged = false;

    private View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            invHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        Intent intent = getIntent();
        currentInvUri = intent.getData();
        if(currentInvUri == null) {
            setTitle(getString(R.string.editor_activity_title_new_inv));
            invalidateOptionsMenu();
        } else {
            setTitle(getString(R.string.editor_activity_title_new_inv));
            getLoaderManager().initLoader(EXISTING_INV_LOADER, null, this);
        }

        nameEditText = (EditText) findViewById(R.id.edit_inv_name);
        priceEditText = (EditText) findViewById(R.id.edit_inv_price);
        quantityEditText = (EditText) findViewById(R.id.edit_inv_qty);

        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
    }

    private void saveItem() {
        String nameString = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();

        if(currentInvUri == null && TextUtils.isEmpty(nameString) && TextUtils.isEmpty(priceString) && TextUtils.isEmpty(quantityString)) {
            return;
        }

        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_INV_NAME, nameString);

        int price = 0;
        if(!TextUtils.isEmpty(priceString)) {
            price = Integer.parseInt(priceString);
        }
        values.put(InventoryEntry.COLUMN_INV_PRICE, priceString);

        int quantity = 0;
        if(!TextUtils.isEmpty(quantityString)) {
            quantity = Integer.parseInt(quantityString);
        }
        values.put(InventoryEntry.COLUMN_INV_QTY_AVAILABLE, quantityString);

        if(currentInvUri == null) {
            Uri newUri = getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
            if(newUri == null) {
                Toast.makeText(this,getString(R.string.insert_inv_failed),Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.insert_inv_success), Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(currentInvUri, values, null, null);
            if(rowsAffected == 0){
                Toast.makeText(this, getString(R.string.update_inv_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.update_inv_success), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, @Nullable Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
