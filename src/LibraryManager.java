import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
/**
 * A program to help a user manage a database of library books.
 * @author Jackson Boes
 *
 */
public class LibraryManager {
	
	/**
	 * The name of the database being used.
	 */
	private static String tableName = "bookEntries";
	
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
	 * @ensures 0 <= getChoice <= 7
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
	      System.out.println("6: delete a book from the database");
	      System.out.println("7: get a specific list of books from the database");
	      System.out.print("Choice: ");
	      String choice = input.nextLine();
	      int choiceNum = -1; //initialize choiceNum with an arbitrary initial value
	      
	      //continue prompting user for their choice until they enter a valid option
	      boolean goodChoice = false;
	      while (!goodChoice) {
		      try {
		    	  choiceNum = Integer.parseInt(choice); //try to parse user's choice into an int
		          //if user's choice is an int ensure it corresponds to an option
		    	  if (choiceNum < 0 || choiceNum > 7) { 
		    		  //give an error if choice doesn't correspond to an option 
		    		  System.out.println("ERROR: choise must be a single digit number that " 
		    				  + "corresponds to an option");
		    		  System.out.print("Choice: ");
		    	      choice = input.nextLine();
		    	  } else {
		    		  //set goodChoice to true so that the loop can end when a valid choice is entered
		    		  goodChoice = true;
		    	  }
		      } catch (NumberFormatException e) {
		    	  //give an error if the choice is not a number
		    	  System.out.println("ERROR: choice must be a single digit number that corresponds to" 
		    			  + " an option");
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
	 * @requires st is open
	 * @ensures The full list of books from the database connected to st will be
	 * printed to the console.
	 */
	public static void printFullList(Statement st) throws SQLException {
		ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + ";"); //get result set
		//print the top of the table of results
		printHeader();
		//print full table
		while (rs.next()) {
			printRow(rs.getString("ISBN"), rs.getString("title"), rs.getString("author"), 
					rs.getString("genre"), rs.getInt("year"));
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
	
	/**
	 * Adds a single book entry to the database.
	 * @param userInput
	 * 		A string of input from the user which should contain
	 * 		all the info for a new book entry.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @returns true if the book could be added, false otherwise.
	 * @requires st is open
	 * @ensures the current book will be added to the database if it
	 * meets the requirements and has all necessary information.
	 */
	public static boolean addEntry(String userInput, Statement st) {
		userInput = userInput.trim(); //take leading or trailing spaces off of the input
		//get the input into a list split at /
		List<String> bookInfo = Arrays.asList(userInput.split("/"));
		int size = bookInfo.size();
		boolean added = true; //added will track whether the new entry was added
		if (size == 5) { //if bookInfo doesn't have 5 entries then the book cannot be added
			String sql = "INSERT INTO " + tableName + "(ISBN, title, author, genre, year) VALUES(";
			String ISBN = bookInfo.get(0);
			//ensure that ISBN is correct since it is the primary key of the table
			if (ISBN.length() == 13 && isNumber(ISBN)) {
				//try to execute the statement to add the book
				try {
					sql += "'" + ISBN;
					for (int i = 1; i < size; i++) { //add all the data fields to the statement
						sql += "', '" + bookInfo.get(i).replace("'", "\\'");
						//replace any ' in any data fields with \'
					}
					sql += "');";
					st.executeUpdate(sql);
				} catch (SQLException e) {
					added = false; //if execute statement fails
				}
			} else {
				added = false; //if ISBN was not correct
			}
		} else {
			added = false; //if size was not 5
		}
		return added;
	}
	
    /**
     * Method to determine whether the given string {@code str} is a 
     * number.
     * @param str
     *            The string to check
     * @return true if str is a number, false otherwise
     * @ensures isNumber = [true if str is a number and false
     *          otherwise]
     */
    public static boolean isNumber(String str) {
        boolean isNumber = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                isNumber = false;
            }
        }
        if (str.length() == 0) {
            isNumber = false;
        }
        return isNumber;
    }
	
	/**
	 * Adds a single manual entry from the user.
	 * @param input
	 * 		The scanner.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @requires input is open, st is open
	 * @ensures the current book will be added to the database if it
	 * meets the requirements and has all necessary information.
	 */
	public static void addManualEntry(Scanner input, Statement st) {
		System.out.println("Enter the information for the book according to the formatting "
  			  + "guidelines: ");
  	  	String userInput = input.nextLine(); //get entry from the user
		//attempt to add the entry to the database, print error if unsuccessful
		if (!addEntry(userInput, st)) {
			System.out.println("ERROR: Book " + userInput + " could not be added,");
			System.out.println("check that you have followed the formatting guidelines.");
		} else {
			System.out.println("Book added successfully.");
		}
	}
	
	/**
	 * Reads a file and adds each book entry from the file.
	 * @param input
	 * 		The scanner.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @requires input is open, st is open
	 * @ensures each book entry from that file that is properly formatted
	 * will be added to the database.
	 */
	public static void addFileOfEntries(Scanner input, Statement st) {
		System.out.print("Enter the location of the file: ");
		String fileName = input.nextLine(); //get file name from user
		boolean noErrors = true; //boolean for whether any errors occured while adding books
		try {
			//open buffered reader
			BufferedReader fileIn = new BufferedReader(new FileReader(fileName));
			try {
				//read each line from the file and attempt to add each book to database
				String line = fileIn.readLine();
				while (line != null) {
					if (!addEntry(line, st)) { //if book could not be added report error
						noErrors = false;
						System.out.println("ERROR: could not add book: " + line);
						System.out.println("Any other books were still added successfully.");
					}
					line = fileIn.readLine();
				}
			} catch (IOException f) {
				System.out.println("ERROR: could not read from the file.");
			}
			try {
				fileIn.close(); //close buffered reader
			} catch (IOException k) {
				System.out.println("File error.");
			}
		} catch (IOException e) {
			System.out.println("ERROR: File " + fileName + " cannot be found.");
		}
		if (noErrors) { //report if all books were added successfully
			System.out.println("All books added successfully.");
		}
	}
	
	/**
	 * Checks for a specific book in the database and prints its row if it
	 * exists or states that it does not exist.
	 * @param input
	 * 		The scanner.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @requires input is open, st is open
	 * @ensures if the specified book is present its row is printed,
	 * or if it is not present a message states that.
	 */
	public static void checkForEntry(Scanner input, Statement st) {
		System.out.print("Enter the ISBN of the book to check for: ");
		String ISBN = input.nextLine();
		if (ISBN.length() == 13 && isNumber(ISBN)) {
			try {
				ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " WHERE ISBN = '" + ISBN + "';");
				if (rs.next()) { //print the result if one was returned
					printHeader(); //print the header for the row
					printRow(rs.getString("ISBN"), rs.getString("title"), rs.getString("author"), 
							rs.getString("genre"), rs.getInt("year"));
				} else { //if no result was returned output that info to the user
					System.out.println("Book " + ISBN + " is not in the database.");
				}
				rs.close(); //close result set
			} catch (SQLException e) {
				System.out.println("ERROR: could not check table for book " + ISBN + ".");
			}
		} else {
			System.out.println("ERROR: " + ISBN + " is not a valid ISBN.");
		}
	}
	
