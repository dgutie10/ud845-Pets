package com.example.android.pets;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by dguti on 12/8/2016.
 */

public class PetCursorAdapter extends CursorAdapter{


    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_view,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView summary = (TextView) view.findViewById(R.id.summary);

        int nameColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumn = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);

        name.setText(cursor.getString(nameColumn));
        summary.setText(cursor.getString(breedColumn));
    }
}
