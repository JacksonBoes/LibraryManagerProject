import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LibraryManager {
   public static void main(String[] args) throws Exception {
	   
	  /*
	   * DatabaseConnector class is used just to separate sensitive 
	   * information from the rest of the code
	   */
      Connection con = DatabaseConnector.connectToDatabase();
      // display status message
      if (con == null) {
         System.out.println("Connection could not be established. Goodbye.");
         return;
      } else
         System.out.println("Connection established successfully.\n");
      
      Statement st = con.createStatement();
      ResultSet rs = st.executeQuery("SELECT * FROM characters");
      while (rs.next()) {
    	  System.out.println(rs.getString("name") + " " + rs.getString("class") 
    	  		+ " " + rs.getInt("HP"));
      }
      

      // close JDBC connection
      rs.close();
      st.close();
      con.close();

   } 
}