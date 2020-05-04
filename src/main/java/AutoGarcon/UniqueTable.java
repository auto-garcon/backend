package AutoGarcon; 


public class UniqueTable {
    int restaurantID;
    int tableNumber;

    public UniqueTable(int restaurantID, int tableNumber) {
        this.restaurantID = restaurantID;
        this.tableNumber = tableNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UniqueTable)) return false;
        UniqueTable key = (UniqueTable) o;
        return restaurantID == key.restaurantID && tableNumber == key.tableNumber;
    }

    @Override
    public int hashCode() {
        int result = restaurantID;
        result = 31 * result + tableNumber;
        return result;
    }
}

