package com.example.demo;

import javax.servlet.http.HttpServletResponse;

import org.springframework.social.oauth1.AuthorizedRequestToken;
import org.springframework.social.oauth1.OAuth1Operations;
import org.springframework.social.oauth1.OAuth1Parameters;
import org.springframework.social.oauth1.OAuthToken;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.social.twitter.api.impl.TwitterTemplate;
import org.springframework.social.twitter.connect.TwitterConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping("/")
	public String loadHome() {
		
		return "home.jsp";
	}
	
	
	OAuth1Operations oauthOperations;
	OAuthToken requestToken;
	
	@RequestMapping("/login")
	public void loginPage(HttpServletResponse response) {
		try {
			
			//Twitter twitter = new TwitterTemplate( "zDtnByxTk3bScWFRS9p47nH3D", "NPAA31uSfcxIlsVlMa4F01PFALFb8snhL5NaIZlSscjHS0bp8z", "809297692459290625-axl9b25UOe2GnZCymlp3mZn9lLBTe49", "RYdstE4P7ZUJfzbtTFaC3WDSzoWvJI6EBzFVuPUigod4r" );
			
			TwitterConnectionFactory connectionFactory = 
			    new TwitterConnectionFactory( "zDtnByxTk3bScWFRS9p47nH3D", "NPAA31uSfcxIlsVlMa4F01PFALFb8snhL5NaIZlSscjHS0bp8z" );
			
			oauthOperations = connectionFactory.getOAuthOperations();
			requestToken = oauthOperations.fetchRequestToken( "http://localhost:8080/success", null );			
			
			String authorizeUrl = oauthOperations.buildAuthorizeUrl(requestToken.getValue(), OAuth1Parameters.NONE);
			response.sendRedirect( authorizeUrl );
												
		}
		catch(Exception e) {
			System.out.println(e);
		} 
	}
		
	@RequestMapping("/logout-success") 
	public String logoutPage() {
	  
		return "logout.jsp"; 
	 }	
	
	@RequestMapping("/success")
	@ResponseBody
	public void user(@RequestParam String oauth_token,@RequestParam String oauth_verifier) {				
		
		OAuthToken oa = new OAuthToken(oauth_token,null);
		
		AuthorizedRequestToken authorizedRequestToken = new AuthorizedRequestToken(oa, oauth_verifier);
		
		OAuthToken accessToken = oauthOperations.exchangeForAccessToken( authorizedRequestToken, null);
						
		String consumerKey = "zDtnByxTk3bScWFRS9p47nH3D"; // The application's consumer key
		String consumerSecret = "NPAA31uSfcxIlsVlMa4F01PFALFb8snhL5NaIZlSscjHS0bp8z"; // The application's consumer secret
		String accessTokenRet = accessToken.getValue();
		String accessTokenSecret = accessToken.getSecret();
		
		Twitter twitter = new TwitterTemplate( consumerKey, consumerSecret, accessTokenRet, accessTokenSecret );
		
		System.out.println("twitter : "+twitter.userOperations().getUserProfile().isVerified());
	}
}
