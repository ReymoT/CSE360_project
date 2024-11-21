package phaseThree;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

import org.bouncycastle.util.Arrays;
import Encryption.EncryptionHelper;
import Encryption.EncryptionUtils;

/*******
 * <p> DatabaseHelper Class </p>
 * 
 * <p> Description: A database helper program with all database/SQL functions</p>
 * 
 * @author Ishita
 * @author Leya
 * @author Morad
 * 
 */

class DatabaseHelper {

	// JDBC driver name and database URL 
	static final String JDBC_DRIVER = "org.h2.Driver";   
	static final String DB_URL = "jdbc:h2:~/CSE360ProjectDatabase";  

	//  Database credentials 
	static final String USER = "sa"; 
	static final String PASS = ""; 

	private Connection connection = null;
	private Statement statement = null; 
	//	PreparedStatement pstmt

	private EncryptionHelper encryptionHelper;
	
	public DatabaseHelper() throws Exception {
		encryptionHelper = new EncryptionHelper();
		connectToDatabase();
	}
	
	public void connectToDatabase() throws SQLException {
		try {
			Class.forName(JDBC_DRIVER); // Load the JDBC driver
			System.out.println("Connecting to database...");
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
			statement = connection.createStatement(); 
			createTables();  // Create the necessary tables if they don't exist
			createArticleTables();
			createMessageTables();
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "email VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(20), "
				+ "access_group VARCHAR(20), "
				+ "rights VARCHAR(255), " // Add rights column
				+ "first_instructor BOOLEAN DEFAULT FALSE)";
		statement.execute(userTable);
	}

	// Check if the database is empty
	public boolean isDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}
	
