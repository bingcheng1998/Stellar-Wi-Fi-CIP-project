package com.sansi.stellar.remote.object;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class NameValidator {
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String NAME_PATTERN = "[\\\\|\\*|\\?|\\||\"|/|:|<|>]"; 
 
	public NameValidator() {
		init(NAME_PATTERN);
	}
	
	public NameValidator(String strPattern) {
		init(strPattern);
	}
	
	private void init(String strPattern) {
		pattern = Pattern.compile(strPattern);
		matcher = pattern.matcher("");		
	}
 
	/**
	 * Validate hex with regular expression
	 * 
	 * @param hex
	 *            hex for validation
	 * @return true valid hex, false invalid hex
	 */
	public boolean validate(final String hex) {
		matcher.reset(hex);
		return !matcher.find(); 
	}
}
