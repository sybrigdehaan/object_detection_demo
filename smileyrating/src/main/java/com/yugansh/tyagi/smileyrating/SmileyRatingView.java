package com.yugansh.tyagi.smileyrating;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.animation.PropertyValuesHolder;

import java.util.Random;


public class SmileyRatingView extends View {

    private static final Logger  LOGGER = new Logger();
    double MouthLeft, MouthRight, MouthBottom, MouthTop;
    boolean MouthOpen;

    private int faceColor,faceColorRed, eyesColor, mouthColor, tongueColor, eyeBrowThickniss;
    private RectF faceBgOval, sadOval, neutralOval, slightHappyOval, happyOval, amazingOval, tongueOval;
    private Paint paint;
    int centerOffset, viewWidth, viewHeight,
            whatToDraw = 0, defaultRating, strokeWidth, eyeRadius;

    int currEyeLX, currEyeRX, currEyeY, eyeTopMargin;

    ValueAnimator rightEyeAnimatorX, leftEyeAnimatorX, eyesAnimatorY;
    final long animationDuration = 300;

    public SmileyRatingView(Context context) {
        super(context);

        //Disable Hardware acceleration on device with API < 18
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    public SmileyRatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Initializing objects
        paint = new Paint();
        faceBgOval = new RectF();
        sadOval = new RectF();
        neutralOval = new RectF();
        slightHappyOval = new RectF();
        happyOval = new RectF();
        amazingOval = new RectF();
        tongueOval = new RectF();
        leftEyeAnimatorX = new ValueAnimator();
        rightEyeAnimatorX = new ValueAnimator();
        eyesAnimatorY = new ValueAnimator();

        //Getting attributes value
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.SmileyRatingView);
        try {
            faceColor = typedArray.getColor(R.styleable.SmileyRatingView_face_color,
                    getResources().getColor(R.color.faceColor));
            faceColorRed = getResources().getColor(R.color.faceColorRed);
            eyesColor = typedArray.getColor(R.styleable.SmileyRatingView_eyes_color,
                    getResources().getColor(R.color.eyesColor));
            mouthColor = typedArray.getColor(R.styleable.SmileyRatingView_mouth_color,
                    getResources().getColor(R.color.mouthColor));
            tongueColor = typedArray.getColor(R.styleable.SmileyRatingView_tongue_color,
                    getResources().getColor(R.color.tongueColor));
            defaultRating = typedArray.getInteger(
                    R.styleable.SmileyRatingView_default_rating, 2);
        } finally {
            typedArray.recycle();
        }
        whatToDraw = defaultRating;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        DisplayMetrics screenMetrics = getContext().getResources().getDisplayMetrics();
        viewWidth = getMeasuredWidth() == 0 ? screenMetrics.widthPixels : getMeasuredWidth();// == 0 ? width : getMeasuredWidth();
        viewHeight = getMeasuredHeight() == 0 ? screenMetrics.heightPixels : getMeasuredHeight();// == 0 ? height : getMeasuredHeight();

        strokeWidth = viewHeight / 30 + viewWidth / 30;
        eyeRadius = viewHeight / 25 + viewWidth / 25;
        eyeTopMargin = 10;
        eyeBrowThickniss = 300;
        centerOffset = viewHeight / 3;

        setMeasuredDimension(viewWidth, viewHeight);

        switch (whatToDraw) {
            case 0:
                currEyeLX = (viewWidth / 2) - (viewWidth / 100 * 25);
                currEyeRX = (viewWidth / 2) + (viewWidth / 100 * 25);
                currEyeY = (viewHeight / 100 * 20);
                break;
            case 1:
                currEyeLX = (viewWidth / 2) - (viewWidth / 100 * 20);
                currEyeRX = (viewWidth / 2) + (viewWidth / 100 * 20);
                currEyeY = (viewHeight / 100 * 20);
                break;
            case 2:
                currEyeLX = (viewWidth / 2) - (viewWidth / 100 * 17);
                currEyeRX = (viewWidth / 2) + (viewWidth / 100 * 17);
                currEyeY = (viewHeight / 100 * 25);
                break;
            case 3:
                currEyeLX = (viewWidth / 2) - (viewWidth / 100 * 19);
                currEyeRX = (viewWidth / 2) + (viewWidth / 100 * 19);
                currEyeY = (viewHeight / 100 * 22);
                break;
            case 4:
                currEyeLX = (viewWidth / 2) - (viewWidth / 100 * 23);
                currEyeRX = (viewWidth / 2) + (viewWidth / 100 * 23);
                currEyeY = (viewHeight / 100 * 23);
                break;
        }

