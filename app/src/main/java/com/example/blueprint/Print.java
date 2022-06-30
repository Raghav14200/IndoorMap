package com.example.blueprint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Print extends View implements View.OnTouchListener {

    private Cell[][] cells;
    private  static final int COLS=642,ROWS=258;
    private static final float WALLTHICKNESS=8;
    private float cellheight,cellwidth,hmargin,vmargin;
    private Paint wallPaint;
    private TextPaint textPaint;
    private int BackgroundColor=0XFFFDFDFD;
    private int StrokeColor=0XFFE1DEDE;
    private int roomColor=0XFFEBEBEB;
    private Random random;
    private ArrayList<ArrayList<Integer>> allRooms=new ArrayList<ArrayList<Integer>>();
    private ArrayList<String> roomNames=new ArrayList<String>();

//    zoom variables
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private float mPosX,pivotPointX;
    private float mPosY,pivotPointY;
    private float mLastTouchX;
    private float mLastTouchY;
    private float startXLimit;
    private float startYLimit;
    private float canvasWidth;
    private float canvasHeight;
    private float endXLimit;
    private float endYLimit;
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId = INVALID_POINTER_ID;
    private Rect map_bounds;

    public Print(Context context,@Nullable AttributeSet attrs){
        super(context,attrs);

        wallPaint=new Paint();
        wallPaint.setColor(StrokeColor);
        wallPaint.setStrokeWidth(WALLTHICKNESS);
//        random= new Random();
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        createMaze();
    }

    @Override
    protected  void onDraw(Canvas canvas){
        canvas.drawColor(BackgroundColor);

        int width=getWidth();
        canvasWidth=canvas.getWidth();
        canvasHeight=canvas.getHeight();
        int height=getHeight();
        cellwidth= (float) (width)/(COLS);
        cellheight = (float) (height)/(ROWS);
        canvas.save();
        canvas.translate(mPosX, mPosY);
        Matrix matrix = new Matrix();
        matrix.postScale(mScaleFactor, mScaleFactor,pivotPointX,pivotPointY);
        canvas.concat(matrix);

//        canvas.drawRect();
        startXLimit=getTranformedValue(0f,0f).get(0);
        startYLimit=getTranformedValue(0f,0f).get(1);

        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(cells[i][j].topWall){
                    canvas.drawLine(j*cellwidth ,i*cellheight,(j+1)*cellwidth,i*cellheight,wallPaint);
                }
                if(cells[i][j].bottomWall){
                    canvas.drawLine(j*cellwidth ,(i+1)*cellheight,(j+1)*cellwidth,(i+1)*cellheight,wallPaint);
                }
                if(cells[i][j].leftWall){
                    canvas.drawLine(j*cellwidth,i*cellheight,(j)*cellwidth,(i+1)*cellheight,wallPaint);
                }
                if(cells[i][j].rightWall){
                    canvas.drawLine((j+1)*cellwidth ,(i)*cellheight,(j+1)*cellwidth,(i+1)*cellheight,wallPaint);
                }
            }
        }

//        for(int j=0;j<allRooms.size();j++){
//            allRooms.get(j).draw(canvas);
//        }
        for(int j=0;j<allRooms.size();j++){
            Paint rectPaint=new Paint();
            rectPaint.setStyle(Paint.Style.FILL);
            rectPaint.setColor(roomColor);
            ArrayList<Integer> curRoom=allRooms.get(j);
            canvas.drawRect(curRoom.get(2)*cellwidth, curRoom.get(0)*cellheight, curRoom.get(3)*cellwidth, (curRoom.get(1)+1)*cellheight, rectPaint);

            rectPaint.setStyle(Paint.Style.STROKE);
            rectPaint.setColor(StrokeColor);
            rectPaint.setStrokeWidth(WALLTHICKNESS);
            canvas.drawRect(curRoom.get(2)*cellwidth, curRoom.get(0)*cellheight, curRoom.get(3)*cellwidth, (curRoom.get(1)+1)*cellheight, rectPaint);
            int xStart=(int)(((curRoom.get(2)+curRoom.get(3))/2)*cellwidth);
            int yStart=(int)(((curRoom.get(0)+curRoom.get(1))/2)*cellheight);
            rectPaint.setColor(Color.GRAY);
            rectPaint.setTextSize(30);
            rectPaint.setTextAlign(Paint.Align.CENTER);
            rectPaint.setStyle(Paint.Style.FILL);
            canvas.drawText(roomNames.get(j),xStart,yStart,rectPaint);
        }
        canvas.restore();
    }

    private ArrayList<Float> getTranformedValue(float x,float y){

//        After scale
        float newX=mScaleFactor*pivotPointX;
        float newY=mScaleFactor*pivotPointY;

//        After translate
        newX=newX-pivotPointX-x*mScaleFactor;
        newY=newY-pivotPointY-y*mScaleFactor;
        ArrayList<Float> arr=new ArrayList<>();
        arr.add(newX);
        arr.add(newY);
        return arr;
    }

    //    Scale class for zoom in and out purpose

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();

            // Don't let the object get too small or too large.
            pivotPointX = detector.getFocusX();
            pivotPointY = detector.getFocusY();
            mScaleFactor = Math.max(1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            return true;
        }
    }

    public boolean onTouchEvent(MotionEvent ev) {
        // Let the ScaleGestureDetector inspect all events.
        mScaleDetector.onTouchEvent(ev);

        final int action = ev.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN: {
                final float x = ev.getX();
                final float y = ev.getY();

                mLastTouchX = x;
                mLastTouchY = y;
                mActivePointerId = ev.getPointerId(0);
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (!mScaleDetector.isInProgress()) {
                    final float dx = x - mLastTouchX;
                    final float dy = y - mLastTouchY;

                    mPosX += dx;
                    mPosY += dy;
                    startXLimit=getTranformedValue(0f,0f).get(0);
                    startYLimit=getTranformedValue(0f,0f).get(1);
                    endXLimit=getTranformedValue(canvasWidth,canvasHeight).get(0);
                    endYLimit=getTranformedValue(canvasWidth,canvasHeight).get(1);
                    System.out.println(endXLimit+" "+endYLimit);
                    mPosX=Math.min(startXLimit,Math.max(mPosX,endXLimit+canvasWidth));
                    mPosY=Math.min(startYLimit,Math.max(mPosY,endYLimit+canvasHeight));
                    invalidate();
                }

                mLastTouchX = x;
                mLastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                mActivePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)>> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = ev.getPointerId(pointerIndex);
                if (pointerId == mActivePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    mLastTouchX = ev.getX(newPointerIndex);
                    mLastTouchY = ev.getY(newPointerIndex);
                    mActivePointerId = ev.getPointerId(newPointerIndex);
                }
                break;
            }
        }

        return true;
    }