	//Check if the group has an instructor
	public boolean doesGroupHaveInstructors(String group) throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users WHERE access_group = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, group);
			pstmt.setString(2, "Instructor");
			try(ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getInt("count") > 0;
				}
				return false;
			}
		}
	}

	public void register(String email, String password, String role, String group, String rights) throws SQLException {
		String insertUser = "INSERT INTO cse360users (email, password, role, access_group, rights, first_instructor) VALUES (?, ?, ?, ?, ?, ?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
	        pstmt.setString(1, email);
	        pstmt.setString(2, password);
	        pstmt.setString(3, role);
	        pstmt.setString(4, group);
	        pstmt.setString(5, rights);
	        pstmt.setBoolean(6, role.equals("Instructor") && !doesGroupHaveInstructors(group));

	        pstmt.executeUpdate();

	    }
	}

	public boolean login(String email, String password, String role, String group) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE email = ? AND password = ? AND role = ? AND access_group = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3, role);
			pstmt.setString(4, group);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
		}
	}
	
	public boolean hasRight(String email, String right) throws SQLException {

	    String rights = getUserRights(email);

	    return rights.indexOf(right) >= 0;

	}

	

	public String getUserRights(String email) throws SQLException {
	    String query = "SELECT * FROM cse360users WHERE email = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
	        if (rs.next()) {
	            return rs.getString("rights");
	        }
	    }
	    return ""; // Default to no rights if not found
	}

	

	public void updateUserRights(String email, String newRights) throws SQLException {
	    String updateQuery = "UPDATE cse360users SET rights = ? WHERE email = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        pstmt.setString(1, newRights);
	        pstmt.setString(2, email);
	        pstmt.executeUpdate();
	    }
	}

	public void updateUserGroup(String email, String newGroup) throws SQLException {
	    String updateQuery = "UPDATE cse360users SET access_group = ? WHERE email = ?";

	    try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
	        pstmt.setString(1, newGroup);
	        pstmt.setString(2, email);
	        pstmt.executeUpdate();
	    }
	}
	
	public boolean doesUserExist(String email) {
	    String query = "SELECT COUNT(*) FROM cse360users WHERE email = ?";
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        
	        pstmt.setString(1, email);
	        ResultSet rs = pstmt.executeQuery();
	        
	        if (rs.next()) {
	            // If the count is greater than 0, the user exists
	            return rs.getInt(1) > 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return false; // If an error occurs, assume user doesn't exist
	}

	public void displayAllUsers() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");
			String group = rs.getString("access_group");

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Email: " + email); 
			System.out.print(", Password: " + password); 
			System.out.println(", Role: " + role); 
			System.out.println(", Group: " + group); 
		} 
	}
	
	public void displayUsersByAdmin() throws SQLException{
		String sql = "SELECT * FROM cse360users WHERE role = 'Admin'"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");
			String group = rs.getString("access_group");

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
			System.out.println(", Group: " + group); 
		} 
	}
	
	public void displayUsersByUser() throws SQLException{
		String sql = "SELECT * FROM cse360users WHERE role = 'Student'"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");  
			String group = rs.getString("access_group");

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
			System.out.println(", Group: " + group); 
		} 
	}
	
	public void displayUsersByInstructor() throws SQLException{
		String sql = "SELECT * FROM cse360users WHERE role = 'Instructor'"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");  
			String group = rs.getString("access_group");

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
			System.out.println(", Group: " + group); 
		} 
	}


	public void closeConnection() {
		try{ 
			if(statement!=null) statement.close(); 
		} catch(SQLException se2) { 
			se2.printStackTrace();
		} 
		try { 
			if(connection!=null) connection.close(); 
		} catch(SQLException se){ 
			se.printStackTrace(); 
		} 
	}
		
	// Create the help_articles table in the database
	private void createArticleTables() throws SQLException {
		// Statement to create the help_articles table if it doesn't already exist
		String articlesTable = "CREATE TABLE IF NOT EXISTS help_articles ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title TEXT, "
				+ "header TEXT, "
				+ "author TEXT, "
				+ "article_group TEXT, "
				+ "description TEXT, "
				+ "keywords TEXT, "
		 		+ "body TEXT, "
				+ "references TEXT)";
		
		// Execute the SQL statement to create the table
		statement.execute(articlesTable);
	}
	
	private String encryptedField(String field) throws Exception {
	    byte[] encryptedBytes = encryptionHelper.encrypt(field);
	    return Base64.getEncoder().encodeToString(encryptedBytes);
	}

	public void addArticle(String title, String header, String author, char[] article_group, String description, char[] keywords, String body, String references) throws Exception {
		String encryptedBody = encryptedField(body);
		// SQL statement to insert a new article into the help_articles table
		String insertArticle = "INSERT INTO help_articles (title, header, author, article_group, description, keywords, body, references) VALUES (?, ?, ?, ?, ?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {

			// Setting parameters
	        pstmt.setString(1, title);
	        pstmt.setString(2, header);
	        pstmt.setString(3, author);
	        pstmt.setString(4, new String(article_group).toLowerCase());
	        pstmt.setString(5, description);
	        pstmt.setString(6, new String(keywords));
	        pstmt.setString(7, encryptedBody);
	        pstmt.setString(8, references);

	        
	        // Execute the update
	        pstmt.executeUpdate();
	    }
	}

	// Delete the article from the database by its title
	public void deleteArticle(String title, String group) throws SQLException {
        String deleteQuery = "DELETE FROM help_articles WHERE title = ? AND article_group = ?";
	        // Prepare the SQL statement
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setString(1, title);
            pstmt.setString(2, group);
            pstmt.executeUpdate();
        }
    }
		
	// Displays all articles in the database
	public void displayArticles() throws Exception {
	    String query = "SELECT * FROM help_articles";
	    try (Statement stmt = connection.createStatement()) {
	        ResultSet resultSet = stmt.executeQuery(query);
	        
	        // Iterate through results
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = resultSet.getString("body");
	            String articleReferences = resultSet.getString("references");

	            // Call method to print to console
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	            
	            clearCharArray(articleKeywords);
	        }
	    }
	}
		
	// Displays the details of a single article
	private void displayArticleConsole(int id, String title, String header, String author, char[] article_group, String description, char[] keywords, String body, String references) {
		System.out.println("ID: " + id);
	    System.out.println("Title: " + new String(title));
	    System.out.println("Header: " + new String(header));
	    System.out.println("Author: " + new String(author));
	    System.out.println("Group: " + new String(article_group));
	    System.out.println("Description: " + new String(description));
	    System.out.println("Keywords: " + new String(keywords));
	    System.out.println("Body: " + new String(body));
	    System.out.println("References: " + new String(references));
	    
	 // Separator for clarity
	    System.out.println("--------------------------");
	}
		// Displays articles matching a specific keyword
	public void displayArticleByKeyword(String keyword) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE keywords LIKE ?";
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, "%" + keyword + "%");  // Use wildcards to search for keyword within the string
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	        
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = resultSet.getString("body");
	            String articleReferences = resultSet.getString("references");
		            // Display the found article
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	            
	            clearCharArray(articleKeywords);
	        }
	        
	        // Error statement if article not found
	        if (!articleFound) {
	            System.out.println("No article found with the keyword: " + keyword);
	        }
	    }
	}
	
	// Returns articles matching a specific keyword
	public Article returnArticleByKeyword(String keyword, String group) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE keywords LIKE ? AND article_group = ?";
	    Article article;
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, "%" + keyword + "%");  // Use wildcards to search for keyword within the string
	        pstmt.setString(2, group);
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	        
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = resultSet.getString("body");
	            String articleReferences = resultSet.getString("references");
		        
	            article = new Article(articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	            clearCharArray(articleKeywords);
	            return article;
	        }
	        
	        // Error statement if article not found
	        if (!articleFound) {
	            System.out.println("No article found with the keyword: " + keyword);
	        }
	    }
        return null;
	}
	
	// Checks whether article exists through specified keyword
	public boolean articleExistsByKeyword(String keyword, String group) throws SQLException {
	   String query = "SELECT COUNT(*) FROM help_articles WHERE keywords LIKE ? AND article_group = ?";
	   
	   // Prepare statement for searching articles by keyword
	   try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	       pstmt.setString(1, "%" + keyword + "%");  // Use wildcards to search for the keyword within keywords
	       pstmt.setString(2, group);
	       ResultSet resultSet = pstmt.executeQuery();
	       
	       // Return true if there is at least one matching article
	       return resultSet.next() && resultSet.getInt(1) > 0;
	   }
	}
	
	// Checks whether article exists through specified title
		public boolean articleExistsByTitle(String title, String group) throws SQLException {
		   String query = "SELECT COUNT(*) FROM help_articles WHERE title LIKE ? AND article_group = ?";
		   
		   // Prepare statement for searching articles by title
		   try (PreparedStatement pstmt = connection.prepareStatement(query)) {
		       pstmt.setString(1, title);
		       pstmt.setString(2, group);
		       ResultSet resultSet = pstmt.executeQuery();
		       
		       // Return true if there is at least one matching article
		       return resultSet.next() && resultSet.getInt(1) > 0;
		   }
		}

	// Backs up all articles to an external data file
    public void backupArticles(String fileName) throws IOException, SQLException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String query = "SELECT * FROM help_articles";
            ResultSet resultSet = statement.executeQuery(query);
            
            // Iterate through the result set
            while (resultSet.next()) {
            	writer.write("ID: " + resultSet.getInt("id") + "\n");
                writer.write("Title: " + resultSet.getString("title") + "\n");
                writer.write("Header: " + resultSet.getString("header") + "\n");
                writer.write("Author: " + resultSet.getString("author") + "\n");
                writer.write("Group: " + resultSet.getString("article_group") + "\n");  // Use "group" instead of "Group"
                writer.write("Description: " + resultSet.getString("description") + "\n");
                writer.write("Keywords: " + resultSet.getString("keywords") + "\n");
                writer.write("Body: " + resultSet.getString("body") + "\n");
                writer.write("References: " + resultSet.getString("references") + "\n");
                writer.write("\n---\n\n");  // Separator between articles
            }
        }
    }

    // Backs up specified groups of articles to an external data file
    public void backupGroupArticles(String fileName, String article_group) throws IOException, SQLException {
        String query = "SELECT * FROM help_articles WHERE article_group LIKE ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query);
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {

        	// Search for articles containing the specified group
            pstmt.setString(1, "%" + article_group + "%");  // Search for articles containing the keyword
            ResultSet resultSet = pstmt.executeQuery();
	            // Iterate through the result set
            while (resultSet.next()) {
                writer.write("Title: " + resultSet.getString("title") + "\n");
                writer.write("Header: " + resultSet.getString("header") + "\n");
                writer.write("Author: " + resultSet.getString("author") + "\n");
                writer.write("Group: " + resultSet.getString("article_group") + "\n");
                writer.write("Description: " + resultSet.getString("description") + "\n");
                writer.write("Keywords: " + resultSet.getString("keywords") + "\n");
                writer.write("Body: " + resultSet.getString("body") + "\n");
                writer.write("References: " + resultSet.getString("references") + "\n");
                writer.write("\n---\n\n");  // Separator between articles
            }
        }
    }
	    
 // Restores articles from an external data file into the dataBase
    public void restoreArticles(String fileName, boolean replace) throws IOException, SQLException {
		if (replace == true) {
			clearDatabase();
		}
			// Read articles from the external data file
		try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
			String line;
			int id = 0;
        	String title = null, header = null, author = null, group = null, description = null, keywords = null, body = null, references = null;
	        	// Parse each line of the external data file
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("ID: ")) {
					id = Integer.parseInt(line.substring(4));
					if (replace == false) {
						String query = "SELECT COUNT(*) FROM help_articles WHERE id = ?";
						
						// Check if the article already exists in the database
                    	try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                        	pstmt.setInt(1, id);
                        	ResultSet resultSet = pstmt.executeQuery();
                        	resultSet.next();
                        	if (resultSet.getInt(1) > 0) {
                        		
                        		// Move to the next article if it already exists
                            	while ((line = reader.readLine()) != null && !line.trim().equals("---")) {
                            	}
                            	continue; 
                        	}
                    	}
					}
						
				// Retrieving each variable of the article
				} else if (line.startsWith("Title: ")) {
					title = line.substring(7);
				} else if (line.startsWith("Header: ")) {
					header = line.substring(8);
				} else if (line.startsWith("Author: ")) {
					header = line.substring(8);
				} else if (line.startsWith("Group: ")) {
					group = line.substring(7);
				} else if (line.startsWith("Description: ")) {
					description = line.substring(13);
				} else if (line.startsWith("Keywords: ")) {
					keywords = line.substring(10);
				} else if (line.startsWith("Body: ")) {
					body = line.substring(6);
				} else if (line.startsWith("References: ")) {
					references = line.substring(12);
				} else if (line.equals("---")) {
					
					// Insert the article into the database
					String insertQuery = "INSERT INTO help_articles (id, title, header, author, article_group, description, keywords, body, references) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
					try (PreparedStatement pstmt = connection.prepareStatement(insertQuery)) {
						pstmt.setInt(1, id);
						pstmt.setString(2, title);
						pstmt.setString(3, header);
						pstmt.setString(4, author);
						pstmt.setString(5, group);
						pstmt.setString(6, description);
						pstmt.setString(7, keywords);
						pstmt.setString(8, body);
						pstmt.setString(9, references);
						pstmt.executeUpdate();
					}
						// Reset article variables for the next article
					id = 0;
                	title = header = author = group = description = keywords = body = references = null;
				}
			}
		}
	}
    
    private String decryptField(String encryptedField) throws Exception {
	    return encryptionHelper.decrypt(encryptedField);
	}

    public void decryptDisplayArticles(String file_name, String group) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE title LIKE ? AND article_group = ?";
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, "%" + file_name + "%");  // Use wildcards to search for keyword within the string
	        pstmt.setString(2, group);
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	      
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = decryptField(resultSet.getString("body"));
	            String articleReferences = resultSet.getString("references");

	            // Display the found article
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	          
	            clearCharArray(articleKeywords);
	        }
	        
	        // Error statement if article not found
	        if (!articleFound) {
	            System.out.println("No article found with the title: " + file_name);
	        }
	    }
	}
    
    public void decryptDisplayArticlesByKeyword(String keyword, String group) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE title LIKE ? OR author LIKE ? OR description LIKE ? AND article_group = ?";
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, "%" + keyword + "%");  // Use wildcards to search for keyword within the string
	        pstmt.setString(2, "%" + keyword + "%");
	        pstmt.setString(3, "%" + keyword + "%");
	        pstmt.setString(4, group);
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	      
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = decryptField(resultSet.getString("body"));
	            String articleReferences = resultSet.getString("references");

	            // Display the found article
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	          
	            clearCharArray(articleKeywords);
	        }
	        
	        // Error statement if article not found
	        if (!articleFound) {
	            System.out.println("No article found with the title: " + keyword);
	        }
	    }
	}

    public void decryptDisplayAllArticles(String group) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE article_group = ?";
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	    	pstmt.setString(1, group);
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	      
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = decryptField(resultSet.getString("body"));
	            String articleReferences = resultSet.getString("references");

	            // Display the found article
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	          
	            clearCharArray(articleKeywords);
	        }
	        
	        // Error statement if articles not found
	        if (!articleFound) {
	            System.out.println("No articles found");
	        }
	    }
	}

    
    public void decryptDisplayArticlesByLevel(String level, String group) throws Exception {
	    String query = "SELECT * FROM help_articles WHERE keywords = ? AND article_group = ?";
	    
	    // Prepare statement for searching articles by keyword
	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
	        pstmt.setString(1, level);
	        pstmt.setString(2, group);
	        ResultSet resultSet = pstmt.executeQuery();
	        
	        // Track if articles were found
	        boolean articleFound = false;
	      
	        // Iterate through the result set
	        while (resultSet.next()) {
	            articleFound = true;
	            int id = resultSet.getInt("id");
	            String articleTitle = resultSet.getString("title");
	            String articleHeader = resultSet.getString("header");
	            String articleAuthor = resultSet.getString("author");
	            char[] articleGroup = resultSet.getString("article_group").toCharArray();
	            String articleDescription = resultSet.getString("description");
	            char[] articleKeywords = resultSet.getString("keywords").toCharArray();
	            String articleBody = decryptField(resultSet.getString("body"));
	            String articleReferences = resultSet.getString("references");

	            // Display the found article
	            displayArticleConsole(id, articleTitle, articleHeader, articleAuthor, articleGroup, articleDescription, articleKeywords, articleBody, articleReferences);
	          
	            clearCharArray(articleKeywords);
	        }
	        
	        // Error statement if article not found
	        if (!articleFound) {
	            System.out.println("No article found with the content level: " + level);
	        }
	    }
	}

    
    // Create the help_messages table in the database
 	private void createMessageTables() throws SQLException {
 		// Statement to create the help_messages table if it doesn't already exist
 		String messageTable = "CREATE TABLE IF NOT EXISTS help_messages ("
 				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
 				+ "title TEXT, "
 		 		+ "body TEXT,"
 		 		+ "type TEXT)";
 		
 		// Execute the SQL statement to create the table
 		statement.execute(messageTable);
 	}
 	
 	// Displays the details of a single help message
 	private void displayMessageConsole(int id, String title, String body, String type) {
 		System.out.println("ID: " + id);
 	    System.out.println("Title: " + new String(title));
 	    System.out.println("Body: " + new String(body));
 	   System.out.println("Message Type: " + new String(type));
 	    
 	 // Separator for clarity
 	    System.out.println("--------------------------");
 	}
 	// Displays all help messages
 	public void displayMessages() throws Exception {
 	    String query = "SELECT * FROM help_messages";
 	    
 	    // Prepare statement for searching messages
 	    try (PreparedStatement pstmt = connection.prepareStatement(query)) {
 	        ResultSet resultSet = pstmt.executeQuery();
 	        
 	        // Track if messages were found
 	        boolean messageFound = false;
 	        
 	        // Iterate through the result set
 	        while (resultSet.next()) {
 	        	messageFound = true;
 	            int id = resultSet.getInt("id");
 	            String messageTitle = resultSet.getString("title");
 	            String messageBody = resultSet.getString("body");
 	           String messageType = resultSet.getString("type");
 		        // Display the found message
 	            displayMessageConsole(id, messageTitle, messageBody, messageType);
 	        }
 	        
 	        // Error statement if messages not found
 	        if (!messageFound) {
 	            System.out.println("No help messages found");
 	        }
 	    }
 	}
 	
 	public void addMessage(String title, String body, String type) throws Exception {
		// SQL statement to insert a new message into the help_messages table
		String insertMessage = "INSERT INTO help_messages (title, body, type) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertMessage)) {

			// Setting parameters
	        pstmt.setString(1, title);
	        pstmt.setString(2, body);
	        pstmt.setString(3, type);

	        
	        // Execute the update
	        pstmt.executeUpdate();
	    }
	}

	    
    private void clearDatabase() throws SQLException {
        String deleteQuery = "DELETE FROM help_articles";
        statement.executeUpdate(deleteQuery);
    }
    //Clean up 
	private void clearCharArray(char[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = ' ';
        }
    }
	
}
