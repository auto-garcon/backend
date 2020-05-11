package AutoGarcon; 
import java.util.Map;
import java.util.HashMap; 



/**
 * Track users at each table so Alexa's can use their accounts
 * @author Tyler Reiland
 *
 * this class follows the singleton pattern,
 * thus only one copy of it will exist at a time. 
 */
public class UserTracker{

    //mapping between unique table and user ID
    private Map<UniqueTable, Integer> users; 
    private static UserTracker instance = null;  

    public static UserTracker getInstance(){
        if( instance == null ){
            UserTracker tracker = new UserTracker(); 
            instance = tracker;  
            return tracker; 
        }
        else{
            return instance; 
        }
    }

    private UserTracker(){
        this.users = new HashMap<UniqueTable, Integer>();
    }

    public void addUser(UniqueTable table , int userID ){
        this.users.put( table, userID ); 
    }

    public void addUser( int restaurantID, int tableNum, int userID ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum);
        this.users.put( table, userID ); 
    }

    /**
     * getOrder: gets the most recent user at the specifed restaurant and table number.  
     * @param restaurantID 
     * @param tableNum
     * @return Integer - the userID for the current table if there is one
     * @return Null - if there is no user at the specifed table. 
     */
    public Integer getUserID( int restaurantID, int tableNum ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum ); 
        return this.users.get( table ); 
    }
    
    public Integer getUserID( UniqueTable table ){
        return this.users.get(table); 
    }


    /**
     * completeOrder: removes the specifed user from the map of users.
     * @param restaurantID 
     * @param tableNum
     */
    public void removeUser( int restaurantID, int tableNum ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum ); 
        this.users.remove( table );  
    }

    public void removeUser( UniqueTable table ){
        this.users.remove( table );  
    }

}
