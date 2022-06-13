package com.example.blueprint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private static final float WALLTHICKNESS=1;
    private float cellheight,cellwidth,hmargin,vmargin;
    private Paint wallPaint;
    private int BackgroundColor=0XFF015B8F;
    private int StrokeColor=0XFFC6DBE8;
    private Random random;

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
////        Log.d(" rhg",cellheight+" " + cellwidth);
//        Log.i("height,Width" ,cellwidth*COLS +  " " +  cellheight*ROWS + " " +  height + " " +  width);
//        Log.i("Row,Cols" , ROWS +" " + COLS);

        hmargin =50;
        vmargin =50;

        canvas.translate(hmargin,vmargin);
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
                    if(door_present==7){
                        if(j<10){
                            cells[i][j].leftWall=false;
                        }
                    }
                    if(door_present==8){
                        if(j>colEnd-10){
                            cells[i][j].leftWall=false;
                        }
                    }
                }
                if(j==colEnd){
                    cells[i][j].rightWall=true;
                }else{
                    Log.i("hi",i+"" + j);
                    cells[i][j].rightWall=false;
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

    public  void Rooms(int rowStart,int rowEnd,int colStart,int colEnd,int door_present){
        createRooms(rowStart,rowEnd,colStart,colEnd,door_present);
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

        Rooms(0,83,152,419,7);
        Rooms(0,83,0,151,1);
        Rooms(98,139,0,151,1);
        Rooms(168,257,0,151,1);
        Rooms(112,167,228,419,1);
        Rooms(0,83,496,641,1);
        Rooms(112,195,496,641,1);
        Rooms(213,257,228,419,1);

//
//
//        Rooms(0,84-10,153,420);

//        Stack<Cell> st = new Stack<>();
//        Cell current,next;
//
//        current  =  cells[0][0];
//        current.visited = true;
//
//        do{
//           next = getNeighbour(current);
//           if(next != null){
//               removeWall(current,next);
//               st.push(current);
//               current = next;
//               current.visited = true;
//           }else{
//               current = st.pop();
//           }
//       }while(!st.isEmpty());

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
