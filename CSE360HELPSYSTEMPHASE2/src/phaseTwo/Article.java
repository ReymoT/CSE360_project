package phaseTwo;

import java.util.ArrayList;

class Article
{
	private String header; //variable for the header
	private String title; //variable for title
	private String author; //variable for author
	private String description; //variable for abstract
	private ArrayList<String> keywords; //variable for a set of keywords
	private String body; //variable for the body
	private ArrayList<String> references; //variable for the references
	
	public static void main(String[] args)
	{
		
	}
	
	public Article(String title, String author, String description, ArrayList<String> keywords, String body, ArrayList<String> references)
	{
		this.title = title;
		this.author = author;
		this.description = description;
		this.keywords = keywords;
		this.body = body;
		this.references = references;
	}
	
}