	/**
	 * Deletes a single entry from the database based on ISBN.
	 * @param input
	 * 		The scanner.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @requires input is open
	 * @ensures The book matching the given ISBN will be deleted or 
	 * if no such book exists an error will be reported.
	 */
	public static void deleteEntry(Scanner input, Statement st) {
		//get the ISBN from the user
		System.out.print("Enter an ISBN number for the book you wish to delete: ");
		String ISBN = input.nextLine();
		//ensure that the ISBN is a proper ISBN number
		if (ISBN.length() == 13 && isNumber(ISBN)) {
			try {
				//delete the entry with the specified ISBN
				st.executeUpdate("DELETE FROM " + tableName + " WHERE ISBN = '" + ISBN + "';");
			} catch (SQLException e) {
				//error if book could not be deleted
				System.out.println("ERROR: could not delete entry.");
			}
		} else {
			//error if ISBN is not entered correctly
			System.out.println("ERROR: " + ISBN + " is not a valid ISBN.");
		}
	}
	
	/**
	 * Method to print a subset of the table chosen by the user.
	 * @param input
	 * 		The scanner.
	 * @param st
	 * 		The statement object used to execute sql statements.
	 * @requires input is open
	 * @ensures a subset of the table which follows a certain restriction
	 * chosen by the user will be printed.
	 */
	public static void printSubset(Scanner input, Statement st) { 
		//get choice from user
		System.out.println("You can get a subset of all the books from the database with");
		System.out.println("the same title, author, or genre.");
		System.out.print("Enter 1 for title, 2 for author, 3 for genre: ");
		String choice = input.nextLine();
		boolean choiceValid = false; //boolean for if the user's choice is valid
		if (choice.equals("1")) {
			choiceValid = true;
			choice = "title";
		} else if (choice.equals("2")) {
			choiceValid = true;
			choice = "author";
		} else if (choice.equals("3")) {
			choiceValid = true;
			choice = "genre";
		}
		
		//check if choice is valid
		if (choiceValid) {
		//now get what the user wants to search for
		System.out.print("Now enter the " + choice + " that you want the entries for (you " + 
				"will be given a table containing every entry whose " + choice + 
				" has the answer you enter here): ");
		String userInput = input.nextLine();
		
			try {
				
				ResultSet rs = st.executeQuery("SELECT * FROM " + tableName + " WHERE " +
						choice + " LIKE '%" + userInput + "%';"); //get result set
				//print the top of the table of results
				printHeader();
				//print full table
				while (rs.next()) {
					printRow(rs.getString("ISBN"), rs.getString("title"), rs.getString("author"), 
							rs.getString("genre"), rs.getInt("year"));
				}
				rs.close(); //close result set
			} catch (SQLException e) {
				System.out.println("ERROR: could not read from table.");
			}
		} else { //print error message for invalid choice
			System.out.println("ERROR: " + choice + " is not a valid option.");
		}
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
		    	  addManualEntry(input, st);
		    	  break;
		      } case 3: { //3 takes a filename of a file full of book entries
		    	  addFileOfEntries(input, st);
		    	  break;
		      } case 4: { //4 prints the full list of books from the database
		    	  printFullList(st);
		    	  break;
		      } case 5: { //allows a check for a specific book in the database
		    	  checkForEntry(input, st);
		    	  break;
		      } case 6: { //allow a user to delete a book
		    	  deleteEntry(input, st);
		    	  break;
		      } case 7: { //allow user to get a subset of the table
		    	  printSubset(input, st);
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