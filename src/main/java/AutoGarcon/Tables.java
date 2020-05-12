package AutoGarcon; 

public class Tables {

    private int numTables; 
    private Table[] tables; 

    public Tables( Table[] tables ){
        
        this.numTables = tables.length; 
        this.tables = tables; 
    }
}
