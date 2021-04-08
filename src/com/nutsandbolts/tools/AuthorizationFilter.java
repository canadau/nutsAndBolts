package com.nutsandbolts.tools;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * This Java filter demonstrates how to intercept the request
 * and transform the response to implement authentication feature.
 * for the website's back-end.
 *
 */
@WebFilter(filterName = "AuthFilter", urlPatterns = { "*.xhtml" })
public class AuthorizationFilter implements Filter{
	
	private HttpServletRequest httpRequest;
	private static final String[] customerLoginRequiredURLs = {"/page.xhtml"};
	
	private static final String[] adminLoginRequiredURLs = {"/registerEmployee.xhtml",
			"/addProduct.xhtml", "/updateProduct.xhtml"};
	public AuthorizationFilter() {};
		
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}
     
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws ServletException {
		try {

			httpRequest  = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;
			
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); 
	        resp.setHeader("Pragma", "no-cache"); 
	        resp.setHeader("Expires", "0");
	        
			//returns a session only if there is one associated with the request.
			HttpSession session  = httpRequest.getSession(false);
			
			boolean isAdminLoggedIn = (session != null && session.getAttribute("admin") != null);
			boolean isCustomerLoggedIn = (session != null && session.getAttribute("customer") != null);
			
			String loginURI = httpRequest.getContextPath() + "/faces/login";
			
	        boolean isLoginRequest = httpRequest.getRequestURI().equals(loginURI);
	        
	        boolean isLoginPage = httpRequest.getRequestURI().endsWith("login.xhtml");
	        	        
	        if((!isAdminLoggedIn && isAdminLoginRequired()) || (!isCustomerLoggedIn && isCustomerLoginRequired())) {
	        	
	        	resp.sendRedirect(httpRequest.getContextPath() + "/faces/login.xhtml");
	        	
	        } else if (isLoginRequest || isLoginPage) {
	            // the admin is already logged in and he's trying to login again
	            // then forward to the admin homepage
	        	if(isAdminLoggedIn && isLoginPage) {	        		
	        		session.invalidate();
	        		//EmployeeLogin.sessionCustomer = null;
	        		//EmployeeLogin.isLoggedin = false;
	        		//System.out.println("Cleaning");
	        	resp.sendRedirect(httpRequest.getContextPath() + "/faces/index.xhtml");
	        	}  else if(isCustomerLoggedIn && isLoginPage) {
	        		session.invalidate();
	        		// the customer is already logged in and he's trying to login again
		            // then forward to the manage account page   
	        		//System.out.println("Cleaning 2");
		        	resp.sendRedirect(httpRequest.getContextPath() + "/faces/login.xhtml");
		        	
	        	} else {
		            // for other requested pages that require authentication
		            // or the user is already logged in, continue to the destination
	        		//System.out.println("Cleaning 3");
		            chain.doFilter(request, response);
		        }
	 
	        } 
	        else {
	            // for other requested pages that do not require authentication
	            // continue to the destination
	        	//System.out.println("Cleaning 4");
	            chain.doFilter(request, response);
	        }
	            
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	private boolean isCustomerLoginRequired() {
        String requestURL = httpRequest.getRequestURL().toString();
 
        for (String loginRequiredURL : customerLoginRequiredURLs) {
            if (requestURL.contains(loginRequiredURL)) {
                return true;
            }
        }
 
        return false;
    }
		
	private boolean isAdminLoginRequired() {
        String requestURL = httpRequest.getRequestURL().toString();
 
        for (String loginRequiredURL : adminLoginRequiredURLs) {
            if (requestURL.contains(loginRequiredURL)) {
                return true;
            }
        }
 
        return false;
    }
	
	@Override
	public void destroy() {
}
}
