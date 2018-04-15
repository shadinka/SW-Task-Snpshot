package com.amazonaws.lambda.demo;

import java.io.IOException;
import java.util.List;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        String reqURL;
                
        String res;
        reqURL = extractURL(input);
    	context.getLogger().log("Input: " + input);
        context.getLogger().log("URL: " + reqURL);
        
        res = takeSnapshot(reqURL);
        context.getLogger().log("Run command return: " + res );
        // TODO: implement your handler
        return res;
    }

    	public String takeSnapshot(String url) {
    	
    	MShellExecuter remoteSSh = new MShellExecuter();
    	String scriptName = "/home/ubuntu/wkhtmltox/bin/takeSnapShot.sh";
    	List<String> result;
    	String ssLinkURL;
    	
    	scriptName = scriptName + " " + url;
    	
    	result = remoteSSh.executeFile(scriptName);   	
    	
    	ssLinkURL = result.get(result.size()-1);
    	
    	return ssLinkURL;
    }
    	
    	
    public String extractURL(Object url) {
    	String strurl = url.toString();
    	
    	strurl = strurl.replaceAll("\\}", "");
    	strurl = strurl.replaceAll("\\{URL=", "");
    	
    	return strurl;
    }
    
    

}
