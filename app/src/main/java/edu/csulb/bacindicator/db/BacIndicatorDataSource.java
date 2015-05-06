package edu.csulb.bacindicator.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.csulb.bacindicator.activities.MainActivity;
import edu.csulb.bacindicator.models.Alcohol;
import edu.csulb.bacindicator.models.AlcoholCategory;
import edu.csulb.bacindicator.models.Drink;
import edu.csulb.bacindicator.models.Measure;
import edu.csulb.bacindicator.models.Settings;
import edu.csulb.bacindicator.utils.Utils;

/**
 * Created by Johan on 26/04/2015.
 */
public class BacIndicatorDataSource {
    private final static String TAG = BacIndicatorDataSource.class.getSimpleName();

    // Database fields
    private SQLiteDatabase database;
    private DBBacIndicatorHelper dbHelper;

    public BacIndicatorDataSource(MainActivity context) {
        dbHelper = new DBBacIndicatorHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        Log.d(TAG, "Check if DB is empty for filling it");
        if (isEmpty()) {
            fillData();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long createDrink(long alcoholId, long unitId, int quantity) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_ALCOHOL_ID, alcoholId);
        values.put(DBBacIndicatorHelper.COLUMN_UNIT_ID, unitId);
        values.put(DBBacIndicatorHelper.COLUMN_QUANTITY, quantity);
        values.put(DBBacIndicatorHelper.COLUMN_TIMESTAMP, System.currentTimeMillis());

        return database.insert(DBBacIndicatorHelper.TABLE_DRINKS, null, values);
    }

    public void deleteDrink(Drink drink) {
        database.delete(DBBacIndicatorHelper.TABLE_DRINKS, DBBacIndicatorHelper.COLUMN_DRINKS_ID + " = ?", new String[]{String.valueOf(drink.id)});
    }

