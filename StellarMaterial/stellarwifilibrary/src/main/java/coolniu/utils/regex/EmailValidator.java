package coolniu.utils.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Chris Liu
 * @version 1.0 
 *
 */
public class EmailValidator {
	private Pattern pattern;
	private Matcher matcher;
 
	private static final String EMAIL_PATTERN =
	//刘工的正则表达式
	"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{1,})$";
	//卿工给出的邮箱的正则表达式
	//"(?:[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?\\.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[A-Za-z0-9-]*[A-Za-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

	private static final String EMAILPATTERN=
			"^[a-zA-Z0-9_.%+-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$";

	public EmailValidator() {
		pattern = Pattern.compile(EMAILPATTERN);
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
		return matcher.matches(); 
	}	
}
