package edu.csulb.bacindicator.activities;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.adapters.AlcoholCategoriesAdapter;
import edu.csulb.bacindicator.adapters.DrinkListAdapter;
import edu.csulb.bacindicator.db.BacIndicatorDataSource;
import edu.csulb.bacindicator.games.GameActivity;
import edu.csulb.bacindicator.models.BAC;
import edu.csulb.bacindicator.models.Drink;
import edu.csulb.bacindicator.models.Settings;
import edu.csulb.bacindicator.utils.AddDrinkUtil;


public class MainActivity extends AppCompatActivity {
    private BacIndicatorDataSource db;

    private final int TEST_GAME_SCORE = 1;
    public static final int RESULT_FAILED = 424242;
    public static final int RESULT_SKIPPED = 424243;

    private SharedPreferences storage;

    private List<Drink> drinks;

    private DrinkListAdapter adapter;

    private TextView bacView;

    private List<String> games = new ArrayList<>();
    private List<String> gamesToPlay = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            db = new BacIndicatorDataSource(MainActivity.this);
            db.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Create data if the database is empty
        if (db.isEmpty()) {
            db.fillData();
        }

        storage = getSharedPreferences(getPackageName(), 0);

        bacView = (TextView) findViewById(R.id.bac_text);
        ListView list = (ListView) findViewById(R.id.drink_list);

        drinks = db.getAllDrinks();

        adapter = new DrinkListAdapter(this);
        adapter.addAll(drinks);
        list.setAdapter(adapter);
        registerForContextMenu(list);

        // tmp
        Settings.init(this);

        // Add all games names
        games.add("Colors");
        games.add("Pint");
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (db != null) {
            db.close();
        }
    }


    public void onDrinksUpdate() {
        drinks.clear();
        drinks.addAll(db.getAllDrinks());
        adapter.clear();
        adapter.addAll(drinks);
        adapter.notifyDataSetChanged();
        float bac = BAC.calculate(drinks);
        bacView.setText(DecimalFormat.getInstance().format(bac));
        int newColor = BAC.getColor(bac);
        int color = bacView.getCurrentTextColor();

        if (newColor != color) {
            ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), color, newColor);
            colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {
                    bacView.setTextColor((Integer) animator.getAnimatedValue());
                }
            });
            colorAnimation.start();
        }
    }

    private void repeatLastDrink() {
        long alcoholId = storage.getLong("lastDrinkAlcoholId", -1);
        long measureId = storage.getLong("lastDrinkMeasureId", -1);
        int quantity = storage.getInt("lastDrinkQuantity", -1);

        if (alcoholId == -1 || measureId == -1 || quantity == -1) {
            Toast.makeText(this, R.string.error_no_last_drink, Toast.LENGTH_SHORT).show();
            return;
        }

        db.createDrink(alcoholId, measureId, quantity);

        onDrinksUpdate();
    }

    private void initPlayGames() {
        gamesToPlay.clear();
        gamesToPlay.addAll(games);
        Collections.shuffle(gamesToPlay);
    }

    private void playNextGame() {
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("game", gamesToPlay.remove(0));
        startActivityForResult(intent, TEST_GAME_SCORE);
    }

    private static final int CONTEXT_MENU_ACTION_DELETE = 0x1;

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.setHeaderIcon(R.mipmap.ic_launcher);
        menu.setHeaderTitle(R.string.context_menu_title);
        menu.add(Menu.NONE, CONTEXT_MENU_ACTION_DELETE, Menu.NONE, R.string.context_menu_delete);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            case CONTEXT_MENU_ACTION_DELETE:
                db.deleteDrink(drinks.get(info.position));
                onDrinksUpdate();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
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
            AddDrinkUtil.displayAddDialog(this, storage, db);
        } else if (id == R.id.action_repeat) {
            repeatLastDrink();
        } else if (id == R.id.action_game) {
            initPlayGames();
            playNextGame();
        } else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == TEST_GAME_SCORE) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                Toast.makeText(this, "You won !", Toast.LENGTH_SHORT).show();
                if (gamesToPlay.size() > 0) {
                    playNextGame();
                }
            } else if (resultCode == RESULT_CANCELED) {
                // The user choose to skip the game
                Toast.makeText(this, "You cancelled !", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_FAILED) {
                Toast.makeText(this, "You failed !", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_SKIPPED) {
                Toast.makeText(this, "You skipped !", Toast.LENGTH_SHORT).show();
                if (gamesToPlay.size() > 0) {
                    playNextGame();
                } else {
                    // TODO you win !
                }
            }
        }
    }
}
