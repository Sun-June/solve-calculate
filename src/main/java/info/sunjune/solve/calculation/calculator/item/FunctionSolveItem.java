package info.sunjune.solve.calculation.calculator.item;

import com.google.common.collect.Lists;
import info.sunjune.solve.calculation.define.Define;

import java.util.List;

public class FunctionSolveItem extends SolveItem {

    private OpenBracketSolveItem open;

    private CloseBracketSolveItem close;

    private List<SolveItem> separators = Lists.newArrayList();

    public FunctionSolveItem(String source, Define define) {
        super(source, Kind.FUNCTION, define);
    }

    public OpenBracketSolveItem getOpen() {
        return open;
    }

    public void setOpen(OpenBracketSolveItem open) {
        this.open = open;
    }

    public CloseBracketSolveItem getClose() {
        return close;
    }

    public void setClose(CloseBracketSolveItem close) {
        this.close = close;
    }

    public void addSeparator(SolveItem item) {
        if (item.kind == Kind.FUNCTION_SEPARATOR) {
            this.separators.add(item);
        }
    }

    public int getParamsCount() {
        if (!separators.isEmpty()) {
            return separators.size() + 1;
        }
        return 0;
    }

    public SolveItem getSeparator(int index) {
        if (index > this.separators.size() - 1) {
            return null;
        }
        return this.separators.get(index);
    }
}
