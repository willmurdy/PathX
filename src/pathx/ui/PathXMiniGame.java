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
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeMap;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import properties_manager.PropertiesManager;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.*;
import pathx.data.PathXDataModel;
import pathx.data.PathXIntersection;
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
    
    private TreeMap<String, Sprite> guiEntities;
    
    private TreeMap<Integer, Sprite> guiIntersections;
    
    private boolean renderDescription;
    
    private boolean mute = false;

    @Override
    public void initAudioContent() {
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String audioPath = props.getProperty(PathXPropertyType.AUDIO_PATH);

            // LOAD ALL THE AUDIO
            loadAudioCue(PathXPropertyType.AUDIO_CUE_POLICE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_LOSS);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_ZOMBIE);
            loadAudioCue(PathXPropertyType.AUDIO_CUE_HOME);
//            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_CHEAT);
//            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_UNDO);
//            loadAudioCue(SortingHatPropertyType.AUDIO_CUE_WIN);
//            loadAudioCue(SortingHatPropertyType.SONG_CUE_MENU_SCREEN);
//            loadAudioCue(SortingHatPropertyType.SONG_CUE_GAME_SCREEN);

            // PLAY THE WELCOME SCREEN SONG
            audio.play(PathXPropertyType.AUDIO_CUE_HOME.toString(), true);
        }
        catch(UnsupportedAudioFileException | IOException | LineUnavailableException | InvalidMidiDataException | MidiUnavailableException e)
        {
//            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_AUDIO);
            System.out.println("ERROR LOADING AUDIO");
        } 
    }
    
        private void loadAudioCue(PathXPropertyType audioCueType) 
            throws  UnsupportedAudioFileException, IOException, LineUnavailableException, 
                    InvalidMidiDataException, MidiUnavailableException
    {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String audioPath = props.getProperty(PathXPropertyType.AUDIO_PATH);
        String cue = props.getProperty(audioCueType.toString());
        audio.loadAudio(audioCueType.toString(), audioPath + cue);        
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
        
        guiEntities = new TreeMap<String, Sprite>();
        
        guiIntersections = new TreeMap<Integer, Sprite>();
        
        renderDescription = false;
        
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
       
       guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(MUTE_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(MUTE_BUTTON_TYPE).setEnabled(false);
       
       ((PathXDataModel)data).setTriggered(false);
       
       if(!mute && !audio.isPlaying(PathXPropertyType.AUDIO_CUE_HOME.toString()))
        audio.play(PathXPropertyType.AUDIO_CUE_HOME.toString(), true);
       
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
       guiButtons.get(SPECIALS_FLAT_TIRE_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_FLAT_TIRE_TYPE).setEnabled(false);
       guiButtons.get(SPECIALS_INTANGABLE_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_INTANGABLE_TYPE).setEnabled(false);
       guiButtons.get(SPECIALS_INVINCIBILITY_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_INVINCIBILITY_TYPE).setEnabled(false);
       guiButtons.get(SPECIALS_SPEED_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_SPEED_TYPE).setEnabled(false);
       guiButtons.get(SPECIALS_FLY_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(SPECIALS_FLY_TYPE).setEnabled(false);
       
       guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
       
       guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(INVISIBLE_STATE);
       guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
       
       ((PathXDataModel)data).setViewportState(LEVEL_SCREEN_STATE);
       guiDecor.get(MAP_TYPE).setState(LEVEL_SCREEN_STATE);
       
       ((PathXDataModel)data).setTriggered(false);
       
       guiDialogs.get(END_DIALOG_TYPE).setState(INVISIBLE_STATE);
       
//       guiDecor.get(INTERSECTION_TYPE).setState(INVISIBLE_STATE);
       BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty("IMG_PATH"); 
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setState(VISIBLE_STATE);
           if(!((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.LOCKED_STATE.toString()))
                guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(true);
           
           if(((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.COMPLETED_STATE.toString())){
                sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);
                sT.addState(INVISIBLE_STATE, null);
                s = new Sprite(sT, ((PathXDataModel)data).getLevel(i).getX(), ((PathXDataModel)data).getLevel(i).getY(),
                    0, 0, VISIBLE_STATE);
                guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setSpriteType(sT);
           }else {
            if(((PathXDataModel)data).getLevel(i).getState().equals(PathXLevelState.AVAILABLE_STATE.toString())){
                 sT = new SpriteType(LEVEL_SELECT_BUTTON_TYPE);
                 img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE));
                 sT.addState(VISIBLE_STATE, img);
                 img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE_MOUSE_OVER));
                 sT.addState(MOUSE_OVER_STATE, img);
                 sT.addState(INVISIBLE_STATE, null);
                 s = new Sprite(sT, ((PathXDataModel)data).getLevel(i).getX(), ((PathXDataModel)data).getLevel(i).getY(),
                     0, 0, VISIBLE_STATE);
                 guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setSpriteType(sT);
            }
           }
       }
       
       currentScreenState = LEVEL_SELECT_SCREEN_STATE;
       
       renderDescription = false;
        
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
       
       guiButtons.get(MUTE_BUTTON_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(MUTE_BUTTON_TYPE).setEnabled(true);
       
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
       guiButtons.get(SPECIALS_REDLIGHT_TYPE).setEnabled(true);
       
       guiButtons.get(SPECIALS_FLAT_TIRE_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_FLAT_TIRE_TYPE).setEnabled(true);
       guiButtons.get(SPECIALS_INTANGABLE_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_INTANGABLE_TYPE).setEnabled(true);
       guiButtons.get(SPECIALS_INVINCIBILITY_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_INVINCIBILITY_TYPE).setEnabled(true);
       guiButtons.get(SPECIALS_SPEED_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_SPEED_TYPE).setEnabled(true);
       guiButtons.get(SPECIALS_FLY_TYPE).setState(VISIBLE_STATE);
       guiButtons.get(SPECIALS_FLY_TYPE).setEnabled(true);
       
       //((PathXDataModel)data).setViewportState(GAMEPLAY_SCREEN_STATE);
       guiDecor.get(MAP_TYPE).setState(GAMEPLAY_SCREEN_STATE);
       
       guiEntities.get(PLAYER_TYPE).setState(VISIBLE_STATE);
       guiEntities.get(ZOMBIE_TYPE).setState(VISIBLE_STATE);
       guiEntities.get(BANDIT_TYPE).setState(VISIBLE_STATE);
       
       ((PathXDataModel)data).setTriggered(false);
       
       renderDescription = true;
       
       if(!mute)
            audio.stop(PathXPropertyType.AUDIO_CUE_HOME.toString());
       
       int currentLevel = ((PathXDataModel)data).getCurrentLevelInt();
       
       PathXIntersection intersection;
       
       guiIntersections = new TreeMap<Integer, Sprite>();
       
       Sprite s;
       SpriteType sT;
       PropertiesManager props = PropertiesManager.getPropertiesManager();
       String imgPath = props.getProperty("IMG_PATH");
       BufferedImage img;
       
       for(int i = 0; i < ((PathXDataModel)data).getLevel(currentLevel).getNumIntersections(); i++){
            intersection = ((PathXDataModel)data).getLevel(currentLevel).getIntersection(i);
            
            sT = new SpriteType(INTERSECTION_TYPE);
            if(intersection.open()){
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);
            } else {
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE));
                sT.addState(VISIBLE_STATE, img);
                img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE_MOUSE_OVER));
                sT.addState(MOUSE_OVER_STATE, img);
            }
            //sT.addState(STARTING_STATE, null);
            //sT.addState(ENDING_STATE, null);
            s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
            
           
            guiIntersections.put(i, s);
