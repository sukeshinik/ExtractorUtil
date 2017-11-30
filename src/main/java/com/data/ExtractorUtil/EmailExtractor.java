package com.data.ExtractorUtil;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class EmailExtractor {
    private URL url = null; //URL Instance Variable
    private String urlString = null;
    private String urlHost = null;
    private List<String> links = new ArrayList<String>();
    private List<String> emails = new ArrayList<String>();

    public EmailExtractor(String url) {
        try {
            this.url = new URL(url); //Initializing our URL object
            this.urlString = url;
            String host = this.url.getHost();
            this.urlHost = host;
        } catch (MalformedURLException ex) {
            System.out.println("Please include Protocol in your URL e.g. http://www.google.com");
            System.exit(1);
        }
    }

    public List<String> retrieveEmails() throws IOException {

        URL innerUrl = null;
        Document doc = Jsoup.connect(urlString).userAgent("Mozilla/5.0").timeout(5000).get();
        Pattern p = Pattern.compile("[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+");
        Matcher matcher = p.matcher(doc.text());

        while (matcher.find()) {
            emails.add(matcher.group());
        }

        Elements elements = doc.select("a[href]");
        for (Element e : elements) {
            links.add(e.attr("href"));
        }

        for (String link : links) {
            if(!(link.matches("^(https?|ftp)://.*$"))) {
                if(link.matches("^(/[a-zA-Z0-9]).*$")){
                    link = "/".concat(link);
                }
                link = "http:".concat(link);
            }

            try {
                innerUrl = new URL(link); //Initializing each link
            } catch (MalformedURLException ex) {
                System.out.println("Please include Protocol in your URL e.g. http://www.google.com");
                System.exit(1);
            }
            
           if((urlHost).equals(innerUrl.getHost()) || 
        		   ((("www.").concat(urlHost)).equals(innerUrl.getHost())) || 
        		   ((urlHost.replaceAll("www.", "")).equals(innerUrl.getHost()))) {
                Document innerDoc = Jsoup.connect(link).userAgent("Mozilla/5.0").timeout(5000).get();
                Matcher innerMatcher = p.matcher(innerDoc.text());
                while (innerMatcher.find()) {
                		if(!emails.contains(innerMatcher.group()))
                			emails.add(innerMatcher.group());
                }
            }
        }
        
        return emails;
    }
}