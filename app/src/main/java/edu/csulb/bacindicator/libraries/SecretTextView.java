package edu.csulb.bacindicator.libraries;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by matt on 5/27/14.
 * Github link : https://github.com/matthewrkula/SecretTextView
 * Apache 2 license
 */
public class SecretTextView extends TextView {
    ValueAnimator animator;
    private String mTextString;
    private SpannableString mSpannableString;
    private double[] mAlphas;
    private boolean mIsVisible;
    private boolean mIsTextResetting = false;
    ValueAnimator.AnimatorUpdateListener listener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float percent = (Float)valueAnimator.getAnimatedValue();
            resetSpannableString(mIsVisible ? percent : 2.0f - percent);
        }
    };
    private int mDuration = 2500;

    public SecretTextView(Context context) {
        super(context);
        init();
    }

    public SecretTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        this.mIsVisible = false;
        animator = ValueAnimator.ofFloat(0.0f, 2.0f);
        animator.addUpdateListener(listener);
        animator.setDuration(mDuration);
    }

    public void toggle(){
        if (mIsVisible) {
            hide();
        } else {
            show();
        }
    }

    public void show(){
        mIsVisible = true;
        animator.start();
    }

    public void hide(){
        mIsVisible = false;
        animator.start();
    }

    public boolean getIsVisible() {
        return mIsVisible;
    }

    public void setIsVisible(boolean isVisible){
        mIsVisible = isVisible;
        resetSpannableString(isVisible == true ? 2.0f : 0.0f);
    }

    private void resetSpannableString(double percent){
        mIsTextResetting = true;

        int color = getCurrentTextColor();
        for(int i=0; i < mSpannableString.length(); i++){
            mSpannableString.setSpan(
                    new ForegroundColorSpan(Color.argb(clamp(mAlphas[i] + percent), Color.red(color), Color.green(color), Color.blue(color))), i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        setText(mSpannableString);
        invalidate();

        mIsTextResetting = false;
    }

    private void resetAlphas(int length){
        mAlphas = new double[length];
        for(int i=0; i < mAlphas.length; i++){
            mAlphas[i] = Math.random()-1;
        }
    }

    private void resetIfNeeded(){
        if (!mIsTextResetting){
            mTextString = getText().toString();
            mSpannableString = new SpannableString(this.mTextString);
            resetAlphas(mTextString.length());
            resetSpannableString(mIsVisible ? 2.0f : 0);
        }
    }

    public void setText(String text) {
        super.setText(text);
        resetIfNeeded();
    }

    @Override
    public void setText(CharSequence text, TextView.BufferType type) {
        super.setText(text, type);
        resetIfNeeded();
    }

    private int clamp(double f){
        return (int)(255*Math.min(Math.max(f, 0), 1));
    }

    public void setDuration(int mDuration){
        this.mDuration = mDuration;
        animator.setDuration(mDuration);
    }
}