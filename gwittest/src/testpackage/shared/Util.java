package testpackage.shared;

import java.util.Scanner;

import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.regexp.shared.SplitResult;

public class Util {

	public static String format(final String format, final Object... args) {
	  final RegExp regex = RegExp.compile("%[a-z]");
	  final SplitResult split = regex.split(format);
	  final StringBuffer msg = new StringBuffer();
	  for (int pos = 0; pos < split.length() - 1; pos += 1) {
	    msg.append(split.get(pos));
	    msg.append(args[pos].toString());
	  }
	  msg.append(split.get(split.length() - 1));
	  return msg.toString();
	}
	
	public static String getResourceAsString(String path) {
		return new Scanner(Util.class.getResourceAsStream(path)).useDelimiter("\\A").next();
	}
}
