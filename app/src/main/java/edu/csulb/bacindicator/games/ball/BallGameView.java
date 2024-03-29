package edu.csulb.bacindicator.games.ball;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.concurrent.TimeUnit;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.games.GameView;

/**
 * Created by Jimmy
 */
public class BallGameView extends GameView implements SensorEventListener {

    private static final int BALL_SIZE = 50;
    private static final int ZONE_SIZE = 175;
    private static final int OUT_MAX_TIME_SEC = 3;
    private static final int GAME_MAX_LENGTH_SEC = 20;
    private Display mDisplay;
    private SensorManager mSensorManager;
    private float mHorizontalBound;
    private float mVerticalBound;
    private Paint mCirclePaint;
    private Paint mTextPaint;
    private Particle mBall;
    private long startTime = 0;
    private long outTime = 0;
    private float mSensorX;
    private float mSensorY;
    private long mSensorTimestamp;

    public BallGameView(Activity context) {
        super(context);
    }

    private static long toSeconds(long nanoSeconds) {
        return TimeUnit.NANOSECONDS.toSeconds(nanoSeconds);
    }

    private void initialize() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mDisplay = windowManager.getDefaultDisplay();

        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        Sensor sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (sensor == null) {
            sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

            if (sensor == null) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setIcon(R.mipmap.ic_launcher);
                builder.setMessage(R.string.message_no_sensor);
                builder.setTitle(R.string.app_name);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        failure();
                    }
                });
                builder.create().show();

//            context.setResult(MainActivity.RESULT_SKIPPED);
//            context.finish();
                return;
            }
        }

        mSensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);

        mHorizontalBound = mDisplay.getWidth();
        mVerticalBound = mDisplay.getHeight();

        mCirclePaint = new Paint();
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.RED);

        mBall = new Particle(mHorizontalBound / 2, mVerticalBound / 2);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(80);

        invalidate();
    }

    @Override
    public void start() {
        initialize();
        startTime = System.nanoTime();
    }

    @Override
    protected void success() {
        super.success();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void failure() {
        super.failure();
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (startTime == 0)
            return;

        long updateTime = System.nanoTime();

        mBall.updatePosition(mSensorX, mSensorY, 0, mSensorTimestamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound, BALL_SIZE);

        canvas.drawColor(Color.parseColor("#ecf0f1"));
        mCirclePaint.setColor(Color.parseColor("#2980b9"));
        canvas.drawCircle(mHorizontalBound / 2, mVerticalBound / 2, ZONE_SIZE, mCirclePaint);

        if (mBall.isInTheCircle(mHorizontalBound, mVerticalBound, ZONE_SIZE)) {
            if (toSeconds(updateTime - startTime) > GAME_MAX_LENGTH_SEC) {
                success();
            } else if (outTime != 0) {
                startTime += (outTime - startTime);
                outTime = 0;
            }
            mTextPaint.setColor(Color.BLACK);
            canvas.drawText(toSeconds(updateTime - startTime) + " sec.", mHorizontalBound / 2, 100, mTextPaint);
        } else {
            if (outTime == 0) {
                outTime = updateTime;
            } else if (toSeconds(updateTime - outTime) > OUT_MAX_TIME_SEC) {
                failure();
            }
            mTextPaint.setColor(Color.RED);
            canvas.drawText(toSeconds(updateTime - outTime) + " sec.", mHorizontalBound / 2, 100, mTextPaint);
        }

        mCirclePaint.setColor(Color.parseColor("#c0392b"));
        canvas.drawCircle(mBall.mPosX, mBall.mPosY, BALL_SIZE, mCirclePaint); // draw ball above everything else

        invalidate();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER || event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {
            mSensorTimestamp = System.nanoTime();
            switch (mDisplay.getRotation()) {
                case Surface.ROTATION_0:
                    mSensorX = event.values[0];
                    mSensorY = -event.values[1];
                    break;
                case Surface.ROTATION_90:
                    mSensorX = -event.values[1];
                    mSensorY = -event.values[0];
                    break;
                case Surface.ROTATION_180:
                    mSensorX = -event.values[0];
                    mSensorY = event.values[1];
                    break;
                case Surface.ROTATION_270:
                    mSensorX = event.values[1];
                    mSensorY = event.values[0];
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
