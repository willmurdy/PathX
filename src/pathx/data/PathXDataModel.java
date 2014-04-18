/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Stack;
import java.util.TreeMap;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Viewport;
import static pathx.PathXConstants.*;

/**
 *
 * @author willmurdy
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
    
    public void initLevels(){
                
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> levels = props.getPropertyOptionsList(PathX.PathXPropertyType.LEVEL_OPTIONS);
        
        
        PathXLevel level = new PathXLevel(305, 420, viewport, PathXLevelState.AVAILABLE_STATE.toString(), "Level One","This is a test description for level one that i want to make a bit long", 10);
        levels.add(level);
        level = new PathXLevel(1400, 900, viewport, PathXLevelState.LOCKED_STATE.toString(), "Level Two","Level two needs a description as well", 20);
        levels.add(level);
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
    
    public String getCurrentLevelDescription(){
        return levels.get(currentLevelint).getLevelDescription();
    }
    
    public void scrollViewPort(int incX, int incY){
        
        if((viewport.getViewportX() + incX >= 0 && viewport.getViewportX() + incX <= viewport.getMaxViewportX())
                && (viewport.getViewportY() + incY >= 0 && viewport.getViewportY() + incY <= viewport.getMaxViewportY()))
            viewport.scroll(incX, incY);
        
        
        for(int i = 0; i < levels.size(); i++){
            levels.get(i).updateLocation(incX, incY);
        }
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
