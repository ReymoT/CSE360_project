package phaseTwo;

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
		} catch (ClassNotFoundException e) {
			System.err.println("JDBC Driver not found: " + e.getMessage());
		}
	}

	private void createTables() throws SQLException {
		String userTable = "CREATE TABLE IF NOT EXISTS cse360users ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "email VARCHAR(255) UNIQUE, "
				+ "password VARCHAR(255), "
				+ "role VARCHAR(20))";
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

	public void register(String email, String password, String role) throws SQLException {
		String insertUser = "INSERT INTO cse360users (email, password, role) VALUES (?, ?, ?)";
		try (PreparedStatement pstmt = connection.prepareStatement(insertUser)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3, role);
			pstmt.executeUpdate();
		}
	}

	public boolean login(String email, String password, String role) throws SQLException {
		String query = "SELECT * FROM cse360users WHERE email = ? AND password = ? AND role = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(query)) {
			pstmt.setString(1, email);
			pstmt.setString(2, password);
			pstmt.setString(3, role);
			try (ResultSet rs = pstmt.executeQuery()) {
				return rs.next();
			}
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

	public void displayUsersByAdmin() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");  

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
		} 
	}
	
	public void displayUsersByUser() throws SQLException{
		String sql = "SELECT * FROM cse360users"; 
		Statement stmt = connection.createStatement();
		ResultSet rs = stmt.executeQuery(sql); 

		while(rs.next()) { 
			// Retrieve by column name 
			int id  = rs.getInt("id"); 
			String  email = rs.getString("email"); 
			String password = rs.getString("password"); 
			String role = rs.getString("role");  

			// Display values 
			System.out.print("ID: " + id); 
			System.out.print(", Age: " + email); 
			System.out.print(", First: " + password); 
			System.out.println(", Last: " + role); 
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
	
	private void createArticleTables() throws SQLException {
		//Help Article Table
		String articlesTable = "CREATE TABLE IF NOT EXISTS help_articles ("
				+ "id INT AUTO_INCREMENT PRIMARY KEY, "
				+ "title TEXT UNIQUE, "
				+ "author TEXT, "
				+ "abstract TEXT, "
		 		+ "keywords TEXT, "
				+ "body TEXT, "
		 		+ "references TEXT)";
		statement.execute(articlesTable);
	}

	public void addArticle(char[] title, char[] author, char[] abstractText, char[] keywords, char[] body, char[] references) throws Exception {
	    String encryptedTitle = encryptedField(title);
	    String encryptedAuthor = encryptedField(author);
	    String encryptedAbstract = encryptedField(abstractText);
	    String encryptedKeywords = encryptedField(keywords);
	    String encryptedBody = encryptedField(body);
	    String encryptedReferences = encryptedField(references);

	    String insertArticle = "INSERT INTO help_articles (title, author, abstract, keywords, body, references) VALUES (?, ?, ?, ?, ?, ?)";
	    try (PreparedStatement pstmt = connection.prepareStatement(insertArticle)) {
	        pstmt.setString(1, encryptedTitle);
	        pstmt.setString(2, encryptedAuthor);
	        pstmt.setString(3, encryptedAbstract);
	        pstmt.setString(4, encryptedKeywords);
	        pstmt.setString(5, encryptedBody);
	        pstmt.setString(6, encryptedReferences);
	        pstmt.executeUpdate();
	    }
	}
	
	// Check if the database is empty
	public boolean isArticlesDatabaseEmpty() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM cse360users";
		ResultSet resultSet = statement.executeQuery(query);
		if (resultSet.next()) {
			return resultSet.getInt("count") == 0;
		}
		return true;
	}

	//Delete the Article
	public void deleteArticle(int id) throws SQLException {
        String deleteQuery = "DELETE FROM help_articles WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }
	
	// Encrypt the Article
	private String encryptedField(char[] field) throws Exception {
	    byte[] encryptedBytes = encryptionHelper.encrypt(new String(field).getBytes());
	    return Base64.getEncoder().encodeToString(encryptedBytes);
	}
	
	// Decrypt the Article
	private char[] decryptField(char[] encryptedField) throws Exception {
	    byte[] decryptedBytes = encryptionHelper.decrypt(Base64.getDecoder().decode(new String(encryptedField)));
	    return new String(decryptedBytes).toCharArray();
	}
	
	public void displayArticles() throws Exception {
	    String query = "SELECT * FROM help_articles";
	    try (Statement stmt = connection.createStatement()) {
	        ResultSet resultSet = stmt.executeQuery(query);
	        
	        while (resultSet.next()) {
	            int id = resultSet.getInt("id");
	            char[] encryptedTitle = resultSet.getString("title").toCharArray();
	            char[] encryptedAuthor = resultSet.getString("author").toCharArray();
	            char[] encryptedAbstract = resultSet.getString("abstract").toCharArray();
	            char[] encryptedKeywords = resultSet.getString("keywords").toCharArray();
	            char[] encryptedBody = resultSet.getString("body").toCharArray();
	            char[] encryptedReferences = resultSet.getString("references").toCharArray();

	            char[] decryptedTitle = decryptField(encryptedTitle);
	            displayArticle(id, decryptedTitle, 
	                decryptField(encryptedAuthor), 
	                decryptField(encryptedAbstract), 
	                decryptField(encryptedKeywords),
	                decryptField(encryptedBody),
	                decryptField(encryptedReferences));
	            
	            clearCharArray(encryptedAuthor);
	            clearCharArray(encryptedAbstract);
	            clearCharArray(encryptedKeywords);
	            clearCharArray(encryptedBody);
	            clearCharArray(encryptedReferences);
	        }
	    }
	}
	
	//Display Article Title
	private void displayArticle(int id, char[] title, char[] author, char[] abstractText, char[] keywords, char[] body, char[] references) {
		System.out.println("ID: " + id);
	    System.out.println("Title: " + new String(title));
	    System.out.println("Author: " + new String(author));
	    System.out.println("Abstract: " + new String(abstractText));
	    System.out.println("Keywords: " + new String(keywords));
	    System.out.println("Body: " + new String(body));
	    System.out.println("References: " + new String(references));
	    System.out.println("--------------------------");
	}
    
    public void backupArticles(String fileName) throws IOException, SQLException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String query = "SELECT * FROM help_articles";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                writer.write(resultSet.getString("title") + "\n");
                writer.write(resultSet.getString("author") + "\n");
                writer.write(resultSet.getString("abstract") + "\n");
                writer.write(resultSet.getString("keywords") + "\n");
                writer.write(resultSet.getString("body") + "\n");
                writer.write(resultSet.getString("references") + "\n\n");
            }
        }
    }
    
    public void restoreArticles(String fileName) throws IOException, Exception {
        clearDatabase();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                char[] title = line.toCharArray();
                char[] author = reader.readLine().toCharArray();
                char[] abstractText = reader.readLine().toCharArray();
                char[] keywords = reader.readLine().toCharArray();
                char[] body = reader.readLine().toCharArray();
                char[] references = reader.readLine().toCharArray();
                addArticle(title, author, abstractText, keywords, body, references);
            }
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
