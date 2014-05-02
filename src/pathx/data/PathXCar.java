/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;

/**
 *
 * @author willmurdy
 */
public class PathXCar {
    
    private int x;
    private int y;
    
    private int renderX;
    private int renderY;
    
    private String type;
    
    private boolean inMotion;
    
    private int intersectionId;
    
    public PathXCar(String carType, int id){
        
        type = carType;
        
        intersectionId = id;
        
        inMotion = false;
        
        
        
    }
    
    public void setX(int x){ this.x = x; } 
    public void setY(int y){ this.y = y; }
    
    public int getX(){ return x; }
    public int getY(){ return y; }
    
    public void setRenderX(int x){ this.renderX = x; } 
    public void setRenderY(int y){ this.renderY = y; }
    
    public int getRenderX(){ return renderX; }
    public int getRenderY(){ return renderY; }
    
    public boolean inMotion(){ return inMotion; }
    public void setInMotion(boolean moving){ inMotion = moving; } 
    
    public int getIntersection(){ return intersectionId; }
    public void setIntersectionId(int id){ intersectionId = id; }
    
}
