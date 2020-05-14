package AutoGarcon;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.ResultSet; 


/**
 * Restaurant: This is a class that represents
 *  the information and functions needed for an order.
 * @author Tyler Reiland
 */
public class Order {

    public enum OrderStatus { 
        OPEN, 
        CLOSED, 
    }

    private int orderID;
    private int tableID;
    private int customerID;
    private Timestamp orderTime;
    private OrderStatus orderStatus;
    private float chargeAmount;
    private int restaurantID;
    private ArrayList<OrderItem> orderItems;


    /**
     * Order - Creates an order object. 
     * @param restaurantID
     * @param restaurantName 
     * @param restaurantAddress
     */

     /**
     * Order - empty order constructor.  
     * Used for 1st step in json deserialization. 
     */
    public Order(){
        //this.orderID = -1;
        this.tableID = -1;
        this.customerID = -1;
        this.orderTime = new Timestamp((long) 0.0);
        this.orderStatus = OrderStatus.OPEN;
        this.chargeAmount = (float) 0.0;
        this.restaurantID = -1;
        this.orderItems = new ArrayList<OrderItem>();
    }

    public Order( int orderID ){
        ResultSet result = DBUtil.getOrder( orderID ); 

        //if result is null return a default order. 
        if( result == null ){
            new Order();
            return;
        }

        try{
            result.next(); 
            this.orderID = result.getInt("orderID"); 
            this.tableID = result.getInt("tableID");  
            this.customerID = result.getInt("userID");
            this.orderTime = result.getTimestamp("orderTime");
            int statusInt = result.getInt("orderStatus");
            this.orderStatus = OrderStatus.values()[statusInt];
            this.chargeAmount = result.getFloat("chargeAmount");
            this.restaurantID = DBUtil.getRestaurantByTable(this.tableID);
            this.orderItems = OrderItem.orderItems(this.orderID);

        }
        catch( SQLException e ){
            System.out.printf("Failed to get the required fields while creating an order Object.\n" + 
                   "Exception: %s.\n", e.toString() );

        }
    }

    /**
     * Menu: Create an order object from sql result. 
     * @param qresult - the result of the SQL query.
     * @return An order object with data from the database.  
     */
    public Order( ResultSet qresult ){

        try {
            this.orderID = qresult.getInt("orderID");
            this.tableID = qresult.getInt("tableID");
            this.customerID = qresult.getInt("userID");
            this.orderTime = qresult.getTimestamp("orderTime");
            int statusInt = qresult.getInt("orderStatus"); 
            this.orderStatus = OrderStatus.values()[statusInt];
            this.chargeAmount = qresult.getFloat("chargeAmount");
            this.orderItems = OrderItem.orderItems(this.orderID);
        }
        catch( SQLException e){
            System.out.printf("Failed to get the required fields while creating a Order object.\n" + 
                   "Exception: %s.\n", e.toString() );
        }

    }

    /**
     * allOrders: Get all of the orders in an array 
     * for the specified user. 
     * @param userID the user to get orders for. 
     * @return An array of orders. 
     */
    public static ArrayList<Order> allOrders( int userID ){

        ResultSet orders = DBUtil.getOrdersWithin24Hours( userID ); 
        ArrayList<Order> list = new ArrayList<Order>();  
        boolean hasResult = false; 

        try{ 
            hasResult = orders.next(); 
            while( hasResult ){
                    Order order = new Order( orders ); 
                    list.add(order); 
                    hasResult = orders.next(); 
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }

        return list; 
    }

    /**
     * allOrdersForRestaurant: Get all of the orders in an array 
     * for the specified restaurant. 
     * @param userID the restaurant to get orders for. 
     * @return An array of orders. 
     */
    public static ArrayList<Order> allOrdersForRestaurant( int restaurantID ){

        ResultSet orders = DBUtil.getOrdersForRestaurant( restaurantID ); 
        ArrayList<Order> list = new ArrayList<Order>();  
        boolean hasResult = false; 

        try{ 
            hasResult = orders.next(); 
            while( hasResult ){
                    Order order = new Order( orders ); 
                    list.add(order); 
                    hasResult = orders.next(); 
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }

        return list; 
    }

    public static Order orderFromJson( String body ){

        Gson gson = new Gson(); 
        Order order = new Order(); 

        try { 
            order = gson.fromJson( body, Order.class );
            System.out.println(gson.toJson(order));

        } catch( JsonSyntaxException e ){
            System.out.printf("Failed to deserialze the request body into an Order object.\n" + 
                    "Request body: %s\n. Exception: %s\n", body, e.toString() );
            return new Order();
        }
        return order;
    }

    public boolean save(){
        if(this.orderItems.size() > 0){
            return DBUtil.saveOrder( this ); 
        } else {
            //don't save if there are no items in the order
            return false;
        }
    }

    public boolean initializeOrder(Order order){
        //initializes an order, sets the 2 fields necessary to add it to the database
        if(!order.isDefault()){
            this.customerID = order.customerID;
            this.tableID = order.tableID;
            return true;
        }
        return false;
    }


    public boolean isDefault(){
        return this.customerID == -1; 
    }

    public int getOrderID() {
        return this.orderID;
    }

    public int getTableID() {
        return this.tableID;
    }

    public int getCustomerID() {
        return this.customerID;
    }

    public Timestamp getOrderTime() {
        return this.orderTime;
    }

    public float getChargeAmount() {
        return this.chargeAmount;
    }

    public int getRestaurantID() {
        return this.restaurantID;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return this.orderItems;
    }

    public void addOrderItem( OrderItem item ){
        this.orderItems.add( item );
    }

    public void removeMenuItemFromOrder( int menuItemID ){
        OrderItem itemToRemove = null;
        for( OrderItem item  : this.orderItems ){
            if( item.getMenuItemID() == menuItemID ){
                itemToRemove = item;
                break;
            }
        }
        //if we found one to remove, remove it
        if(!(itemToRemove == null)){
            this.orderItems.remove(itemToRemove);
        }
    }

    public OrderStatus getOrderStatus() {
        return this.orderStatus;
    }
    
    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public void setOrderTime(Timestamp orderTime) {
        this.orderTime = orderTime;
    }

    public void setChargeAmount(float chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("orderID: " + this.getOrderID() + "\n");
        str.append("tableID: " + this.getTableID() + "\n");
        str.append("customerID: " + this.getCustomerID()+ "\n");
        str.append("orderTime: " + this.getOrderTime() + "\n" ); 
        str.append("chargeAmount: " + this.getChargeAmount() + "\n");
        str.append("restaurantID" + this.getRestaurantID() + "\n"); 
        str.append("orderItems:\n");
        for( int i = 0; i < this.orderItems.size(); i++ ){
            String item = String.format("item: %s,\n", this.orderItems.get(i).toString() );
            str.append( item );
        }
        return str.toString();
    }

} 
