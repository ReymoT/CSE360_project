package phaseThree;

//JavaFX imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.*;

//SQL Import
import java.sql.SQLException;

/*******
 * <p> Dashboard Class </p>
 * 
 * <p> Description: A dashboard GUI of the user functions after successfully logging in </p>
 * 
 * @author Reimo
 * @author Leya
 * @author Morad
 * 
 */

public class Dashboard
{
    private Button addButton; // button to add articles
    private Button viewButton; // button to view articles
    private Button searchButton; // button to search for articles
    private Button editButton; // button to edit articles
    private Button deleteButton; // button to delete articles
    private Button backupButton; // button to backup articles
    private Button restoreButton; // button to restore articles
    private Button displayMessagesButton; // button to display all messages
    private Button sendMessageButton; // button to send messages
    private Button viewAllArticlesButton; // button to view all articles
    
    private TextField titleField; // field to enter the title
	private TextField headerField; // field to enter the header
	private TextField articleGroupField; // field to enter the article group
	private TextField descriptionField; // field to enter the description
	private TextField keywordsField; // field to enter the keywords
	private TextField bodyField; // field to enter the body
	private TextField referencesField; // field to enter the references
	private Button submitButton; // button to submit the data
	
	private TextField searchByTitle; // field to search articles by the title
	
	private TextField deleteByTitle; // field to delete articles by the title
	
	private TextField search; // field to search for articles
	
	private TextField fileName; // field to get the backup filename
	
	private ComboBox<String> replaceArticles; // drop-down list for choosing whether to replace the articles
	
	private TextField messageTitle; // field to enter the title for the help message
	private TextField messageBody; // field to enter the body for the help message
	private ComboBox<String> messageType; // drop-down list for choosing the type of message
	
	private ComboBox<String> contentLevel; // drop-down list for choosing the content level of articles


