package phaseTwo;

/*******
 * <p> Article Class </p>
 * 
 * <p> Description: An article program for providing an overall blueprint for an article </p>
 * 
 * @author Reimo
 * @author Morad
 * 
 */

class Article
{
	private String title; //variable for title
	private String header; //variable for the header
	private char[] articleGroup; //variable for the article group
	private String description; //variable for abstract
	private char[] keywords; //variable for a set of keywords
	private String body; //variable for the body
	private String references; //variable for the references
	
	public static void main(String[] args)
	{
		
	}
	
	public Article(String title, String header, char[] articleGroup, String description, char[] keywords, String body, String references)
	{
		this.title = title;
		this.header = header;
		this.articleGroup = articleGroup;
		this.description = description;
		this.keywords = keywords;
		this.body = body;
		this.references = references;
	}
	
	//getters for the private variables
	public String getTitle()
	{
		return this.title;
	}
	
	public String getHeader()
	{
		return this.header;
	}
	
	public char[] getArticleGroup()
	{
		return this.articleGroup;
	}
	
	public String getDescription()
	{
		return this.description;
	}
	
	public char[] getKeywords()
	{
		return this.keywords;
	}
	
	public String getBody()
	{
		return this.body;
	}
	
	public String getReferences()
	{
		return this.references;
	}
	
}