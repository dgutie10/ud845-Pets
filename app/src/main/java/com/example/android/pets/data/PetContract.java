package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by diegog on 11/29/2016.
 */

public final  class PetContract {

    private PetContract() {}

    public static final String CONTENT_AUTHORITY = "com.example.android.pets";

    public static final class PetEntry implements BaseColumns{

        public static final String TABLE_NAME = "pets";

        //Pet unique Id, stored as an integer
        public static final String _ID = BaseColumns._ID;

        //Pet's name , stored as TEXT
        public static final String COLUMN_PET_NAME = "name";

        //Pet's breed , stored as TEXT
        public static final String COLUMN_PET_BREED = "breed";

        //Pet's gender, stored as INTEGER. 1 -Male, 2 - Female, 0 - Unknown
        public static final String COLUMN_PET_GENDER = "gender";

        //Pet's weight, stored as INTEGER
        public static final String COLUMN_PET_WEIGHT = "weight";



        //Gender types
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;
        public static final int GENDER_UNKNOWN = 0;



    }
}
