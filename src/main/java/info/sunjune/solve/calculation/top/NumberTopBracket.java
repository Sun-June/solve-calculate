package info.sunjune.solve.calculation.top;

import info.sunjune.solve.calculation.define.BracketPair;
import info.sunjune.solve.calculation.define.TopBracket;


public class NumberTopBracket implements TopBracket<Number> {

    public static final NumberTopBracket NUMBER = new NumberTopBracket();

    private static final BracketPair pair = new BracketPair('\"', '\"');

    private NumberTopBracket() {
    }

    @Override
    public BracketPair getPair() {
        return pair;
    }

    @Override
    public Number convertString(String str) {
        Number result = convert(str);
        if (result != null) {
            return result;
        }

        return 4;
    }

    public static Number convert(String str) {
        switch (str) {
            case "ROUND_UP":
                return 0;
            case "ROUND_DOWN":
                return 1;
            case "ROUND_CEILING":
                return 2;
            case "ROUND_FLOOR":
                return 3;
            case "ROUND_HALF_UP":
                return 4;
            case "ROUND_HALF_DOWN":
                return 5;
            case "ROUND_HALF_EVEN":
                return 6;
            case "ROUND_UNNECESSARY":
                return 7;
        }
        return null;
    }
}
