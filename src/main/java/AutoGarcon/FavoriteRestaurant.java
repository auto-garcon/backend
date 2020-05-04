package AutoGarcon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class FavoriteRestaurant {

    private String firstName;
    private String lastName;
    private String restaurantName;
    private int menuID;
    private String menuName;
    private int startTime;
    private int endTime;
    private int restaurantId;
    private int userID;

    //default constructor
    public FavoriteRestaurant(){
        this.firstName = "";
        this.lastName = "";
        this.restaurantName = "Default Favorite Restaurant";
        this.menuID = -1;
        this.menuName = "";
        this.startTime = 0;
        this.endTime = 0;
        this.restaurantId = -1;
        this.userID = -1;
    }

    /**
     * FavoriteRestaurant: Create a FavoriteRestaurant object from sql result. 
     * @param qresult - the result of the SQL query.
     * @return An order object with data from the database.  
     */
    public FavoriteRestaurant( ResultSet qresult ){

        try {
            this.firstName = qresult.getString("firstName");
            this.lastName = qresult.getString("lastName");
            this.restaurantName = qresult.getString("restaurantName");
            this.menuID = qresult.getInt("menuID");
            this.menuName = qresult.getString("menuName");
            this.startTime = qresult.getInt("startTime");
            this.endTime = qresult.getInt("endTime");
            this.restaurantId = qresult.getInt("restaurantID");
            this.userID = qresult.getInt("userID");
        }
        catch( SQLException e){
            System.out.printf("Failed to get the required fields while creating a Order object.\n" + 
                   "Exception: %s.\n", e.toString() );
        }

    }

    /**
     * allOrders: Get all of the favorite restaurants
     * for the specified user. 
     * @param userID the user's id 
     * @return An array of menus. 
     */
    public static ArrayList<FavoriteRestaurant> allFavorites( int userID ){

        ResultSet favorites = DBUtil.getFavoriteRestaurants( userID ); 
        ArrayList<FavoriteRestaurant> list = new ArrayList<FavoriteRestaurant>();  
        boolean hasResult = false; 

        try{ 
            hasResult = favorites.next(); 
            while( hasResult ){
                    FavoriteRestaurant favorite = new FavoriteRestaurant( favorites ); 
                    list.add(favorite); 
                    hasResult = favorites.next(); 
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }

        return list; 
    }

}