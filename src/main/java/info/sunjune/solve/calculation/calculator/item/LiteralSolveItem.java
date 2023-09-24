package info.sunjune.solve.calculation.calculator.item;

public class LiteralSolveItem<T> extends SolveItem {

    private final T value;

    public LiteralSolveItem(String source, String literal, T value) {
        super(source, literal);
        this.value = value;
    }

    public T getValue() {
        return value;
    }

}
