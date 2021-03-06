package com.chayedan.wuziqi;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WuziqiPanel extends View {

    private int mPanelWidth;
    private float mLineHeight;
    // 最大行数
    private static int MAX_LINE = 10;

    private Paint mPaint = new Paint();

    //棋子图片
    private Bitmap mWhitePiece;
    private Bitmap mBlackPiece;

    private float ratioPieceOfLineHeight = 3*1.0f/4;

    // 白棋执子
    private boolean mIsWhite = true;
    private List<Point> mWhiteArray = new ArrayList<>();
    private List<Point> mBlackArray = new ArrayList<>();

    public WuziqiPanel(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        setBackgroundColor(0x44ff0000);
        init();
    }

    private void init() {
        mPaint.setColor(0x88000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mWhitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_w2);
        mBlackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.stone_b1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthModel = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightModel = MeasureSpec.getMode(heightMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if (widthModel == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        } else if (heightModel == MeasureSpec.UNSPECIFIED) {
            width = widthSize;
        } else {
            width = Math.min(widthSize, heightSize);
        }
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mPanelWidth = w;
        mLineHeight = mPanelWidth*1.0f / MAX_LINE;

        int pieceWidth = (int) (mLineHeight * ratioPieceOfLineHeight);

        mWhitePiece = Bitmap.createScaledBitmap(mWhitePiece, pieceWidth, pieceWidth, false);
        mBlackPiece = Bitmap.createScaledBitmap(mBlackPiece, pieceWidth, pieceWidth, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_UP) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            Point p = getValidPoint(x,y);

            if (mWhiteArray.contains(p) || mBlackArray.contains(p)){
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
        return new Point((int)(x / mLineHeight),(int)(y / mLineHeight));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBoard(canvas);

        drawPieces(canvas);
    }

    private void drawPieces(Canvas canvas) {
        // 绘制棋子
        for(int i =0 ,n = mWhiteArray.size(); i<n;i++){
            Point whitePoint = mWhiteArray.get(i);
            canvas.drawBitmap(mWhitePiece,
                    (whitePoint.x + (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (whitePoint.y + (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    null);
        }

        for(int i =0 ,n = mBlackArray.size(); i<n;i++){
            Point blackPoint = mBlackArray.get(i);
            canvas.drawBitmap(mBlackPiece,
                    (blackPoint.x + (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    (blackPoint.y + (1-ratioPieceOfLineHeight)/2)*mLineHeight,
                    null);
        }
    }

    private void drawBoard(Canvas canvas) {
       // 绘制棋盘
        int w = mPanelWidth;
        float lineHeight = mLineHeight;

        for (int i = 0; i < MAX_LINE; i++) {
            int startX = (int) (lineHeight / 2);
            int endX = (int)(w-lineHeight/2);

            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX, y, endX, y, mPaint);
            canvas.drawLine(y, startX, y, endX, mPaint);
        }
    }
}
