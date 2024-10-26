package phaseTwo;

import java.util.ArrayList;

class Article
{
	private String title; //variable for title
	private String author; //variable for author
	private String abstractText; //variable for abstract
	private ArrayList<String> keywords; //variable for a set of keywords
	private String body; //variable for the body
	private ArrayList<String> references; //variable for the references
	
	public static void main(String[] args)
	{
		
	}
	
	public Article(String title, String author, String abstractText, ArrayList<String> keywords, String body, ArrayList<String> references)
	{
		this.title = title;
		this.author = author;
		this.abstractText = abstractText;
		this.keywords = keywords;
		this.body = body;
		this.references = references;
	}
	
	
}