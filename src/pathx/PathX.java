/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx;

import static pathx.PathXConstants.FPS;
import static pathx.PathXConstants.WINDOW_HEIGHT;
import static pathx.PathXConstants.WINDOW_WIDTH;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;

/**
 *
 * @author willmurdy
 */
public class PathX {
        static PathXMiniGame miniGame = new PathXMiniGame();
        
        static String PROPERTY_TYPES_LIST = "property_types.txt";
        static String UI_PROPERTIES_FILE_NAME = "properties.xml";
        static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
        static String DATA_PATH = "./data/";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try
        {
             // LOAD THE SETTINGS FOR STARTING THE APP
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, DATA_PATH);
            props.loadProperties(UI_PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(PathXPropertyType.GAME_FLAVOR_FILE_NAME);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(PathXPropertyType.GAME_TITLE_TEXT);
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH, WINDOW_HEIGHT);
            
            // GET THE PROPER WINDOW DIMENSIONS
            miniGame.startGame();
        }
        // THERE WAS A PROBLEM LOADING THE PROPERTIES FILE
        catch(InvalidXMLFileFormatException ixmlffe)
        {
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
           // MahjongSolitaireErrorHandler errorHandler = miniGame.getErrorHandler();
           // errorHandler.processError(MahjongSolitairePropertyType.INVALID_XML_FILE_ERROR_TEXT);
        }
        
    }
    
    public enum PathXPropertyType
    {
        /* SETUP FILE NAMES */
        UI_PROPERTIES_FILE_NAME,
        PROPERTIES_SCHEMA_FILE_NAME,
        GAME_FLAVOR_FILE_NAME,
        RECORD_FILE_NAME,

        /* DIRECTORIES FOR FILE LOADING */
        AUDIO_PATH,
        DATA_PATH,
        IMG_PATH,
        LEVEL_PATH,
        
        /* WINDOW DIMENSIONS & FRAME RATE */
        WINDOW_WIDTH,
        WINDOW_HEIGHT,
        FPS,
        GAME_WIDTH,
        GAME_HEIGHT,
        GAME_LEFT_OFFSET,
        GAME_TOP_OFFSET,
        
        /* GAME TEXT */
        GAME_TITLE_TEXT,
        EXIT_REQUEST_TEXT,
        INVALID_XML_FILE_ERROR_TEXT,
        ERROR_DIALOG_TITLE_TEXT,
        
        /* ERROR TYPES */
        AUDIO_FILE_ERROR,
        LOAD_LEVEL_ERROR,
        RECORD_SAVE_ERROR,

        /* IMAGE FILE NAMES */
        WINDOW_ICON,
        
        //background image names
        HOME_SCREEN_IMAGE_NAME,
        LEVEL_SELECT_SCREEN_IMAGE_NAME,
        GAMEPLAY_SCREEN_IMAGE_NAME,
        
        //Home Screen Image Names
        PLAY_GAME_BUTTON_IMAGE_NAME,
        PLAY_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME,
        RESET_GAME_BUTTON_IMAGE_NAME,
        VIEW_SETTINGS_BUTTON_IMAGE_NAME,
        QUIT_GAME_BUTTON_IMAGE_NAME,
        
        //Home screen button types
        PLAY_GAME_BUTTON_TYPE,
        RESET_GAME_BUTTON_TYPE,
        VIEW_SETTINGS_BUTTON_TYPE,
        QUIT_GAME_BUTTON_TYPE,
        
        FIRST_LEVEL_BACKGROUND_IMAGE_NAME,
        SECOND_LEVEL_BACKGROUND_IMAGE_NAME,
        LEVEL_SELECT_IMAGE_NAME,
//        BLANK_TILE_IMAGE_NAME,
//        BLANK_TILE_SELECTED_IMAGE_NAME,
//        BLOCKED_TILE_SELECTED_IMAGE_NAME,
        EXIT_BUTTON_IMAGE_NAME,
        EXIT_BUTTON_MOUSE_OVER_IMAGE_NAME,
        LEFT_BUTTON_IMAGE_NAME,
        LEFT_BUTTON_MOUSE_OVER_IMAGE_NAME,
        RIGHT_BUTTON_IMAGE_NAME,
        RIGHT_BUTTON_MOUSE_OVER_IMAGE_NAME,
        PLAY_GAME_IMAGE_NAME,
        PLAY_GAME_MOUSE_OVER_IMAGE_NAME,
        TILE_COUNT_IMAGE_NAME,
        TIME_IMAGE_NAME,
        UNDO_BUTTON_IMAGE_NAME,
        UNDO_BUTTON_MOUSE_OVER_IMAGE_NAME,
        STATS_BUTTON_IMAGE_NAME,
        STATS_BUTTON_MOUSE_OVER_IMAGE_NAME,
        TILE_STACK_IMAGE_NAME,
        
        // AND THE DIALOGS
        STATS_DIALOG_IMAGE_NAME,
        WIN_DIALOG_IMAGE_NAME,
        LOSS_DIALOG_IMAGE_NAME,
        
        /* TILE LOADING STUFF */
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS,
        LEVEL_FILE_NAMES,
        SPLASH_SCREEN_IMAGE_OPTIONS,
        SPLASH_SCREEN_MOUSE_OVER_IMAGE_OPTIONS,
        REGULAR_TILE_TYPES,
        
        /* AUDIO CUES */
        SELECT_AUDIO_CUE,
        MATCH_AUDIO_CUE,
        NO_MATCH_AUDIO_CUE,
        BLOCKED_TILE_AUDIO_CUE,
        UNDO_AUDIO_CUE,
        WIN_AUDIO_CUE,
        SPLASH_SCREEN_SONG_CUE,
        GAMEPLAY_SONG_CUE,
        LOSS_AUDIO_CUE
    }
    
}
