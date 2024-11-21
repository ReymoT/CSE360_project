package phaseThree;

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
    private ComboBox<String> accessGroup; //drop-down list for choosing the special access group
    private Button testCases; // button for executing test cases
    
    String rights = null; // rights to be passed to the dashboard
    
    int passedCases = 0;
    int failedCases = 0;
    
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
    	theStage.setTitle("Login Screen");

        userNameField = new TextField();
        userNameField.setPromptText("Enter your username");
        
        emailField = new TextField();
        emailField.setPromptText("Enter your email");

        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        
        ObservableList<String> roleOptions = FXCollections.observableArrayList("Student", "Instructor", "Admin");
        roleList = new ComboBox<>(roleOptions);
        roleList.setValue("Student"); // default role is student
        
        ObservableList<String> groupOptions = FXCollections.observableArrayList("Group 1", "Group 2", "Group 3");
        accessGroup = new ComboBox<>(groupOptions);
        accessGroup.setValue("Group 1"); // default role is Group 1
        
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
				boolean validUser = handleLogin();
				if (validUser)
				{
					String role = roleList.getValue();
					String group = accessGroup.getValue();
					String email = emailField.getText();
					Dashboard dashboard = new Dashboard();
		        	Scene dashboardScene = dashboard.createScene(database, role, rights, group);

		            theStage.setScene(dashboardScene);
		            theStage.setTitle("Home Screen");
				}
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

        testCases = new Button("EXECUTE TEST CASES");
        testCases.setOnAction(action -> {
			testFunction();
		});

        
        VBox layout = new VBox(10);
        layout.getChildren().addAll(userNameField, emailField, passwordField, roleList, accessGroup, firstNameField, middleNameField, lastNameField, preferredNameField, loginButton, registerButton, testCases);

        Scene theScene = new Scene(layout, 800, 600);
        theStage.setScene(theScene);
        theStage.show();
    }

    private boolean handleLogin() throws SQLException
    {
        String email = emailField.getText();
        String password = passwordField.getText();
        String roleValue = roleList.getValue();
        String groupValue = accessGroup.getValue();

        if (email.isEmpty() || password.isEmpty())
        {
        	createAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter your email and password.");
            return false;
        }
        
        if (database.doesUserExist(email) == false)
        {
        	createAlert(Alert.AlertType.ERROR, "Login Failed", "User does not exist");
        	return false;
        }

        if (database.login(email, password, roleValue, groupValue))
        {
        	createAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + email + "\nYour group is: " + groupValue);
        	rights = database.getUserRights(email); // update user rights
        	System.out.println(rights);
        	database.displayAllUsers();
        	return true;
        } 
        else
        {
        	createAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password or group number.");
        	return false;
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
        String groupValue = accessGroup.getValue();
        
        /*
         * Add - a
         * View - v
         * Search - s
         * Edit - e
         * Delete - d
         * Backup - b
         * Restore - r
         * Display Help - h
         * Modify Student Members - m
         */
        
        String rightsValue = null;
        
        if (roleValue.equals("Student"))
        {
        	rightsValue = "v, s";
        }
        else if (roleValue.equals("Instructor"))
        {
        	boolean hasInstructors = database.doesGroupHaveInstructors(groupValue);
        	if (!hasInstructors)
        	{
        		rightsValue = "a, v, s, e, d, b, r, h, m";
        	}
        	else
        	{
        		rightsValue = "a, v, s, e, d, b, r, m";
        	}
        }
        else
        {
        	rightsValue = "a, v, s, e, d, b, r, h";
        }
        rights = rightsValue;
        
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
        //User newUser = new User(email, userName, securePassword, firstName, middleName, lastName, preferredName, role);
        database.register(email, password, roleValue, groupValue, rightsValue); // register the new user

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
    
    private void testFunction()
    {
    	performTestCase("a", "a", "Admin", "Group 1");
    	performTestCase("b", "b", "Student", "Group 1");
    	performTestCase("c", "c", "Instructor", "Group 1");
    	performTestCase("d", "d", "Student", "Group 2");
    	performTestCase("k@gmail.com", "passsssssword", "Instructor", "Group 2");
    	performTestCase("s@hotmail.com", "word", "Student", "Group 3");
    	performTestCase("testtest", "jdjdj", "Student", "Group 3");
    	performTestCase("js", "sd", "Student", "Group 3");
    	performTestCase("test1", "sd", "Student", "Group 4");
    	performTestCase("test2", "jdjdj", "Student", "Group 8");
    	performTestCase("user", "jdjdj", "Student", "Group 1");
    	performTestCase("user4", "jdjdj", "Instructor", "Group 0");
    	
    	System.out.println("PASSED TEST CASES: " + passedCases);
    	System.out.println("FAILED TEST CASES: " + failedCases);
    }
    
    
    private void performTestCase(String email, String password, String role, String group)
    {	
    	boolean loggedin = false;
    	try {
			loggedin = database.login(email, password, role, group);
		} catch (SQLException e) {
			failedCases++;
			e.printStackTrace();
		}
    	if (loggedin)
    	{
    		passedCases++;
    	}
    	else
    	{
    		failedCases++;
    	}
    	
    }
}
