package com.example.liuzhuang.mywuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhuang on 2016/10/6.
 */
public class WuziqiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    private int MAX_LINE=10;
    private Paint mPaint = new Paint();

    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float radioPieceOfLineHight=3*1.0f/4;

    private boolean mIsWhite = true;
    private ArrayList<Point> mWhiteArray = new ArrayList<>();
    private ArrayList<Point> mBlackArray = new ArrayList<>();

    private boolean isGameOver;
    private boolean isWhiteWinner;
    private int MAX_COUNT = 5;


    public WuziqiPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {

        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.mipmap.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize=MeasureSpec.getSize(widthMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize, heightSize);
        if (widthModel==MeasureSpec.UNSPECIFIED){
            width=heightSize;
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        }
        setMeasuredDimension(width,width);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth=w;
        mLineHeight = mPanelWidth * 1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * radioPieceOfLineHight);
        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece , pieceWidth, pieceWidth, false);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isGameOver){
            return false;
        }
       int action= event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            int x= (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x,y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)) {
                return false;
            }
            if (mIsWhite) {
                mWhiteArray.add(p);
            } else {
                mBlackArray.add(p);
            }
            invalidate();
            mIsWhite = !mIsWhite;

        }
        return true;
    }

    private Point getValidPoint(int x, int y) {

        return new Point((int) (x/mLineHeight),(int) (y/mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        drawBoard(canvas);
        drawPiece(canvas);
        checkGameOver();
    }

    private void checkGameOver() {
        boolean whiteWin = checkFiveInLine(mWhiteArray);
        boolean blackWin = checkFiveInLine(mBlackArray);
        if (whiteWin || blackWin) {
            isGameOver=true;
            isWhiteWinner = whiteWin;

            String text=isWhiteWinner?"白棋胜利":"黑棋胜利";
            Toast.makeText(getContext(), text, Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkFiveInLine(List<Point> points) {
        for (Point p : points) {
            int x=p.x;
            int y = p.y;

            boolean win = checkHorizontal(x, y, points);
            if (win) return true;
             win = checkVertical(x, y, points);
            if (win) return true;
             win = checkLeft(x, y, points);
            if (win) return true;
             win = checkRight(x, y, points);
            if (win) return true;
        }



    return false;
    }

    /**
     * 判断x，y位置的棋子是否横向有相邻的五个一致
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkHorizontal(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }

        if (count==MAX_COUNT) return true;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }
        if (count==MAX_COUNT) return true;
        return false;
    }
 /**
     * 判断x，y位置的棋子是否纵向有相邻的五个一致
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkVertical(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x , y-i))) {
                count++;
            } else {
                break;
            }
        }

        if (count==MAX_COUNT) return true;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count==MAX_COUNT) return true;
        return false;
    }/**
     * 判断x，y位置的棋子是否左斜有相邻的五个一致
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkLeft(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x-i , y-i))) {
                count++;
            } else {
                break;
            }
        }

        if (count==MAX_COUNT) return true;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x+i , y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count==MAX_COUNT) return true;
        return false;
    }/**
     * 判断x，y位置的棋子是否右斜有相邻的五个一致
     * @param x
     * @param y
     * @param points
     * @return
     */
    private boolean checkRight(int x, int y, List<Point> points) {
        int count=1;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x+i , y-i))) {
                count++;
            } else {
                break;
            }
        }

        if (count==MAX_COUNT) return true;
        for (int i=1;i<MAX_COUNT;i++) {
            if (points.contains(new Point(x -i, y+i))) {
                count++;
            } else {
                break;
            }
        }
        if (count==MAX_COUNT) return true;
        return false;
    }


    private void drawPiece(Canvas canvas) {
        for (int i=0;i<mWhiteArray.size();i++) {
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x+(1-radioPieceOfLineHight)/2)*mLineHeight,
                    (whitePoint.y+(1-radioPieceOfLineHight)/2)*mLineHeight,null);
        }
        for (int i=0;i<mBlackArray.size();i++) {
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x+(1-radioPieceOfLineHight)/2)*mLineHeight,
                    (blackPoint.y+(1-radioPieceOfLineHight)/2)*mLineHeight,null);
        }
    }

    private void drawBoard(Canvas canvas) {

        int w = mPanelWidth;
        float lineHight = mLineHeight;
        for (int i=0;i<MAX_LINE;i++) {
            int startX = (int) (lineHight / 2);
            int endX = (int) (w - lineHight / 2);

            int y = (int) ((0.5 + i) * lineHight);
            canvas.drawLine(startX,y,endX,y,mPaint);

            canvas.drawLine(y,startX,y,endX,mPaint);
        }
    }


    public void reStart() {
        mBlackArray.clear();
        mWhiteArray.clear();
        isGameOver = false;
        isWhiteWinner = false;
        invalidate();
    }

    private static final String INSTANCE = "instance";
    private static final String INSTANCE_GAME_OVER = "instance_game_over";
    private static final String INSTANCE_WHITE_ARRAY = "instance_white_array";
    private static final String INSTANCE_BLACK_ARRAY = "instance_black_array";

    @Override
    protected Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE,super.onSaveInstanceState());
        bundle.putBoolean(INSTANCE_GAME_OVER, isGameOver);
        bundle.putParcelableArrayList(INSTANCE_WHITE_ARRAY, mWhiteArray);
        bundle.putParcelableArrayList(INSTANCE_BLACK_ARRAY, mBlackArray);

        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            isGameOver = bundle.getBoolean(INSTANCE_GAME_OVER);
            mWhiteArray = bundle.getParcelableArrayList(INSTANCE_WHITE_ARRAY);
            mBlackArray = bundle.getParcelableArrayList(INSTANCE_BLACK_ARRAY);

            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE));
            return;
        }

        super.onRestoreInstanceState(state);
    }
}
