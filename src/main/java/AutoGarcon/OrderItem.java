package AutoGarcon;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * Restaurant: This is a class that represents the information and functions
 * needed for an order.
 * 
 * @author Tyler Reiland
 */
public class OrderItem {

    private int orderItemID;
    private int menuItemID;
    private Integer menuID;
    private int quantity;
    private String comments;
    private int orderID;
    private float price;

    /**
     * Consruct an empty order item.
     */
    public OrderItem(){
        this.orderItemID = -1;
        this.menuItemID = -1;
        this.quantity = 0;
        this.comments = "Default OrderItem";
        this.orderID = -1;
        this.price = 0;
    }

    /** 
     * OrderItem - Create a OrderItem object from a sql query row. 
     * @param ResultSet -  a sql result set from a database query to turn into a OrderItem object. 
     * @return a full OrderItem object. 
     */
    public OrderItem( ResultSet rs, int orderID ){
        this.orderID = orderID; 

        try{ 
            this.orderItemID = rs.getInt( "orderItemID" );  
            this.menuItemID = rs.getInt("itemID");
            this.quantity = rs.getInt("quantity"); 
            this.comments = rs.getString("comments"); 
            this.price = rs.getFloat("price"); 
            
        } catch( SQLException e) {
            System.out.printf("Failed to get the required fields while creating a orderItem object.\n" + 
                    "Exception: %s\n", e.toString() );
        }
    }

	public static ArrayList<OrderItem> orderItems(int orderID) {
        ResultSet rs = DBUtil.getOrderItems( orderID ); 
        ArrayList<OrderItem> result = new ArrayList<OrderItem>(); 

        try { 
            while( rs.next() ){
                OrderItem oItem = new OrderItem( rs, orderID ); 

                result.add( oItem ); 
            }
        } catch( SQLException e ){
            System.out.printf("Failed to get next row in result set.\n Exception: %s\n", e.toString() );
            return null; 
        }

        return result;
    }
    
    /**
     * orderItemFromJson - create a orderItem from a JSON string. 
     * @param body - the string containing the orderItem in JSON format. 
     * @return a OrderItem with the feilds conatined from the JSON string. 
     */
    public static OrderItem orderItemFromJson( String body ) {
        Gson gson = new Gson(); 
        OrderItem item = new OrderItem(); 

        try { 
            item = gson.fromJson( body, OrderItem.class );
        } catch( JsonSyntaxException e ){
            System.out.printf("Failed to deserialze the request body into a OrderItem object.\n" + 
                    "Request body: %s\n. Exception: %s\n", body, e.toString() );
        }
        return item; 
    }

    public int getOrderItemID() {
        return this.orderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        this.orderItemID = orderItemID;
    }

    public int getMenuItemID() {
        return this.menuItemID;
    }

    public void setMenuItemID(int menuItemID) {
        this.menuItemID = menuItemID;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return this.comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getOrderID() {
        return this.orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getMenuID() {
        return this.menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

}