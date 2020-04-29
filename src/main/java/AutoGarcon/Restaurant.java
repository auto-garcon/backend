package AutoGarcon;
import com.google.gson.Gson; 
import com.google.gson.JsonSyntaxException; 
import javax.swing.text.html.Option;
import java.sql.SQLException; 
import java.sql.ResultSet; 


/**
 * Restaurant: This is a class that represents
 *  the information and functions needed for a restaurant.
 * @author Sosa Edison
 * @author Tyler Beverley
 */
public class Restaurant {

    private int restaurantID;
    private String restaurantName;
    private int[] availableMenus;
    private transient Table[] restaurantTables;
    private String description; 
    private String address;  
    private String city; 
    private int zipCode;
    private String country; 
    private String state; 
    private int numTables; 


    /**
     * Restaurant - Creates a restaurant object. 
     * @param restaurantID
     * @param restaurantName 
     * @param restaurantAddress
     */
    public Restaurant(int restaurantID, String restaurantName, String restaurantAddress) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.address = restaurantAddress;
    }

    /**
     * Restaurant - empty restaurant constructor.  
     * Used for 1st step in json deserialization. 
     */
    public Restaurant(){
        this.restaurantID = -1; 
        this.restaurantName = "Default Restaurant"; 
        this.address = ""; 
    }

    public Restaurant( int restaurantID ){
        ResultSet result = DBUtil.getRestaurant( restaurantID ); 

        //if result is null return a default restaurant. 
        if( result == null ){
            this.restaurantID = -1; 
            this.restaurantName = "Default Restaurant"; 
            this.address = ""; 
        }

        try{
            result.next(); 
            this.restaurantID = result.getInt("restaurantID"); 
            this.restaurantName = result.getString("restaurantName");  
            this.description = result.getString("description"); 
            this.address = result.getString("address"); 
            this.city = result.getString("city");
            this.state = result.getString("state"); 
            this.zipCode = result.getInt("zipCode"); 
            this.country = result.getString("country"); 

        }
        catch( SQLException e ){
            System.out.printf("Failed to get the required fields while creating a restaurant Object.\n" + 
                   "Exception: %s.\n", e.toString() );

        }
    }

    public static Restaurant restaurantFromJson( String body){

        Gson gson = new Gson(); 
        Restaurant restaurant = new Restaurant(); 

        try { 
            restaurant = gson.fromJson( body, Restaurant.class );
        } catch( JsonSyntaxException e ){
            System.out.printf("Failed to deserialze the request body into a MenuItem object.\n" + 
                    "Request body: %s\n. Exception: %s\n", body, e.toString() );
        }
        return restaurant; 
    }


    public boolean isDefault(){
        return this.restaurantName.equals("Default Restaurant"); 
    }

    public boolean save(){
        return DBUtil.saveRestaurant( this ); 
        // save logo. 
    }

    public int getRestaurantID() {
        return this.restaurantID;
    }

    public int[] getAvailableMenus() { 
        return this.availableMenus; 
    }

    public Table[] getRestaurantTables() { 
        return this.restaurantTables; 
    }
    
    public String getName() {
        return this.restaurantName;
    }

    public String getDescription() {
        return this.description;  
    }

    public String getAddress() {
        return this.address; 
    }

    public String getCity() {
        return this.city; 
    }

    public String getState(){
        return this.state; 
    }

    public int getZip(){
        return this.zipCode; 
    }

    public String getCountry() {
        return this.country; 
    }

    public float getSalesTax(){
        return this.getSalesTax(); 
    }

    public int getNumTables() {
        return this.numTables; 
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("restaurantID: " + this.getRestaurantID() + "\n");
        str.append("restaurantName: " + this.getName()+ "\n");
        str.append("description: " + this.getDescription() + "\n" ); 
        str.append("restaurantAddress: " + this.getAddress() + "\n");
        str.append("city" + this.getState() + "\n"); 
        str.append("state: " + this.getState() + "\n"); 
        str.append("zipcode: " + Integer.toString(this.getZip()) ); 
        str.append("country: " + this.getCountry() ); 
        str.append("numTables; " + Integer.toString(this.getNumTables()) );
        return str.toString();
    }

} 
