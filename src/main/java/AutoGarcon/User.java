package AutoGarcon;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class User {

    private int userID;
    private String firstName;
    private String lastName;
    private String email;
    private int restaurantID;

    public User(int userID, String firstName, String lastName, String email) {
        this.userID = userID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    /**
     * User - Empty constructor for user object. 
     * For use in json deserialization. 
     */
    public User(){
        this.firstName = "Default User";
        this.lastName = "";
        this.email = "";
    }

    public int getUserID(){return this.userID;}
    
    public void setUserID( int userID ) {
        this.userID = userID;
    }

    public String getFirstName(){return this.firstName;}

    public String getLastName(){return this.lastName;}

    public String getName(){return this.firstName+ " " + this.lastName;}

    public String getEmail(){return this.email;}

    public void setFirstName(String firstName) {this.firstName = firstName;}

    public void setLastName(String lastName) {this.lastName = lastName;}

    public void setEmail(String email) {this.email = email;}
    
    public Integer getRestaurantID() {
        return this.restaurantID;
    }

    public void setRestaurantID(int restaurantID) {
        this.restaurantID = restaurantID;
    }

    public boolean isDefault() {
        if( this.firstName.equals( "Default User" ) ){
            return true; 
        }
        else {
            return false; 
        }
    }

    public static User userFromJson( String body ) {

        Gson gson = new Gson();
        User user = new User();

        try {
            user = gson.fromJson( body, User.class );
        } catch ( JsonSyntaxException e ) {
            System.out.printf("Failed to deserialize the request data into a User Object.\n" +
                    "Request body: %s.\n Exception: %s\n", body, e.toString() );
        }
        return user; 
    }

    public boolean save() { return DBUtil.addUser(this);}

    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("firstName: ").append(this.firstName).append("\n");
        str.append("lastName: ").append(this.lastName).append("\n");
        str.append("email: ").append(this.email).append("\n");
        return str.toString();
    }

}
