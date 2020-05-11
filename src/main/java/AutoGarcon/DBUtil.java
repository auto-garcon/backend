package AutoGarcon; 
import java.sql.*; 
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays; 

/**
 * DBUtil: Utility functions for interacting with the datbase.
 * @author Tyler Beverley
 * @author Tyler Reiland
 * @param DB_USER - System Property that holds username for the database.
 * @param DB_PASS - System Property that holds the Password for the database.
 *
 * Enviroment variables DB_USER and DB_PASS hold the db creditentials.
 * HOST_NAME is used to determine if the program is running on the host.
 *
 * connection string = protocol//user:password@[hosts][/database][?properties]
 * Note that the connection obj is not thread safe. So for now we will create 
 * a new one everytime. 
 */
public class DBUtil {

    public final static String HOST_URL = "auto-garcon-database.cd4hzqa9i8mi.us-east-1.rds.amazonaws.com";
    private Connection connection;


    /** getRestaurant = gets the restaurant with the specified restaurantID.
     * @param restaurantID 
     * @return ResultSet - SQL result representing feilds for a restaurant. 
     */
    public static ResultSet getRestaurant( int restaurantID ) {

        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;
        try {
            stmt = c.prepareCall("{call GetRestaurantByID(?)}" ); 
            stmt.setInt( "rID", restaurantID );  
            result = stmt.executeQuery();  
            result.beforeFirst(); 
            return result;

        } catch( SQLException e ){
            System.out.printf("Failed to query for restaurant. restaurantID %d .\n" +
                    "Exception: %s\n", restaurantID, e.toString() );
        }
        return result; 
    }

    public static ResultSet getTable( int restaurantID, int tableNum ){

        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try{
            stmt = c.prepareCall("{call GetTableID(?, ?)}" ); 
            stmt.setInt( "rID", restaurantID );
            stmt.setInt( "tableNum", tableNum );  
            result = stmt.executeQuery();  
            result.beforeFirst(); 
            return result; 
        } catch( SQLException e ){
            System.out.printf("Failed to query for table. restaurantID %d .\n" +
                    "Exception: %s\n", restaurantID, e.toString() );
        }
        return result;
    }

    public static ResultSet getTable( String alexaID ){

        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try{
            stmt = c.prepareCall("{call GetTableByAlexaID(?)}"); 
            stmt.setNString("aID", alexaID ); 

            result = stmt.executeQuery(); 
            result.beforeFirst(); 

        } catch( SQLException e ){
            System.out.printf("Failed to exectue getTablesByAlexaID stored procedure.\n" +
                    "Exception: " + e.toString() );
        }

        return result;
    }


    public static ResultSet setAlexaIDForTable( int tableID, String alexaID ){

        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;
        return null;
    }


