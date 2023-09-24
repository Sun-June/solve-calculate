package info.sunjune.solve.calculation.calculator.item;

public class OpenBracketSolveItem extends SolveItem {

    private boolean isFunction;

    private FunctionSolveItem function;

    private CloseBracketSolveItem close;

    public OpenBracketSolveItem(String source) {
        super(source, Kind.OPEN_BRACKET);
    }

    public boolean isFunction() {
        return isFunction;
    }

    public void setFunction(boolean function) {
        isFunction = function;
    }

    public FunctionSolveItem getFunction() {
        return function;
    }

    public void setFunction(FunctionSolveItem function) {
        this.function = function;
        this.isFunction = true;
    }

    public CloseBracketSolveItem getClose() {
        return close;
    }

    public void setClose(CloseBracketSolveItem close) {
        this.close = close;
    }

    @Override
    public String toString() {
        return "OpenBracketSolveItem{" +
                "isFunction=" + isFunction +
                ", function=" + (isFunction ? function.define : null) +
                ", close=" + close +
                '}';
    }
}
