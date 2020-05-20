package AutoGarcon; 
import java.io.File;
import java.io.FileOutputStream; 
import java.io.IOException;
import java.io.InputStream; 
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64; 
import com.google.gson.JsonElement; 
import com.google.gson.JsonPrimitive;



/**
 * ImageUtil: This class contains utility function 
 * retaining to the handling of images for menus and menu items. 
 *
 * @author Tyler Beverley
 * @version 0.1
 * @since 4/23/20
 */
public class ImageUtil { 


    public static final String basePath = "images/";
    public static final String defaultImage = "images/default.jpg"; 


    /**
     * saveImage: Saves an image file to the server given an input stream.
     * @param filename - the name to give the file. 
     * @param is - an InputStream of image bytes. 
     * @return True if saved correctly, false otherwise. 
     */
    public static boolean saveImage( String filename, InputStream is ){
        String path = String.format( basePath + filename );
        File f = new File( path ); 

        try{
            Files.copy( is, f.toPath(), StandardCopyOption.REPLACE_EXISTING ); 
            return true; 
        } catch( IOException ioe ){
            System.out.printf("IOException while trying to save an image.\n" + 
                    "Exception: %s\n", ioe.toString() );
            return false; 
        }
    }

    /**
     * saveImage: Saves an image file to the server given a byte array. 
     * @param menuID - the menuID of the image to be saved. 
     * @param menuItemID - the menuItemID of the image to be saved. 
     * @param bytes - the bytes of the image to be saved. 
     * @return File - The image file that was saved to the filesystem.
     *
     * @Deprecitaed 
     */
    public static File saveImage( int menuID, int menuItemID, byte[] bytes ){

        String path = String.format( basePath + "%d/%d", menuID, menuItemID );
        createMenuFolder( menuID ); 
        File image = new File( path  ); 
        FileOutputStream fos = null; 

        try{ 
            fos = new FileOutputStream( image ); 
            fos.write( bytes );
        } catch (IOException ioe){
            System.out.printf("IOException while trying to save an image.\n" + 
                    "Exception: %s\n", ioe.toString() );
        }
        return image; 
    }

    /**
     * getImage: Gets a specifed image from the filesystem.
     * @param menuID - menuID of the image to get. 
     * @param menuItemID - itemID of the image to get. 
     *
     * @return File - The image file requested. 
     * @depreciated
     */
    public static File getImage( int menuID, int menuItemID ) {

        String path = String.format( basePath + "/%d", menuItemID );
        File image = new File( path ); 

        if( !image.exists() ){
            System.out.printf("Failed to get the requested image for: " + 
                    "MenuID: %d, MenuItem: %d\n", menuID, menuItemID 
            ); 
        }
        return image; 
    }

    /**
     * getImageURL: gets the URL where you can download the image. 
     * @param menuID - the menuID of the image you want a URL for. 
     * @param menuItemID - the itemID of the image that you want to a URL for. 
     *
     * @return - The autogarcon url for retreiving the specifed image from the API. 
     */
    public static String getMenuItemImageURL( int menuItemID ){


        String path = String.format( "https://autogarcon.live/images/"
                + "menuitem.%d.jpg",  menuItemID 
        );
        File f = new File( path ); 
        if( f.exists() ){
            return path; 
        }
        else {
            return defaultImage; 
        }
    }

    /**
     * getImageURL: gets the URL where you can download the image. 
     * @param menuID - the menuID of the image you want a URL for. 
     * @return - The autogarcon url for retreiving the specifed image from the API. 
     */
    public static String getMenuImageURL( int menuID ){
        String path = String.format( "https://autogarcon.live/images/"
                + "menu.%d.jpg", menuID 
        );
        
        File f = new File( path ); 
        if( f.exists() ){
            return path; 
        }
        else {
            return defaultImage; 
        }
    }

    /**
     * getImageURL: gets the URL where you can download the image. 
     * @param menuID - the menuID of the image you want a URL for. 
     * @return - The autogarcon url for retreiving the specifed image from the API. 
     */
    public static String getRestaurantImageURL( int restaurantID ){
        String path = String.format( "https://autogarcon.live/images/"
                + "restaurant.%d.jpg", restaurantID 
        );

        File f = new File( path ); 
        if( f.exists() ){
            return path; 
        }
        else {
            return defaultImage; 
        }
    }


    /**
     * createMenuFolder: create a folder for a new menu to store images in. 
     * @param menuID - menuID of the images that will be stored in the folder. 
     * @depreciated 
     */
    public static void createMenuFolder( int menuID ){
        File dir = new File( basePath + Integer.toString( menuID ) );
        boolean result = dir.mkdir(); 
    }

    /**
     * deserialize: get the image bytes from a json element. 
     * @param json - the json element containing the image data.
     * @return byte[] - the image data. 
     */
    public static byte[] deserialize( JsonElement json ) {
        Base64.Decoder decoder = Base64.getDecoder(); 
        return decoder.decode(json.getAsString() );
    }
     
    /**
     * deserialize: get the image bytes from a string of bytes.  
     * @param bytes - the string to deserialize into bytes.    
     */
    public static byte[] deserialize( String bytes ){
        Base64.Decoder decoder = Base64.getDecoder(); 
        return decoder.decode( bytes );
    }

    /**
     * serialize: serialize the image bytes into a Json element.
     * @param src - Image bytes to serialize into a json element.
     *
     * @return JsonElement - the resulting JSON containing the image data.  
     */
    public static JsonElement serialize(byte[] src ){
        Base64.Encoder encoder = Base64.getEncoder(); 
        return new JsonPrimitive(encoder.encodeToString(src));
    }
}