        faceBgOval.set(-centerOffset, -viewHeight, viewWidth + centerOffset, viewHeight);

        sadOval.set((viewWidth / 2) - (viewWidth / 100 * 25), viewHeight - (viewHeight / 100 * 45),
                (viewWidth / 2) + (viewWidth / 100 * 25), viewHeight - (viewHeight / 100 * 10));

//        neutralOval.set((viewWidth / 2) - (viewWidth / 100) * 30,
//                viewHeight - (viewHeight / 100) * 40,
//                (viewWidth / 2) + (viewWidth / 100) * 30,
//                viewHeight - (viewHeight / 100) * 40);

        slightHappyOval.set((viewWidth / 2) - (viewWidth / 100 * 30), viewHeight - (viewHeight / 100 * 60),
                (viewWidth / 2) + (viewWidth / 100 * 30), viewHeight - (viewHeight / 100 * 20));

        happyOval.set((viewWidth / 2) - (viewWidth / 100 * 35), viewHeight - (viewHeight / 100 * 90),
                (viewWidth / 2) + (viewWidth / 100 * 35), viewHeight - (viewHeight / 100 * 20));

        amazingOval.set((viewWidth / 2) - (viewWidth / 100 * 20), viewHeight - (viewHeight / 100 * 90),
                (viewWidth / 2) + (viewWidth / 100 * 20), viewHeight - (viewHeight / 100 * 15));

        tongueOval.set((viewWidth / 2) - (viewWidth / 100 * 10), viewHeight - (viewHeight / 100 * 60),
                (viewWidth / 2) + (viewWidth / 100 * 10), viewHeight - (viewHeight / 100 * 20));

        MouthLeft = (viewWidth / 2) - (viewWidth / 100 * 25);
        MouthRight = (viewWidth / 2) + (viewWidth / 100 * 25);
        MouthBottom = viewHeight - (viewHeight / 100 * 20);
        MouthTop = viewHeight - (viewHeight / 100 * 60);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //Draw face BG
        paint.setColor(whatToDraw == 0 ? faceColorRed : faceColor);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawRect(0,0, viewWidth, viewHeight , paint);

