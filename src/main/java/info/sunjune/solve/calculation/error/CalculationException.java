package info.sunjune.solve.calculation.error;

/**
 * Exceptions occurring during calculations, such as when parameter types do not match or division by zero is attempted.
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 计算中出现的异常，比如计算时参数类型不匹配或进行了除0的运算
 * <br>
 * ------------------------------------------------------------------
 * <br>
 * 計算中に発生する例外、例えば、計算時にパラメーターの型が一致しない場合やゼロで割り算を行った場合など。
 */
public class CalculationException extends Exception {

    protected final ErrorInfo errorInfo;

    public CalculationException(ErrorInfo errorInfo) {
        this.errorInfo = errorInfo;
    }

    public CalculationException(String message, ErrorInfo errorInfo) {
        super(message);
        this.errorInfo = errorInfo;
    }

    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }
}