    /**
     * getMenu: Gets all the menus offered by a restaurant.
     * @param restaurantID the id of the restaurant.
     * @return SQL result set containing menu data.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static ResultSet getMenus( int restaurantID ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetMenusByRestaurantId(?)}" ); 
            stmt.setInt( "id", restaurantID );  
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetMenusByRestaurantId stored procedure.\n" +
                    "Exception: " + e.toString() );
        }
        return result;
    }

    /**
     * getAvailableMenus: Gets all the menus offered by a restaurant at the current time
     * @param restaurantID the id of the restaurant.
     * @param curTime current time in integer form
     * @return SQL result set containing menu data.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static ResultSet getAvailableMenus( int restaurantID, int curTime ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetAccessibleMenus(?, ?)}" ); 
            stmt.setInt( "rID", restaurantID );
            stmt.setInt( "currentTime", curTime);
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetMenusByRestaurantId stored procedure.\n" +
                    "Exception: " + e.toString() );
        }
        return result;
    }

    /**
     * getOrder: Gets all off the fields for an order by an ID.
     * @param orderID the id of the order.
     * @return SQL result set containing order data.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static ResultSet getOrder( int orderID ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetOrderByID(?)}" ); 
            stmt.setInt( "oID", orderID );  
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetOrderByID stored procedure.\n" +
                    "Exception: " + e.toString() );
        }
        return result;
    }

    /**
     * getOrder: Gets all off the fields for all orders a user made within 24 hours.
     * @param userID the id of the user.
     * @return SQL result set containing order data.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static ResultSet getOrdersWithin24Hours( int userID ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetOrdersInPast24Hours(?)}" ); 
            stmt.setInt( "uID", userID );  
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetOrderByID stored procedure.\n" +
                    "Exception: " + e.toString() );
        }
        return result;
    }

    /**
     * getOrdersForRestaurant: Gets all off the fields for every order at a restaurant.
     * @param restaurantID the id of the restaurant.
     * @return SQL result set containing order data.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static ResultSet getOrdersForRestaurant( int restaurantID ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetOrdersForRestaurant(?)}" ); 
            stmt.setInt( "rID", restaurantID );  
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetOrderByID stored procedure.\n" +
                    "Exception: " + e.toString() );
        }
        return result;
    }

    /**
     * getOrder: Gets a restaurant ID from a table ID
     * @param tableID the id of the table.
     * @return SQL result set containing restaurant ID.
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static int getRestaurantByTable( int tableID ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetRestaurantByTable(?)}" ); 
            stmt.setInt( "tID", tableID );  
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetRestaurantByTable stored procedure.\n" +
                    "Exception: " + e.toString() );
        }

        try{
            result.next(); 
            return result.getInt("restaurantID");
        } catch (SQLException e){
            System.out.printf("Failed to get next row in result set.\n Exception: %s\n", e.toString() );
            return -1;
        }
    }

    /**
     * getTableID: Gets a unique tableID from a restaurantID and table number
     * @param restaurantID the id of the restaurant.
     * @param tableNumber the table number (not unique)
     * @return the table ID that corresponds, or -1 if none found
     *
     * Result set's cursor will start just before the first row.
     * So use ResultSet.next() to get to the first row.
     */
    public static int getTableID( int restaurantID, int tableNumber ){
        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetTableID(?, ?)}" ); 
            stmt.setInt( "rID", restaurantID );
            stmt.setInt( "tableNum", tableNumber);
            
