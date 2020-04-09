package AutoGarcon; 
import static spark.Spark.*;

import com.google.gson.*;
import spark.Request;
import spark.Response;
import spark.Route;

import java.lang.reflect.Type;


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
 */
public class Main {


    /*
     * Can implement custom 501 handling in the future. 
     */
    public static Object endpointNotImplemented( Request req, Response res ){
        res.status(501); 
        return "Endpoint Not Implemented"; 
    }

    public static Object serveStatic(Request req, Response res) {
        res.type("text/html");
        res.redirect("index.html", 201);
        return "";
    }



    public static Object addMenu( Request req, Response res) {
  
        Menu menu = Menu.menuFromJson( req.body() );   

        System.out.printf("Printing Incoming menu:\n %s\n", menu.toString());
        boolean saved = menu.save(); 

        if (!menu.isDefault() && saved) {
            res.status(200);
            return "Successfully recieved menu.";
        } else {
            res.status(500); 
            return "Error recieving menu"; 
        }
    }


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

    public static Object addUser( Request req, Response res) {

        User user = User.userFromJson( req.body() ); 
        boolean saved = user.save(); 

        if( !user.isDefault() && saved ){
            res.status(200); 
            return user;  
        } else {
            res.status(500); 
            return "Error receiving User"; 
        }
    }

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

    public static void initRouter(){

		get("/", Main::serveStatic);

        path("/api", () -> {
            path("/users", () -> {
                post("/newuser", "application/json", Main::addUser, new JsonTransformer() );
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
        });
    }


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



