package info.sunjune.solve.calculation.calculator;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.calculator.context.MixedContext;
import info.sunjune.solve.calculation.define.*;
import info.sunjune.solve.calculation.function.NormalFunction;
import info.sunjune.solve.calculation.function.NumberFunction;
import info.sunjune.solve.calculation.operator.*;
import info.sunjune.solve.calculation.operator.monadic.NegativeOperator;
import info.sunjune.solve.calculation.operator.monadic.ThousandthOperator;
import info.sunjune.solve.calculation.top.StringTopBracket;

import java.util.List;

public class MixedCalculator extends Calculator {

    protected static final List<Function> functions = Lists.newArrayList(NumberFunction.ROUND, NumberFunction.MAX,
            NumberFunction.AVG, NumberFunction.MIN, NumberFunction.SUM, NumberFunction.LENGTH, NumberFunction.RANDOM,
            NormalFunction.IF, NormalFunction.TO_NUMBER, NormalFunction.REGULAR_MATCH);

    protected static final List<Operator> operators = Lists.newArrayList(AdditionOperator.ADDITION, DivisionOperator.DIVISION,
            MultiplicationOperator.MULTIPLICATION, SubtractionOperator.SUBTRACTION, CompareOperator.GREATER_EQUAL_THAN,
            CompareOperator.GREATER_THAN, CompareOperator.LESS_THAN, CompareOperator.LESS_EQUAL_THAN, CompareOperator.EQUAL,
            CompareOperator.NOT_EQUAL, RelationalOperator.AND, RelationalOperator.OR, ModuloOperator.MODULO,
            ExponentOperator.EXPONENT);

    protected static final List<MonadicOperator> monadicOperators = Lists.newArrayList(NegativeOperator.NEGATIVE, ThousandthOperator.THOUSANDTH);

    protected static final List<TopBracket> tops = Lists.newArrayList(StringTopBracket.STRING);


    @Override
    protected List<Function> getFunctions() {
        return functions;
    }

    @Override
    protected List<Operator> getOperators() {
        return operators;
    }

    @Override
    protected List<MonadicOperator> getMonadicOperators() {
        return monadicOperators;
    }

    @Override
    protected List<TopBracket> topBrackets() {
        return tops;
    }

    @Override
    protected Context getDefaultContext(String formula) {
        return new MixedContext();
    }
}
