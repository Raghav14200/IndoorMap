package com.example.blueprint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Print extends View {

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

    public Print(Context context,@Nullable AttributeSet attrs){
        super(context,attrs);

        wallPaint=new Paint();
        wallPaint.setColor(StrokeColor);
        wallPaint.setStrokeWidth(WALLTHICKNESS);
//        random= new Random();
        createMaze();
    }

    @Override
    protected  void onDraw(Canvas canvas){
        canvas.drawColor(BackgroundColor);

        int width=getWidth();
        int height=getHeight();

        cellwidth= (float) (width-100)/(COLS);
        cellheight = (float) (height-100)/(ROWS);

        hmargin =50;
        vmargin =50;

        canvas.translate(hmargin,vmargin);
//        canvas.drawRect();
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
                }
                if(j==colEnd){
                    cells[i][j].rightWall=true;
                }
//  Top boundary
                if(i==rowStart){
                    cells[i][j].topWall=true;
                }
                if(i==rowEnd){
                    cells[i][j].bottomWall=true;
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
