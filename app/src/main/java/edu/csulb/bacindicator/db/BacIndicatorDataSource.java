package edu.csulb.bacindicator.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.csulb.bacindicator.activities.MainActivity;
import edu.csulb.bacindicator.models.Category;

/**
 * Created by Johan on 26/04/2015.
 */
public class BacIndicatorDataSource {
    // Database fields
    private SQLiteDatabase database;
    private DBBacIndicatorHelper dbHelper;

    private MainActivity context;

    public BacIndicatorDataSource(MainActivity context) {
        dbHelper = new DBBacIndicatorHelper(context);
        this.context = context;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    /** Strings */

    public long createString(String name, String lang) {
        ContentValues values = new ContentValues();

        int id = getMaxStringId() + 1;
        values.put(DBBacIndicatorHelper.COLUMN_STRING_ID, id);
        values.put(DBBacIndicatorHelper.COLUMN_STRING, name);
        values.put(DBBacIndicatorHelper.COLUMN_STRING_LANG, lang);

        database.insert(DBBacIndicatorHelper.TABLE_STRINGS, null, values);
        return id;
    }

    public long createString(String name, String lang, long id) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_STRING_ID, id);
        values.put(DBBacIndicatorHelper.COLUMN_STRING, name);
        values.put(DBBacIndicatorHelper.COLUMN_STRING_LANG, lang);

        database.insert(DBBacIndicatorHelper.TABLE_STRINGS, null, values);
        return id;
    }

    public int getMaxStringId() {
        Cursor cursor = database.rawQuery("SELECT MAX ("+DBBacIndicatorHelper.COLUMN_STRING_ID+") FROM " + DBBacIndicatorHelper.TABLE_STRINGS, null);
        int max = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                max = cursor.getInt(0);
            }
            cursor.close();
        }
        return max;
    }

    /** Categories */
    public long createCategory(long stringId) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_STR_ID, stringId);

        return database.insert(DBBacIndicatorHelper.TABLE_ALCOHOL_CAT, null, values);
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        return categories;
    }

    /** Alcohols */
    public long createAlcohol(long catId, String brand, float vol) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_CAT_ID, catId);
        values.put(DBBacIndicatorHelper.COLUMN_BRAND, brand);
        values.put(DBBacIndicatorHelper.COLUMN_VOL, vol);

        return database.insert(DBBacIndicatorHelper.TABLE_ALCOHOLS, null, values);
    }

    /** Check if DB is empty and fill it methods*/

    public boolean isEmpty() {
        Cursor cursor = database.rawQuery("SELECT COUNT (*) FROM " + DBBacIndicatorHelper.TABLE_ALCOHOL_CAT, null);

        int count = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count > 0;
    }

    public void fillData() {
        long stringId, catId;

        // Beers
        stringId = createString("Bière", DBBacIndicatorHelper.LANG_FR);
        createString("Beer", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Heineken", 5f);
        createAlcohol(catId, "Leffe", 6.6f);
        createAlcohol(catId, "Corona", 4.6f);
        createAlcohol(catId, "Stella", 4f);
        createAlcohol(catId, "Guiness", 5f);
        createAlcohol(catId, "Tekate", 4.55f);
        createAlcohol(catId, "Adelscott", 6.6f);
        createAlcohol(catId, "Kronenbourg", 4.2f);

        stringId = createString("Vodka", DBBacIndicatorHelper.LANG_FR);
        createString("Vodka", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Ketel One", 40f);
        createAlcohol(catId, "Absolut", 50f);
        createAlcohol(catId, "Smirnoff", 50f);

        stringId = createString("Rhum", DBBacIndicatorHelper.LANG_FR);
        createString("RUM", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Havana Club", 40f);
        createAlcohol(catId, "Bacardi", 37.5f);
        createAlcohol(catId, "Pampero", 40f);
        createAlcohol(catId, "Santa Teresa", 40f);
        createAlcohol(catId, "Zacapa", 40f);

        stringId = createString("Scotch", DBBacIndicatorHelper.LANG_FR);
        createString("Scotch", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Johnny Walker Black", 40f);
        createAlcohol(catId, "Dewar’s", 40f);
        createAlcohol(catId, "Chivas Regal", 40f);
        createAlcohol(catId, "The Macallan", 40f);
        createAlcohol(catId, "Cutty Sark", 40f);
        createAlcohol(catId, "Famous Grouse", 40f);
        createAlcohol(catId, "J&B", 40f);
    }
}
