package AutoGarcon;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
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
    private String primaryColor;
    private String secondaryColor;
    private Menu[] menus;
    private int numTables; 
    private float salesTax; 


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

    public Restaurant( int restaurantID, boolean withMenus){
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
            this.primaryColor = result.getString("primaryColor");
            this.secondaryColor = result.getString("secondaryColor");
            if(withMenus){
                int currentTime = Main.getCurrentTimestamp();
                this.menus = Menu.allAvailableMenus(this.restaurantID, currentTime);
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get the required fields while creating a restaurant Object.\n" + 
                   "Exception: %s.\n", e.toString() );

        }
    }

    public static ArrayList<Restaurant> getFavorites(int userID){
        ResultSet result = DBUtil.getFavoriteRestaurants( userID );
        boolean hasResult;
        ArrayList<Restaurant> list = new ArrayList<Restaurant>();  

        //if result is null return an empty list
        if( result == null ){
            return list;
        }

        try{
            hasResult = result.next();
            while(hasResult){
                //fill restaurant object
                Restaurant restaurant = new Restaurant();
                restaurant.restaurantID = result.getInt("restaurantID"); 
                restaurant.restaurantName = result.getString("restaurantName");  
                restaurant.description = result.getString("description"); 
                restaurant.address = result.getString("address"); 
                restaurant.city = result.getString("city");
                restaurant.state = result.getString("state"); 
                restaurant.zipCode = result.getInt("zipCode"); 
                restaurant.country = result.getString("country");
                restaurant.primaryColor = result.getString("primaryColor");
                restaurant.secondaryColor = result.getString("secondaryColor");
                restaurant.menus = Menu.allMenusWithoutItems(restaurant.restaurantID);
                
                //add to list
                list.add(restaurant);
                hasResult = result.next();
            }
            return list;
        }
        catch( SQLException e ){
            System.out.printf("Failed to get the required fields while creating a restaurant Object.\n" + 
                   "Exception: %s.\n", e.toString() );
            //return empty list
            return new ArrayList<Restaurant>();

        }
    }


    public static Restaurant[] getAllRestaurants(){
        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>(); 
        ResultSet result = DBUtil.getAllRestaurants(); 
        try{
            while( result.next() ){
                //fill restaurant object
                Restaurant restaurant = new Restaurant();
                restaurant.restaurantID = result.getInt("restaurantID"); 
                restaurant.restaurantName = result.getString("restaurantName");  
                restaurant.description = result.getString("description"); 
                restaurant.address = result.getString("address"); 
                restaurant.city = result.getString("city");
                restaurant.state = result.getString("state"); 
                restaurant.zipCode = result.getInt("zipCode"); 
                restaurant.country = result.getString("country");
                restaurant.primaryColor = result.getString("primaryColor");
                restaurant.secondaryColor = result.getString("secondaryColor");
                //restaurant.menus = Menu.allMenusWithoutItems(restaurant.restaurantID);
                restaurants.add(restaurant); 
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get the required fields while creating a restaurant Object.\n" + 
                   "Exception: %s.\n", e.toString() );
        }
        return restaurants.toArray( new Restaurant[ restaurants.size() ] );
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

    public int save(){
        this.restaurantID = DBUtil.saveRestaurant( this ); 
        return this.restaurantID;
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
        return this.salesTax; 
    }

    public int getNumTables() {
        return this.numTables; 
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return this.restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    public void setAvailableMenus(int[] availableMenus) {
        this.availableMenus = availableMenus;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public int getZipCode() {
        return this.zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setState(String state) {
        this.state = state;
    }

    public String getPrimaryColor() {
        return this.primaryColor;
    }

    public void setPrimaryColor(String primaryColor) {
        this.primaryColor = primaryColor;
    }

    public String getSecondaryColor() {
        return this.secondaryColor;
    }

    public void setSecondaryColor(String secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public Menu[] getMenus() {
        return this.menus;
    }

    public void setMenus(Menu[] menus) {
        this.menus = menus;
    }
    public void setNumTables(int numTables) {
        this.numTables = numTables;
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
