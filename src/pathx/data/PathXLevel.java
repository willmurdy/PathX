/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import mini_game.Viewport;
import static pathx.PathXConstants.NORTH_PANEL_HEIGHT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_LEFT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_TOP;

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
    
    public PathXLevel(int xPos, int yPos, Viewport gameViewport, String state, String name,
                            String description, int reward){
        
        vp = gameViewport;
        x = xPos + 20 - vp.getViewportX();
        y = yPos + 120 - vp.getViewportY(); 
        
        renderx = x + 20 - vp.getViewportX();
        rendery = y + 120 - vp.getViewportY();
        
        levelName = name;
        
        levelDescription = description;
        
        currentState = state;
        
        money = reward;
        
    }
    
    public void setSpriteID(int id){
        spriteID = id;
    }
    
    public int getReward(){
        return money;
    }
    
    public String getLevelName(){
        return levelName;
    }
    
    public String getLevelDescription(){
        return levelDescription;
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
        
        //renderx += -xInc;
        //rendery += -yInc;
        
        renderx = x + VIEWPORT_MARGIN_LEFT - vp.getViewportX();
        rendery = y + VIEWPORT_MARGIN_TOP + NORTH_PANEL_HEIGHT - vp.getViewportY();

    }
    
    public void changeState(String newState){
        currentState = newState;
    }
    
}
