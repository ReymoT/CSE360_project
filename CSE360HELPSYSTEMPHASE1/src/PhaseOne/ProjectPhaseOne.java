package PhaseOne;

import java.time.*;
import java.util.ArrayList;

public class ProjectPhaseOne
{
	private String email;
	private String userName;
	private char[] password;
	private boolean flag;
	private LocalDateTime oneTimePasswordExpiry;
	private String firstName, middleName, lastName, preferredName;
	private ArrayList<String> beginnerList = new ArrayList<String>();
	private ArrayList<String> intermediateList = new ArrayList<String>();
	private ArrayList<String> advancedList = new ArrayList<String>();
	private ArrayList<String> expertList = new ArrayList<String>();
	private String role;
	
	public static void main(String[] args)
	{
		
	}
	
	
	public ProjectPhaseOne(String email, String userName, char[] password, String role) 
	{
		this.email = email;
		this.userName = userName;
		this.password = password;
		this.role = role;
	}
}