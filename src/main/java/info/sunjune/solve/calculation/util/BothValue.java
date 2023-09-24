package info.sunjune.solve.calculation.util;

public class BothValue<U, T> {

    private final T right;

    private final U left;

    public BothValue(U left, T right) {
        this.right = right;
        this.left = left;
    }

    public T getRight() {
        return right;
    }

    public U getLeft() {
        return left;
    }
}
