package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.pets.data.PetContract.PetEntry;

/**
 * Created by diegog on 12/6/2016.
 */

public class PetProvider extends ContentProvider  {

    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final int PETS = 100;
    private static final int PET_ID = 101;
    private static final String LOG_TAG = PetProvider.class.getSimpleName();
    private PetDbHelper mDbHelper;

    static {
        URI_MATCHER.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);
        URI_MATCHER.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS+"/#", PET_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = URI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return PetEntry.CONTENT_LIST_TYPE;
            case PET_ID:
                return  PetEntry.CONTENT_LIST_TYPE;
            default:
                throw new IllegalArgumentException("Unknown URI "+ uri + " with match "+ match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final int match = URI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is nor supported for "+uri);
        }
    }

    private Uri insertPet(Uri uri, ContentValues values){

        if (values.getAsString(PetEntry.COLUMN_PET_NAME) == null){
            throw new IllegalArgumentException("Pet name is required.");
        }
        if(values.getAsInteger(PetEntry.COLUMN_PET_GENDER) == null || !PetEntry.isValidGender(values.getAsInteger(PetEntry.COLUMN_PET_GENDER))){
            throw new IllegalArgumentException("Pet gender is required.");
        }
        if(values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT)== null || values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT) < 0){
            throw new IllegalArgumentException("Pet weight need to be valid.");
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(PetEntry.TABLE_NAME,null,values);
        if (id == - 1){
            Log.e(LOG_TAG, "Failed to insert new row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final int match = URI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return updatePet(uri, values,selection, selectionArgs);
            case PET_ID:

                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Update is nor supported in "+ uri);
        }
    }

    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs){
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            if (values.getAsString(PetEntry.COLUMN_PET_NAME) == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        return database.update(PetEntry.TABLE_NAME,values,selection,selectionArgs);
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = URI_MATCHER.match(uri);

        switch (match) {
            case PETS:
                cursor = database.query(PetEntry.TABLE_NAME,projection,null,null,null,null,null);
                break;
            case PET_ID:
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(PetEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        final int match = URI_MATCHER.match(uri);

        switch (match){
            case PETS:
                return database.delete(PetEntry.TABLE_NAME,selection, selectionArgs);

            case PET_ID:

                selection =PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return database.delete(PetEntry.TABLE_NAME, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Cannot delete unknown URI "+ uri);
        }

    }
}
