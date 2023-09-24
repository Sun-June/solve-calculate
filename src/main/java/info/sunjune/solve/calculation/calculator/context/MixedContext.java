package info.sunjune.solve.calculation.calculator.context;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.top.NumberTopBracket;

public class MixedContext extends Context {

    @Override
    public Object getLiteralValue(String literal) {
        Object topValue = super.getLiteralValue(literal);
        if (topValue != null) {
            return topValue;
        }
        Number value = NumberContext.getNumberValue(literal);
        value = value == null ? NumberTopBracket.convert(literal) : value;
        if (value != null) {
            return value;
        }
        return null;
    }
}
