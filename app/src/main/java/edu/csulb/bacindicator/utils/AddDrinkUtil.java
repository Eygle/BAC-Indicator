package edu.csulb.bacindicator.utils;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.activities.MainActivity;
import edu.csulb.bacindicator.adapters.AlcoholAdapter;
import edu.csulb.bacindicator.adapters.AlcoholCategoriesAdapter;
import edu.csulb.bacindicator.adapters.MeasureAdapter;
import edu.csulb.bacindicator.db.BacIndicatorDataSource;
import edu.csulb.bacindicator.db.DBBacIndicatorHelper;
import edu.csulb.bacindicator.models.Alcohol;
import edu.csulb.bacindicator.models.AlcoholCategory;
import edu.csulb.bacindicator.models.Measure;

/**
 * Created by Johan on 30/04/2015.
 */
public class AddDrinkUtil {
    private static MainActivity context;
    private static SharedPreferences storage;
    private static BacIndicatorDataSource db;

    private static AlcoholCategoriesAdapter alcoholCategoriesAdapter;
    private static AlcoholAdapter alcoholAdapter;
    private static MeasureAdapter measureAdapter;

    private static AlcoholCategory category;
    private static Alcohol alcohol;

    public static void displayAddDialog(final MainActivity context, final SharedPreferences storage, BacIndicatorDataSource db) {
        AddDrinkUtil.context = context;
        AddDrinkUtil.storage = storage;
        AddDrinkUtil.db = db;

        alcoholCategoriesAdapter = new AlcoholCategoriesAdapter(context,
                db.getAllCategories());

        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_title)
                .setAdapter(alcoholCategoriesAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                displayAlcoholList(which);
                            }
                        })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private static void displayAlcoholList(int which) {
        category = alcoholCategoriesAdapter.getItem(which);

        alcoholAdapter = new AlcoholAdapter(context, db.getAllAlcoholForCat(category.id));

        new AlertDialog.Builder(context)
                .setTitle(R.string.dialog_add_title)
                .setAdapter(alcoholAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                displayQuantitySelector(which);
                            }
                        })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private static void displayQuantitySelector(int which) {
        alcohol = alcoholAdapter.getItem(which);

        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_quantity, null);


//        ((TextView)layout.findViewById(R.id.quantity_title)).setText(
//                String.format(
//                        context.getString(R.string.dialog_add_quantity_message), alcohol.name));

        final NumberPicker value = (NumberPicker)layout.findViewById(R.id.quantity_nbr);
        value.setMinValue(1);
        value.setMaxValue(10);

        measureAdapter = new MeasureAdapter(context, db.getAllMeasures(DBBacIndicatorHelper.MEASURE_METRIC));
        final Spinner quantityLabel = (Spinner)layout.findViewById(R.id.quantity_labels);
        quantityLabel.setAdapter(measureAdapter);

        new AlertDialog.Builder(context)
                .setView(layout)
                .setTitle(R.string.dialog_add_quantity_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Measure measure = (Measure)quantityLabel.getSelectedItem();
                        context.addDrink(alcohol.id, measure.id, value.getValue(), true);

                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }
}
