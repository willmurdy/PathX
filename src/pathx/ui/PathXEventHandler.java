/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.KeyEvent;
import static pathx.PathXConstants.CLOSE_BUTTON_TYPE;
import static pathx.PathXConstants.GAMEPLAY_SCREEN_STATE;
import static pathx.PathXConstants.INVISIBLE_STATE;
import static pathx.PathXConstants.LEVEL_DIALOG_TYPE;
import static pathx.PathXConstants.LEVEL_SELECT_SCREEN_STATE;
import static pathx.PathXConstants.MOUSE_OVER_STATE;
import static pathx.PathXConstants.PAUSE_BUTTON_TYPE;
import static pathx.PathXConstants.VIEWPORT_INC;
import static pathx.PathXConstants.VISIBLE_STATE;
import pathx.data.PathXDataModel;

/**
 *
 * @author willmurdy
 */
public class PathXEventHandler {
    
    // THE SORTING HAT GAME, IT PROVIDES ACCESS TO EVERYTHING
    private PathXMiniGame game;

    /**
     * Constructor, it just keeps the game for when the events happen.
     */
    public PathXEventHandler(PathXMiniGame initGame)
    {
        game = initGame;
    }

    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }

    /**
     * Called when the user clicks the New button.
     */
    public void respondToPlayGameRequest()
    {
       game.switchToLevelSelectScreen();
    }
    
    public void respondToResetGameRequest()
    {
        game.getDataModel().reset(game);
    }
    
    public void respondToHomeRequest()
    {
      game.switchToHomeScreen();
    }
    
        public void respondToHelpRequest()
    {
      game.switchToHelpScreen();
    }

    /**
     * Called when the user clicks a button to select a level.
     */    
    public void respondToSelectLevelRequest(String levelFile)
    {
       
    }

    /**
     * Called when the user clicks the Stats button.
     */
    public void respondToDisplayStatsRequest()
    {
          
    }

        /**
     * Called when the user clicks the scroll button.
     */
    public void respondToScrollRequest(int incX, int incY)
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        //data.getViewport().scroll(incX, incY);
        //data.getLevel(data.getCurrentLevelInt()).updateIntersectionLocations();
        data.scrollViewPort(incX, incY);
    }
    
            /**
     * Called when the user clicks the scroll button.
     */
    public void respondToSettingsRequest()
    {
        game.switchToSettingsScreen();
    }
    
    public void respondToLevelSelectRequest(int level)
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.setCurrentLevel(level);
        data.getLevel(data.getCurrentLevelInt()).setIngame(true);
        data.setViewportState(GAMEPLAY_SCREEN_STATE);
        data.updateViewport();
        data.getLevel(data.getCurrentLevelInt()).setViewport(data.getViewport());
        data.getLevel(data.getCurrentLevelInt()).updateIntersectionLocations();
        data.getLevel(data.getCurrentLevelInt()).updatePoliceLocations();
        data.getLevel(data.getCurrentLevelInt()).updateRoadLocations();
        //((PathXDataModel)data).initGameplayViewPort();
        game.switchToGamePlayScreen();
    }
        
    public void respondToCloseRequest()
    {
        game.getGUIButtons().get(CLOSE_BUTTON_TYPE).setState(INVISIBLE_STATE);
        game.getGUIDialogs().get(LEVEL_DIALOG_TYPE).setState(INVISIBLE_STATE);
        game.setRenderDescription(false);
    }
    
    public void respondToStartRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getLevel(data.getCurrentLevelInt()).startGame();
    }
    
    public void respondToBackRequest()
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        if(data.getLevel(data.getCurrentLevelInt()).wonLevel()){
            data.unlockNextLevel(data.getCurrentLevelInt());
        }
        data.getLevel(data.getCurrentLevelInt()).setIngame(false);
        data.setViewportState(LEVEL_SELECT_SCREEN_STATE);
        data.setCurrentLevel(-1);
        respondToCloseRequest();
        game.setRenderDescription(false);
        game.switchToLevelSelectScreen();
    }
    
    public void respondToPauseRequest()
    {
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.pauseGame();
        if(data.isPaused()){
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(MOUSE_OVER_STATE);
        } else {
            game.getGUIButtons().get(PAUSE_BUTTON_TYPE).setState(VISIBLE_STATE);
        }
    }
    
    public void respondToIntersectionRequest(int id){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getLevel(data.getCurrentLevelInt()).movePlayerToIntersection(id);
    }
    
    public void respondToTryAgainRequest(){
    }
    
    public void respondToLeaveTownRequest(){
    }
    
    /**
     * Called when the user presses a key on the keyboard.
     */    
    public void respondToKeyPress(int keyCode)
    { 
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        if(keyCode == KeyEvent.VK_DOWN){
            respondToScrollRequest(0, VIEWPORT_INC);
        }
        if(keyCode == KeyEvent.VK_UP){
            respondToScrollRequest(0, -VIEWPORT_INC);
        }
        if(keyCode == KeyEvent.VK_LEFT){
            respondToScrollRequest(-VIEWPORT_INC, 0);
        }
        if(keyCode == KeyEvent.VK_RIGHT){
            respondToScrollRequest(VIEWPORT_INC, 0);
        }

    }
    
}
