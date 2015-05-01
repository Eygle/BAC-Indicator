package edu.csulb.bacindicator.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Johan on 26/04/2015.
 */
public class DBBacIndicatorHelper extends SQLiteOpenHelper {
    public final static String DATABASE_NAME = "BacIndicatorDB";
    public final static int DATABASE_VERSION = 1;

    public final static String TABLE_ALCOHOL_CAT = "BacIndicatorAlcoholCategories";
    public final static String TABLE_MEASURE_UNIT = "BacIndicatorMeasureUnit";
    public final static String TABLE_ALCOHOLS = "BacIndicatorAlcohols";
    public final static String TABLE_DRINKS = "BacIndicatorDrinks";
    public final static String TABLE_STRINGS = "BacIndicatorStringS";

    public final static String COLUMN_STR_ID = "string_id";

    public final static String COLUMN_CAT_ID = "category_id";

    public final static String COLUMN_BRAND = "alcohol_brand";
    public final static String COLUMN_VOL = "alcohol_vol";

    public final static String COLUMMN_MEASURE_UNIT_LITTER = "quantity_litter";
    public final static String COLUMMN_MEASURE_UNIT_SYSTEM = "quantity_system";

    public final static String COLUMN_ALCOHOL_ID = "alcohol_id";
    public final static String COLUMN_UNIT_ID = "unit_id";

    public final static String COLUMN_STRING = "string";
    public final static String COLUMN_STRING_ID = "string_id";
    public final static String COLUMN_STRING_LANG = "string_lang";

    public final static String LANG_FR = "fr";
    public final static String LANG_EN = "en";

    public final static String CREATE_TABLE_ALCOHOL_CATEGORIES = String.format(
            "CREATE TABLE IF NOT EXISTS %S (" +
                    " _id integer primary key autoincrement," +
                    " %s long)",
            TABLE_ALCOHOL_CAT, COLUMN_STR_ID);

    public final static String CREATE_TABLE_MEASURE_UNIT = String.format(
            "CREATE TABLE IF NOT EXISTS %S (" +
                    " _id integer primary key autoincrement," +
                    " %s long," +
                    " %s float," +
                    " %s text)",
            TABLE_MEASURE_UNIT, COLUMN_STR_ID, COLUMMN_MEASURE_UNIT_LITTER, COLUMMN_MEASURE_UNIT_SYSTEM);

    public final static String CREATE_TABLE_ALCOHOLS = String.format(
            "CREATE TABLE IF NOT EXISTS %S (" +
                    " _id integer primary key autoincrement," +
                    " %s long," +
                    " %s text," +
                    " %s float)",
            TABLE_ALCOHOLS, COLUMN_CAT_ID, COLUMN_BRAND, COLUMN_VOL);

    public final static String CREATE_TABLE_ALCOHOL_DRINKS = String.format(
            "CREATE TABLE IF NOT EXISTS %S (" +
                    " _id integer primary key autoincrement," +
                    " %s long," +
                    " %s long," +
                    " %s long)",
            TABLE_DRINKS, COLUMN_CAT_ID, COLUMN_ALCOHOL_ID, COLUMN_UNIT_ID);

    public final static String CREATE_TABLE_STRINGS = String.format(
            "CREATE TABLE IF NOT EXISTS %S (" +
                    " _id integer primary key autoincrement," +
                    " %s long," +
                    " %s text," +
                    " %s text)",
            TABLE_STRINGS, COLUMN_STR_ID, COLUMN_STRING, COLUMN_STRING_LANG);

    public DBBacIndicatorHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ALCOHOL_CATEGORIES);
        db.execSQL(CREATE_TABLE_ALCOHOL_DRINKS);
        db.execSQL(CREATE_TABLE_ALCOHOLS);
        db.execSQL(CREATE_TABLE_MEASURE_UNIT);
        db.execSQL(CREATE_TABLE_STRINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALCOHOL_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALCOHOL_DRINKS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_ALCOHOLS);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_MEASURE_UNIT);
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_STRINGS);

        // create new tables
        onCreate(db);
    }
}
