package com.example.blueprint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Paint;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class Print extends View {

    private Cell[][] cells;
    private  static final int COLS=8,ROWS=8;
    private static final float WALLTHICKNESS=1;
    private float cellSize,hmargin,vmargin;
    private Paint wallPaint;
    private int BackgroundColor=0XFF015B8F;
    private int StrokeColor=0XFFC6DBE8;
    private Random random;

    public Print(Context context,@Nullable AttributeSet attrs){
        super(context,attrs);

        wallPaint=new Paint();
        wallPaint.setColor(StrokeColor);
        wallPaint.setStrokeWidth(WALLTHICKNESS);
        random= new Random();
        createMaze();
    }

    @Override
    protected  void onDraw(Canvas canvas){
        canvas.drawColor(BackgroundColor);

        int width=getWidth();
        int height=getHeight();

        if(width/COLS < height/ROWS)
            cellSize= width/(COLS+1);
        else
            cellSize = height/(ROWS+1);

        hmargin = (width - cellSize*COLS)/2;
        vmargin = (height - cellSize*ROWS)/2;

        canvas.translate(hmargin,vmargin);
        for(int i=0;i<ROWS;i++){
            for(int j=0;j<COLS;j++){
                if(cells[i][j].topWall){
                    canvas.drawLine(i*cellSize ,j*cellSize,(i+1)*cellSize,j*cellSize,wallPaint);
                }
                if(cells[i][j].bottomWall){
                    canvas.drawLine(i*cellSize ,(j+1)*cellSize,(i+1)*cellSize,(j+1)*cellSize,wallPaint);
                }
                if(cells[i][j].leftWall){
                    canvas.drawLine(i*cellSize ,j*cellSize,(i)*cellSize,(j+1)*cellSize,wallPaint);
                }
                if(cells[i][j].rightWall){
                    canvas.drawLine((i+1)*cellSize ,(j)*cellSize,(i+1)*cellSize,(j+1)*cellSize,wallPaint);
                }
            }
        }


    }

    private Cell getNeighbour(Cell cell){
        ArrayList<Cell> neighbours=new ArrayList<>();

//        left Neighbour
        if(cell.col>0)
        if(!cells[cell.row][cell.col -1 ].visited){
            neighbours.add(cells[cell.row][cell.col-1]);
        }

        if(cell.col< COLS-1)
//        Right neighbour
        if(!cells[cell.row][cell.col+1 ].visited){
            neighbours.add(cells[cell.row][cell.col+1]);
        }

//        Top neighbour
        if(cell.row > 0)
        if(!cells[cell.row-1][cell.col].visited){
            neighbours.add(cells[cell.row-1][cell.col]);
        }

//        bottom neighbour
        if(cell.row < ROWS-1)
        if(!cells[cell.row+1][cell.col].visited){
            neighbours.add(cells[cell.row+1][cell.col]);
        }

        if(neighbours.size() > 0){
            int index=random.nextInt(neighbours.size());
            return neighbours.get(index);
        }
        return null;
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
            }
        }
        Stack<Cell> st = new Stack<>();
        Cell current,next;

        current  =  cells[0][0];
        current.visited = true;

        do{
           next = getNeighbour(current);
           if(next != null){
               removeWall(current,next);
               st.push(current);
               current = next;
               current.visited = true;
           }else{
               current = st.pop();
           }
       }while(!st.isEmpty());

    }

    private class Cell{
       boolean topWall=true,bottomWall=true,leftWall=true,rightWall=true,visited=false;
       int col,row;
       public Cell(int col,int row){
           this.col=col;
           this.row=row;
       }
    }


}
