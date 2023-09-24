package info.sunjune.solve.calculation.calculator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import info.sunjune.solve.calculation.calculator.item.*;
import info.sunjune.solve.calculation.define.BracketPair;
import info.sunjune.solve.calculation.define.Context;
import info.sunjune.solve.calculation.define.Function;
import info.sunjune.solve.calculation.define.MonadicOperator;
import info.sunjune.solve.calculation.error.ErrorInfo;
import info.sunjune.solve.calculation.error.FormulaException;
import info.sunjune.solve.calculation.util.ThrowFunction;

import java.util.List;
import java.util.Map;

import static info.sunjune.solve.calculation.error.FormulaError.*;

public class CalculatorContext<T> {

    protected final List<SolveItem> items = Lists.newArrayList();

    protected final Context<T> context;

    private int index = 0;

    private List<OpenBracketSolveItem> openBrackets = Lists.newArrayList();

    public T leftValue;

    protected final BracketPair expressionBracket;

    public Map<Integer, ThrowFunction<Object, T>> stacks = Maps.newHashMap();


    public CalculatorContext(Context<T> context, BracketPair expressionBracket) {
        this.context = context;
        this.expressionBracket = expressionBracket;
    }

    public void addIndex(String str) {
        if (str != null) {
            this.index += str.length();
        }
    }

    public SolveItem getPrevious() {
        return items.isEmpty() ? null : items.get(items.size() - 1);
    }

