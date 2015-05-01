package edu.csulb.bacindicator.games;

import android.app.Activity;
import android.view.View;

import edu.csulb.bacindicator.activities.MainActivity;

/**
 * Created by Johan on 30/04/2015.
 */
public abstract class GameView extends View {
    protected Activity context;

    public GameView(Activity context) {
        super(context);
        this.context = context;
    }

    public abstract void start();

    protected void success() {
        context.setResult(MainActivity.RESULT_OK);
        context.finish();
    }

    protected void failure() {
        context.setResult(MainActivity.RESULT_FAILED);
        context.finish();
    }
}