            result = stmt.executeQuery(); 
            result.beforeFirst(); 
        } catch( SQLException e ){
            System.out.printf("Failed to exectue GetRestaurantByTable stored procedure.\n" +
                    "Exception: " + e.toString() );
        }

        try{
            result.next(); 
            return result.getInt("tableID");
        } catch (SQLException e){
            System.out.printf("Failed to get next row in result set.\n Exception: %s\n", e.toString() );
            return -1;
        }
    }

    /**
     * getMenu: Gets a specifed menu from a specifed restaurant. 
     * @param restaurantID
     * @param menuID
     * @return SQL result representing the restaurantID, and menuID.
     */
    public static ResultSet getMenu( int menuID, int restaurantID ){

        ResultSet menus;
        boolean hasResult = false ;
        menus = getMenus( restaurantID );

        if( menus == null){
            hasResult = false;
            System.out.printf("Failed to find menuid: %d.\n", menuID);
        }

        int resultID; 
        try{
            hasResult = menus.next(); 
        } catch (SQLException e){
            System.out.printf("Failed to get next row in result set.\n Exception: %s\n", e.toString() );
            return null;
        }

        while( hasResult ){
            try {
                resultID = menus.getInt( "menuID" ); 
                if( resultID == menuID ){
                    return menus;
                } else {
                    hasResult = menus.next();
                }
            } catch (SQLException e ){
                System.out.printf("SQL Excpetion when trying to get next row in result set.\n" +
                        "Exception: " + e.toString() );
                System.exit(1);
            }
        }
        return menus;
    }

    /**
     * saveRestaurant: saves restaurant info to the database.
     * @param restaurant - restaurant object to save to the database. 
     *
     * @return true if the restaurant was saved to the database correctly. 
     * false if otherwise. 
     */
    public static boolean saveRestaurant( Restaurant restaurant ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result; 

        try{
            stmt = c.prepareCall("{call CreateNewRestaurant( ?,?,?,?,?,?,?,? )}"); 
            stmt.setNString("rName", restaurant.getName() ); 
            stmt.setNString("rDescr", restaurant.getDescription() ); 
            stmt.setNString("rAddress", restaurant.getAddress() ); 
            stmt.setObject("salesTax", restaurant.getSalesTax(), Types.DECIMAL, 2 ); 
            stmt.setNString("city", restaurant.getCity() ); 
            stmt.setNString("state", restaurant.getState() );  
            stmt.setInt("rZip", restaurant.getZip() ); 
            stmt.setNString("rCountry", restaurant.getCountry() ); 

            result = stmt.executeQuery(); 
            int restaurantID = result.getInt("newRestaurantID"); 
            return true; 
        }
        catch( SQLException e ) {
            System.out.printf("SQL Exception while executing CreateNewMenu.\n" + 
                    "Exception: %s\n", e.toString() 
            );
        }
        return false; 
    }

    /**
     * saveMenu: Saves the passed menu object to the database. 
     * Inserting into the database will give us a menuID to use, so 
     * this function will get that ID, and save it. 
     * It will save all fields, including any number of time ranges that are
     * included in the menu object. 
     * @param menu Menu object to be saved to the database. 
     */
    public static void saveMenu( Menu menu ){

        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result; 
        int menuID; 
        
        try {
            stmt = c.prepareCall("{call CreateNewMenu(?, ?, ?, ?, ?)}");
            stmt.setInt( "mStatus", menu.getStatus() ); 
            stmt.setInt("restaurantID", menu.getRestaurantID() ); 
            stmt.setNString("menuName", menu.getName() ); 
            stmt.setInt("startTime", menu.getTimeRanges()[0].getStartTime()); 
            stmt.setInt("endTime", menu.getTimeRanges()[0].getEndTime() );  

            result = stmt.executeQuery(); 
            result.next(); 
            menuID = result.getInt( "menuID");
            menu.setMenuID( menuID ); 
        }
        catch(SQLException e){ 
            System.out.printf("SQL Exception while executing CreateNewMenu.\n" + 
                    "Exception: %s\n", e.toString() );
        }
    }

    /**
     * saveMenuItem - saves the menuItem to the database.   
     * @param menuID - the menu that contains the menuItem. 
     * @param restaurantID - the restaurant associated with the restaurant.  
     * @param menuItem - the menuItem object to add to the database. 
     */
    public static boolean saveMenuItem( int menuID, int restaurantID, MenuItem menuItem ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result; 
        ResultSet result2; 

        try {
            stmt = c.prepareCall( "{call CreateNewMenuItem(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}" ); 
            stmt.setInt("mID", menuID ); 
            stmt.setNString("iName", menuItem.getName()); 
            stmt.setString("idesc", menuItem.getDescription()); 
            stmt.setString("iCategory", menuItem.getCategory()); 
            stmt.setInt("calories", menuItem.getCalories() ); 

            List<MenuItem.Allergen> allergens = Arrays.asList(menuItem.getAllergens());
            if( allergens.contains( MenuItem.Allergen.MEAT ) ){
                stmt.setInt("iMeat", 1);  
            } else { 
                stmt.setInt("iMeat", 0); 
            }

            if( allergens.contains( MenuItem.Allergen.DAIRY ) ){
                stmt.setInt("iDairy", 1);
            } else {
                stmt.setInt("iDairy", 0); 
            }

            if( allergens.contains( MenuItem.Allergen.GLUTEN ) ){
                stmt.setInt("iGluten", 1); 
            } else {
                stmt.setInt("iGluten", 0); 
            }

            if( allergens.contains( MenuItem.Allergen.NUTS ) ){
                stmt.setInt("iNuts", 1); 
            } else {
                stmt.setInt("iNuts", 0); 
            }

            if( allergens.contains( MenuItem.Allergen.SOY ) ){
                stmt.setInt("iSoy", 1); 
            } else {
                stmt.setInt("iSoy", 0); 
            }

            stmt.setObject("iPrice",  menuItem.getPrice(), Types.DECIMAL, 2 ); 
            //stmt.registerOutParameter("menuItemID", Types.INTEGER); 

            
            //createdMenuID is in second result set...
            result = stmt.executeQuery(); 
            stmt.getMoreResults(); 
            result2 = stmt.getResultSet();
            result2.next();

            //get output param 
            int menuItemID = result2.getInt("createdMenuItemID"); 
            menuItem.setItemID( menuItemID ); 

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing CreateNewMenuItem.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
        return true; 
    }

    /**
     * getMenuTimes: gets the menuTimes for the specified menuID. 
     * Calls the GetMenuTimes stored procedure. 
     * @param menuID the menuID to get menu times for. 
     */
    public static ResultSet getMenuTimes( int menuID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result = null;

        try { 
            stmt = c.prepareCall("{call GetMenuTimes(?)}"); 
            stmt.setInt("mID", menuID ); 
            result = stmt.executeQuery(); 
            result.beforeFirst();  

        } catch (SQLException e){
            System.out.printf("SQL Exception while executing GetMenuTimes.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return result; 
    }

    /**
     * getMenuItems: gets the menu Items associated with a menuID. 
     * @param menuID
     * @return result set containing tuples of menu items.
     */
    public static ResultSet getMenuItems( int menuID ) {

        ResultSet result = null; 
        Connection c = connectToDB(); 
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetMenuItemByMenuId(?)}" ); 
            stmt.setInt( "id", menuID);  

            result = stmt.executeQuery(); 
            result.beforeFirst(); 

        } catch (SQLException e){
            System.out.printf("SQL Exception while executing GetMenuItemsByMenuId\n" + 
                    "Exception: %s\n", e.toString()); 
        }
        return result;
    }



    /**
     * getOrderItems: gets the order Items associated with a orderID. 
     * @param orderID
     * @return result set containing tuples of order items.
     */
    public static ResultSet getOrderItems( int orderID ) {

        ResultSet result = null; 
        Connection c = connectToDB(); 
        CallableStatement stmt;

        try { 
            stmt = c.prepareCall("{call GetAllOrderItemsFromOrder(?)}" ); 
            stmt.setInt( "myOrderID", orderID);  

            result = stmt.executeQuery(); 
            result.beforeFirst(); 

        } catch (SQLException e){
            System.out.printf("SQL Exception while executing GetAllOrderItemsFromOrder\n" + 
                    "Exception: %s\n", e.toString()); 
        }
        return result;
    }

    /**
     * getUserID: gets the userID of the specified user object.
     *
     * finds the specifed user by userID. 
     * @param user - user object with an unknown userID. 
     */
    public static int getUserID( User user ){
        ResultSet result = null; 
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        int userID = -1; 

        try{
            stmt = c.prepareCall("{call GetUserIdByEmail(?)}" ); 
            stmt.setNString("emailAddress", user.getEmail() );
            result = stmt.executeQuery(); 

            result.next(); 
            userID = result.getInt("userID"); 
        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing AddUser.\n" +
                    "Exception: %s\n", e.toString() );
            userID = -1; 
        }
        return userID; 
    }


    public static boolean addUser(User user) {

        ResultSet result = null;
        Connection c = connectToDB();
        CallableStatement stmt;

        try {
            stmt = c.prepareCall("{call CreateUser(?,?,?,?)}");
            stmt.setNString("firstName", user.getFirstName());
            stmt.setNString("lastName", user.getLastName());
            stmt.setNString("email", user.getEmail());

            result = stmt.executeQuery();
            result.next();
            int userID = result.getInt("newUserID"); 
            user.setUserID( userID ); 
            return true;

        } catch (SQLException e) {
            System.out.printf("SQL Exception while executing AddUser.\n" +
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * saveOrder - saves the order and all of its order items to the database.   
     * @param order - a full order containing all of its menu items
     */
    public static boolean saveOrder( Order order ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result;

        try {
            stmt = c.prepareCall( "{call CreateNewOrder(?, ?)}" ); 
            stmt.setInt("tableID", order.getTableID() ); 
            stmt.setInt("customerID", order.getCustomerID()); 
            
            //initialize order in DB and store the new orderID
            result = stmt.executeQuery(); 
            result.next();
            int orderID = result.getInt("newOrderID"); 

            order.setOrderID( orderID ); 

            //add all orderitems to order
            ArrayList<OrderItem> orderItems = order.getOrderItems();
            for( OrderItem orderItem : orderItems ){
                stmt = c.prepareCall( "{call AddItemToOrder(?, ?, ?, ?, ?)}" );
                stmt.setInt("orderIDToAddTo", orderID);
                stmt.setInt("menuItemIDToAdd", orderItem.getMenuItemID());
                stmt.setInt("menuID", orderItem.getMenuID());
                stmt.setInt("quantityToAdd", orderItem.getQuantity());
                stmt.setString("commentsToAdd", orderItem.getComments());
                stmt.executeQuery();
            }

            //complete order
            stmt = c.prepareCall( "{call CompleteOrder(?)}" );
            stmt.setInt("oID", orderID);
            stmt.executeQuery();

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing saveOrder.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
        return true; 
    }

    /**
     * markOrderReady - marks an order ready to go out to a table  
     * @param orderID - the id of the order that is now ready
     */
    public static boolean markOrderReady( int orderID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call MarkOrderReady(?)}" ); 
            stmt.setInt("oID", orderID); 
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing markOrderReady.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }
    
    /**
     * addFavoriteRestaurant - adds a favorite restaurant for a user  
     * @param userID - the id of the user
     * @param restaurantID - the id of the restaurant
     */
    public static boolean addFavoriteRestaurant( int userID, int restaurantID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call AddFavoriteRestaurant(?, ?)}" ); 
            stmt.setInt("uID", userID);
            stmt.setInt("rID", restaurantID); 
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing addFavoriteRestaurant.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * removeFavoriteRestaurant - removes a favorite restaurant for a user  
     * @param userID - the id of the user
     * @param restaurantID - the id of the restaurant
     */
    public static boolean removeFavoriteRestaurant( int userID, int restaurantID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call RemoveFavoriteRestaurant(?, ?)}" ); 
            stmt.setInt("uID", userID);
            stmt.setInt("rID", restaurantID); 
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing removeFavoriteRestaurant.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * removeMenu - removes a menu from the database
     * @param menuID - the id of the menu
     */
    public static boolean removeMenu( int menuID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call RemoveMenu(?)}" ); 
            stmt.setInt("mID", menuID);
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing removeMenu.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * removeMenuItem - removes a menu item from all menus
     * @param itemID - the id of the item
     */
    public static boolean removeMenuItem( int itemID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call RemoveMenuItem(?)}" ); 
            stmt.setInt("iID", itemID);
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing removeMenuItem.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * removeMenuItemFromMenu - removes a menu item from the specified menu
     * @param itemID - the id of the item
     * @param menuID - the id of the menu
     */
    public static boolean removeItemFromMenu( int itemID, int menuID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 

        try {
            stmt = c.prepareCall( "{call RemoveItemFromMenu(?, ?)}" ); 
            stmt.setInt("iID", itemID);
            stmt.setInt("mID", menuID);
            
            //return true if succeded
            stmt.executeQuery(); 
            return true;

        }
        catch( SQLException e ){
            System.out.printf("SQL Exception while executing removeItemFromMenu.\n" + 
                    "Exception: %s\n", e.toString() );
            return false; 
        }
    }

    /**
     * getFavoriteRestaurants - gets the favorite restaurants for a user
     * @param userID - the id of the user
     */
    public static ResultSet getFavoriteRestaurants( int userID ){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result = null;

        try {
            stmt = c.prepareCall( "{call GetFavoriteRestaurants(?)}" ); 
            stmt.setInt("inputUserID", userID);
            
            //return response formatted in JSON
            result = stmt.executeQuery(); 
            return result;

        }
        catch( Exception e ){
            System.out.printf("SQL Exception while executing getFavoriteRestaurants.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return result;
    }

    /**
     * getFavoriteRestaurants - gets the favorite restaurants for a user
     * @param userID - the id of the user
     */
    public static ResultSet getAllRestaurants(){
        Connection c = connectToDB(); 
        CallableStatement stmt; 
        ResultSet result = null;

        try {
            stmt = c.prepareCall( "{call GetAllRestaurants()}" ); 
            
            //return response formatted in JSON
            result = stmt.executeQuery(); 
            return result;

        }
        catch( Exception e ){
            System.out.printf("SQL Exception while executing getFavoriteRestaurants.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return result;
    }

    public static Connection connectToDB(){

        Connection c = null;
        String baseURL, userName, password, connString;
        boolean isHost;

        baseURL = "jdbc:mysql://%s:%s@%s/AutoGarcon";
        userName = getUserName();
        password = getPass();
        isHost = isHost();


        if( isHost ){
            connString = String.format(baseURL, userName, password, HOST_URL);
        } else {
            //just use the hosted db for now.
            connString = String.format(baseURL, userName, password, HOST_URL);
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            c = DriverManager.getConnection( connString );

        } catch( SQLException e ){
            System.out.printf("SQL Exception while trying to connect to the Database.\n" +
                    "Exception: " + e.toString() + "\n" );
            System.exit(1);

        } catch( ClassNotFoundException e ){
            System.out.printf("Failed to find the Java Databse Driver for Mysql.\n");
            System.exit(1);
        }
        System.out.printf("Connected to the hosted database!\n");

        return c;
    }

    private static String getUserName() {
        String username = System.getProperty("DB_USER");
        if( username == null ){
            System.out.println("WARNING! DB_USER is null.");
        }
        return username;
    }

    private static String getPass(){
        String password = System.getProperty("DB_PASS");
        if( password == null ){
            System.out.println("WARNING! DB_PASS is null.");
        }
        return password;
    }

    private static boolean isHost(){
        if( System.getProperty("HOST_NAME") == null) {
            return false;
        } else {
            return true;
        }
    }
}
