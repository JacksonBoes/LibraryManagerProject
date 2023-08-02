import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class LibraryManager {
	
	/**
	 * Prints the formatting rules for adding entries to the database.
	 * @ensures the format guidelines will be printed to the console.
	 */
	public static void printEntryFormat() {
		System.out.println("When adding entries to the database they must follow a specific format.");
		System.out.println("They must be in the form of ISBN/Title/Author/Genre/Year Published.");
		System.out.println("The ISBN must contain only the digits of the ISBN-13 number for the book");
		System.out.println("with no hyphens or dashes. The title cannot be longer than one hundred");
		System.out.println("characters (including spaces). The author cannot be more than fifty");
		System.out.println("characters (including spaces). The genres can be divided up any way you see");
		System.out.println("fit just ensure that each book has only one genre which has a name that is");
		System.out.println("fifteen characters or less. The year must be in yyyy format.");
		System.out.println("You also may add books by giving the program the name and location of a file,");
		System.out.println("containing a list of entries in the above format with one entry per line.");
	}
	
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
	 * Prints the top of the table of results.
	 * @ensures The header for each column of the table is printed.
	 */
	public static void printHeader() {
		//printing the top boundary of the table
		for (int i = 0; i < 137; i++) {
			System.out.print("-");
		}
		System.out.println();
		//printing each column header
		System.out.printf("|%-13s", "ISBN");
		System.out.printf("|%-50s", "Title");
		System.out.printf("|%-50s", "Author");
		System.out.printf("|%-15s", "Genre");
		System.out.println("|Year|");
		//printing the bottom boundary of the table
		for (int i = 0; i < 137; i++) {
			System.out.print("-");
		}
		System.out.println();
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
		//print the top of the table of results
		printHeader();
		//print full table
		while (rs.next()) {
			printRow(rs.getString("ISBN"), rs.getString("title"), rs.getString("author"), rs.getString("genre"), rs.getInt("year"));
		}
		rs.close(); //close result set
	}
	
	/**
	 * Prints each row of the table of results, formatted correctly.
	 * @param ISBN
	 * 		The ISBN number of the given book.
	 * @param title
	 * 		The title of the given book.
	 * @param author
	 * 		The author of the given book.
	 * @param genre
	 * 		The genre of the given book.
	 * @param year
	 * 		The publication year of the given book.
	 * @ensures All of the information from the book entry will be printed in a table row format.
	 */
	public static void printRow(String ISBN, String title, String author, String genre, int year) {
		int titleLength = title.length(); //get length of title
		//print out all the rows formatted for the table
		System.out.print("|" + ISBN + "|");
		//if the title is longer than 50 characters then print just the first 50
		if (titleLength <= 50) {
			System.out.printf("%-50s", title);
		} else {
			System.out.print(title.substring(0, 50));
		}
		System.out.printf("|%-50s|", author);
		System.out.printf("%-15s|", genre);
		System.out.println(year + "|");
		/*
		 * if the title is longer than 50 characters print another row across the table to 
		 * add the rest of the title
		 */
		if (titleLength > 50) {
			System.out.printf("|%-13s|", "");
			System.out.printf("%-50s", title.substring(50));
			System.out.printf("|%-50s|", "");
			System.out.printf("%-15s|", "");
			System.out.println("    |");
		}
		//print the bottom boundary of the row
		for (int i = 0; i < 137; i++) {
			System.out.print("-");
		}
		System.out.println();
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
	      switch (choice) {
		      case 1: { //1 gets the user the formatting rules for adding entries
		    	  printEntryFormat();
		    	  break;
		      } case 2: { //2 adds a single book entry manually
		    	  
		    	  break;
		      } case 3: { //3 takes a filename of a file full of book entries
		    	  
		    	  break;
		      } case 4: { //4 prints the full list of books from the database
		    	  printFullList(st);
		    	  break;
		      } case 5: { //allows a check for a specific book in the database
		    	  
		    	  break;
		      } default: {
		    	  
		    	  break;
		      }
	      }
	      System.out.println(); //add new line
      }
      
      

      // close statement, JDBC connection, and scanner
      st.close();
      con.close();
      input.close();

   } 
}