    public void addItem(SolveItem item) throws FormulaException {
        SolveItem previous = this.getPrevious();

        items.add(item);
        item.startIndex = index;

        switch (item.kind) {
            case OPEN_BRACKET:
                OpenBracketSolveItem openBracketSolveItem = (OpenBracketSolveItem) item;
                if (previous instanceof FunctionSolveItem) {
                    FunctionSolveItem functionSolveItem = (FunctionSolveItem) previous;
                    Function<T> define = (Function<T>) previous.define;
                    if (!define.getPair().getOpen().equals(item.source)) {
                        throw this.buildException(item, INVALID_BRACKET_AFTER_FUNCTION);
                    }
                    openBracketSolveItem.setFunction(functionSolveItem);
                    functionSolveItem.setOpen(openBracketSolveItem);
                } else {
                    if (!this.expressionBracket.getOpen().equals(item.source)) {
                        throw this.buildException(item, INVALID_BRACKET_IN_EXPRESSION);
                    }
                    openBracketSolveItem.setFunction(false);

                    // "abc (", "sum(1)(", "~("
                    if (previous != null && (previous.kind == Kind.LITERAL || previous.kind == Kind.CLOSE_BRACKET || this.isMonadicOperatorLeft(previous))) {
                        throw this.buildException(item, INVALID_BRACKET_IN_EXPRESSION);
                    }
                }
                openBrackets.add(openBracketSolveItem);
                break;
            case CLOSE_BRACKET:
                if (previous == null) { // ") 1+1"
                    throw this.buildException(item, START_WITH_CLOSE_BRACKET);
                }
                // "sum(10,)", "(10+)"
                if (previous.kind == Kind.FUNCTION_SEPARATOR || this.isNotSupportOperator(previous)) {
                    throw this.buildException(item, ARGUMENT_MISSING);
                }
                // "100)"
                if (openBrackets.isEmpty()) {
                    throw this.buildException(item, INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);
                }

                // find open
                OpenBracketSolveItem find = null;
                for (int i = 0; i < openBrackets.size(); i++) {
                    OpenBracketSolveItem open = openBrackets.get(openBrackets.size() - i - 1);
                    if (open.isFunction()) {
                        Function<T> define = (Function<T>) open.getFunction().define;
                        if (define.getPair().getClose().equals(item.source)) {
                            find = open;
                            FunctionSolveItem functionSolveItem = open.getFunction();
                            Function<T> function = (Function<T>) functionSolveItem.define;
                            int argCount = functionSolveItem.getParamsCount();
                            if (function.minArgumentCount() > argCount || function.maxArgumentCount() < argCount) {
                                System.out.println("errorCount::" + argCount + "the func::" + function);
                                throw this.buildException(functionSolveItem, FUNCTION_ARGUMENT_COUNT_ERROR);
                            }
                            break;
                        }
                    } else if (this.expressionBracket.getClose().equals(item.source) && this.expressionBracket.getOpen().equals(open.source)) {
                        find = open;
                        if (find == previous) { // "() + 1"
                            throw this.buildException(item, INVALID_BRACKET_POINTLESS);
                        }
                        break;
                    }
                }
                // "sum(1, 2))", "(1 + 2))"
                if (find == null) {
                    throw this.buildException(item, INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);
                }
                openBrackets.remove(find);

                CloseBracketSolveItem closeBracketSolveItem = (CloseBracketSolveItem) item;
                closeBracketSolveItem.setByOpen(find);

                break;
            case FUNCTION_SEPARATOR:
                // ",100"
                if (previous == null) {
                    throw this.buildException(item, CAN_NOT_START_WITH_ARGUMENT_SEPARATOR);
                }
                // "sum(,", "sum(100,,)"
                if (previous.kind == Kind.OPEN_BRACKET || previous.kind == Kind.FUNCTION_SEPARATOR || openBrackets.isEmpty() || this.isNotSupportOperator(previous)) {
                    throw this.buildException(item, ARGUMENT_MISSING);
                }
                // find function open
                OpenBracketSolveItem funcOpen = null;
                for (int i = 0; i < openBrackets.size(); i++) {
                    OpenBracketSolveItem open = openBrackets.get(openBrackets.size() - i - 1);
                    if (open.isFunction()) {
                        funcOpen = open;
                        break;
                    }
                }
                if (funcOpen == null) {
                    throw this.buildException(item, INVALID_PARENTHESIS_MATCH_OPEN_BRACKET);
                }
                funcOpen.getFunction().addSeparator(item);

                break;
            case FUNCTION:
                // "abc max(100)", "max()min()"
                if (previous != null && (previous.kind == Kind.LITERAL || previous.kind == Kind.FUNCTION ||
                        previous.kind == Kind.CLOSE_BRACKET)) {
                    throw this.buildException(item, FUNCTION_MISSING);
                }
                break;
            case OPERATOR:
                if (previous == null || previous.kind == Kind.FUNCTION_SEPARATOR || previous.kind == Kind.OPEN_BRACKET ||
                        previous.kind == Kind.OPERATOR || this.isNotSupportOperator(previous)) {
                    throw this.buildException(item, OPERATOR_MISSING);
                }
                break;
            case MONADIC_OPERATOR:
                if (this.isMonadicOperatorLeft(item) && (previous == null || previous.kind == Kind.FUNCTION_SEPARATOR || previous.kind == Kind.OPEN_BRACKET || previous.kind == Kind.OPERATOR)) {
                    throw this.buildException(item, OPERATOR_MISSING);
                }
                break;
            case LITERAL:
                // "abc abc", "sum(1, 2)abc"
                if (previous != null && (previous.kind == Kind.LITERAL || previous.kind == Kind.CLOSE_BRACKET ||
                        this.isMonadicOperatorLeft(previous))) {
                    throw this.buildException(item, LITERAL_MISSING);
                }
                break;
        }
        index += item.source.length();
    }

    public boolean isMonadicOperatorLeft(SolveItem item) {
        if (item.kind == Kind.MONADIC_OPERATOR) {
            MonadicOperator monadicOperator = (MonadicOperator) item.define;
            return !monadicOperator.right();
        }
        return false;
    }

    public FormulaException buildException(SolveItem item, ErrorInfo error) {
        return new FormulaException(item, error);
    }

    public boolean isNotSupportOperator(SolveItem previous) {
        if (previous != null) {
            if (previous.kind == Kind.OPERATOR) {
                return true;
            } else if (previous.kind == Kind.MONADIC_OPERATOR) {
                MonadicOperator monadicOperator = (MonadicOperator) previous.define;
                return monadicOperator.right();
            }
        }
        return false;
    }

}
