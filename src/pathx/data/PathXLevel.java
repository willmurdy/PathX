/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import mini_game.Viewport;

/**
 *
 * @author willmurdy
 */
public class PathXLevel {
    
    private String currentState;
    
    private String levelName;
    
    private String levelDescription;
    
    private int money;
    
    private BufferedImage startingLoc;
    
    private BufferedImage backgroundImage;
    
    private int spriteID;
    
    //the x and y position on the gameworld
    private int x;
    private int y;
    
    private int renderx;
    private int rendery;
    
    private Viewport vp;
    
    public PathXLevel(int xPos, int yPos, Viewport gameViewport, String state, String name){
        
        vp = gameViewport;
        x = xPos + vp.getViewportMarginLeft() - vp.getViewportX();
        y = yPos + vp.getViewportMarginTop() - vp.getViewportY(); 
        
        renderx = x + vp.getViewportMarginLeft() - vp.getViewportX();
        rendery = y + vp.getViewportMarginTop() - vp.getViewportY();
        
        levelName = name;
        
        currentState = state;
        
    }
    
    public void setSpriteID(int id){
        spriteID = id;
    }
    
    public String getLevelName(){
        return levelName;
    }
    
    public int getSpriteID(){
        return spriteID;
    }
    
    public int getX(){
        return renderx;
    }
    
    public int getY(){
        return rendery;
    }
    
    public String getState(){
        return currentState;
    }
    
    public void updateLocation(int xInc, int yInc){
        
//        x += xInc;
//        y += yInc;
        
        renderx = x + vp.getViewportMarginLeft() - vp.getViewportX();
        rendery = y + vp.getViewportMarginTop() - vp.getViewportY();

    }
    
    public void changeState(String newState){
        currentState = newState;
    }
    
}
