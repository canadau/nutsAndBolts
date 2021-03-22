package com.nutsandbolts.tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
	
	public static boolean isEmpEmail(String email) {
		
		return email.endsWith("@nab.com");
	
	}
	
	private static final String EMAIL_PATTERN =
			"^.+@nab.com$";

    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmailAddress(final String password) {
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

}
