/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and oneWay the template in the editor.
 */

package pathx.data;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Stack;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Viewport;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static pathx.PathX.PathXPropertyType.LEVEL_IMG_PATH;
import static pathx.PathX.PathXPropertyType.LEVEL_OPTIONS_FILES;
import static pathx.PathX.PathXPropertyType.LEVEL_PATH;
import static pathx.PathX.PathXPropertyType.LEVEL_SCHEMA;
import static pathx.PathXConstants.*;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

/**
 *
 * @author willmurdid2
 */
public class PathXDataModel extends MiniGameDataModel{
    
        // THIS CLASS HAS A REFERERENCE TO THE MINI GAME SO THAT IT
    // CAN NOTIFY IT TO UPDATE THE DISPLAY WHEN THE DATA MODEL CHANGES
    private MiniGame miniGame;

    // THIS STORES THE TILES ON THE GRID DURING THE GAME
//    private ArrayList<SortingHatTile> tilesToSort;

    // THE LEGAL TILES IN ORDER FROM LOW SORT INDEX TO HIGH
//    private ArrayList<SnakeCell> snake;

    // GAME GRID AND TILE DATA
    private int gameTileWidth;
    private int gameTileHeight;
    private int numGameGridColumns;
    private int numGameGridRows;

    // THESE ARE THE TILES STACKED AT THE START OF THE GAME
//    private ArrayList<SortingHatTile> stackTiles;
//    private int stackTilesX;
//    private int stackTilesY;

    // THESE ARE THE TILES THAT ARE MOVING AROUND, AND SO WE HAVE TO UPDATE
//    private ArrayList<SortingHatTile> movingTiles;

    // THIS IS THE TILE THE USER IS DRAGGING
//    private SortingHatTile selectedTile;
//    private int selectedTileIndex;

    // THIS IS THE TEMP TILE
//    private SortingHatTile tempTile;
    
    //Levels
    private ArrayList<PathXLevel> levels;
    
    

    // KEEPS TRACK OF HOW MANY BAD SPELLS WERE CAST
    private int badSpellsCounter;

    // THESE ARE USED FOR TIMING THE GAME
    private GregorianCalendar startTime;
    private GregorianCalendar endTime;

    // LEVEL
    private String currentLevel;
    
    int currentLevelint;

    // THE SORTING ALGORITHM WHICH GENERATES THE PROPER TRANSACTIONS
//    private SortingHatAlgorithm sortingAlgorithm;

    // THE PROPER TRANSACTIONS TO USE FOR COMPARISION AGAINST PLAYER MOVES
//    private ArrayList<SortTransaction> properTransactionOrder;
//    private int transactionCounter;
    
    private Stack prevMoves;
    
    private int balance;
    
    private TreeMap<String, Viewport> viewports;
    
    public static String CURRENT_VIEWPORT_IMG;
    
    public PathXDataModel(MiniGame initMiniGame)
    {
        // KEEP THE GAME FOR LATER
        miniGame = initMiniGame;
        
        // INIT THESE FOR HOLDING MATCHED AND MOVING TILES
//        stackTiles = new ArrayList();
//        movingTiles = new ArrayList();
//        tilesToSort = new ArrayList();

        // NOTHING IS BEING DRAGGED YET
//        selectedTile = null;
//        selectedTileIndex = -1;
//        tempTile = null;
        prevMoves = new Stack();
        levels = new ArrayList<PathXLevel>();
        balance = 1000;  
        viewports = new TreeMap<String, Viewport>();
    }
    
    public boolean notStarted(){
        return true;
    }
    
    public void initLevelSelectionViewport(){
        
        Viewport vp = new Viewport();
        
        vp.setNorthPanelHeight(NORTH_PANEL_HEIGHT);
        vp.setViewportSize(WINDOW_WIDTH -40, WINDOW_HEIGHT - 20 - 100);
        vp.setGameWorldSize(MAP_WIDTH, MAP_HEIGHT);
        vp.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        vp.initViewportMargins(20, 20, 20, 30);
        vp.updateViewportBoundaries();
        vp.scroll(150, 150);
        
        viewports.put(LEVEL_SCREEN_STATE, vp);
        
    }
    