//    private Cell getNeighbour(Cell cell){
//        ArrayList<Cell> neighbours=new ArrayList<>();
//
////        left Neighbour
//        if(cell.col>0)
//        if(!cells[cell.row][cell.col -1 ].visited){
//            neighbours.add(cells[cell.row][cell.col-1]);
//        }
//
//        if(cell.col< COLS-1)
////        Right neighbour
//        if(!cells[cell.row][cell.col+1 ].visited){
//            neighbours.add(cells[cell.row][cell.col+1]);
//        }
//
////        Top neighbour
//        if(cell.row > 0)
//        if(!cells[cell.row-1][cell.col].visited){
//            neighbours.add(cells[cell.row-1][cell.col]);
//        }
//
////        bottom neighbour
//        if(cell.row < ROWS-1)
//        if(!cells[cell.row+1][cell.col].visited){
//            neighbours.add(cells[cell.row+1][cell.col]);
//        }
//
//        if(neighbours.size() > 0){
////            int index=random.nextInt(neighbours.size());
//            return neighbours.get(index);
//        }
//        return null;
//    }

    public void createBoundry(int i,int j){
        if(j==0){
            cells[i][j].leftWall=true;
        }
        if(j==COLS-1){
            cells[i][j].rightWall=true;
        }
//  Top boundary
        if(i==0){
            cells[i][j].topWall=true;
        }
        if(i==ROWS-1){
            cells[i][j].bottomWall=true;
        }
    }

//    0 - no door
//    1 - top-left
//    2 - top-right
//    3 - bottom-left
//    4 - bottom-right
//    5 - left-top
//    6 - left-bottom
//    7 - right-top
//    8- right-bottom
    public void createRooms(int rowStart,int rowEnd,int colStart,int colEnd,int door_present){

        for(int i=rowStart;i<=rowEnd;i++){
            for(int j=colStart;j<=colEnd;j++){

                if(j==colStart){
                    cells[i][j].leftWall=true;
                    if(j-1>=0){
                        cells[i][j-1].rightWall=true;
                    }
                }
                if(j==colEnd){
                    cells[i][j].rightWall=true;
                    if(j+1<COLS){
                        cells[i][j+1].leftWall=true;
                    }
                }
//  Top boundary
                if(i==rowStart){
                    cells[i][j].topWall=true;
                    if(i-1>=0){
                        cells[i-1][j].bottomWall=true;
                    }
                }
                if(i==rowEnd){
                    cells[i][j].bottomWall=true;
                    if(i+1<ROWS){
                        cells[i+1][j].topWall=true;
                    }
                }
            }
        }
    }

    public void drawAllRooms(){
        Rooms(0,83,152,419,7,"Intel COE");
        Rooms(0,83,0,151,1,"ISE Lab 1");
        Rooms(98,139,0,151,1,"ISE Lab 2");
        Rooms(168,257,0,151,1,"Apex block AI Lab");
        Rooms(112,167,228,419,1,"");
        Rooms(0,83,496,641,1,"Java Lab");
        Rooms(112,195,496,641,1,"R and D lab");
        Rooms(213,257,228,419,1,"CS Lab 1");
    }

    public  void Rooms(int rowStart,int rowEnd,int colStart,int colEnd,int door_present,String text){
        createRooms(rowStart,rowEnd,colStart,colEnd,door_present);
        ArrayList<Integer> tempRoom=new ArrayList<Integer>();
        tempRoom.add(rowStart);
        tempRoom.add(rowEnd);
        tempRoom.add(colStart);
        tempRoom.add(colEnd);
        allRooms.add(tempRoom);
        roomNames.add(text);
    }

    private void removeWall(Cell current,Cell next){
//        current is below next
        if(current.col==next.col && current.row==next.row+1){
            next.bottomWall=false;
            current.topWall=false;
        }

//        current is above next
        if(current.col==next.col && current.row + 1==next.row){
            next.topWall=false;
            current.bottomWall=false;
        }

//        current is left of next
        if(current.col + 1==next.col && current.row==next.row){
            next.leftWall=false;
            current.rightWall=false;
        }

        //current is right of next
        if(current.col==next.col + 1 && current.row==next.row){
            next.rightWall=false;
            current.leftWall=false;
        }
    }

    private void createMaze(){
        cells=new Cell[ROWS][COLS];

        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                cells[i][j]=new Cell(i,j);
                createBoundry(i,j);
            }
        }

        drawAllRooms();

    }

    private class Cell{
       boolean topWall=false,bottomWall=false,leftWall=false,rightWall=false,visited=false;
       int col,row;
       public Cell(int col,int row){
           this.col=col;
           this.row=row;
       }
    }


}