//            if(i == 0){
//                guiIntersections.get(i).getSpriteType().addState(STARTING_STATE, ((PathXDataModel)data).getLevel(currentLevel).getStartingImage());
//                guiIntersections.get(i).setState(STARTING_STATE);
//            } else
//                if(i == 1){
//                    guiIntersections.get(i).getSpriteType().addState(ENDING_STATE, ((PathXDataModel)data).getLevel(currentLevel).getDestinationImage());
//                    guiIntersections.get(i).setState(ENDING_STATE);  
//                } else
//                    if(intersection.open())
//                        guiIntersections.get(i).setState(OPEN_STATE);
//                    else
//                        guiIntersections.get(i).setState(CLOSED_STATE);
            
            guiIntersections.get(i).setEnabled(true);
            
//            guiButtons.put(((PathXDataModel)data).getLevel(currentLevel).getLevelName() + i,guiDecor.get(INTERSECTION_TYPE));
       }
       
       initIntersectionsHandlers();
       
       
       for (int i = 0; i < ((PathXDataModel)data).getNumLevels(); i++)
       {
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setState(INVISIBLE_STATE);
           guiButtons.get(((PathXDataModel)data).getLevel(i).getLevelName()).setEnabled(false);
       }
       
       currentScreenState = GAMEPLAY_SCREEN_STATE;
        
    }
    
    public void showWinDialog(){
        if(!guiDialogs.get(END_DIALOG_TYPE).getState().equals(VISIBLE_STATE)){
                 guiDialogs.get(END_DIALOG_TYPE).setState(VISIBLE_STATE);
                
                
                guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setState(VISIBLE_STATE);
                guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setEnabled(true);
                
                
                guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setState(VISIBLE_STATE);
                guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(true);
                
                renderDescription = true;
        }
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
        
        //load the enitites images
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ENTITY_PLAYER));
        sT = new SpriteType(PLAYER_TYPE);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(INVISIBLE_STATE, null);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        guiEntities.put(PLAYER_TYPE, s);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ENTITY_POLICE));
        sT = new SpriteType(POLICE_TYPE);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(INVISIBLE_STATE, null);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        guiEntities.put(POLICE_TYPE, s);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ENTITY_ZOMBIE));
        sT = new SpriteType(ZOMBIE_TYPE);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(INVISIBLE_STATE, null);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        guiEntities.put(ZOMBIE_TYPE, s);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ENTITY_BANDIT));
        sT = new SpriteType(BANDIT_TYPE);
        sT.addState(VISIBLE_STATE, img);
        sT.addState(INVISIBLE_STATE, null);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        guiEntities.put(BANDIT_TYPE, s);
        
        sT = new SpriteType(INTERSECTION_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE));
        sT.addState(OPEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_COMPLETE_MOUSE_OVER));
        sT.addState(OPEN_MOUSE_OVER_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE));
        sT.addState(CLOSED_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEVEL_AVAILABLE_MOUSE_OVER));
        sT.addState(CLOSED_MOUSE_OVER_STATE, img);
        //sT.addState(STARTING_STATE, null);
        //sT.addState(ENDING_STATE, null);
        s = new Sprite(sT, 0, 0, 0, 0, INVISIBLE_STATE);
        //guiButtons.put(INTERSECTION_TYPE, s);
        
        
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
        
        String tryButton = props.getProperty(PathXPropertyType.TRY_AGAIN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
	img = loadImage(imgPath + tryButton);
        sT.addState(VISIBLE_STATE, img);
        String tryMouseOverButton = props.getProperty(PathXPropertyType.TRY_AGAIN_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + tryMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, TRY_AGAIN_BUTTON_X, TRY_AGAIN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
        
        String leaveButton = props.getProperty(PathXPropertyType.LEAVE_TOWN_BUTTON_IMAGE_NAME);
        sT = new SpriteType(LEAVE_TOWN_BUTTON_TYPE);
	img = loadImage(imgPath + leaveButton);
        sT.addState(VISIBLE_STATE, img);
        String leaveMouseOverButton = props.getProperty(PathXPropertyType.LEAVE_TOWN_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + leaveMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, LEAVE_TOWN_BUTTON_X, LEAVE_TOWN_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(LEAVE_TOWN_BUTTON_TYPE, s);
        
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
        
        String muteButton = props.getProperty(PathXPropertyType.MUTE_BUTTON_IMAGE_NAME);
        sT = new SpriteType(MUTE_BUTTON_TYPE);
	img = loadImage(imgPath + muteButton);
        sT.addState(VISIBLE_STATE, img);
        String muteMouseOverButton = props.getProperty(PathXPropertyType.MUTE_MOUSE_OVER_BUTTON_IMAGE_NAME);
        img = loadImage(imgPath + muteMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, MUTE_BUTTON_X, MUTE_BUTTON_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(MUTE_BUTTON_TYPE, s);
        
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
        
        redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_FLAT_TIRE);
        sT = new SpriteType(SPECIALS_FLAT_TIRE_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_FLAT_TIRE_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X + 40, SPECIALS_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_FLAT_TIRE_TYPE, s);
        
        redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_INTANGABLE);
        sT = new SpriteType(SPECIALS_INTANGABLE_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_INTANGABLE_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X + 80, SPECIALS_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_INTANGABLE_TYPE, s);
        
        redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_INVINCIBILITY);
        sT = new SpriteType(SPECIALS_INVINCIBILITY_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_INVINCIBILITY_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X + 120, SPECIALS_Y, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_INVINCIBILITY_TYPE, s);
        
        redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_SPEED);
        sT = new SpriteType(SPECIALS_SPEED_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_SPEED_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X, SPECIALS_Y + 40, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_SPEED_TYPE, s);
        
        redButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_FLY);
        sT = new SpriteType(SPECIALS_FLY_TYPE);
	img = loadImage(imgPath + redButton);
        sT.addState(VISIBLE_STATE, img);
        redMouseOverButton = props.getProperty(PathXPropertyType.IMAGE_SPECIALS_FLY_MOUSE_OVER);
        img = loadImage(imgPath + redMouseOverButton);
        sT.addState(MOUSE_OVER_STATE, img);
        s = new Sprite(sT, SPECIALS_X + 40, SPECIALS_Y + 40, 0, 0, INVISIBLE_STATE);
        guiButtons.put(SPECIALS_FLY_TYPE, s);
        
        ((PathXDataModel)data).initLevels();
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
        

        // NOW ADD THE DIALOGS
        
        // AND THE STATS DISPLAY
        sT = new SpriteType(LEVEL_DIALOG_TYPE);
        String levelDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_LEVEL);
        img = loadImageWithColorKey(imgPath + levelDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_DIALOG_X, LEVEL_DIALOG_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(LEVEL_DIALOG_TYPE, s);
        
        sT = new SpriteType(END_DIALOG_TYPE);
        String endDialog = props.getProperty(PathXPropertyType.IMAGE_DIALOG_END);
        img = loadImageWithColorKey(imgPath + endDialog, COLOR_KEY);
        sT.addState(VISIBLE_STATE, img);
        s = new Sprite(sT, LEVEL_DIALOG_X, LEVEL_DIALOG_Y, 0, 0, INVISIBLE_STATE);
        guiDialogs.put(END_DIALOG_TYPE, s);

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
        guiButtons.get(TRY_AGAIN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToTryAgainRequest();    }
        }); 
        
                              // Home BUTTON EVENT HANDLER
        guiButtons.get(LEAVE_TOWN_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToBackRequest();    }
        });
        
                      // Home BUTTON EVENT HANDLER
        guiButtons.get(VIEW_HELP_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToHelpRequest();    }
        }); 
        
        guiButtons.get(START_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToStartRequest();    }
        });
        
        guiButtons.get(MUTE_BUTTON_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToMuteRequest();    }
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
        
        guiButtons.get(SPECIALS_INTANGABLE_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToIntangableRequest();    }
        });
        
        guiButtons.get(SPECIALS_INVINCIBILITY_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToInvincibilityRequest();    }
        });
        
        guiButtons.get(SPECIALS_SPEED_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToSpeedRequest();    }
        });
        
        guiButtons.get(SPECIALS_FLY_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToFlyRequest();    }
        });
        
        guiButtons.get(SPECIALS_REDLIGHT_TYPE).setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   eventHandler.respondToRedlightRequest();    }
        });
        
                // Home BUTTON EVENT HANDLER
