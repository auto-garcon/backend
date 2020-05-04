package AutoGarcon;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import javax.swing.text.html.Option;
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
        this.orderID = -1;
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

    public static Order orderFromJson( String body ){

        Gson gson = new Gson(); 
        Order order = new Order(); 

        try { 
            //TODO: Figure out why orderItems arent being parsed
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
        return DBUtil.saveOrder( this ); 
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
