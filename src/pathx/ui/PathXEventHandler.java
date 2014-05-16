/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathx.PathX;
import static pathx.PathXConstants.*;
import pathx.data.PathXDataModel;
import properties_manager.PropertiesManager;

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
    
    public void respondToMuteRequest(){
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty("IMG_PATH");
        
        game.toggleMute();
        
        if(game.mute()){
            String muteButton = props.getProperty(PathX.PathXPropertyType.MUTE_SELECTED_BUTTON_IMAGE_NAME);
            sT = new SpriteType(MUTE_BUTTON_TYPE);
            img = game.loadImage(imgPath + muteButton);
            sT.addState(VISIBLE_STATE, img);
            String muteMouseOverButton = props.getProperty(PathX.PathXPropertyType.MUTE_SELECTED_MOUSE_OVER_BUTTON_IMAGE_NAME);
            img = game.loadImage(imgPath + muteMouseOverButton);
            sT.addState(MOUSE_OVER_STATE, img);
        } else {
            String muteButton = props.getProperty(PathX.PathXPropertyType.MUTE_BUTTON_IMAGE_NAME);
        sT = new SpriteType(MUTE_BUTTON_TYPE);
	img = game.loadImage(imgPath + muteButton);
        sT.addState(VISIBLE_STATE, img);
        String muteMouseOverButton = props.getProperty(PathX.PathXPropertyType.MUTE_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = game.loadImage(imgPath + muteMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        }
        game.getGUIButtons().get(MUTE_BUTTON_TYPE).setSpriteType(sT);
        
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
        data.getLevel(data.getCurrentLevelInt()).resetLevel();
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
    
    public void respondToIntangableRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.makePlayerIntangable();
    }
    
    public void respondToInvincibilityRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.makePlayerInvincible();
    }
    
    public void respondToIntersectionRequest(int id){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getLevel(data.getCurrentLevelInt()).movePlayerToIntersection(id);
    }
    
    public void respondToSpeedRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getLevel(data.getCurrentLevelInt()).increasePlayerSpeed();
    }
    
    public void respondToFlyRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        data.getLevel(data.getCurrentLevelInt()).setFlyToNext();
    }
    
    public void respondToTryAgainRequest(){
        PathXDataModel data = (PathXDataModel)game.getDataModel();
        game.getGUIDialogs().get(END_DIALOG_TYPE).setState(INVISIBLE_STATE);
        
        game.getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        game.getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
        game.getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
        
        data.getLevel(data.getCurrentLevelInt()).setIngame(false);
        data.getLevel(data.getCurrentLevelInt()).resetLevel();
        game.setRenderDescription(false);
        respondToCloseRequest();
        game.switchToGamePlayScreen();
    }
    
    public void respondToLeaveTownRequest(){
    }
    
    public void respondToRedlightRequest(){
        
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
        if(keyCode == KeyEvent.VK_U){
            data.unlockAllLevels();
        }
        if(keyCode == KeyEvent.VK_B){
            this.respondToIntangableRequest();
        }
        if(keyCode == KeyEvent.VK_B){
            this.respondToIntangableRequest();
        }
        if(keyCode == KeyEvent.VK_P){
            this.respondToSpeedRequest();
        }
        if(keyCode == KeyEvent.VK_V){
            this.respondToInvincibilityRequest();
        }
        if(keyCode == KeyEvent.VK_Y){
            this.respondToFlyRequest();
        }

    }
    
}
