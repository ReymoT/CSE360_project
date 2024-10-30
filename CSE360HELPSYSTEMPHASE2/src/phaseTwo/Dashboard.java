package phaseTwo;

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
    
    private TextField titleField; // field to enter the title
	private TextField headerField; // field to enter the header
	private TextField articleGroupField; // field to enter the article group
	private TextField descriptionField; // field to enter the description
	private TextField keywordsField; // field to enter the keywords
	private TextField bodyField; // field to enter the body
	private TextField referencesField; // field to enter the references
	private Button submitButton; // button to submit the data
	
	private TextField searchByKeyword; // field to search articles by the keywords
	
	private TextField deleteByTitle; // field to delete articles by the title
	
	private TextField search; // field to search for articles
	
	private TextField fileName; // field to get the backup filename
	
	private ComboBox<String> replaceArticles; // drop-down list for choosing whether to replace the articles


    public Scene createScene(DatabaseHelper database)
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
                
        layout.getChildren().addAll(addButton, viewButton, searchButton, editButton, deleteButton, backupButton, restoreButton);
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

        searchByKeyword = new TextField();
        searchByKeyword.setPromptText("Enter the keyword:");
        
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

        popupLayout.getChildren().addAll(searchByKeyword, submitButton);
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
    
    private boolean handleSubmit(DatabaseHelper database, String type) throws SQLException
    {
    	//if the type is add then try adding the article to the database
    	if (type == "add")
    	{
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
			}
    		createAlert(Alert.AlertType.INFORMATION, "Success", "Article successfully added!\nThis window will now close");
    		return true; //article was added
    	}
    	else if (type == "view")
    	{
    		String keywords = searchByKeyword.getText();
    		
    		if (keywords.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid keywords.");
                return false;
    		}
    		
    		boolean exists = database.articleExistsByKeyword(keywords);
    		
    		if (!exists)
    		{
    			createAlert(Alert.AlertType.ERROR, "Error!", "Article doesn't exist.");
                return false;
    		}

    		
    		try {
				database.displayArticleByKeyword(keywords);
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
    		//TODO: implement search functionality
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
    		//TODO: implement edit functionality; delete given article and add again with new information
    	}
    	else if (type == "delete")
    	{
    		String title = deleteByTitle.getText();
    		
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
				database.deleteArticle(title);
			} catch (Exception e) {
				e.printStackTrace();
				createAlert(Alert.AlertType.ERROR, "Error!", "Something went wrong.\nPlease try again");
				return false;
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
    	else
    	{
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
    	
    	return false;
    	
    }
    
    private void createAlert(Alert.AlertType type, String title, String message)
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