    public void initGameplayViewPort(){
        
        Viewport vp = new Viewport();
        
        vp.setNorthPanelHeight(0);
        vp.setViewportSize(550, 590);
        vp.setGameWorldSize(800, 1600);
        vp.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        vp.initViewportMargins(20, 180, 20, 40);
        vp.updateViewportBoundaries();
        vp.scroll(0, 0);
       
        
        viewports.put(GAMEPLAY_SCREEN_STATE, vp);
        
        
    }
    
    public void setViewportState(String state){
        viewport = viewports.get(state);
    }
    
    public void updateViewport(){
        viewport.setGameWorldSize(levels.get(currentLevelint).getBackgroundWidth(), levels.get(currentLevelint).getBackgroundHeight());
        viewport.updateViewportBoundaries();
    }
    
    public void initLevels(){
                
        XMLUtilities xmlUtil = new XMLUtilities();
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        ArrayList<String> levelFiles = props.getPropertyOptionsList(LEVEL_OPTIONS_FILES);
        boolean aval = true;
        for(String file : levelFiles){
            try {
                
                PathXLevel newLevel = new PathXLevel();
                
                Document doc = xmlUtil.loadXMLDocument(props.getProperty(LEVEL_PATH) + file, props.getProperty(LEVEL_SCHEMA));

                Node node = xmlUtil.getNodeWithName(doc, "level");
                NamedNodeMap temp = node.getAttributes();

                //get the background image
                String imageName = temp.getNamedItem("image").getNodeValue();
                BufferedImage backgroundImage = miniGame.loadImage(props.getProperty(LEVEL_IMG_PATH) + imageName);
                newLevel.setBackgroundImage(backgroundImage);
                
                //get level name
                String levelName = temp.getNamedItem("name").getNodeValue();
                newLevel.setLevelName(levelName);
                
                //initialize the intersections
                int intersectionNodes = xmlUtil.getNumNodesOfElement(doc, "intersection");
                PathXIntersection newInter;
                //Node intersection = xmlUtil.getNodeWithName(doc, "intersection");
                
                newLevel.setViewport(viewport);

                
                //adds the intersections to the Level
                for(int j = 0; j < intersectionNodes; j++)
                {
                    Node intersection = xmlUtil.getNodeInSequence(doc, "intersection", j);
                    temp = intersection.getAttributes();
                    
                    boolean open = Boolean.parseBoolean(temp.getNamedItem("open").getNodeValue());
                    int x = Integer.parseInt(temp.getNamedItem("x").getNodeValue());
                    int y = Integer.parseInt(temp.getNamedItem("y").getNodeValue());
                    
                    //newInter = new PathXIntersection(x, y, open);
                    
                    //newLevel.addIntersection(newInter);
                    newLevel.addIntersection(x, y, open);
                    
//                    NamedNodeMap attributes = n.getAttributes();
//                    for (int i = 0; i < attributes.getLength(); i++)
//                    {
//                        Node att = attributes.getNamedItem(NAME_ATT);
//                        String attName = attributes.getNamedItem(NAME_ATT).getTextContent();
//                        String attValue = attributes.getNamedItem(VALUE_ATT).getTextContent();
//                        //properties.put(attName, attValue);
//                    }
                }
                
                //initialize the roads
                int roadNodes = xmlUtil.getNumNodesOfElement(doc, "road");
                PathXRoad newRoad;
                //Node intersection = xmlUtil.getNodeWithName(doc, "intersection");
                
                //adds the roads to the Level
                for(int j = 0; j < roadNodes; j++)
                {
                    Node road = xmlUtil.getNodeInSequence(doc, "road", j);
                    temp = road.getAttributes();
                    
                    boolean oneWay = Boolean.parseBoolean(temp.getNamedItem("one_way").getNodeValue());
                    int limit = Integer.parseInt(temp.getNamedItem("speed_limit").getNodeValue());
                    int id1 = Integer.parseInt(temp.getNamedItem("int_id1").getNodeValue());
                    int id2 = Integer.parseInt(temp.getNamedItem("int_id2").getNodeValue());
                    
                    newRoad = new PathXRoad(id1, id2, limit, oneWay);
                    
                    newLevel.addRoad(newRoad);

                }
                
                //get the startingLocation image
                node = xmlUtil.getNodeWithName(doc, "start_intersection");
                temp = node.getAttributes();
                imageName = temp.getNamedItem("image").getNodeValue();
                backgroundImage = miniGame.loadImage(props.getProperty(LEVEL_IMG_PATH) + imageName);
                newLevel.setStartingImage(backgroundImage);
                
                //get the destinationLocation image
                node = xmlUtil.getNodeWithName(doc, "destination_intersection");
                temp = node.getAttributes();
                imageName = temp.getNamedItem("image").getNodeValue();
                backgroundImage = miniGame.loadImage(props.getProperty(LEVEL_IMG_PATH) + imageName);
                newLevel.setDestinationImage(backgroundImage);

                //get the reward
                node = xmlUtil.getNodeWithName(doc, "money");
                temp = node.getAttributes();
                int amount = Integer.parseInt(temp.getNamedItem("amount").getNodeValue());
                newLevel.setReward(amount);
                
                
                
                
                if(aval){
                    newLevel.setState(PathXLevelState.AVAILABLE_STATE.toString());
                    newLevel.setLocation(305, 405);
                    aval = false;
                }
                else{
                   newLevel.setState(PathXLevelState.LOCKED_STATE.toString()); 
                   newLevel.setLocation(400, 505);
                }
                
                newLevel.setLevelDescription("TEST");
            
                levels.add(newLevel);

            } catch (InvalidXMLFileFormatException ex) {
                Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
    //        } catch (IOException ex) {
    //            Logger.getLogger(PathXLevel.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        
        if(this.badSpellsCounter == 0)
            ;
    }
    
    public PathXLevel getLevel(int levelNum){
        return levels.get(levelNum);
    }
    
    public PathXLevel getLevelSprite(int spriteID){
        for(int i = 0; i < levels.size(); i++)
            if(levels.get(i).getSpriteID() == spriteID)
                return levels.get(i);
        return null;
    }
    
    public void setCurrentLevel(int level){
        currentLevel = levels.get(level).getLevelName();
        currentLevelint = level;
    }
    
    public String getCurrentLevel(){
        return currentLevel;
    }
    
    public int getCurrentLevelInt(){
        return currentLevelint;
    }    
    
    public String getCurrentLevelDescription(){
        if(levels.size() > 0)
            return levels.get(currentLevelint).getLevelDescription();
        return null;
    }
    
    public int getCurrentLevelReward(){
        return levels.get(currentLevelint).getReward();
    }
    
    public void scrollViewPort(int incX, int incY){
        
        if((viewport.getViewportX() + incX >= 0 && viewport.getViewportX() + incX <= viewport.getMaxViewportX())
                && (viewport.getViewportY() + incY >= 0 && viewport.getViewportY() + incY <= viewport.getMaxViewportY()))
            viewport.scroll(incX, incY);
        
        
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).updateLocation(incX, incY);
        }
    }
    
    public ArrayList<PathXLevel> getLevels(){
        return (ArrayList<PathXLevel>)levels.clone();
    }
    
    public int getNumLevels(){
        return levels.size();
    }
    
    public int getBalance(){
        return balance;
    }

    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void reset(MiniGame game) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAll(MiniGame game) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDebugText(MiniGame game) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
