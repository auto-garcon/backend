package AutoGarcon; 

public class Restaurants {

    private int numRestaurants; 
    private Restaurant[] restaurants; 

    public Restaurants( Restaurant[] restaurants ){
        
        this.numRestaurants = restaurants.length; 
        this.restaurants = restaurants; 
    }
}