//        guiDecor.get(INTERSECTION_TYPE).setActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent ae)
//            {   eventHandler.respondToIntersectionRequest();    }
//        });
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                eventHandler.respondToKeyPress(ke.getKeyCode());    
            }
        });
        
        
    }
    
    public void initIntersectionsHandlers(){
        int currentLevel = ((PathXDataModel)data).getCurrentLevelInt();
        for (int i = 0; i < ((PathXDataModel)data).getLevel(currentLevel).getNumIntersections(); i++)
        {
           //PathXIntersection intersection = ((PathXDataModel)data).getLevel(currentLevel).getIntersection(i);
           guiIntersections.get(i).setActionListener(new ActionListener(){
               
                int id;
                public ActionListener init(int intersectionId) 
                {   id = intersectionId; 
                    return this;    }
                public void actionPerformed(ActionEvent ae)
                {   eventHandler.respondToIntersectionRequest(id);    }
            
               
//            public void actionPerformed(ActionEvent ae)
//            {   eventHandler.respondToLevelSelectRequest(level.toString());    }
            }.init(i));
       }
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
    
    public TreeMap<String, Sprite> getGuiEntities(){
        return guiEntities;
    }
    
    public TreeMap<Integer, Sprite> getGuiIntersections(){
        return guiIntersections;
    }   
    
    @Override
    public boolean processButtonPress(int x, int y)
    {
        boolean buttonClickPerformed = false;

        // TEST EACH BUTTON
        for (Sprite s : guiButtons.values())
        {
            // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
            buttonClickPerformed = s.testForClick(this, x, y);

            // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
            // SHOULD NOT OVERLAP
            if (buttonClickPerformed)
            {
                return true;
            }
        }
        
        if(!data.notStarted()){
            for (Sprite s : guiIntersections.values())
            {
                // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
                buttonClickPerformed = s.testForClick(this, x, y);

                // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
                // SHOULD NOT OVERLAP
                if (buttonClickPerformed)
                {
                    return true;
                }
            }
        } 
            for (Sprite s : guiEntities.values())
            {
                // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
                buttonClickPerformed = s.testForClick(this, x, y);

                // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
                // SHOULD NOT OVERLAP
                if (buttonClickPerformed)
                {
                    System.out.println("IT WORKS!");
                    return true;
                }
            }
        
        return false;
    }
    
    public void setRenderDescription(boolean render){
        renderDescription = render;
    }
    
    public boolean renderDescription(){
        return renderDescription;
    }
    
    public boolean mute(){
        return mute;
    }
    
    public void toggleMute(){
        mute = !mute;
        if(mute){
            audio.stop(PathXPropertyType.AUDIO_CUE_HOME.toString());
        }
    }
    
}
