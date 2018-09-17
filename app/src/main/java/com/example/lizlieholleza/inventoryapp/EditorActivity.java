package com.example.lizlieholleza.inventoryapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

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
