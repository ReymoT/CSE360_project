package phaseTwo;

//JavaFX imports
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
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
 * 
 */

public class Dashboard
{
    private Button addButton; // button to add articles
    private Button viewButton; // button to view articles
    private Button searchButton; // button to search for articles
    private Button editButton; // button to edit articles
    private Button deleteButton; // button to edit articles
    private Button backupButton; // button to edit articles
    private Button restoreButton; // button to edit articles
    private Button logoutButton; // button to edit articles

    public Scene createScene()
    {
    	VBox layout = new VBox(10);
        addButton = new Button("Add Article");
        addButton.setOnAction(action -> {
			try {
				addArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        viewButton = new Button("View Article");
        viewButton.setOnAction(action -> {
			try {
				viewArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        searchButton = new Button("Search Article");
        searchButton.setOnAction(action -> {
			try {
				searchArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        editButton = new Button("Edit Article");
        editButton.setOnAction(action -> {
			try {
				editArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        deleteButton = new Button("Delete Article");
        deleteButton.setOnAction(action -> {
			try {
				deleteArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        backupButton = new Button("Backup Article");
        backupButton.setOnAction(action -> {
			try {
				backupArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        
        restoreButton = new Button("Restore Article");
        restoreButton.setOnAction(action -> {
			try {
				restoreArticle();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        logoutButton = new Button("Log Out");
        logoutButton.setOnAction(action -> {
			try {
				handleLogout();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
        
        layout.getChildren().addAll(addButton, viewButton, searchButton, editButton, deleteButton, backupButton, restoreButton, logoutButton);
        Scene theScene = new Scene(layout, 300, 200);
        return theScene;
    }

    private void addArticle() throws SQLException
    {
        
    }

    private void viewArticle() throws SQLException
    {
    	
    }
    
    private void searchArticle() throws SQLException
    {
    	
    }
    
    private void editArticle() throws SQLException
    {
    	
    }
    
    private void deleteArticle() throws SQLException
    {
    	
    }
    
    private void backupArticle() throws SQLException
    {
    	
    }
    
    private void restoreArticle() throws SQLException
    {
    	
    }
    
    private void handleLogout() throws SQLException
    {
    	
    }
}
