package AutoGarcon;
import java.sql.ResultSet; 
import java.sql.SQLException; 

public class Table {

    private int restaurantID; 
    private int tableNumber; 
    private User customer; 
    private String alexaID; 

    public static Table tableFromResultSet( ResultSet rs ){

        Table table = null; 
        try{
            rs.next(); 
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


    public static Table tableFromTableID( int restaurantID, int tableNum ){
        ResultSet rs = DBUtil.getTable( restaurantID, tableNum ); 
        return tableFromResultSet( rs ); 
    }

    public static Table tableFromAlexaID( String alexaID ){
        ResultSet rs = DBUtil.getTable( alexaID ); 
        return tableFromResultSet( rs ); 
    }

    private Table( int restaurantID, int tableNumber, int tableID, String alexaID){
        this.restaurantID = restaurantID; 
        this.tableNumber = tableNumber;  
        this.alexaID = alexaID;  
        this.tableNumber = tableNumber;  
    }



    public Order getCurrentOrder(){
        OrderTracker tracker = OrderTracker.getInstance();  
        return tracker.getOrder( this.restaurantID, this.tableNumber ); 
    }
}
