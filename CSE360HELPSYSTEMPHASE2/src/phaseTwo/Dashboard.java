package passwordEvaluationTestbed;

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
				handleSubmit(database, "add");
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
				handleSubmit(database, "view");
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
    	
    }
    
    private void editArticle(DatabaseHelper database) throws SQLException
    {
    	
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
				handleSubmit(database, "delete");
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
    	
    }
    
    private void restoreArticle(DatabaseHelper database) throws SQLException
    {
    	
    }
    
    private void handleSubmit(DatabaseHelper database, String type) throws SQLException
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
                return;
    		}
    		
    		try {
				database.addArticle(title, header, articleGroup, description, keywords, body, references);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	else if (type == "view")
    	{
    		String keywords = searchByKeyword.getText();
    		
    		if (keywords.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter valid keywords.");
                return;
    		}
    		
    		try {
				database.displayArticleByKeyword(keywords);
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    	
    	else if (type == "search")
    	{
    		//TODO: implement search functionality
    	}
    	else if (type == "edit")
    	{
    		//TODO: implement edit functionality
    	}
    	else if (type == "delete")
    	{
    		String title = deleteByTitle.getText();
    		
    		if (title.isEmpty())
    		{
    			createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter a valid title.");
                return;
    		}
    		
    		database.deleteArticle(title);
    	}
    	else if (type == "backup")
    	{
    		//TODO: implement backup functionality
    	}
    	else
    	{
    		//TODO: implement restore functionality
    	}
    	
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
