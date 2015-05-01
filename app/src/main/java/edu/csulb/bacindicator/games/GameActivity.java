package edu.csulb.bacindicator.games;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.activities.MainActivity;
import edu.csulb.bacindicator.games.colors.ColorsGameView;
import edu.csulb.bacindicator.games.pint.PintGameView;

/**
 * Created by Johan on 30/04/2015.
 */
public class GameActivity extends Activity {
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getIntent().getExtras();
        String g = b.getString("game");

        String title = null, message = null;

        switch (g) {
            case "Colors":
                game = new ColorsGameView(this);
                title = getString(R.string.game_color_dialog_title);
                message = getString(R.string.game_color_dialog_text);
                break;
            case "Pint":
                game = new PintGameView(this);
                title = getString(R.string.game_pint_dialog_title);
                message = getString(R.string.game_pint_dialog_text);
                break;
        }

        setContentView(game);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.play, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        game.start();
                    }
                })
                .setNeutralButton(R.string.skip, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                        setResult(MainActivity.RESULT_SKIPPED);
                        finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        setResult(RESULT_CANCELED);
                        finish();
                    }
                })
                .setCancelable(false)
                .create()
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        setResult(RESULT_CANCELED);
        finish();
    }
}
