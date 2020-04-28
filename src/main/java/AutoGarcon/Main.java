package AutoGarcon; 
import static spark.Spark.*;

import com.google.gson.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;
import java.io.OutputStream; 
import java.io.InputStream;
import java.io.IOException;
import java.io.File;
import java.nio.file.Files; 
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException; 
import javax.servlet.http.HttpServletResponse; 
import javax.imageio.ImageIO; 


/**
 * Main:  This class contains all of the logic for 
 * API routes and their respective handlers.
 * 
 * 
 * @author  Tyler Beverley, 
 * @author Sosa Edison  
 * @version 0.1
 * @since 2/24/20
 * @see <a href="https://github.com/auto-garcon/documentation/blob/master/APISpecification.md">Documentation</a>;  For API Endpoints. 
 * @see <a href="https://github.com/auto-garcon/backend">README</a>; For build instructions. 
 * 
 * You *can* refer to the documentation 
 * for information about this API, but as always, 
 * the code is the primary source of truth. 
 * Meaning that it's possible for the documentation to be out of date 
 * But the code will always be current. 
 *
 *
 * All of the route handlers will take in two paramaters, a request and a response object. 
 * These are from the Spark Java web framework, and are used to interact with 
 * the response and request sent to the API's endpoints. 
 *
 */
public class Main {


    /**
     * endpointNotImplemented: Default functionality for when an endpoint has not been implemeneted yet. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object endpointNotImplemented( Request req, Response res ){
        res.status(501); 
        return "Endpoint Not Implemented"; 
    }

    /**
     * serveStatic: Serve the web UI's files. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object serveStatic(Request req, Response res) {
        res.type("text/html");
        res.redirect("index.html", 201);
        return "";
    }

    public static Object serveImage( Request req, Response res ){
        res.redirect("images", 201);
        res.type("image/jpeg");
        return "";
    }

    /**
     * addMenu: Handler for /api/restaurant/:restaurantid/menu/add
     * Adds a menu to a database. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object addMenu( Request req, Response res) {
  
        Menu menu = Menu.menuFromJson( req.body() );   

        //System.out.printf("Printing Incoming menu:\n %s\n", menu.toString());
        boolean saved = menu.save(); 

        if (!menu.isDefault() && saved) {
            res.status(200);
            return "Successfully recieved menu.";
        } else {
            res.status(500); 
            return "Error recieving menu"; 
        }
    }


    /**
     * getAllMenu: Handler for /api/restaurant/:restaurantid/menu/
     * gets all the menus for the specified restaurant.  
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object getAllMenu( Request req, Response res ){

        try{ 
            int restaurantID = Integer.parseInt(req.params(":restaurantid")); 
            res.status(200); 
            return Menu.allMenus( restaurantID ); 
        } catch( NumberFormatException nfe){
            res.status(400); 
            return "Failed to parse restaurantID as an integer."; 
        }
    }

    /**
     * addUser: When a user is signing in, if they are not 
     * already added to the database, add them to the database.
     * @param User - User object. 
     * @param Response - Response object.  
     */
    public static Object addUser(User user, Response res ) {

        boolean saved = user.save(); 

        if( !user.isDefault() && saved ){
            res.status(200); 
            return user.getUserID();  
        } else {
            res.status(500); 
            return "Error receiving User"; 
        }
    }

    /**
     * signIn: Handler for /api/users/signin 
     * Check if the user is in hte database, and give back the userID. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object signIn( Request req, Response res ){

        User user = User.userFromJson( req.body() );
        int userID = DBUtil.getUserID( user ); 
        System.out.println( user.toString() ); 

        if(userID == -1 ){
            return addUser( user, res ); 
        }
        else {
            res.status(200); 
        }
        return userID; 
    }

    /**
     * getRestaurant: Handler for /api/restaurant/:restaurantid  
     * gets info about the specifed restaurantID.
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object getRestaurant( Request req, Response res ){

        try {
            int restaurantID = Integer.parseInt(req.params(":restaurantid")); 
            Restaurant restaurant = new Restaurant( restaurantID );  
            return restaurant; 

        } catch( NumberFormatException nfe){
            res.status(400); 
            return "Failed to parse restaurantID as an integer.";
        }
    }

    /**
     * addRestaurant: Handler for /api/restaurant/add
     * adds restaurant info to the databse.
     * @param Request - Request object. 
     * @param Response - Response object.  
     */
    public static Object addRestaurant( Request req, Response res ){
        Restaurant restaurant = Restaurant.restaurantFromJson( req.body() ); 

        if( !restaurant.isDefault() ){
            boolean saved = restaurant.save(); 
            if( saved ){
                res.status(200); 
                return "Successfully saved new restuarant"; 
            }
            else { 
                res.status(500); 
                return "Failed to save new restaurant"; 
            }
        }
        else {
            res.status(400); 
            return "Failed to parse out request for adding a restaurant"; 
        }
    }

