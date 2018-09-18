package com.example.lizlieholleza.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lizlieholleza.inventoryapp.data.InventoryContract.InventoryEntry;
import com.example.lizlieholleza.inventoryapp.data.InventoryContract;

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int EXISTING_INV_LOADER = 0;
    private Uri currentInvUri;
    private EditText nameEditText;
    private EditText priceEditText;
    private EditText quantityEditText;
    private EditText supplierEditText;
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
        supplierEditText = (EditText) findViewById(R.id.edit_inv_supplier);

        nameEditText.setOnTouchListener(touchListener);
        priceEditText.setOnTouchListener(touchListener);
        quantityEditText.setOnTouchListener(touchListener);
        supplierEditText.setOnTouchListener(touchListener);
    }

    private void saveItem() {
        String nameString = nameEditText.getText().toString().trim();
        String priceString = priceEditText.getText().toString().trim();
        String quantityString = quantityEditText.getText().toString().trim();
        String supplierString = supplierEditText.getText().toString().trim();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(currentInvUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveItem();
                finish();
                return true;
            case R.id.action_delete:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if(!invHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }
                DialogInterface.OnClickListener discardButtonClickistener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    }
                };
                 showUnsavedChangesDialog(discardButtonClickistener);
                 return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(!invHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_INV_NAME,
                InventoryEntry.COLUMN_INV_PRICE,
                InventoryEntry.COLUMN_INV_QTY_AVAILABLE,
                InventoryEntry.COLUMN_INV_SUPPLIER,
                InventoryEntry.COLUMN_INV_PICTURE
        };
        return new CursorLoader(this, currentInvUri, projection, null, null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if(cursor == null || cursor.getCount() < 1) {
            return;
        }
        if(cursor.moveToFirst()) {
            int nameColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_NAME);
            int priceColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_PRICE);
            int qtyColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_QTY_AVAILABLE);
            int supColumnIndex = cursor.getColumnIndex(InventoryEntry.COLUMN_INV_SUPPLIER);

            String name = cursor.getString(nameColumnIndex);
            int price = cursor.getInt(priceColumnIndex);
            int quantity = cursor.getInt(qtyColumnIndex);
            String supplier = cursor.getString(supColumnIndex);

            nameEditText.setText(name);
            priceEditText.setText(Integer.toString(price));
            quantityEditText.setText(Integer.toString(quantity));
            supplierEditText.setText(supplier);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        nameEditText.setText("");
        priceEditText.setText("");
        quantityEditText.setText("");
        supplierEditText.setText("");
    }

    private void showUnsavedChangesDialog(DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteInventory();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteInventory() {
        if (currentInvUri != null) {
            int rowsDeleted = getContentResolver().delete(currentInvUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, getString(R.string.editor_delete_failed), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, getString(R.string.edit_delete_success), Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
}
