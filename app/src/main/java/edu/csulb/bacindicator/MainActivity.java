package edu.csulb.bacindicator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {

    private SharedPreferences storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        storage = getSharedPreferences(getPackageName(), 0);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_add) {
            displayAddDialog();
        } else if (id == R.id.action_repeat) {
            repeatLastDrink();
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayAddDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                MainActivity.this);
        dialog.setTitle(R.string.dialog_add_title);

        final ArrayAdapter<CharSequence> alcoholAdapter = ArrayAdapter.createFromResource(this,
                R.array.alcohol,
                android.R.layout.select_dialog_item);

        dialog.setNegativeButton(R.string.cancel, null);

        dialog.setAdapter(alcoholAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final String alcohol = alcoholAdapter.getItem(which).toString();
                        AlertDialog.Builder dialogInner = new AlertDialog.Builder(
                                MainActivity.this);
                        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
                        View layout = inflater.inflate(R.layout.dialog_quantity, null);
                        dialogInner.setView(layout);

                        final ArrayAdapter<CharSequence> quantityAdapter = ArrayAdapter.createFromResource(MainActivity.this,
                                R.array.quantity_us,
                                android.R.layout.select_dialog_item);

                        ((TextView)layout.findViewById(R.id.quantity_title)).setText(String.format(getString(R.string.dialog_add_quantity_message), alcohol.toLowerCase()));

                        final NumberPicker value = (NumberPicker)layout.findViewById(R.id.quantity_nbr);
                        value.setMinValue(1);
                        value.setMaxValue(10);

                        final Spinner quantityLabel = (Spinner)layout.findViewById(R.id.quantity_labels);
                        quantityLabel.setAdapter(quantityAdapter);

                        dialogInner.setTitle(R.string.dialog_add_quantity_title);
                        dialogInner.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Drink d = new Drink(alcohol, quantityLabel.getSelectedItem().toString(), value.getValue());

                                // TODO add d in list

                                SharedPreferences.Editor editor = storage.edit();
                                editor.putString("lastDrinkAlcohol", d.getAlcohol());
                                editor.putInt("lastDrinkQuantity", d.getQuantity());
                                editor.putString("lastDrinkMeasure", d.getMeasure());

                                editor.commit();
                                dialog.dismiss();
                            }
                        });
                        dialogInner.setNegativeButton(R.string.cancel, null);
                        dialogInner.show();
                    }
                });
        dialog.show();
    }

    private void repeatLastDrink() {
        String a = storage.getString("lastDrinkAlcohol", null);
        int q = storage.getInt("lastDrinkQuantity", 0);
        String m = storage.getString("lastDrinkMeasure", null);

        if (a == null || q == 0 || m == null) {
            Toast.makeText(this, R.string.error_no_last_drink, Toast.LENGTH_SHORT).show();
            return;
        }

        Drink d = new Drink(a, m, q);
        Toast.makeText(this, a + " " + q + " " + m + "", Toast.LENGTH_SHORT).show();
        // TODO add d in drinks list
    }
}
