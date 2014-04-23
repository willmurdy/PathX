/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;
import pathx.data.PathXLevelState;
import pathx.data.PathXRecord;
import pathx.file.PathXFileManager;

/**
 *
 * @author willmurdy
 */
public class PathXMiniGame extends MiniGame{
    
        // THE PLAYER RECORD FOR EACH LEVEL, WHICH LIVES BEYOND ONE SESSION
    private PathXRecord record;

    // HANDLES GAME UI EVENTS
    private PathXEventHandler eventHandler;
    
    // HANDLES ERROR CONDITIONS
    private PathXErrorHandler errorHandler;
    
    // MANAGES LOADING OF LEVELS AND THE PLAYER RECORDS FILES
    private PathXFileManager fileManager;
    
    // THE SCREEN CURRENTLY BEING PLAYED
    private String currentScreenState;
    
    private Viewport map;
    
    private Viewport level;

    @Override
    public void initAudioContent() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initData() {
        // INIT OUR ERROR HANDLER
        errorHandler = new PathXErrorHandler(window);
        
        // INIT OUR FILE MANAGER
        fileManager = new PathXFileManager(this);

        // LOAD THE PLAYER'S RECORD FROM A FILE
        record = fileManager.loadRecord();
        
        // INIT OUR DATA MANAGER
        data = new PathXDataModel(this);
        
        
    }
    
