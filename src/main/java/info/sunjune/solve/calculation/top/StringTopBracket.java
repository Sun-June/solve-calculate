package info.sunjune.solve.calculation.top;

import info.sunjune.solve.calculation.define.BracketPair;
import info.sunjune.solve.calculation.define.TopBracket;

public class StringTopBracket implements TopBracket<String> {

    public static final StringTopBracket STRING = new StringTopBracket();

    private static final BracketPair pair = new BracketPair('\"', '\"');

    private StringTopBracket() {
    }

    @Override
    public BracketPair getPair() {
        return pair;
    }

    @Override
    public String convertString(String str) {
        return str;
    }
}
