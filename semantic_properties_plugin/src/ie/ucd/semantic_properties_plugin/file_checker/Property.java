package ie.ucd.semantic_properties_plugin.file_checker;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.*;

public class Property {
	
	//definition of exceptable properties using regular expressions
	
	final static String prop_Scope = "(files|modules|features|variables|all|special)";
	final static String prop_Description = ".*";
	final static String prop_Name="(.*|[(.*,)*.*]|optional:.*)";
	
	// class variables
	public static ArrayList<Object> format;
	public static ArrayList<String> scope;
	public static String description;
	public static String name;
	
	public Property(){
		format=new ArrayList<Object>();
		scope= new ArrayList<String>();
		description= new String();
		name=new String();
		
	}
	

	public void setFormat(ArrayList<Object> format) {
		Property.format = format;
	}
	public static ArrayList<String> getScope() {
		return scope;
	}
	public  void setScope(ArrayList<String> scope) {
		Property.scope = scope;
	}
	public static String getDescription() {
		return description;
	}
	public  void setDescription(String string) {
		Property.description = string;
	}
	public boolean checkValidity(){
		Pattern scopePattern = Pattern.compile(prop_Scope, Pattern.CASE_INSENSITIVE);
		Pattern descriptionPattern = Pattern.compile(prop_Description, Pattern.DOTALL);
		Pattern namePattern = Pattern.compile(prop_Name);
		
		
		
		//check scopes
		for( String scopeValue : scope){
			Matcher scopeMatcher = scopePattern.matcher(scopeValue);
			if(!scopeMatcher.matches()){
				System.out.println(name+" scope value is invalid @"+scopeValue);
				return false;
			}
		}
		
		// check Description
		Matcher descriptionMatcher = descriptionPattern.matcher(description);
		if(!descriptionMatcher.matches()){
			System.out.println("The  properties description is invalid @"+ description);
			return false;
		}
		
		//check name
		return checkFormatValidity(format);

	}
	private boolean checkFormatValidity(Object nameValue){
		
		
		Pattern namePattern = Pattern.compile(prop_Name);
		//case for normal property
		if(nameValue instanceof String){
			Matcher nameMatcher = namePattern.matcher((String)nameValue);
			if(!nameMatcher.matches()){
				System.out.println(" name value is invalid @"+nameValue);
				return false;
			}
		}
		//case for inner list [a,b,c]
		else if(nameValue instanceof ArrayList<?>){
			for(Object optionalNameValue :(ArrayList<?>)nameValue){
				checkFormatValidity(optionalNameValue);
				
			}
			
		}
		//case for optional: or choice:  -- needs more complete implementation to avoid false positives
		else if(nameValue instanceof LinkedHashMap<?,?>){
			LinkedHashMap<String,?> r= (LinkedHashMap<String,?>)nameValue;
			
			if(r.containsKey("choice")){
				checkFormatValidity(r.get("choice"));
			}
			if(r.containsKey("optional")){
				checkFormatValidity(r.get("optional"));
			}			
		}
		// any case i didn't predict
		else if(nameValue instanceof Nat ){
			
		}
		else{
			System.out.println("Should not have got here in name check, reason @ "+ nameValue);
		}
		return true;
		
		
		
	}

	public static String getName() {
		return name;
	}

	public static void setName(String name) {
		Property.name = name;
	}

}
