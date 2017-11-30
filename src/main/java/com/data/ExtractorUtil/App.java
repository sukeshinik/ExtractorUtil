package com.data.ExtractorUtil;

import java.io.IOException;
import java.util.List;

import com.data.ExtractorUtil.EmailExtractor;


public class App 
{
	public static void main( String[] args ) throws IOException 
    {
    		String urlString = "web.mit.edu";
    		
    		if(args.length > 0 && args[0] != null) 
    			urlString = args[0];
    		else
    			System.out.println("Please provide URL.");
    		
    		List<String> emails = extractEmails(urlString);
        print(emails);
    }
    
    public static List<String> extractEmails(String urlString) throws IOException 
    {
    		urlString = (!(urlString.matches("^(https?|ftp)://.*$"))) ? "http://".concat(urlString) : urlString;
        EmailExtractor ee = new EmailExtractor(urlString);
        return ee.retrieveEmails();
    }
    
    public static void print(List<String> emails) {
    	//Check if email addresses have been extracted
        if(emails.size() > 0) {
            //Print out all the extracted emails
            System.out.println("Extracted Email Addresses: ");
            for(String email : emails) {
                System.out.println(email);
            }
        } else {
            //In case, no email addresses were found
            System.out.println("No emails were extracted!");
        }
    }
}