        switch (whatToDraw) {
            case 0:
                drawSadFace(canvas);
                break;
            case 1:
                drawNeutralFace(canvas);
                break;
            case 2:
                drawSlightSmileFace(canvas);
                break;
            case 3:
                drawHappyFace(canvas);
                break;
            case 4:
                drawAmazingFace(canvas);
                break;
        }
    }

    private void drawSadFace(Canvas canvas) {
        //Draw Eyes
        paint.setColor(eyesColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.save();

        canvas.rotate(15, ((currEyeLX - 100) + (currEyeLX + 500))/2, (100 + 220) / 2);
        canvas.drawRect(currEyeLX - 100, 100, currEyeLX + 500, 220, paint);

        //restore canvas
        canvas.restore();
        canvas.save();

        canvas.rotate(-15, ((currEyeRX - 500) + (currEyeRX + 100))/2, (100 + 220) / 2);
        canvas.drawRect(currEyeRX - 500, 100, currEyeRX + 100, 220, paint);

        //restore canvas
        canvas.restore();

        canvas.drawCircle(currEyeLX, currEyeY + (eyeRadius+ eyeTopMargin), eyeRadius, paint);
        canvas.drawCircle(currEyeRX, currEyeY + (eyeRadius+ eyeTopMargin), eyeRadius, paint);

        //Draw mouth
        paint.setColor(mouthColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(sadOval, 0, -180, false, paint);

    }

    private void drawNeutralFace(Canvas canvas) {
        //Draw Eyes
        paint.setColor(eyesColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(currEyeLX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);
        canvas.drawCircle(currEyeRX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);

        //Draw mouth
        paint.setColor(mouthColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        //canvas.drawArc(neutralOval, 0, 180, false, paint);
        canvas.drawLine((viewWidth / 2) - (viewWidth / 100) * 30,
                viewHeight - (viewHeight / 100) * 30,
                (viewWidth / 2) + (viewWidth / 100) * 30,
                viewHeight - (viewHeight / 100) * 30,paint);

    }

    private void drawSlightSmileFace(Canvas canvas) {

        //Draw Eyes
        paint.setColor(eyesColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(currEyeLX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);
        canvas.drawCircle(currEyeRX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);

        //Draw mouth
        paint.setColor(mouthColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(slightHappyOval, 0, 180, false, paint);
    }

    private void drawHappyFace(Canvas canvas) {
        //Draw Eyes
        paint.setColor(eyesColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(currEyeLX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);
        canvas.drawCircle(currEyeRX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);

        //Draw mouth
        paint.setColor(mouthColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawArc(happyOval, 0, 180, false, paint);
    }

    public void ChangeMouth(int randomLeftRight, int randomTopBottom) {

        if (amazingOval.left > (MouthLeft + 250)) {
           if(MouthOpen)
               MouthOpen = false;
        }

        else if (amazingOval.left < (MouthLeft - 20))
        {
            if(!MouthOpen)
            {
                MouthOpen = true;
            }
        }

        if(MouthOpen){
            amazingOval.left += randomLeftRight;
            amazingOval.right -= randomLeftRight;

            tongueOval.left += 2 ;
            tongueOval.right -= 2 ;

            amazingOval.top += randomTopBottom;
            amazingOval.bottom-= randomTopBottom;

            tongueOval.top += 2 ;
            tongueOval.bottom -= 2;
        }

        else {
            amazingOval.left -= randomLeftRight;
            amazingOval.right += randomLeftRight;

            tongueOval.left -= 2;
            tongueOval.right += 2;

            amazingOval.top -= randomTopBottom;
            amazingOval.bottom += randomTopBottom;

            tongueOval.top -= 2;
            tongueOval.bottom += 2;
        }
        postInvalidate();
    }

    private void drawAmazingFace(Canvas canvas) {

        //Draw Eyes
        paint.setColor(eyesColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawCircle(currEyeLX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);
        canvas.drawCircle(currEyeRX, currEyeY + (eyeRadius + eyeTopMargin), eyeRadius, paint);

        //Draw mouth
        paint.setColor(mouthColor);
        paint.setStyle(Paint.Style.FILL);

        canvas.drawArc(amazingOval, 0, 180, true, paint);

        //Draw tongue
        paint.setColor(tongueColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawArc(tongueOval, 0, 180, true, paint);
    }

    public void setSmiley(float rating) {
        switch ((int) rating) {
            case 0:
                whatToDraw = 0;
                startEyesAnimation((viewWidth / 2) - (viewWidth / 100 * 25),
                        (viewWidth / 2) + (viewWidth / 100 * 25),
                        (viewHeight / 100 * 20));
                break;
            case 1:
                whatToDraw = 1;
                startEyesAnimation((viewWidth / 2) - (viewWidth / 100 * 20),
                        (viewWidth / 2) + (viewWidth / 100 * 20),
                        (viewHeight / 100 * 20));
                break;
            case 2:
                whatToDraw = 2;
                startEyesAnimation((viewWidth / 2) - (viewWidth / 100 * 17),
                        (viewWidth / 2) + (viewWidth / 100 * 17),
                        (viewHeight / 100 * 25));
                break;
            case 3:
                whatToDraw = 3;
                startEyesAnimation((viewWidth / 2) - (viewWidth / 100 * 19),
                        (viewWidth / 2) + (viewWidth / 100 * 19),
                        (viewHeight / 100 * 22));
                break;
            case 4:
                whatToDraw = 4;
                startEyesAnimation((viewWidth / 2) - (viewWidth / 100 * 23),
                        (viewWidth / 2) + (viewWidth / 100 * 23),
                        (viewHeight / 100 * 23));
                break;
        }
    }

    private void startEyesAnimation(int... newPositions) {
        leftEyeAnimatorX.setIntValues(currEyeLX, newPositions[0]);
        leftEyeAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currEyeLX = (int) animation.getAnimatedValue();
                invalidate();
            }
        });
        rightEyeAnimatorX.setIntValues(currEyeRX, newPositions[1]);
        rightEyeAnimatorX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currEyeRX = (int) animation.getAnimatedValue();
            }
        });
        eyesAnimatorY.setIntValues(currEyeY, newPositions[2]);
        eyesAnimatorY.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currEyeY = (int) animation.getAnimatedValue();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(animationDuration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(rightEyeAnimatorX, leftEyeAnimatorX, eyesAnimatorY);
        animatorSet.start();
    }
}
