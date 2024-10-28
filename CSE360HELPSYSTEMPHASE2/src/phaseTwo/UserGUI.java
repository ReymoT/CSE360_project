package passwordEvaluationTestbed;

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
 * <p> UserGUI Class </p>
 * 
 * <p> Description: A JavaFX GUI of the login screen </p>
 * 
 * @author Reimo
 * @author Leya
 * 
 */

public class UserGUI extends Application
{
    private TextField emailField; // field to enter the email
    private TextField userNameField; // field to enter the user name
    private PasswordField passwordField; // field to enter the password
    private TextField firstNameField; // field to enter the first name
    private TextField middleNameField; // field to enter the middle name
    private TextField lastNameField; // field to enter the last name
    private TextField preferredNameField; // field to enter the preferred name
    private Button loginButton; // button to log in
    private Button registerButton; // button to register
    private ComboBox<String> roleList; // drop-down list for choosing the role
    
    private DatabaseHelper database; //new instance of the database helper
    
    public UserGUI() throws Exception
    {
    	database = new DatabaseHelper();
    }
    
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage theStage)
    {
    	theStage.setTitle("CSE 360 Project Phase One");

        userNameField = new TextField();
        userNameField.setPromptText("Enter your username");
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        
        ObservableList<String> roleOptions = FXCollections.observableArrayList("Student", "Instructor", "Admin");
        roleList = new ComboBox<>(roleOptions);
        roleList.setValue("Student"); // default role is student
        
        firstNameField = new TextField();
        firstNameField.setPromptText("Enter your first name");
        
        middleNameField = new TextField();
        middleNameField.setPromptText("Enter your middle name");
        
        lastNameField = new TextField();
        lastNameField.setPromptText("Enter your last name");
        
        preferredNameField = new TextField();
        preferredNameField.setPromptText("Enter your preferred name");

        loginButton = new Button("Login");
        loginButton.setOnAction(action -> {
			try {
				handleLogin();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        registerButton = new Button("Register");
        registerButton.setOnAction(action -> {
			try {
				handleRegistration();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});

        VBox layout = new VBox(10);
        layout.getChildren().addAll(userNameField, emailField, passwordField, roleList, firstNameField, middleNameField, lastNameField, preferredNameField, loginButton, registerButton);

        Scene theScene = new Scene(layout, 300, 200);
        theStage.setScene(theScene);
        theStage.show();
    }

    private void handleLogin() throws SQLException
    {
        String email = emailField.getText();
        String password = passwordField.getText();
        String roleValue = roleList.getValue();

        if (email.isEmpty() || password.isEmpty())
        {
        	createAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter your email and password.");
            return;
        }
        
        if (database.doesUserExist(email) == false)
        {
        	createAlert(Alert.AlertType.ERROR, "Login Failed", "User does not exist");
        }

        if (database.login(email, password, roleValue))
        {
        	createAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + email);
        } 
        else
        {
        	createAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private void handleRegistration() throws SQLException
    {
    	// get all necessary values from the fields
    	String userName = userNameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String lastName = lastNameField.getText();
        String preferredName = preferredNameField.getText();
        String roleValue = roleList.getValue();
        
        char[] securePassword = password.toCharArray(); //non-string type secure password
        Role role = new Role(roleValue); // new instance of a role
        

        if (email.isEmpty() || password.isEmpty()) {
        	createAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter both email and password.");
            return;
        }

        if (database.doesUserExist(email)) {
        	createAlert(Alert.AlertType.ERROR, "Registration Failed", "User already exists.");
            return;
        }


        // Instance of a new user
        User newUser = new User(email, userName, securePassword, firstName, middleName, lastName, preferredName, role);
        database.register(email, password, roleValue); // register the new user

        createAlert(Alert.AlertType.INFORMATION, "Registration Successful", "Account created successfully.");
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
