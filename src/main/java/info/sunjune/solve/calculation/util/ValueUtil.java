package info.sunjune.solve.calculation.util;

import java.math.BigDecimal;

public class ValueUtil {

    /**
     * Obtain an appropriate numeric result, return either int or long type based on the length when it's an integer, and always return double type when it's a decimal.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 获取合适的数值结果，比如为整数时，则根据长度返回int或long类型，小数时统一返回double类型
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 適切な数値結果を取得し、整数の場合は長さに応じて int または long 型を返し、小数の場合は常に double 型を返します。
     *
     * @param decimal decimal
     * @return ReasonableNumber
     */
    public static Number getReasonableNumber(BigDecimal decimal) {
        if (decimal.scale() > 0 && decimal.remainder(BigDecimal.ONE).doubleValue() != 0) {
            return decimal.doubleValue();
        } else {
            if (decimal.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) <= 0 &&
                    decimal.compareTo(BigDecimal.valueOf(Integer.MIN_VALUE)) >= 0) {
                return decimal.intValue();
            }
            return decimal.longValue();
        }
    }

    /**
     * Perform division and retain 15 decimal places.
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 进行除运算，为了防止除不尽，设置保留15位小数
     * <br>
     * ------------------------------------------------------------------
     * <br>
     * 除算を行い、小数点以下15桁を保持します。
     *
     * @param dividend dividend
     * @param divisor  divisor
     * @return result
     */
    public static BigDecimal divide(Number dividend, Number divisor) {
        return BigDecimal.valueOf(dividend.doubleValue()).divide(BigDecimal.valueOf(divisor.doubleValue()), 15, BigDecimal.ROUND_HALF_UP);
    }

    public static boolean getBooleanValue(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return value instanceof Number && ((Number) value).doubleValue() > 0;
    }

    private static final String NUMBER_MATCHES = "-?\\d+(\\.\\d+)?";

    public static boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        return str.matches(NUMBER_MATCHES);
    }

    public static Number getNumberByString(String str) {
        if (isNumeric(str)) {
            try {
                return Double.valueOf(str);
            } catch (Exception e) {
            }
        }
        return null;
    }

}
