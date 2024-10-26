package phaseTwo;

import java.time.*;
import java.util.ArrayList;
import java.util.Scanner;

/*******
 * <p> User Class </p>
 * 
 * <p> Description: An implementation of the User class to use as a blueprint of a user </p>
 * 
 * @author Reimo
 * @author Leya
 * 
 */

public class User
{
	private String email; //email of the user
	private String userName; //user name of the user
	private char[] password; //password of the user
	private boolean flag; //flag indicating the password is a one time password
	private LocalDateTime oneTimePasswordExpiry; //LocalDateTime type indicating the expiration date of the onetime password
	private String firstName, middleName, lastName, preferredName; //first, middle, last and preferred name
	private ArrayList<String> beginnerList = new ArrayList<String>(); //list for beginner topics
	private ArrayList<String> intermediateList = new ArrayList<String>(); //list for intermediate topic
	private ArrayList<String> advancedList = new ArrayList<String>(); //list for advanced topics
	private ArrayList<String> expertList = new ArrayList<String>(); //list for expert topics
	private Role role; //role of type Role
	
	//main function
	public static void main(String[] args)
	{
		
	}
	
	//constructor for the private variables
	public User(String email, String userName, char[] password, String firstName, String middleName, String lastName, String preferredName, Role role) 
	{
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.firstName = firstName;
		this.middleName = middleName;
		this.lastName = lastName;
		this.preferredName = preferredName;
		this.role = role;
	}
	
	//getters and setters for the user variables
	public void setEmail(String email)
	{
		this.email = email;
	}
	
	public void setUsername(String userName)
	{
		this.userName = userName;
	}
	
	public void setPassword(char[] password)
	{
		this.password = password;
	}
	
	public void setFlag(boolean flag)
	{
		this.flag = flag;
	}
	
	public void setExpiryDate(LocalDateTime oneTimePasswordExpiry)
	{
		this.oneTimePasswordExpiry = oneTimePasswordExpiry;
	}
	
	public void setRole(Role role)
	{
		this.role = role;
	}
	
	
	public String getEmail()
	{
		return this.email;
	}
	
	public String getUsername()
	{
		return this.userName;
	}
	
	public String getPassword()
	{
		return this.password.toString();
	}
	
	public boolean getFlag()
	{
		return this.flag;
	}
	
	public LocalDateTime getExpiryDate()
	{
		return this.oneTimePasswordExpiry;
	}
	
	public Role getRole()
	{
		return this.role;
	}
}