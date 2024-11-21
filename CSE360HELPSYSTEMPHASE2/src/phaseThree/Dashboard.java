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
import java.util.Arrays;
import java.util.List;

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
    private Button modifyRightsButton; //button to modify rights
    private Button modifyStudentsButton; // button to modify student groups
    private ComboBox<String> newGroup; // list for choosing a new group for the student
    
    private TextField titleField; // field to enter the title
	private TextField headerField; // field to enter the header
	private TextField authorField; // field to enter the author
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
	
	private TextField emailField; // field for entering the email
	private TextField rightsField; // field for entering the rights
	
	private ComboBox<String> contentLevel; // drop-down list for choosing the content level of articles


    public Scene createScene(DatabaseHelper database, String role, String rights, String group)
    {
    	VBox layout = new VBox(10);
        addButton = new Button("Add Article");
        addButton.setOnAction(action -> {
			try {
				addArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        viewButton = new Button("View Article");
        viewButton.setOnAction(action -> {
			try {
				viewArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        viewAllArticlesButton = new Button("View All Articles");
        viewAllArticlesButton.setOnAction(action -> {
			try {
				viewAllArticles(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        searchButton = new Button("Search Article");
        searchButton.setOnAction(action -> {
			try {
				searchArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        editButton = new Button("Edit Article");
        editButton.setOnAction(action -> {
			try {
				editArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(action -> {
			try {
				deleteArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        backupButton = new Button("Backup Article");
        backupButton.setOnAction(action -> {
			try {
				backupArticle(database, group);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        
        restoreButton = new Button("Restore Article");
        restoreButton.setOnAction(action -> {
			try {
				restoreArticle(database, group);
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
				sendMessage(database, group);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        modifyRightsButton = new Button("Modify Rights");
        modifyRightsButton.setOnAction(action -> {
			try {
				modifyRights(database, group);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        modifyStudentsButton = new Button("Modify Students");
        modifyStudentsButton.setOnAction(action -> {
			try {
				modifyStudents(database, group);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
        
        List<String> userRights = Arrays.asList(rights.split(",\\s*"));
		if (userRights.contains("a"))
		{
			layout.getChildren().add(addButton);
		}
		if (userRights.contains("v"))
		{
			layout.getChildren().add(viewButton);
			layout.getChildren().add(viewAllArticlesButton);
			layout.getChildren().add(sendMessageButton);
		}
		if (userRights.contains("s"))
		{
			layout.getChildren().add(searchButton);
		}
		if (userRights.contains("e"))
		{
			layout.getChildren().add(editButton);
		}
		if (userRights.contains("d"))
		{
			layout.getChildren().add(deleteButton);
		}
		if (userRights.contains("b"))
		{
			layout.getChildren().add(backupButton);
		}
		if (userRights.contains("r"))
		{
			layout.getChildren().add(restoreButton);
		}
		if (userRights.contains("h"))
		{
			layout.getChildren().add(displayMessagesButton);
			layout.getChildren().add(modifyRightsButton);
		}
		if (userRights.contains("m"))
		{
			layout.getChildren().add(modifyStudentsButton);
		}
        Scene theScene = new Scene(layout, 800, 600);
        return theScene;
    }

    private void addArticle(DatabaseHelper database, String group) throws SQLException
    {    	
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        titleField = new TextField();
        titleField.setPromptText("Enter the title:");
        
        headerField = new TextField();
        headerField.setPromptText("Enter the header:");
        
        authorField = new TextField();
        authorField.setPromptText("Enter the author:");
        
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
				boolean added = handleSubmit(database, "add", group);
				if (added)
				{
					popupStage.close(); // close the pop up window
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(titleField, headerField, descriptionField, keywordsField, bodyField, referencesField, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Add Article");
        popupStage.show();
    }

    private void viewArticle(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        searchByTitle = new TextField();
        searchByTitle.setPromptText("Enter the title:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean viewed = handleSubmit(database, "view", group);
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
    
    private void viewAllArticles(DatabaseHelper database, String group) throws SQLException
    {    	
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

    	ObservableList<String> levelOptions = FXCollections.observableArrayList("All", "Beginner", "Intermediate", "Advanced", "Expert");
        contentLevel = new ComboBox<>(levelOptions);
        contentLevel.setValue("All"); // default choice is all
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean viewed = handleSubmit(database, "viewAll", group);
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
    
    private void searchArticle(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        search = new TextField();
        search.setPromptText("Enter the keyword of the article to search:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean success = handleSubmit(database, "search", group);
				if (success)
				{
					
				}
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
    
    private void editArticle(DatabaseHelper database, String group) throws SQLException
    {
    	//TODO: Implement edit functionality; delete the article and add a new one with new info
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);
        
        titleField = new TextField();
        titleField.setPromptText("Enter the NEW title:");
        
        headerField = new TextField();
        headerField.setPromptText("Enter the NEW header:");
        
        authorField = new TextField();
        authorField.setPromptText("Enter the NEW author:");
        
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
				boolean edited = handleSubmit(database, "edit", group);
				if (edited)
				{
					popupStage.close(); // close the pop up window
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(titleField, headerField, descriptionField, keywordsField, bodyField, referencesField, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Edit Article");
        popupStage.show();
    }
    
    private void deleteArticle(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        deleteByTitle = new TextField();
        deleteByTitle.setPromptText("Enter the title of the article to be deleted:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean deleted = handleSubmit(database, "delete", group);
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
    
    private void backupArticle(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);

        fileName = new TextField();
        fileName.setPromptText("Enter the file name where to back up:");
        
        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean backedup = handleSubmit(database, "backup", group);
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
    
    private void restoreArticle(DatabaseHelper database, String group) throws SQLException
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
				boolean restored = handleSubmit(database, "backup", group);
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
    
    private void sendMessage(DatabaseHelper database, String group) throws SQLException
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
				boolean sent = handleSubmit(database, "sendMessage", group);
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
    
    private void modifyRights(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);
        
        emailField = new TextField();
        emailField.setPromptText("Enter the email");

        rightsField = new TextField();
        rightsField.setPromptText("Enter the rights");

        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean sent = handleSubmit(database, "modifyRights", group);
				if (sent)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(emailField, rightsField, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Modify Rights of Users");
        popupStage.show();
    }
    
    private void modifyStudents(DatabaseHelper database, String group) throws SQLException
    {
    	Stage popupStage = new Stage();
        VBox popupLayout = new VBox(10);
        
        emailField = new TextField();
        emailField.setPromptText("Enter the email");

        ObservableList<String> groupOptions = FXCollections.observableArrayList("Group 1", "Group 2", "Group 3");
        newGroup = new ComboBox<>(groupOptions);
        newGroup.setValue("Group 1"); // default role is Group 1

        submitButton = new Button("Submit");
        submitButton.setOnAction(action -> {
        	try {
				boolean sent = handleSubmit(database, "modifyStudents", group);
				if (sent)
				{
					popupStage.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
        });

        popupLayout.getChildren().addAll(emailField, newGroup, submitButton);
        Scene popupScene = new Scene(popupLayout, 600, 400);
        
        popupStage.setScene(popupScene);
        popupStage.setTitle("Modify Groups of Users");
        popupStage.show();
    }
    
    private boolean handleSubmit(DatabaseHelper database, String type, String group) throws SQLException
    {
    	//if the type is add then try adding the article to the database
    	if (type == "add")
    	{
    		//extract all the information from the fields
    		String title = titleField.getText();
    		String header = headerField.getText();
    		String author = authorField.getText();
    		String description = descriptionField.getText();
    		String keywordsString = keywordsField.getText();
    		char[] keywords = keywordsString.toCharArray();
    		String body = bodyField.getText();
    		String references = referencesField.getText();
    		
    		char[] groupValue = group.toCharArray();
    		
    		if (title.isEmpty() || header.isEmpty() || author.isEmpty() ||
    			description.isEmpty() || keywordsString.isEmpty() || body.isEmpty() || references.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByTitle(title, group);
    		
    		if (exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article with that title already exists.");
                return false;
    		}
    		
    		try {
				database.addArticle(title, header, author, groupValue, description, keywords, body, references);
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
    		
    		boolean exists = database.articleExistsByTitle(title, group);
    		
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist in your group.");
                return false;
    		}

    		
    		try {
    			database.decryptDisplayArticles(title, group);
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
    				database.decryptDisplayAllArticles(group);
    			}
    			else
    			{
    				database.decryptDisplayArticlesByLevel(level, group);
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
    		
    		try
    		{
    			database.decryptDisplayArticlesByKeyword(keywords, group);
    		} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article displayed in the console!");
    		return true;
    	}
    	else if (type == "edit")
    	{
    		//extract all necessary information
    		String newTitle = titleField.getText();
    		String newHeader = headerField.getText();
    		String newAuthor = authorField.getText();
    		char[] newArticleGroup = group.toCharArray();
    		String newDescription = descriptionField.getText();
    		String newKeywordsString = keywordsField.getText();
    		char[] newKeywords = newKeywordsString.toCharArray();
    		String newBody = bodyField.getText();
    		String newReferences = referencesField.getText();
    		
    		Article toBeEdited;
    		
    		//if the fields are empty, we prompt the user
    		if (newTitle.isEmpty() || newHeader.isEmpty() || newAuthor.isEmpty() ||
    				newDescription.isEmpty() || newKeywordsString.isEmpty() || newBody.isEmpty() || newReferences.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByKeyword(newKeywordsString, group);
    		
    		//if the article doesn't exist, prompt the user
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false; // article doesn't exist
    		}
    		
    		//we return an article that matches the keywords
    		try {
				toBeEdited = database.returnArticleByKeyword(newKeywordsString, group);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		
    		String oldTitle = toBeEdited.getTitle();
    		
    		//delete the old article and add a new one with updated information
    		try {
				database.deleteArticle(oldTitle, group);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
			}
    		
    		try {
				database.addArticle(newTitle, newHeader,  newAuthor, newArticleGroup, newDescription, newKeywords, newBody, newReferences);
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
    		
    		boolean exists = database.articleExistsByTitle(title, group);
    		
    		//if the article doesn't exist, prompt the user
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false;
    		}
    		
    		try {
				database.deleteArticle(title, group);
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
    	else if (type == "modifyRights")
    	{
    		String email = emailField.getText();
    		String rights = rightsField.getText();
    		
    		if (email.isEmpty() || rights.isEmpty())
        	{
        		createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
        	}
    		
    		try {
    			database.updateUserRights(email, rights);
    		} catch (Exception e) {
    			createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
    			e.printStackTrace();
    			return false;
    		}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Rights successfully modified!\nThis window will now close");
    		return true;
    	}
    	else if (type == "modifyStudents")
    	{
    		String email = emailField.getText();
    		String studentGroup = newGroup.getValue();
    		
    		if (email.isEmpty() || studentGroup.isEmpty())
        	{
        		createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid information.");
                return false;
        	}
    		
    		try {
    			database.updateUserGroup(email, studentGroup);
    		} catch (Exception e) {
    			createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
    			e.printStackTrace();
    			return false;
    		}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Group members have been updated!\nThis window will now close");
    		return true;
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