    /**
     * getImage: Handler for /api/images/:menuid/:menuitemid 
     * gets the image associated with the specifed menuitem. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     *
     * This function writes the image bytes to the response's outputstream.
     */
    public static Object getImage( Request req, Response res ){
        
        res.raw().setContentType("image/png");
        byte[] data = null;

        try{
            int menuID = Integer.parseInt( req.params(":menuid") ); 
            int menuItemID = Integer.parseInt( req.params(":menuitemid") ); 

            HttpServletResponse raw = res.raw();
            res.header("Content-Disposition", "attachment; filename=image.jpg");
            //res.type("application/force-download");

            File f = ImageUtil.getImage( menuID, menuItemID );
            data = Files.readAllBytes( f.toPath() );
            raw.getOutputStream().write(data);
            raw.getOutputStream().flush();
            raw.getOutputStream().close();
            res.status(200); 
            return raw; 

        } catch( NumberFormatException nfe ){
            res.status(400); 
            return "Failed to parse paramaters an integer."; 
        }
        catch( IOException ioe ){
            res.status(500); 
            System.out.printf("Failed to retreive the requested image. " + 
                    "Exception: %s\n", ioe.toString()
            ); 
            return "IOException occured";
        }
    }

    /**
     * saveImage: Handler for /api/images/:menuid/:menuitemid (POST)
     * Saves an image to the server's file system given an image upload. 
     * @param Request - Request object. 
     * @param Response - Response object.  
     *
     * This function reads from the request's inputstream.
     */
    public static Object saveImage( Request req, Response res ){

        try{
            int menuID = Integer.parseInt( req.params(":menuid") ); 
            int menuItemID = Integer.parseInt( req.params(":menuitemid") ); 
            InputStream is = req.raw().getPart("uploaded_file").getInputStream();
            req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            boolean saved = ImageUtil.saveImage( menuID, menuItemID, is ); 
            if( saved ){
                return "File uploaded";
            }
            else{
                return "Failed to upload the image"; 
            }
        }
        catch( IOException ioe ){
            res.status(400); 
            System.out.printf("Failed to get the input stream from the request.\n" + 
                    "Exception: %s\n", ioe.toString()
            );
            return "Failed to get the input stream for the request.";
        }
        catch( ServletException se ){
            res.status(400); 
            System.out.printf("ServletException ? " + 
                    "Exception: %s\n", se.toString() 
            ); 
            return "Failed to get the multipart request config.";  
        }
        catch( NumberFormatException nfe ){
            res.status(400); 
            return "Failed to parse paramaters an integer."; 
        }
    }

    /**
     * initRouter: specifes all of the routes for the API. 
     */
    public static void initRouter(){

		get("/", Main::serveStatic);

        path("/api", () -> {
            path("/users", () -> {
                post("/signin", "application/json", Main::signIn, new JsonTransformer() );
                path("/:userid", () -> {
                    get("", Main::endpointNotImplemented );
                    get("/favorites", Main::endpointNotImplemented);
                    get("/orders", Main::endpointNotImplemented); 
                });
            });
            path("/restaurant", () -> {
                post("/add", Main::addRestaurant ); 
                path("/:restaurantid", () -> {
                    get("", Main::getRestaurant); 
                    get("/orders", Main::endpointNotImplemented);
                    get("/tables", Main::endpointNotImplemented); 
                    post("/sitdown",Main::endpointNotImplemented); 
                    post("/orders/submit", Main::endpointNotImplemented); 
                    post("/orders/complete", Main::endpointNotImplemented); 
                    path("/menu", () -> {
                        get("", Main::getAllMenu, new JsonTransformer() ); 
                        post("/add", "application/json", Main::addMenu); 
                        post("/remove", Main::endpointNotImplemented); 
                    });
                });
            });
            path("/images", () -> {
                get("/:menuitemid", Main::serveImage); 
                post("/:menuitemid", Main::saveImage); 
            });
        });
    }


    /**
     * startServer: Start the server on the specified port (443 for https).
     */
    public static void startServer() {

        port(80);
        // port(443); // HTTPS port
		staticFiles.location("/public/build");
        //secure("/home/ubuntu/env/keystore.jks","autogarcon", null, null); // HTTPS key configuration for spark
        initRouter(); 
        DBUtil.connectToDB();
    }

	public static void main(String[] args) {
        startServer(); 
	}
}



