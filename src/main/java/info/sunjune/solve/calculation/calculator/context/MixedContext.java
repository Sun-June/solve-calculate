package info.sunjune.solve.calculation.calculator.context;

import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.top.NumberTopBracket;

public class MixedContext extends Context {

    @Override
    public Object getCustomerLiteralValue(String literal) {
        Number value = NumberContext.getNumberValue(literal);
        value = value == null ? NumberTopBracket.convert(literal) : value;
        if (value != null) {
            return value;
        }
        return null;
    }
}
