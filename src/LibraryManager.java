import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LibraryManager {
	
	
	/**
	 * Method to get the choice from the user for what action they would like to take.
	 * Continually asks user for a choice until they choose a valid option.
	 * @param input
	 * 		The input scanner.
	 * @requires input is open
	 * @ensures 0 <= getChoice <= 5
	 * @return An int that corresponds to the user's choice.
	 */
	public static int getChoice(Scanner input) {
		  //get user's choice of action
	      System.out.println("Now what would you like to do? (Enter the number of your choice).");
	      System.out.println("0: quit program");
	      System.out.println("1: read the formatting rules for adding entries");
	      System.out.println("2: add a single book entry by manually typing the information");
	      System.out.println("3: enter a filename of a file that holds multiple book entries to "
	    		  + "be added");
	      System.out.println("4: get a full list of books from the database");
	      System.out.println("5: check for a single book's presence in the database");
	      System.out.print("Choice: ");
	      String choice = input.nextLine();
	      int choiceNum = -1; //initialize choiceNum with an arbitrary initial value
	      
	      //continue prompting user for their choice until they enter a valid option
	      boolean goodChoice = false;
	      while (!goodChoice) {
		      try {
		    	  choiceNum = Integer.parseInt(choice); //try to parse user's choice into an int
		          //if user's choice is an int ensure it corresponds to an option
		    	  if (choiceNum < 0 || choiceNum > 5) { 
		    		  //give an error if choice doesn't correspond to an option 
		    		  System.out.println("Error: choise must be a single digit number that corresponds to an option");
		    		  System.out.print("Choice: ");
		    	      choice = input.nextLine();
		    	  } else {
		    		  //set goodChoice to true so that the loop can end when a valid choice is entered
		    		  goodChoice = true;
		    	  }
		      } catch (NumberFormatException e) {
		    	  //give an error if the choice is not a number
		    	  System.out.println("Error: choice must be a single digit number that corresponds to an option");
		    	  System.out.print("Choice: ");
		          choice = input.nextLine();
		      }
	      }
	      System.out.println(); //add empty line after getting choice
	      return choiceNum;
	}
	
	/**
	 * Prints the full list of books from the database.
	 * @param st
	 * 		The statement to be used with the database.
	 * @requires st is a statement connected to the database that holds the book list.
	 * @ensures The full list of books from the database connected to st will be
	 * printed to the console.
	 */
	public static void printFullList(Statement st) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM bookEntries"); //get result set
		//print full table
		while (rs.next()) {
			System.out.println(rs.getString("ISBN") + ", " + rs.getString("title") + ", " 
					+ rs.getString("author") + ", " + rs.getString("genre") + ", " + 
					rs.getInt("year"));
		}
		rs.close(); //close result set
	}
	
    public static void main(String[] args) throws Exception {
	  /*
	   * DatabaseConnector class is used just to separate sensitive 
	   * information from the rest of the code, has been ignored
	   */
      Connection con = DatabaseConnector.connectToDatabase();
      // display status message regarding whether successful connection was made
      if (con == null) {
         System.out.println("Connection could not be established. Goodbye.");
         return;
      } else {
         System.out.println("Connection established successfully." + System.lineSeparator());
      }
      
	  Scanner input = new Scanner(System.in); //open a scanner      
      Statement st = con.createStatement(); //create a statement
      
      //explain program to user
      System.out.println("Hello, this is the library book manager!");
      System.out.println("Here you can update and maintain a database of library books.");
      System.out.println("Each book has an entry that includes the ISBN, the title, the " 
    		  + "author, the genre, and the year of publication.");
      
      int choice = -1; //initialize choice with an arbitrary intial value
      //repeat until user enters a 0 to quit the program
      while (choice != 0) {
	      //get the choice for the course of action from the user
	      choice = getChoice(input);
	      if (choice == 1) {
	    	  
	      } else if (choice == 2) {
	    	  
	      } else if (choice == 3) {
	    	  
	      } else if (choice == 4) {
	    	  printFullList(st);
	      } else if (choice == 5) {
	    	  
	      }
	      System.out.println(); //add new line
      }
      
      

      // close statement, JDBC connection, and scanner
      st.close();
      con.close();
      input.close();

   } 
}