    public void switchToHomeScreen(){
        
       guiDecor.get(BACKGROUND_TYPE).setState(HOME_SCREEN_STATE);
       guiDecor.get(MAP_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(HOME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(START_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
       
       
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setX(QUIT_BUTTON_X);
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setY(QUIT_BUTTON_Y);
       
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setState(INVISIBLE_STATE);
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(false);
       }
       
       
       currentScreenState = HOME_SCREEN_STATE;
    }
    
    public void switchToLevelSelectScreen(){
        
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        guiDecor.get(MAP_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setEnabled(false);
       
       
       guiButtons.get(RIGHT_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(LEFT_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(UP_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(DOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
       

       guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(UP_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(RIGHT_BUTTON_TYPE).setX(RIGHT_BUTTON_X);
       guiButtons.get(RIGHT_BUTTON_TYPE).setY(RIGHT_BUTTON_Y);
       guiButtons.get(LEFT_BUTTON_TYPE).setX(LEFT_BUTTON_X);
       guiButtons.get(LEFT_BUTTON_TYPE).setY(LEFT_BUTTON_Y);
       guiButtons.get(UP_BUTTON_TYPE).setX(UP_BUTTON_X);
       guiButtons.get(UP_BUTTON_TYPE).setY(UP_BUTTON_Y);
       guiButtons.get(DOWN_BUTTON_TYPE).setX(DOWN_BUTTON_X);
       guiButtons.get(DOWN_BUTTON_TYPE).setY(DOWN_BUTTON_Y);

       guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setX(QUIT_BUTTON_X);
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setY(QUIT_BUTTON_Y);
       guiButtons.get(START_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(BACK_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(SPECIALS_REDLIGHT_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_REDLIGHT_TYPE).setEnabled(false);
       
       ((PathXDataModel)data).setViewportState(LEVEL_SCREEN_STATE);
       guiDecor.get(MAP_TYPE).setState(LEVEL_SCREEN_STATE);
       
       guiDecor.get(INTERSECTION_TYPE).setState(INVISIBLE_STATE);
       
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setState(VISIBLE_STATE);
           if(!((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.LOCKED_STATE.toString()))
                guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(true);
       }
       
       currentScreenState = LEVEL_SELECT_SCREEN_STATE;
        
    }
    
    public void switchToSettingsScreen(){
        
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setEnabled(false);

       guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setX(QUIT_BUTTON_X);
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setY(QUIT_BUTTON_Y);
       
       currentScreenState = SETTINGS_SCREEN_STATE;
        
    }
    
    public void switchToHelpScreen(){
        
       guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setEnabled(false);

       guiButtons.get(HOME_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(HOME_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setX(QUIT_BUTTON_X);
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setY(QUIT_BUTTON_Y);
       
       currentScreenState = HELP_SCREEN_STATE;
        
    }
    
        public void switchToGamePlayScreen(){
        
       guiDecor.get(BACKGROUND_TYPE).setState(GAMEPLAY_SCREEN_STATE);
        
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setState(INVISIBLE_STATE);
       
       guiButtons.get(PLAY_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(RESET_GAME_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(VIEW_HELP_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(HOME_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(HOME_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setX(GAMEPLAY_QUIT_BUTTON_X);
       guiButtons.get(QUIT_GAME_BUTTON_TYPE).setY(GAMEPLAY_QUIT_BUTTON_Y);
       
//       guiButtons.get(RIGHT_BUTTON_TYPE).setState(INVISIBLE_STATE);
//       guiButtons.get(LEFT_BUTTON_TYPE).setState(INVISIBLE_STATE);
//       guiButtons.get(UP_BUTTON_TYPE).setState(INVISIBLE_STATE);
//       guiButtons.get(DOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(RIGHT_BUTTON_TYPE).setX(RIGHT_BUTTON_GAME_X);
       guiButtons.get(RIGHT_BUTTON_TYPE).setY(RIGHT_BUTTON_GAME_Y);
       guiButtons.get(LEFT_BUTTON_TYPE).setX(LEFT_BUTTON_GAME_X);
       guiButtons.get(LEFT_BUTTON_TYPE).setY(LEFT_BUTTON_GAME_Y);
       guiButtons.get(UP_BUTTON_TYPE).setX(UP_BUTTON_GAME_X);
       guiButtons.get(UP_BUTTON_TYPE).setY(UP_BUTTON_GAME_Y);
       guiButtons.get(DOWN_BUTTON_TYPE).setX(DOWN_BUTTON_GAME_X);
       guiButtons.get(DOWN_BUTTON_TYPE).setY(DOWN_BUTTON_GAME_Y);
       
       guiButtons.get(START_BUTTON_TYPE).setState(VISIBLE_STATE);
       
//       guiButtons.get(RIGHT_BUTTON_TYPE).setEnabled(false);
//       guiButtons.get(LEFT_BUTTON_TYPE).setEnabled(false);
//       guiButtons.get(UP_BUTTON_TYPE).setEnabled(false);
//       guiButtons.get(DOWN_BUTTON_TYPE).setEnabled(false);
       guiButtons.get(START_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(PAUSE_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
       
       guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(BACK_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
       guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
       guiDialogs.get(LEVEL_DIALOG_TYPE).setState(VISIBLE_STATE);
       
       guiButtons.get(SPECIALS_REDLIGHT_TYPE).setState(VISIBLE_STATE);
//       guiButtons.get(SPECIALS_REDLIGHT_TYPE1).setState(VISIBLE_STATE);
//       guiButtons.get(SPECIALS_REDLIGHT_TYPE2).setState(VISIBLE_STATE);
//       guiButtons.get(SPECIALS_REDLIGHT_TYPE3).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_REDLIGHT_TYPE).setEnabled(true);
    
       
       //((PathXDataModel)data).setViewportState(GAMEPLAY_SCREEN_STATE);
       guiDecor.get(MAP_TYPE).setState(GAMEPLAY_SCREEN_STATE);
       
       
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setState(INVISIBLE_STATE);
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(false);
       }
       
       currentScreenState = GAMEPLAY_SCREEN_STATE;
        
    }

    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
 
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty("IMG_PATH");        
        String windowIconFile = props.getProperty("WINDOW_ICON");
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);

        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        currentScreenState = HOME_SCREEN_STATE;
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT = new SpriteType(BACKGROUND_TYPE);
        sT.addState(HOME_SCREEN_STATE, img);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAMEPLAY));
        sT.addState(GAMEPLAY_SCREEN_STATE, img);
        
        s = new Sprite(sT, 0, 0, 0, 0, HOME_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        sT = new SpriteType(INTERSECTION_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE));
        sT.addState(OPEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE));
        sT.addState(CLOSED_STATE, img);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        guiDecor.put(INTERSECTION_TYPE, s);
        
        
        //viewport
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MAP));
        sT = new SpriteType(MAP_TYPE);
        sT.addState(LEVEL_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL_MAP));
        sT.addState(GAMEPLAY_SCREEN_STATE, img);
        sT.addState(INVISIBLE_STATE, null);
        s = new Sprite(sT, data.getViewport().getViewportMarginRight(), data.getViewport().getViewportMarginTop(), 0, 0, INVISIBLE_STATE);
        guiDecor.put(MAP_TYPE, s);
        
        ((PathXDataModel)data).initLevelSelectionViewport();
        ((PathXDataModel)data).initGameplayViewPort();
        ((PathXDataModel)data).setViewportState(LEVEL_SCREEN_STATE);
        
        
        //Add the Buttons for the Home screen
        String playButton = props.getProperty(PathXPropertyType.PLAY_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(PLAY_GAME_BUTTON_TYPE);
	img = loadImage(imgPath + playButton);
        sT.addState(VISIBLE_STATE, img);
        String playMouseOverButton = props.getProperty(PathXPropertyType.PLAY_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + playMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(PLAY_GAME_BUTTON_TYPE, s);
        
        //add the reset button to the home screen
        String resetButton = props.getProperty(PathXPropertyType.RESET_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(RESET_GAME_BUTTON_TYPE);
	img = loadImage(imgPath + resetButton);
        sT.addState(VISIBLE_STATE, img);
        String resetMouseOverButton = props.getProperty(PathXPropertyType.RESET_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + resetMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, RESET_BUTTON_X, RESET_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(RESET_GAME_BUTTON_TYPE, s);
        
        //add the SETTINGS button to the home screen
        String settingButton = props.getProperty(PathXPropertyType.VIEW_SETTINGS_BUTTON_IMAGE_NAME);
        sT = new SpriteType(VIEW_SETTINGS_BUTTON_TYPE);
	img = loadImage(imgPath + settingButton);
        sT.addState(VISIBLE_STATE, img);
        String settingMouseOverButton = props.getProperty(PathXPropertyType.VIEW_SETTINGS_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + settingMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SETTINGS_BUTTON_X, SETTINGS_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(VIEW_SETTINGS_BUTTON_TYPE, s);
        
        //add the HELP button to the home screen
        String helpButton = props.getProperty(PathXPropertyType.VIEW_HELP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(VIEW_HELP_BUTTON_TYPE);
	img = loadImage(imgPath + helpButton);
        sT.addState(VISIBLE_STATE, img);
        String helpMouseOverButton = props.getProperty(PathXPropertyType.VIEW_HELP_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + helpMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, HELP_BUTTON_X, HELP_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(VIEW_HELP_BUTTON_TYPE, s);
        
        //add the HOME button to the home screen
        String quitButton = props.getProperty(PathXPropertyType.QUIT_GAME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(QUIT_GAME_BUTTON_TYPE);
	img = loadImage(imgPath + quitButton);
        sT.addState(VISIBLE_STATE, img);
        String quitMouseOverButton = props.getProperty(PathXPropertyType.QUIT_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + quitMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, QUIT_BUTTON_X, QUIT_BUTTON_Y, 0, 0, VISIBLE_STATE);
        guiButtons.put(QUIT_GAME_BUTTON_TYPE, s);
        
        //add the HOME button to the home screen
        String homeButton = props.getProperty(PathXPropertyType.HOME_BUTTON_IMAGE_NAME);
        sT = new SpriteType(HOME_BUTTON_TYPE);
	img = loadImage(imgPath + homeButton);
        sT.addState(VISIBLE_STATE, img);
        String homeMouseOverButton = props.getProperty(PathXPropertyType.HOME_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + homeMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, HOME_BUTTON_X, HOME_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(HOME_BUTTON_TYPE, s);
        
        //add the right scroll button for the viewport
        String rightButton = props.getProperty(PathXPropertyType.RIGHT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(RIGHT_BUTTON_TYPE);
	img = loadImage(imgPath + rightButton);
        sT.addState(VISIBLE_STATE, img);
        String rightMouseOverButton = props.getProperty(PathXPropertyType.RIGHT_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + rightMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, RIGHT_BUTTON_X, RIGHT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(RIGHT_BUTTON_TYPE, s);
        
        //add the left scroll button for the viewport
        String leftButton = props.getProperty(PathXPropertyType.LEFT_BUTTON_IMAGE_NAME);
        sT = new SpriteType(LEFT_BUTTON_TYPE);
	img = loadImage(imgPath + leftButton);
        sT.addState(VISIBLE_STATE, img);
        String leftMouseOverButton = props.getProperty(PathXPropertyType.LEFT_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + leftMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEFT_BUTTON_X, LEFT_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEFT_BUTTON_TYPE, s);
         
        //add the up scroll button for the viewport
        String upButton = props.getProperty(PathXPropertyType.UP_BUTTON_IMAGE_NAME);
        sT = new SpriteType(UP_BUTTON_TYPE);
	img = loadImage(imgPath + upButton);
        sT.addState(VISIBLE_STATE, img);
        String upMouseOverButton = props.getProperty(PathXPropertyType.UP_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + upMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, UP_BUTTON_X, UP_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(UP_BUTTON_TYPE, s);
        
        //add the down scroll button for the viewport
        String downButton = props.getProperty(PathXPropertyType.DOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(DOWN_BUTTON_TYPE);
	img = loadImage(imgPath + downButton);
        sT.addState(VISIBLE_STATE, img);
        String downMouseOverButton = props.getProperty(PathXPropertyType.DOWN_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + downMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, DOWN_BUTTON_X, DOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(DOWN_BUTTON_TYPE, s);
        
        String closeButton = props.getProperty(PathXPropertyType.CLOSE_BUTTON_IMAGE_NAME);
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
	img = loadImage(imgPath + closeButton);
        sT.addState(VISIBLE_STATE, img);
        String closeMouseOverButton = props.getProperty(PathXPropertyType.CLOSE_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + closeMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, CLOSE_BUTTON_X, CLOSE_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(CLOSE_BUTTON_TYPE, s);
        
        //add the HOME button to the home screen
        String backButton = props.getProperty(PathXPropertyType.BACK_BUTTON_IMAGE_NAME);
        sT = new SpriteType(BACK_BUTTON_TYPE);
	img = loadImage(imgPath + backButton);
        sT.addState(VISIBLE_STATE, img);
        String backMouseOverButton = props.getProperty(PathXPropertyType.BACK_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + backMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, BACK_BUTTON_X, BACK_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(BACK_BUTTON_TYPE, s);
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        
        //add the HOME button to the home screen
        String startButton = props.getProperty(PathXPropertyType.START_BUTTON_IMAGE_NAME);
        sT = new SpriteType(START_BUTTON_TYPE);
	img = loadImage(imgPath + startButton);
        sT.addState(VISIBLE_STATE, img);
        String startMouseOverButton = props.getProperty(PathXPropertyType.START_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + startMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, START_BUTTON_X, START_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(START_BUTTON_TYPE, s);   
        
        //add the HOME button to the home screen
        String pauseButton = props.getProperty(PathXPropertyType.PAUSE_BUTTON_IMAGE_NAME);
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
	img = loadImage(imgPath + pauseButton);
        sT.addState(VISIBLE_STATE, img);
        String pauseMouseOverButton = props.getProperty(PathXPropertyType.PAUSE_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + pauseMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, PAUSE_BUTTON_GAME_X, PAUSE_BUTTON_GAME_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(PAUSE_BUTTON_TYPE, s);
        
        //add the HOME button to the home screen
        String redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_REDLIGHT);
        sT = new SpriteType(SPECIALS_REDLIGHT_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        String redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_REDLIGHT_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X, SPECIALS_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_REDLIGHT_TYPE, s);
//        
//        s = new Sprite(sT, SPECIALS_X,40 + SPECIALS_Y, 0, 0, INVISIBLE_STATE);
//        guiButtons.put(SPECIALS_REDLIGHT_TYPE1, s);
//        
//        s = new Sprite(sT, SPECIALS_X, 80+SPECIALS_Y, 0, 0, INVISIBLE_STATE);
//        guiButtons.put(SPECIALS_REDLIGHT_TYPE2, s);
//        
//        s = new Sprite(sT, SPECIALS_X, 120+SPECIALS_Y, 0, 0, INVISIBLE_STATE);
//        guiButtons.put(SPECIALS_REDLIGHT_TYPE3, s);
        
        ((PathXDataModel)data).initLevels();
//        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
//        float totalWidth = levels.size() * (LEVEL_BUTTON_WIDTH + LEVEL_BUTTON_MARGIN) - LEVEL_BUTTON_MARGIN;
//        Viewport viewport = data.getViewport();
//        x = (viewport.getScreenWidth() - totalWidth)/2.0f;
        for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
        {
            sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
            if(((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.AVAILABLE_STATE.toString())){
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);

            } else if(((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.COMPLETED_STATE.toString())){
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);

            } else if(((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.LOCKED_STATE.toString())){
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_LOCKED));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_LOCKED_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);

            }
            sT.addState(INVISIBLE_STATE, null);
            s = new Sprite(sT, ((PathXDataModel)data).getLevel(i).getX(), ((PathXDataModel)data).getLevel(i).getY(),
                    0, 0, INVISIBLE_STATE);
            guiButtons.put(((PathXDataModel)data).getLevel(i).getLevelName(), s);
            guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(false);
            ((PathXDataModel)data).getLevel(i).setSpriteID(s.getID());
        }
        
        //add the down scroll button for the viewport
//        String levelButton = props.getProperty(PathXPropertyType.DOWN_BUTTON_IMAGE_NAME);
//        sT = new SpriteType(DOWN_BUTTON_TYPE);
//	img = loadImage(imgPath + downButton);
//        sT.addState(VISIBLE_STATE, img);
//        String downMouseOverButton = props.getProperty(PathXPropertyType.DOWN_MOUSE_OVER_BUTTON_IMAGE_NAME);
//        img = loadImage(imgPath + downMouseOverButton);
//        sT.addState(MOUSE_OVER_STATE, img);
//        s = new Sprite(sT, DOWN_BUTTON_X, DOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
//        guiButtons.put(DOWN_BUTTON_TYPE, s);

        // AND THE TILE STACK
//        String tileStack = props.getProperty(SortingHatPropertyType.IMAGE_BUTTON_TEMP_TILE);
//        sT = new SpriteType(ALGORITHM_TYPE);
//        img = loadImageWithColorKey(imgPath + tileStack, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        s = new Sprite(sT, TEMP_TILE_X, TEMP_TILE_Y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDecor.put(ALGORITHM_TYPE, s);

        // NOW ADD THE DIALOGS
        
        // AND THE STATS DISPLAY
        sT = new SpriteType(LEVEL_DIALOG_TYPE);
        String levelDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_LEVEL);
        img = loadImageWithColorKey(imgPath + levelDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_DIALOG_X, LEVEL_DIALOG_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LEVEL_DIALOG_TYPE, s);
        
        // AND THE WIN CONDITION DISPLAY
//        String winDisplay = props.getProperty(SortingHatPropertyType.IMAGE_DIALOG_WIN);
//        sT = new SpriteType(WIN_DIALOG_TYPE);
//        img = loadImageWithColorKey(imgPath + winDisplay, COLOR_KEY);
//        sT.addState(SortingHatTileState.VISIBLE_STATE.toString(), img);
//        x = (viewport.getScreenWidth()/2) - (img.getWidth(null)/2);
//        y = (viewport.getScreenHeight()/2) - (img.getHeight(null)/2);
//        s = new Sprite(sT, x, y, 0, 0, SortingHatTileState.INVISIBLE_STATE.toString());
//        guiDialogs.put(WIN_DIALOG_TYPE, s);
//		
        // THEN THE TILES STACKED TO THE TOP LEFT
//        ((SortingHatDataModel)data).initTiles();
     }

    @Override
    public void initGUIHandlers() {
        // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);
                
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            { eventHandler.respondToExitRequest(); }
        });

//        // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> levels = props.getPropertyOptionsList(SortingHatPropertyType.LEVEL_OPTIONS);
//        for (String levelFile : levels)
//        {
//            Sprite levelButton = guiButtons.get(levelFile);
//            levelButton.setActionCommand(PATH_DATA + levelFile);
//            levelButton.setActionListener(new ActionListener(){
//                Sprite s;
//                public ActionListener init(Sprite initS) 
//                {   s = initS; 
//                    return this;    }
//                public void actionPerformed(ActionEvent ae)
//                {   eventHandler.respondToSelectLevelRequest(s.getActionCommand());    }
//            }.init(levelButton));
//        }   

        // NEW GAME EVENT HANDLER
        guiButtons.get(PLAY_GAME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToPlayGameRequest();     }
        });
        
        guiButtons.get(RESET_GAME_BUTTON_TYPE).setActionListener(new ActionListener() {   //ADDED
            public void actionPerformed(ActionEvent ae){
                eventHandler.respondToResetGameRequest();
            }
        });

        // QUIT BUTTON EVENT HANDLER
        guiButtons.get(QUIT_GAME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToExitRequest();    }
        });
        
        // Home BUTTON EVENT HANDLER
        guiButtons.get(HOME_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToHomeRequest();    }
        });
        
                // Home BUTTON EVENT HANDLER
        guiButtons.get(RIGHT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(VIEWPORT_INC, 0);    }
        });
        
                // Home BUTTON EVENT HANDLER
        guiButtons.get(LEFT_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(-VIEWPORT_INC, 0);    }
        });
        
                // Home BUTTON EVENT HANDLER
        guiButtons.get(UP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(0, -VIEWPORT_INC);    }
        });
        
                // Home BUTTON EVENT HANDLER
        guiButtons.get(DOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToScrollRequest(0, VIEWPORT_INC);    }
        });  
        
                      // Home BUTTON EVENT HANDLER
        guiButtons.get(VIEW_SETTINGS_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSettingsRequest();    }
        }); 
        
                      // Home BUTTON EVENT HANDLER
        guiButtons.get(VIEW_HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToHelpRequest();    }
        }); 
        
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           PathXLevel level = ((PathXDataModel)data).getLevel(i);
           guiButtons.get(level.getLevelName()).setActionListener(new ActionListener(){
               
                int lev;
                public ActionListener init(int level) 
                {   lev = level; 
                    return this;    }
                public void actionPerformed(ActionEvent ae)
                {   eventHandler.respondToLevelSelectRequest(lev);    }
            
               
//            public void actionPerformed(ActionEvent ae)
//            {   eventHandler.respondToLevelSelectRequest(level.toString());    }
            }.init(i));
       }
 
        // Home BUTTON EVENT HANDLER
        guiButtons.get(CLOSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToCloseRequest();    }
        }); 
        
        // Home BUTTON EVENT HANDLER
        guiButtons.get(BACK_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToBackRequest();    }
        });
        
        // Home BUTTON EVENT HANDLER
        guiButtons.get(PAUSE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToPauseRequest();    }
        });
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                eventHandler.respondToKeyPress(ke.getKeyCode());    
            }
        });
        
//                // SEND ALL LEVEL SELECTION HANDLING OFF TO THE EVENT HANDLER
//        PropertiesManager props = PropertiesManager.getPropertiesManager();
//        ArrayList<String> levels = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
//        for (String levelFile : levels)
//        {
//            Sprite levelButton = guiButtons.get(levelFile);
//            //levelButton.setActionCommand(PATH_DATA + levelFile);
//            levelButton.setActionListener(new ActionListener(){
//                Sprite s;
//                public ActionListener init(Sprite initS) 
//                {   s = initS; 
//                    return this;    }
//                public void actionPerformed(ActionEvent ae)
//                {   eventHandler.respondToSelectLevelRequest(s.getActionCommand());    }
//            }.init(levelButton));
//        } 
    }
    
        public boolean isCurrentScreenState(String testScreenState)
    {
        return testScreenState.equals(currentScreenState);
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateGUI() {
         // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(VISIBLE_STATE))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(MOUSE_OVER_STATE);
                        
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(MOUSE_OVER_STATE))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(VISIBLE_STATE);
                }
            }
        }
    }
    
}
