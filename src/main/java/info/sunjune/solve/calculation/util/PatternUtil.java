package info.sunjune.solve.calculation.util;

import info.sunjune.solve.calculation.define.BracketPair;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class PatternUtil {

    public static Pattern delimitersToRegexp(Set<String> delimiters) {
        StringBuilder result = new StringBuilder();
        result.append('(');
        for (String delim : delimiters) {
            // For each delimiter
            if (result.length() != 1) {
                // Add it to the union
                result.append('|');
            }
            // Quote the delimiter as it could contain some regexpr reserved characters
            result.append("\\Q").append(delim).append("\\E");
        }
        result.append(')');
        return Pattern.compile(result.toString());
    }

    public static Pattern topBracketsRegexp(List<BracketPair> topBrackets) {

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < topBrackets.size(); i++) {
            BracketPair pair = topBrackets.get(i);
            if (i != 0) {
                result.append("|");
            }
            result.append(formatPattern(pair.getOpen())).append("([^").append(formatPattern(pair.getClose()))
                    .append("]*)").append(formatPattern(pair.getClose()));
        }
        return Pattern.compile(result.toString());
    }

    static String formatPattern(String str) {
        if ("[".equals(str) || "]".equals(str)) {
            return "\\" + str;
        }
        return str;
    }

}
