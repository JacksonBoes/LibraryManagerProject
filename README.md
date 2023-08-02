# LibraryManagerProject
This program uses java to update and pull data from a MySQL database with a table representing library books that includes the book's ISBN, name, author, genre, and publication year. It allows for functionality such as adding single entries to the table, uploading a file full of entries and adding them to the table, deleting single entries, checking for the presence of specific entries, printing the whole table, printing subsets of the table that meet certain restrictions.
Program is set up to work with MySQL with a table initialized with CREATE TABLE bookEntries(ISBN VARCHAR(13), title VARCHAR(100), author VARCHAR(50), genre VARCHAR(15), year INT, PRIMARY KEY (ISBN));
