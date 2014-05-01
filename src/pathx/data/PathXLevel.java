/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import mini_game.Viewport;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static pathx.PathX.PathXPropertyType.LEVEL_IMG_PATH;
import static pathx.PathX.PathXPropertyType.LEVEL_SCHEMA;
import static pathx.PathXConstants.NORTH_PANEL_HEIGHT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_LEFT;
import static pathx.PathXConstants.VIEWPORT_MARGIN_TOP;
import properties_manager.PropertiesManager;
import static properties_manager.PropertiesManager.NAME_ATT;
import static properties_manager.PropertiesManager.VALUE_ATT;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

/**
 *
 * @author willmurdy
 */
public class PathXLevel {
    
    private String currentState;
    
    private String levelName;
    
    private String levelDescription;
    
    private int money;
    
    private BufferedImage startingImage;
    
    private BufferedImage destinationImage;
    
    private BufferedImage backgroundImage;
    
    private int spriteID;
    
    //the x and y position on the gameworld
    private int x;
    private int y;
    
    private int renderx;
    private int rendery;
    
    private Viewport vp;
    
    private ArrayList<PathXIntersection> intersections;
    
    private ArrayList<PathXRoad> roads;
    
    private boolean inGame;
    
    private PathXCar player;
    
    int i;

    
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
        
        player = new PathXCar(PathXCarType.PLAYER_TYPE.toString());
        
        intersections = new ArrayList<PathXIntersection>();
        
        roads = new ArrayList<PathXRoad>();
        
    }
    
    public PathXLevel(){
        intersections = new ArrayList<PathXIntersection>();
        
        roads = new ArrayList<PathXRoad>();
        
        inGame = false;
        
        player = new PathXCar(PathXCarType.PLAYER_TYPE.toString());
        
        i = 2;
    }
    
    public void setLocation(int xPos, int yPos){
        x = xPos + 20 - vp.getViewportX();
        y = yPos + 120 - vp.getViewportY(); 
        
        renderx = x + 20 - vp.getViewportX();
        rendery = y + 120 - vp.getViewportY();
        
        //for(int i = 0; i < intersections.size(); i++){
          //  intersections.get(i).setRenderX(x);
        //}
        
        //initPlayerLocation();
    }
    
    
    
    public void updateIntersectionLocations(){
        for(int i = 0; i < intersections.size(); i++){
            intersections.get(i).setRenderX(intersections.get(i).getX() + 150 - vp.getViewportX() - 12);
            intersections.get(i).setRenderY(intersections.get(i).getY() + 150 - vp.getViewportY() - 12);
        }
    }
    
    public int getPlayerX(){
        return player.getX();
    }
    
    public int getPlayerY(){
        return player.getY();
    }
    
    public void addIntersection(int x, int y, boolean open){
        PathXIntersection newInter = new PathXIntersection();
        newInter.setX(x + 180 - vp.getViewportX());
        newInter.setY(y + 20 - vp.getViewportY()); 
        
        newInter.setRenderX(x + 180 - vp.getViewportX());
        newInter.setRenderY(y + 20 - vp.getViewportY());
        
        newInter.setOpen(open);
        
        newInter.setId(intersections.size());
        
        intersections.add(newInter);
        
    }
    
    public void calculatePath(int id1, int id2){
        
    }
        
    public void setStartingImage(BufferedImage img){ startingImage = img; }  
    public BufferedImage getStartingImage(){ return startingImage; }
    public void setDestinationImage(BufferedImage img){ destinationImage = img; }
    public BufferedImage getDestinationImage(){ return destinationImage; }
    public void setIngame(boolean in){ inGame = in; }
    public boolean inGame(){ return inGame; }
    public void setViewport(Viewport view){ vp = view; }  
    public void setState(String state){ currentState = state; }
    public int getBackgroundWidth(){ return backgroundImage.getWidth(); }
    public int getBackgroundHeight(){ return backgroundImage.getHeight(); }
    public Viewport getViewport(){ return vp; }
    public void setSpriteID(int id){ spriteID = id; }
    public void setLevelDescription(String desc){ levelDescription = desc; }
    public int getReward(){ return money; }
    public void setReward(int amount){ money = amount; }
    public String getLevelName(){ return levelName; }
    public void setLevelName(String name){ levelName = name; }
    public String getLevelDescription(){ return levelDescription; }    
    public int getSpriteID(){ return spriteID; }
    public int getX(){ return renderx; }
    public int getY(){ return rendery; }
    public String getState(){ return currentState; }
    public void setX(int x){ this.x = x;}
    public void setY(int y){this.y = y;}
    public int getNumIntersections(){ return intersections.size(); }
    
    public void addIntersection(PathXIntersection newIntersection){
        newIntersection.setId(intersections.size());
        intersections.add(newIntersection);
    }
    
    public ArrayList<PathXIntersection> getIntersections(){
        return (ArrayList<PathXIntersection>)intersections.clone();
    }
    
    public void addRoad(PathXRoad road){
        roads.add(road);
        intersections.get(road.getId1()).addRoad(road);
        if(!road.oneWay())
            intersections.get(road.getId2()).addRoad(road);
        
    }
    
    public void initConnections(){
        for(PathXRoad road : roads){
            if(road.oneWay()){
                intersections.get(road.getId1()).addConnection(road.getId2(), road.getCost());
            } else {
                intersections.get(road.getId1()).addConnection(road.getId2(), road.getCost());
                intersections.get(road.getId2()).addConnection(road.getId1(), road.getCost());
            }
        }
    }
    
    public ArrayList<PathXRoad> getRoads(){
        return (ArrayList<PathXRoad>)roads.clone();
    }
    
    public void setBackgroundImage(BufferedImage img){
        backgroundImage = img;
    }
    
    public BufferedImage getBackgroundImage(){
        return backgroundImage;
    }
    
    public void updateLocation(int xInc, int yInc){
        
        if(!inGame){
            renderx = x + VIEWPORT_MARGIN_LEFT - vp.getViewportX();
            rendery = y + VIEWPORT_MARGIN_TOP + NORTH_PANEL_HEIGHT - vp.getViewportY();
        } else{
            updateIntersectionLocations();
            updateRoadLocations();
            updatePlayerLocations();
        }
        i++;
        
    }
    
    
    
    public void updatePlayerLocations(){
        PathXRoad rd = intersections.get(0).getRoads().get(0);
        player.setX(rd.getXPosition(i) + 15 - vp.getViewportX() - 12);
        player.setY(rd.getYPosition(i) + 15 - vp.getViewportY() - 12);
    }
    
    public void initPlayerLocation(){
        player.setX(intersections.get(0).getRoads().get(0).getXPosition(i) + 165 - vp.getViewportX() - 12);
        player.setY(intersections.get(0).getRoads().get(0).getYPosition(i) + 165 - vp.getViewportY() - 12);
        
        
    }
    
    public PathXIntersection getIntersection(int id){
        return intersections.get(id);
    }
    
    public void changeState(String newState){
        currentState = newState;
    }
    
    public void updateRoadLocations(){
        for(PathXRoad rd : roads){
            PathXIntersection inter = getIntersection(rd.getId1());
            rd.setId1Coords(inter.getRenderX(), inter.getRenderY());
            inter = getIntersection(rd.getId2());
            rd.setId2Coords(inter.getRenderX(), inter.getRenderY());
        }
    }        
    
    
}