    public int countDrinks() {
        Cursor cursor = database.rawQuery("SELECT COUNT (*) FROM " + DBBacIndicatorHelper.TABLE_DRINKS, null);

        int count = 0;
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            cursor.close();
        }
        return count;
    }

    public Drink getDrink(long id) {
        Cursor cursor = database.rawQuery("SELECT " +
                DBBacIndicatorHelper.COLUMN_DRINKS_ID + ", " +
                DBBacIndicatorHelper.COLUMN_BRAND + ", " +
                DBBacIndicatorHelper.COLUMN_STRING + ", " +
                DBBacIndicatorHelper.COLUMN_QUANTITY + ", " +
                DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_LITTER + ", " +
                DBBacIndicatorHelper.COLUMN_VOL + ", " +
                DBBacIndicatorHelper.COLUMN_TIMESTAMP +
                " FROM " + DBBacIndicatorHelper.TABLE_DRINKS + " AS tDri" +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_ALCOHOLS + " AS tAlc" +
                " ON tDri." + DBBacIndicatorHelper.COLUMN_ALCOHOL_ID +
                " = tAlc." + DBBacIndicatorHelper.COLUMN_ALCOHOL_ID +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_MEASURE_UNIT + " AS tMea" +
                " ON tDri." + DBBacIndicatorHelper.COLUMN_UNIT_ID +
                " = tMea." + DBBacIndicatorHelper.COLUMN_UNIT_ID +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_STRINGS + " AS tStr" +
                " ON tMea." + DBBacIndicatorHelper.COLUMN_STR_ID +
                " = tStr." + DBBacIndicatorHelper.COLUMN_STR_ID +

                " WHERE tStr." + DBBacIndicatorHelper.COLUMN_STRING_LANG +
                " LIKE ? " +
                " AND " + DBBacIndicatorHelper.COLUMN_DRINKS_ID + " = " + id +
                " ORDER BY " + DBBacIndicatorHelper.COLUMN_TIMESTAMP + " DESC"
                , new String[]{Utils.getLanguage()});

        if (cursor == null || cursor.getCount() == 0)
            return null;
        Drink d = new Drink();

        cursor.moveToFirst();
        d.id = cursor.getLong(0);
        d.alcohol = cursor.getString(1);
        d.measure = cursor.getString(2);
        d.quantity = cursor.getInt(3);
        d.liters = cursor.getFloat(4);
        d.vol = cursor.getFloat(5);
        d.time = cursor.getLong(6);

        cursor.close();

        return d;
    }

    public List<Drink> getAllDrinks() {
        List<Drink> list = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT " +
                DBBacIndicatorHelper.COLUMN_DRINKS_ID + ", " +
                DBBacIndicatorHelper.COLUMN_BRAND + ", " +
                DBBacIndicatorHelper.COLUMN_STRING + ", " +
                DBBacIndicatorHelper.COLUMN_QUANTITY + ", " +
                DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_LITTER + ", " +
                DBBacIndicatorHelper.COLUMN_VOL + ", " +
                DBBacIndicatorHelper.COLUMN_TIMESTAMP +
                " FROM " + DBBacIndicatorHelper.TABLE_DRINKS + " AS tDri" +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_ALCOHOLS + " AS tAlc" +
                " ON tDri." + DBBacIndicatorHelper.COLUMN_ALCOHOL_ID +
                " = tAlc." + DBBacIndicatorHelper.COLUMN_ALCOHOL_ID +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_MEASURE_UNIT + " AS tMea" +
                " ON tDri." + DBBacIndicatorHelper.COLUMN_UNIT_ID +
                " = tMea." + DBBacIndicatorHelper.COLUMN_UNIT_ID +

                " INNER JOIN " + DBBacIndicatorHelper.TABLE_STRINGS + " AS tStr" +
                " ON tMea." + DBBacIndicatorHelper.COLUMN_STR_ID +
                " = tStr." + DBBacIndicatorHelper.COLUMN_STR_ID +

                " WHERE tStr." + DBBacIndicatorHelper.COLUMN_STRING_LANG +
                " LIKE ? ORDER BY " + DBBacIndicatorHelper.COLUMN_TIMESTAMP + " DESC"
                , new String[]{Utils.getLanguage()});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Drink d = new Drink();

                d.id = cursor.getLong(0);
                d.alcohol = cursor.getString(1);
                d.measure = cursor.getString(2);
                d.quantity = cursor.getInt(3);
                d.liters = cursor.getFloat(4);
                d.vol = cursor.getFloat(5);
                d.time = cursor.getLong(6);

                list.add(d);
            }

            cursor.close();
        }
        return list;
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

    public List<AlcoholCategory> getAllCategories() {
        List<AlcoholCategory> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT " +
                DBBacIndicatorHelper.COLUMN_CAT_ID + ", " +
                DBBacIndicatorHelper.COLUMN_STRING +
                " FROM " +
                DBBacIndicatorHelper.TABLE_ALCOHOL_CAT + " AS tCat" +
                " INNER JOIN " +
                DBBacIndicatorHelper.TABLE_STRINGS + " AS tStr" +
                " ON tCat." + DBBacIndicatorHelper.COLUMN_STRING_ID +
                " = tStr." + DBBacIndicatorHelper.COLUMN_STR_ID +
                " WHERE tStr." + DBBacIndicatorHelper.COLUMN_STRING_LANG +
                " LIKE ?"
                , new String[]{Utils.getLanguage()});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                AlcoholCategory cat = new AlcoholCategory();
                cat.id = cursor.getLong(0);
                cat.name = cursor.getString(1);
                res.add(cat);
            }

            cursor.close();
        }
        return res;
    }

    /** Alcohols */
    public long createAlcohol(long catId, String brand, float vol) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_CAT_ID, catId);
        values.put(DBBacIndicatorHelper.COLUMN_BRAND, brand);
        values.put(DBBacIndicatorHelper.COLUMN_VOL, vol);

        return database.insert(DBBacIndicatorHelper.TABLE_ALCOHOLS, null, values);
    }

    public List<Alcohol> getAllAlcoholForCat(long catId) {
        List<Alcohol> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT " +
                DBBacIndicatorHelper.COLUMN_ALCOHOL_ID + ", " +
                DBBacIndicatorHelper.COLUMN_BRAND + ", " +
                DBBacIndicatorHelper.COLUMN_VOL +
                " FROM " +
                DBBacIndicatorHelper.TABLE_ALCOHOLS +
                " WHERE " + DBBacIndicatorHelper.COLUMN_CAT_ID +
                " = ?" , new String[]{String.valueOf(catId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Alcohol alcohol = new Alcohol();
                alcohol.id = cursor.getLong(0);
                alcohol.name = cursor.getString(1);
                alcohol.volume = cursor.getFloat(2);
                res.add(alcohol);
            }

            cursor.close();
        }
        return res;
    }

    /**
     * Measure units
     */

    public long createMeasure(long strId, float litter, String measureSystem) {
        ContentValues values = new ContentValues();

        values.put(DBBacIndicatorHelper.COLUMN_STR_ID, strId);
        values.put(DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_LITTER, litter);
        values.put(DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_SYSTEM, measureSystem);

        return database.insert(DBBacIndicatorHelper.TABLE_MEASURE_UNIT, null, values);
    }

    public List<Measure> getAllMeasures() {
        List<Measure> res = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT " +
                DBBacIndicatorHelper.COLUMN_UNIT_ID + ", " +
                DBBacIndicatorHelper.COLUMN_STRING + ", " +
                DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_LITTER +
                " FROM " +
                DBBacIndicatorHelper.TABLE_MEASURE_UNIT + " AS tMea" +
                " INNER JOIN " +
                DBBacIndicatorHelper.TABLE_STRINGS + " AS tStr" +
                " ON tMea." + DBBacIndicatorHelper.COLUMN_STRING_ID +
                " = tStr." + DBBacIndicatorHelper.COLUMN_STR_ID +
                " WHERE tStr." + DBBacIndicatorHelper.COLUMN_STRING_LANG +
                " LIKE ? AND tMea." + DBBacIndicatorHelper.COLUMMN_MEASURE_UNIT_SYSTEM +
                " LIKE ?"
                , new String[]{Utils.getLanguage(), Settings.getUnit()});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Measure m = new Measure();
                m.id = cursor.getLong(0);
                m.name = cursor.getString(1);
                m.liter = cursor.getFloat(2);
                res.add(m);
            }

            cursor.close();
        }
        return res;
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
        return count == 0;
    }

    public void fillData() {
        Log.d(TAG, "fill DB data");
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

        // Vodka
        stringId = createString("Vodka", DBBacIndicatorHelper.LANG_FR);
        createString("Vodka", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Ketel One", 40f);
        createAlcohol(catId, "Absolut", 50f);
        createAlcohol(catId, "Smirnoff", 50f);

        // Rum
        stringId = createString("Rhum", DBBacIndicatorHelper.LANG_FR);
        createString("Rum", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Havana Club", 40f);
        createAlcohol(catId, "Bacardi", 37.5f);
        createAlcohol(catId, "Pampero", 40f);
        createAlcohol(catId, "Santa Teresa", 40f);
        createAlcohol(catId, "Zacapa", 40f);

        // Scotch
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

        // Gin
        stringId = createString("Gin", DBBacIndicatorHelper.LANG_FR);
        createString("Gin", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Beefeater", 50f);
        createAlcohol(catId, "Tanqueray", 47.3f);
        createAlcohol(catId, "Bombay Sapphirel", 40f);
        createAlcohol(catId, "Hendrick's", 41.4f);
        createAlcohol(catId, "Gordon's", 47.6f);
        createAlcohol(catId, "Plymouth", 41.2f);
        createAlcohol(catId, "New Amsterdam", 40f);

        // Tequila
        stringId = createString("Tequila", DBBacIndicatorHelper.LANG_FR);
        createString("Tequila", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Jose Cuervo", 50f);
        createAlcohol(catId, "Calle 23", 47.3f);
        createAlcohol(catId, "El Jimador", 40f);
        createAlcohol(catId, "Olmeca ", 38f);
        createAlcohol(catId, "Don Julio", 40f);
        createAlcohol(catId, "Tapatio", 40f);
        createAlcohol(catId, "Patron", 40f);

        // Tequila
        stringId = createString("Whisky", DBBacIndicatorHelper.LANG_FR);
        createString("Whisky", DBBacIndicatorHelper.LANG_EN, stringId);
        catId = createCategory(stringId);

        createAlcohol(catId, "Maker's Mark", 45f);
        createAlcohol(catId, "Jack Daniel's", 40f);
        createAlcohol(catId, "Woodford Reserve", 50f);
        createAlcohol(catId, "Maker's Mark", 45.2f);
        createAlcohol(catId, "Rittenhouse", 40f);
        createAlcohol(catId, "Jameson", 40f);
        createAlcohol(catId, "Jim Beam", 44f);
        createAlcohol(catId, "Four Roses", 40f);

        // Metrics units
        stringId = createString("shot", DBBacIndicatorHelper.LANG_FR);
        createString("shot", DBBacIndicatorHelper.LANG_EN, stringId);
        createMeasure(stringId, 0.044f, DBBacIndicatorHelper.MEASURE_METRIC);
        createMeasure(stringId, 0.044f, DBBacIndicatorHelper.MEASURE_IMPERIAL);

        stringId = createString("verre", DBBacIndicatorHelper.LANG_FR);
        createString("glass", DBBacIndicatorHelper.LANG_EN, stringId);
        createMeasure(stringId, 0.12f, DBBacIndicatorHelper.MEASURE_METRIC);
        createMeasure(stringId, 0.12f, DBBacIndicatorHelper.MEASURE_IMPERIAL);

        stringId = createString("demi", DBBacIndicatorHelper.LANG_FR);
        createString("quarter of liter", DBBacIndicatorHelper.LANG_EN, stringId);
        createMeasure(stringId, 0.25f, DBBacIndicatorHelper.MEASURE_METRIC);

        stringId = createString("pinte", DBBacIndicatorHelper.LANG_FR);
        createString("pint", DBBacIndicatorHelper.LANG_EN, stringId);
        createMeasure(stringId, 0.5f, DBBacIndicatorHelper.MEASURE_METRIC);
        createMeasure(stringId, 0.47f, DBBacIndicatorHelper.MEASURE_IMPERIAL);

        stringId = createString("bock", DBBacIndicatorHelper.LANG_FR);
        createString("liter", DBBacIndicatorHelper.LANG_EN, stringId);
        createMeasure(stringId, 1f, DBBacIndicatorHelper.MEASURE_METRIC);
        createMeasure(stringId, 1f, DBBacIndicatorHelper.MEASURE_IMPERIAL);
    }
}
