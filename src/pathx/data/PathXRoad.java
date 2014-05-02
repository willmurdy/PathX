/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;

/**
 *
 * @author willmurdy
 */
public class PathXRoad {
    
    private int id1;
    private int id1x;
    private int id1y;
    
    private int id2;
    private int id2x;
    private int id2y;
    
    private int speedLimit;
    
    private double length;
    
    private double cost;
    
    private boolean oneWay;
    
    private ArrayList<Integer> x;
    private ArrayList<Integer> y;
    
    public PathXRoad(int firstId, int secondId, int limit, boolean oneWay){
        id1 = firstId;
        id2 = secondId;
        
        speedLimit = limit;
        
        this.oneWay = oneWay;
        
        x = new ArrayList<Integer>();
        y = new ArrayList<Integer>();
    }
    
    public PathXRoad(){
        
    }
    
    public double calculateLength(){
        double tempX = id1x - id2x;
        tempX = tempX * tempX;
        
        double tempY = id1y - id2y;
        tempY = tempY * tempY;
        
        double temp = tempX + tempY;
        length = Math.sqrt(temp);
        
        cost = length / speedLimit;
      
        calculatePositions();
        
        return length;
    }
    
    public void calculatePositions(){
//        int deltaX = id2x - id1x;
//        int deltaY = id2y - id1y;
//        float error = 0;
//        float deltaError;
//        if(deltaX != 0)
//            deltaError = Math.abs(deltaY / deltaX);
//        else 
//            return;
//        int y = id1y;
//        for(int x = id1x; x < id2x; x++){
//            this.x.add(x);
//            this.y.add(y);
//            error = error + deltaError;
//            if(error >= 0.5){
//                y = y + 1;
//                error = error - 1;
//            }
//        }
        
//         function line(x0, y0, x1, y1)
//   dx := abs(x1-x0)
//   dy := abs(y1-y0) 
//   if x0 < x1 then sx := 1 else sx := -1
//   if y0 < y1 then sy := 1 else sy := -1
//   err := dx-dy
// 
//   loop
//     plot(x0,y0)
//     if x0 = x1 and y0 = y1 exit loop
//     e2 := 2*err
//     if e2 > -dy then 
//       err := err - dy
//       x0 := x0 + sx
//     end if
//     if e2 < dx then 
//       err := err + dx
//       y0 := y0 + sy 
//     end if
//   end loop
        if(!x.isEmpty())
            return;
        int deltaX = Math.abs(id2x - id1x);
        int deltaY = Math.abs(id2y - id1y);
        int sx;
        int sy;
        if(id1x < id2x)
            sx = 1;
        else
            sx = -1;
        if(id1y < id2y)
            sy = 1;
        else 
            sy = -1;
        int error = deltaX - deltaY;
        int x = id1x;
        int y = id1y;
        int e2;
        while(x != id2x && y != id2y){
            this.x.add(x);
            this.y.add(y);
            if(x == id2x && y == id2y)
                break;
            e2 = 2 * error;
            if(e2 > -deltaY){
                error = error - deltaY;
                x += sx;
            }
            if(e2 < deltaX){
                error = error + deltaX;
                y += sy;
            }
        }
        this.getCost();
    }
    
    public int getXPosition(int i){
        if(x.isEmpty())
            return 0;
        return x.get(i);
    }
    
    public int getYPosition(int i){
        if(y.isEmpty())
            return 0;
        return y.get(i);
    }
    
    public double getCost(){
        return cost;
    }
    
    public boolean oneWay(){
        return oneWay;
    }
    
    public int getId1(){
        return id1;
    }
   
    public int getId2(){
        return id2;
    }
    
    public void setId1Coords(int x, int y){
        id1x = x;
        id1y = y;
    }
    
    public void setId2Coords(int x, int y){
        id2x = x;
        id2y = y;
    }
    
    public int getId1x(){
        return id1x;
    }
    
    public int getId1y(){ return id1y; }
    
    public int getid2x(){ return id2x; }

    public int getid2y(){ return id2y; }
    
    public int getSpeedLimit(){ return speedLimit; }
    
    public ArrayList<Integer> getXList(){ return (ArrayList<Integer>)x.clone(); }
    public ArrayList<Integer> getYList(){ return (ArrayList<Integer>)y.clone(); }
}
