package info.sunjune.solve.calculation.util;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.define.BracketPair;
import info.sunjune.solve.calculation.top.StringTopBracket;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PatternUtilTest {

    @Test
    void topBracketsRegexp_should_be_work() {
        String input = "\"12<3>4\" + <456> * [\"122\"<333>] - <\"123\">";

        List<BracketPair> topBrackets = Lists.newArrayList(StringTopBracket.STRING.getPair(), BracketPair.ANGLES, BracketPair.BRACKETS);

        Pattern pattern = PatternUtil.topBracketsRegexp(topBrackets);

        Matcher matcher = pattern.matcher(input);

        int loop = 0;
        while (matcher.find()) {
            String doubleQuotedValue = matcher.group(1);
            String angleBracketValue = matcher.group(2);
            String squareBracketValue = matcher.group(3);

            if (loop == 0) {
                assertEquals(doubleQuotedValue, "12<3>4");
                assertNull(angleBracketValue);
                assertNull(squareBracketValue);
            } else if (loop == 1) {
                assertEquals(angleBracketValue, "456");
                assertNull(doubleQuotedValue);
                assertNull(squareBracketValue);
            } else if (loop == 2) {
                assertEquals(squareBracketValue, "\"122\"<333>");
                assertNull(doubleQuotedValue);
                assertNull(angleBracketValue);
            } else if (loop == 3) {
                assertEquals(angleBracketValue, "\"123\"");
                assertNull(doubleQuotedValue);
                assertNull(squareBracketValue);
            }

            loop++;
        }
    }


}
