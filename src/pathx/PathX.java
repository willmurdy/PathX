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
            String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);

            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR_GAME);
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
        // LOADED FROM properties.xml
        
        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,

        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
            // sorting_hat_properties.xml
                
        /* IMAGE FILE NAMES */
        IMAGE_BACKGROUND_GAME,
        IMAGE_BACKGROUND_LEVEL,
        IMAGE_BACKGROUND_MENU,
        IMAGE_BACKGROUND_MAP,
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BACKGROUND_HELP,
        IMAGE_BACKGROUND_GAMEPLAY,
        
        IMAGE_LEVEL_AVAILABLE,
        IMAGE_LEVEL_AVAILABLE_MOUSE_OVER,
        IMAGE_LEVEL_COMPLETE,
        IMAGE_LEVEL_COMPLETE_MOUSE_OVER,
        IMAGE_LEVEL_LOCKED,
        IMAGE_LEVEL_LOCKED_MOUSE_OVER,
        
        IMAGE_DIALOG_LEVEL,
        
        IMAGE_BUTTON_NEW,
        IMAGE_BUTTON_NEW_MOUSE_OVER,
        IMAGE_BUTTON_BACK,              //Added for the back button
        IMAGE_BUTTON_BACK_MOUSE_OVER,   //Added for the Back Button
        IMAGE_BUTTON_UNDO,              //Added for the undo button
        IMAGE_BUTTON_UNDO_MOUSE_OVER,   //Added for the undo Button
        IMAGE_BUTTON_STATS,
        IMAGE_BUTTON_STATS_MOUSE_OVER,
        IMAGE_BUTTON_TEMP_TILE,
        IMAGE_BUTTON_TEMP_TILE_MOUSE_OVER,
        IMAGE_CURSOR_ARROW,
        IMAGE_DECOR_TIME,      
        IMAGE_DECOR_MISCASTS,
        IMAGE_DIALOG_STATS,
        IMAGE_DIALOG_WIN,
        IMAGE_SPRITE_SHEET_CHARACTER_TILES,        
        IMAGE_TILE_BACKGROUND,
        IMAGE_TILE_BACKGROUND_SELECTED,
        IMAGE_TILE_BACKGROUND_MOUSE_OVER,
        IMAGE_WINDOW_ICON,
        
        LEVEL_SELECT_SCREEN_IMAGE_NAME,
        GAMEPLAY_SCREEN_IMAGE_NAME,
        
        //Home Screen Image Names
        PLAY_GAME_BUTTON_IMAGE_NAME,
        PLAY_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME,
        RESET_GAME_BUTTON_IMAGE_NAME,
        RESET_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME,
        VIEW_SETTINGS_BUTTON_IMAGE_NAME,
        VIEW_SETTINGS_MOUSE_OVER_BUTTON_IMAGE_NAME,
        VIEW_HELP_BUTTON_IMAGE_NAME,
        VIEW_HELP_MOUSE_OVER_BUTTON_IMAGE_NAME,
        QUIT_GAME_BUTTON_IMAGE_NAME,
        QUIT_GAME_MOUSE_OVER_BUTTON_IMAGE_NAME,
        
        HOME_BUTTON_IMAGE_NAME,
        HOME_MOUSE_OVER_BUTTON_IMAGE_NAME,
        RIGHT_BUTTON_IMAGE_NAME,
        RIGHT_MOUSE_OVER_BUTTON_IMAGE_NAME,
        LEFT_BUTTON_IMAGE_NAME,
        LEFT_MOUSE_OVER_BUTTON_IMAGE_NAME,
        UP_BUTTON_IMAGE_NAME,
        UP_MOUSE_OVER_BUTTON_IMAGE_NAME,
        DOWN_BUTTON_IMAGE_NAME,
        DOWN_MOUSE_OVER_BUTTON_IMAGE_NAME,
        //Home screen button types
//        PLAY_GAME_BUTTON_TYPE,
//        RESET_GAME_BUTTON_TYPE,
//        VIEW_SETTINGS_BUTTON_TYPE,
//        QUIT_GAME_BUTTON_TYPE,
        
        /* GAME TEXT */
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_LABEL_STATS_ALGORITHM,
        TEXT_LABEL_STATS_GAMES,
        TEXT_LABEL_STATS_WINS,
        TEXT_LABEL_STATS_PERFECT_WINS,
        TEXT_LABEL_STATS_FASTEST_PERFECT_WIN,
        TEXT_PROMPT_EXIT,
        TEXT_TITLE_BAR_GAME,
        TEXT_TITLE_BAR_ERROR,
        TEXT_HELP,
        
        /* AUDIO CUES */
        AUDIO_CUE_BAD_MOVE,
        AUDIO_CUE_CHEAT,
        AUDIO_CUE_DESELECT_TILE,
        AUDIO_CUE_GOOD_MOVE,
        AUDIO_CUE_SELECT_TILE,
        AUDIO_CUE_UNDO,
        AUDIO_CUE_WIN,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN,
        
        /* TILE LOADING STUFF */
        LEVEL_OPTIONS,
        LEVEL_IMAGE_OPTIONS,
        LEVEL_MOUSE_OVER_IMAGE_OPTIONS        
    }
    
}