    public Scene createScene(DatabaseHelper database, String role)
    {
    	VBox layout = new VBox(10);
        addButton = new Button("Add Article");
        addButton.setOnAction(action -> {
			try {
				addArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        viewButton = new Button("View Article");
        viewButton.setOnAction(action -> {
			try {
				viewArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        viewAllArticlesButton = new Button("View All Articles");
        viewAllArticlesButton.setOnAction(action -> {
			try {
				viewAllArticles(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        searchButton = new Button("Search Article");
        searchButton.setOnAction(action -> {
			try {
				searchArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        editButton = new Button("Edit Article");
        editButton.setOnAction(action -> {
			try {
				editArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(action -> {
			try {
				deleteArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        backupButton = new Button("Backup Article");
        backupButton.setOnAction(action -> {
			try {
				backupArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        
        restoreButton = new Button("Restore Article");
        restoreButton.setOnAction(action -> {
			try {
				restoreArticle(database);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        displayMessagesButton = new Button("Display Help Messages");
        displayMessagesButton.setOnAction(action -> {
			try {
				//Display all help messages
				database.displayMessages();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        sendMessageButton = new Button("Help");
        sendMessageButton.setOnAction(action -> {
			try {
				//Send a help message
				sendMessage(database);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        if (role == "Admin")
        {
        	layout.getChildren().addAll(addButton, viewButton, viewAllArticlesButton, searchButton, editButton, deleteButton, backupButton, restoreButton, displayMessagesButton);
        } else {
        	layout.getChildren().addAll(viewButton, viewAllArticlesButton, searchButton, sendMessageButton);
        }
        Scene theScene = new Scene(layout, 800, 600);
        return theScene;
    }

    private void addArticle(DatabaseHelper database) throws SQLException
    {    	
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        titleField = new TextField();
        titleField.setPromptText("Enter the title:");
        
        headerField = new TextField();
        headerField.setPromptText("Enter the header:");
        
        articleGroupField = new TextField();
        articleGroupField.setPromptText("Enter the article group:");
        
        descriptionField = new TextField();
        descriptionField.setPromptText("Enter the description:");
        
        keywordsField = new TextField();
        keywordsField.setPromptText("Enter the keywords:");
        
        bodyField = new TextField();
        bodyField.setPromptText("Enter the body of the article:");
        
        referencesField = new TextField();
        referencesField.setPromptText("Enter the references:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean added = handleSubmit(database, "add");
				if (added)
				{
					popupStage.close(); // close the pop up window
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(titleField, headerField, articleGroupField, descriptionField, keywordsField, bodyField, referencesField, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Add Article");
        popupStage.show();
    }

    private void viewArticle(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        searchByTitle = new TextField();
        searchByTitle.setPromptText("Enter the title:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean viewed = handleSubmit(database, "view");
				if (viewed)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(searchByTitle, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("View Article");
        popupStage.show();
    }
    
    private void viewAllArticles(DatabaseHelper database) throws SQLException
    {    	
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

    	ObservableList<String> levelOptions = FXCollections.observableArrayList("All", "Beginner", "Intermediate", "Advanced", "Expert");
        contentLevel = new ComboBox<>(levelOptions);
        contentLevel.setValue("All"); // default choice is all
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean viewed = handleSubmit(database, "viewAll");
				if (viewed)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(contentLevel, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("View Article");
        popupStage.show();

    }
    
    private void searchArticle(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        search = new TextField();
        search.setPromptText("Enter the keyword of the article to search:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				handleSubmit(database, "search");
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(search, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Search Article");
        popupStage.show();

    }
    
    private void editArticle(DatabaseHelper database) throws SQLException
    {
    	//TODO: Implement edit functionality; delete the article and add a new one with new info
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);
        
        titleField = new TextField();
        titleField.setPromptText("Enter the NEW title:");
        
        headerField = new TextField();
        headerField.setPromptText("Enter the NEW header:");
        
        articleGroupField = new TextField();
        articleGroupField.setPromptText("Enter the NEW article group:");
        
        descriptionField = new TextField();
        descriptionField.setPromptText("Enter the NEW description:");
        
        keywordsField = new TextField();
        keywordsField.setPromptText("Enter the NEW keywords:");
        
        bodyField = new TextField();
        bodyField.setPromptText("Enter the NEW body of the article:");
        
        referencesField = new TextField();
        referencesField.setPromptText("Enter the NEW references:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean edited = handleSubmit(database, "edit");
				if (edited)
				{
					popupStage.close(); // close the pop up window
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(titleField, headerField, articleGroupField, descriptionField, keywordsField, bodyField, referencesField, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit Article");
        popupStage.show();
    }
    
    private void deleteArticle(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        deleteByTitle = new TextField();
        deleteByTitle.setPromptText("Enter the title of the article to be deleted:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean deleted = handleSubmit(database, "delete");
				if (deleted)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(deleteByTitle, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Delete Article");
        popupStage.show();

    }
    
    private void backupArticle(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        fileName = new TextField();
        fileName.setPromptText("Enter the file name where to back up:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean backedup = handleSubmit(database, "backup");
				if (backedup)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(fileName, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Backup Articles");
        popupStage.show();
    }
    
    private void restoreArticle(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        fileName = new TextField();
        fileName.setPromptText("Enter the file name from where to restore:");
        
        ObservableList<String> roleOptions = FXCollections.observableArrayList("No", "Yes");
        replaceArticles = new ComboBox<>(roleOptions);
        replaceArticles.setValue("No"); // default answer is no
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean restored = handleSubmit(database, "backup");
				if (restored)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(fileName, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Restore Articles");
        popupStage.show();
    }
    
    private void sendMessage(DatabaseHelper database) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);
        
        messageTitle = new TextField();
        messageTitle.setPromptText("Enter the message title");

        messageBody = new TextField();
        messageBody.setPromptText("Enter the message body");
        
        ObservableList<String> typeOptions = FXCollections.observableArrayList("Generic", "Specific");
        messageType = new ComboBox<>(typeOptions);
        messageType.setValue("Generic"); // default answer is generic

        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean sent = handleSubmit(database, "sendMessage");
				if (sent)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(messageTitle, messageBody, messageType, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Send a Help Message");
        popupStage.show();
    }
    
    private boolean handleSubmit(DatabaseHelper database, String type) throws SQLException
    {
    	//if the type is add then try adding the article to the database
    	if (type == "add")
    	{
    		//extract all the information from the fields
    		String title = titleField.getText();
    		String header = headerField.getText();
    		String articleGroupString = articleGroupField.getText();
    		char[] articleGroup = articleGroupString.toCharArray();
    		String description = descriptionField.getText();
    		String keywordsString = keywordsField.getText();
    		char[] keywords = keywordsString.toCharArray();
    		String body = bodyField.getText();
    		String references = referencesField.getText();
    		
    		if (title.isEmpty() || header.isEmpty() || articleGroupString.isEmpty() ||
    			description.isEmpty() || keywordsString.isEmpty() || body.isEmpty() || references.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByKeyword(keywordsString);
    		
    		if (exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article already exists.");
                return false;
    		}
    		
    		try {
				database.addArticle(title, header, articleGroup, description, keywords, body, references);
			} catch (Exception e) {
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				e.printStackTrace();
				return false;
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article successfully added!\nThis window will now close");
    		return true; //article was added
    	}
    	else if (type == "view")
    	{
    		String title = searchByTitle.getText();
    		
    		if (title.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid title.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByTitle(title);
    		
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false;
    		}

    		
    		try {
    			database.decryptDisplayArticles(title);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article displayed in the console!\nThis window will now close");
    		return true; // article was successfully viewed
    	}
    	
    	else if (type == "viewAll")
    	{
    		String level = contentLevel.getValue().toLowerCase();
    		
    		try {
    			// if the level is "All", display all articles with the title
    			if (level.equals("all"))
    			{
    				database.decryptDisplayAllArticles();
    			}
    			else
    			{
    				database.decryptDisplayArticlesByLevel(level);
    			}
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article displayed in the console!\nThis window will now close");
    		return true; // article was successfully viewed
    	}
    	
    	else if (type == "search")
    	{
    		String keywords = search.getText();
    		
    		if (keywords.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid keywords.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByKeyword(keywords);
    		
    		if (exists)
    		{
                createAlert(Alert.AlertType.INFORMATION, "Success", "Article exists!");
        		return true; // article does exist
    		}
    		else
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false; // article doesn't exist
    		}

    	}
    	else if (type == "edit")
    	{
    		//extract all necessary information
    		String newTitle = titleField.getText();
    		String newHeader = headerField.getText();
    		String newArticleGroupString = articleGroupField.getText();
    		char[] newArticleGroup = newArticleGroupString.toCharArray();
    		String newDescription = descriptionField.getText();
    		String newKeywordsString = keywordsField.getText();
    		char[] newKeywords = newKeywordsString.toCharArray();
    		String newBody = bodyField.getText();
    		String newReferences = referencesField.getText();
    		
    		Article toBeEdited;
    		
    		//if the fields are empty, we prompt the user
    		if (newTitle.isEmpty() || newHeader.isEmpty() || newArticleGroupString.isEmpty() ||
    				newDescription.isEmpty() || newKeywordsString.isEmpty() || newBody.isEmpty() || newReferences.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByKeyword(newKeywordsString);
    		
    		//if the article doesn't exist, prompt the user
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false; // article doesn't exist
    		}
    		
    		//we return an article that matches the keywords
    		try {
				toBeEdited = database.returnArticleByKeyword(newKeywordsString);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		
    		String oldTitle = toBeEdited.getTitle();
    		
    		//delete the old article and add a new one with updated information
    		try {
				database.deleteArticle(oldTitle);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		
    		try {
				database.addArticle(newTitle, newHeader, newArticleGroup, newDescription, newKeywords, newBody, newReferences);
			} catch (Exception e) {
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				e.printStackTrace();
				return false;
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article successfully updated!\nThis window will now close");
    		return true; //article was updated
    		
    	}
    	else if (type == "delete")
    	{
    		String title = deleteByTitle.getText();
    		
    		//if the title is empty, prompt the user
    		if (title.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid title.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByTitle(title);
    		
    		//if the article doesn't exist, prompt the user
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false;
    		}
    		
    		try {
				database.deleteArticle(title);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false; //article wasn't deleted
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article was deleted!\nThis window will now close");
    		return true; // article was deleted
    	}
    	else if (type == "backup")
    	{
    		String file = fileName.getText();
    		
    		//if the fileName field is empty, prompt the user
    		if (file.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid filename.");
                return false;
    		}
    		
    		try {
				database.backupArticles(file);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false; // articles weren't backed up
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Articles were backed up!\nThis window will now close");
    		return true; // articles were backed up

    	}
    	else if (type == "sendMessage")
    	{
    		String title = messageTitle.getText();
    		String body = messageBody.getText();
    		String typeOfMessage = messageType.getValue();
    		
    		if (title.isEmpty() || body.isEmpty() || typeOfMessage.isEmpty())
        	{
        		createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
        	}
        		
        	try {
    			database.addMessage(title, body, typeOfMessage);
    		} catch (Exception e) {
    			createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
    			e.printStackTrace();
    			return false;
    		}
        	createAlert(Alert.AlertType.INFORMATION, "Success", "Message successfully sent!\nThis window will now close");
        	return true; //article was added
    	}
    	else
    	{
    		//else, we RESTORE the articles
    		String file = fileName.getText();
    		String replaceString = replaceArticles.getValue();
    		boolean replace = true ? (replaceString == "Yes") : false;
    		
    		//if the fileName field is empty, prompt the user
    		if (file.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid filename.");
                return false;
    		}
    		
    		try {
				database.restoreArticles(file, replace);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false; // articles weren't restored
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Articles were restored!\nThis window will now close");
    		return true; // articles were restored

    	}
    	
    }
    
    //function for providing the user with alerts
    private void createAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
