package info.sunjune.solve.calculation.calculator.item;

public class CloseBracketSolveItem extends SolveItem {

    private boolean isFunction;

    private FunctionSolveItem function;

    private OpenBracketSolveItem open;

    public CloseBracketSolveItem(String source) {
        super(source, Kind.CLOSE_BRACKET);
    }

    public void setByOpen(OpenBracketSolveItem open) {
        this.open = open;
        this.isFunction = open.isFunction();
        if (open.isFunction()) {
            this.function = open.getFunction();
        }
        this.open.setClose(this);
    }

    public boolean isFunction() {
        return isFunction;
    }

    public FunctionSolveItem getFunction() {
        return function;
    }

    public OpenBracketSolveItem getOpen() {
        return open;
    }

}
