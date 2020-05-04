package AutoGarcon; 
import java.util.Map;
import java.util.HashMap; 



/**
 * Track open orders in memory for dynamic adding of items before
 * sending to the database. 
 * @author Tyler Beverley
 *
 * this class follows the singleton pattern,
 * thus only one copy of it will exist at a time. 
 */
public class OrderTracker{


    private Map<UniqueTable, Order> orders; 
    private static OrderTracker instance = null;  

    public static OrderTracker getInstance(){
        if( instance == null ){
            OrderTracker tracker = new OrderTracker(); 
            instance = tracker;  
            return tracker; 
        }
        else{
            return instance; 
        }
    }

    private OrderTracker(){
        this.orders = new HashMap<UniqueTable, Order>();
    }

    public void addOrder(UniqueTable table , Order order ){
        this.orders.put( table, order ); 
    }

    public void addOrder( int restaurantID, int tableNum, Order order ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum);
        this.orders.put( table, order ); 
    }

    /**
     * getOrder: gets the open order for the specifed restaurant and table number.  
     * @param restaurantID 
     * @param tableNum
     * @return Order - the open order for the specifed table if there is one. 
     * @return Null - if there is no open order for the specifed table. 
     */
    public Order getOrder( int restaurantID, int tableNum ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum ); 
        return this.orders.get( table ); 
    }
    
    public Order getOrder( UniqueTable table ){
        return this.orders.get(table); 
    }


    /**
     * completeOrder: removes the specifed order from the map of open orders.
     * @param restaurantID 
     * @param tableNum
     */
    public void completeOrder( int restaurantID, int tableNum ){
        UniqueTable table = new UniqueTable( restaurantID, tableNum ); 
        this.orders.remove( table );  
    }

    public void completeOrder( UniqueTable table ){
        this.orders.remove( table );  
    }

}
