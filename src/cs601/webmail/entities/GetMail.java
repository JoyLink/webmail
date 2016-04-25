package cs601.webmail.entities;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xyz on 11/8/14.
 */
public class GetMail {
    public static String getEmail(String str) {
        Pattern pattern = Pattern.compile("[\\w[.-]]+@[\\w[.-]+\\.[\\w]]+");
        Matcher matcher = pattern.matcher(str);
        String Str=null;
        while (matcher.find()) {
            Str = matcher.group();
        }
        return Str;
    }
}
