package AutoGarcon;
import java.sql.ResultSet; 
import java.sql.SQLException; 
import java.util.ArrayList; 

public class Table {

    private int restaurantID; 
    private int tableNumber; 
    private User customer; 
    private String alexaID; 
    private Order currentOrder; 
    private int tableID; 

    public static Table tableFromResultSet( ResultSet rs ){

        Table table = null; 
        try{
            table = new Table( 
                    rs.getInt("restaurantID"),
                    rs.getInt("tableNumber"),
                    rs.getInt("tableID"), 
                    rs.getString("alexaID")
            );
        }
        catch( SQLException e){
            System.out.printf("Failed to get the required fields while creating a table Object.\n" + 
                   "Exception: %s.\n", e.toString() );
        }
        return table; 
    }


    /**
     * tableFromTableID: retreive the table object from the Databse
     * based on the specifed restaurantID and table number.. 
     * @param restaurantID
     * @param tableNum
     * @return the table object associated with the alexaID.
     */
    public static Table tableFromTableID( int restaurantID, int tableNum ){
        ResultSet rs = DBUtil.getTable( restaurantID, tableNum ); 
        try{
            rs.next();
        }
        catch(SQLException e){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return tableFromResultSet( rs ); 
    }

    /**
     * tableFromAlexaID: retreive the table object from the Databse
     * based on the specifed alexaID. 
     * @param alexaID  
     * @return the table object associated with the alexaID.
     */
    public static Table tableFromAlexaID( String alexaID ){
        ResultSet rs = DBUtil.getTable( alexaID ); 
        try{
            rs.next();
        }
        catch(SQLException e){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return tableFromResultSet( rs ); 
    }


    /**
     * getAllTables: gets all the tables associated with the restaurant.  
     * @param restaurantID
     * @return Table[] - an array of tables at the restaurant. 
     */
    public static Table[] getAllTables( int restaurantID ){

        ArrayList<Table> tables = new ArrayList<Table>();  
        ResultSet rs = DBUtil.getAllTables( restaurantID ); 
        try{
            while( rs.next() ){
                Table table = tableFromResultSet( rs );
                tables.add( table ); 
            }
        }
        catch( SQLException e ){
            System.out.printf("Failed to get next row in result set.\n" + 
                    "Exception: %s\n", e.toString() );
        }
        return tables.toArray( new Table[ tables.size() ] ); 
    }

    /**
     * Table: create a table obj from restaurantID, tableNum, tableID, and alexaID.
     * @param restaurantID
     * @param tableNumber
     * @param tableID
     * @param alexaID
     */
    private Table( int restaurantID, int tableNumber, int tableID, String alexaID){
        this.restaurantID = restaurantID; 
        this.tableNumber = tableNumber;  
        this.alexaID = alexaID;  
        this.tableID = tableID;  
    }

    /**
     * Table: create a table obj with no tableID
     * @param restaurantID
     * @param tableNumber
     * @param alexaID
     */
    private Table( int restaurantID, int tableNumber, String alexaID){
        this.restaurantID = restaurantID; 
        this.tableNumber = tableNumber;  
        this.alexaID = alexaID;  
    }


    /**
     * registerAlexa : register an AlexaID to a table. 
     * @param alexaID - the base64 alexaID to register
     */
    public boolean registerAlexa( String alexaID ){
        this.alexaID = alexaID; 
        return  DBUtil.setAlexaIDForTable( this.tableID, alexaID ); 
    }


    /**
     * updateCurrentOrder: updates the currentOrder field 
     * by getting the order for this table from the table tracker. 
     */
    public void updateCurrentOrder(){
        OrderTracker tracker = OrderTracker.getInstance();  
        this.currentOrder = tracker.getOrder( this.restaurantID, this.tableNumber ); 
    }


    /**
     * getCurrentOrder: get the current order for this table. 
     */
    public Order getCurrentOrder(){
        OrderTracker tracker = OrderTracker.getInstance();  
        Order order = tracker.getOrder( this.restaurantID, this.tableNumber );
        //System.out.println(order.toString() ); 
        return order;  
    }


    public String toString(){
        StringBuilder str = new StringBuilder(); 

        str.append("restaurantID: " +  Integer.toString(this.restaurantID) + "\n" );
        str.append("tableNumber: " +  Integer.toString(this.tableNumber) + "\n" );
        str.append("tableID: " +  Integer.toString(this.tableID) + "\n" );
        str.append("alexaID: "  + this.alexaID + "\n" );
        if(this.currentOrder != null ){
            str.append("currentOrder: " + this.currentOrder.toString() + "\n" ); 
        }
        return str.toString();
    }

}
