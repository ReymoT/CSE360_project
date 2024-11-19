package phaseThree;

/*******
 * <p> Role Class </p>
 * 
 * <p> Description: An implementation of the Role class to be used to specify the role and related actions of a user</p>
 * 
 * @author Reimo
 * @author Leya
 * 
 */

public class Role
{
	private String roleName; //all roles in one string
	
	public static void main(String[] args)
	{
		
	}
	
	public Role(String roleName)
	{
		this.roleName = roleName;
	}
	
	//getters and setters for the private variables
	public String getRoleName()
	{
		return this.roleName;
	}
	
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
}