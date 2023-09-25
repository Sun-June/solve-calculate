# solve-calculate

#### [中文](./README.cn.md)

#### [日本語](./README.jp.md)

solve-calculate is a simple formula parsing and calculation tool, mainly aimed at node code or business scenarios that
require custom formulas.

> The formula parsing and definition part of this project is inspired by the implementation
> of [javaluator](https://javaluator.sourceforge.net/en/home/).

> You can also take a look at the [future plans](#future-plans).

## How to Use

* Maven Dependency

> Currently under development, cannot be directly referenced yet

```xml

<dependency>
    <groupId>info.sun-june.solve</groupId>
    <artifactId>solve-calculate</artifactId>
    <version>0.7.1</version>
</dependency>
```

* Simple Example：

> NumberCalculator is a numeric calculator, and objects involved in the operation must be numeric. It also provides a
> mixed calculator implementation, which can perform conditional and string calculations, as explained later.
>
> assertEquals is an assertion method used to verify that the results on both sides are the same.

```java
public class NumberCalculatorTest {
    @Test
    void base_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        assertEquals(calculator.calculation("1 + 1"), 2);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10"), 9);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 - 10 * 2 + sum(2, 3)"), -6);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))"), 27);
        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 / 2 + 5 / 2"), -43.5d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_UP\")"), 8.72d);
        assertEquals(calculator.calculation("1 + round(3.15 * 2.45, 2, \"ROUND_DOWN\")"), 8.71d);
        assertEquals(calculator.calculation("1 + 2 ^ 3 / 2 + 1"), 6);
        assertEquals(calculator.calculation("2 + 5 % 2000‰ + 1"), 4);
        assertEquals(calculator.calculation("2 + round( 2 * π * 7, 0) + 1"), 47);
    }
}        
```

### Features

#### Independent Formula Validation

Example：

```java
public class NumberCalculatorTest {
    @Test
    void errorCheck() {
        NumberCalculator calculator = new NumberCalculator();
        String input = "π + sum(10, min(, 10)) - 10";

        FormulaException ex = assertThrows(FormulaException.class, () -> calculator.checkFormula(input));
        assertEquals(FormulaError.ARGUMENT_MISSING, ex.error);
        assertEquals(",", input.substring(ex.startIndex, ex.endIndex));

    }
}
```

* Use the `checkFormula` method to check if the formula is correct.
* In case of an error, a `FormulaException` is thrown.
* `error` represents the error code of the exception, and each code corresponds to an enum that you can use for
  internationalized error messages.
* `startIndex` and `endIndex` indicate the starting and ending positions of the error in the formula.
* With this information, you can better provide error messages and check the correctness of the formula.

#### Retrieval of Calculation History

Example：

```java
public class NumberCalculatorTest {
    @Test
    void record_test() throws Exception {
        NumberCalculator calculator = new NumberCalculator();

        BothValue<Number, Context<Number>> bothValue = calculator.calculationBoth("-1 + -100 + 11 * 10  + sum(1, 2, 5 * 2, min(5, 6, avg(8, 9 / 3 , 10 + 2 + (5 - 3))))");

        assertEquals(bothValue.getLeft(), 27);
        Gson gson = new Gson();
        for (CalculationRecord record : bothValue.getRight().recordList) {
            if (record.kind != Kind.LITERAL) {
                System.out.println("record::" + gson.toJson(record));
            }
        }
    }
}
```

Output：

```shell
record::{"arithmetic":"-","index":0,"values":[1.0],"result":-1,"kind":"MONADIC_OPERATOR"}
record::{"arithmetic":"-","index":5,"values":[100.0],"result":-100,"kind":"MONADIC_OPERATOR"}
record::{"arithmetic":"+","index":3,"values":[-1,-100],"result":-101,"kind":"OPERATOR"}
record::{"arithmetic":"*","index":15,"values":[11.0,10.0],"result":110,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":10,"values":[-101,110],"result":9,"kind":"OPERATOR"}
record::{"arithmetic":"*","index":35,"values":[5.0,2.0],"result":10,"kind":"OPERATOR"}
record::{"arithmetic":"/","index":59,"values":[9.0,3.0],"result":3,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":68,"values":[10.0,2.0],"result":12,"kind":"OPERATOR"}
record::{"arithmetic":"-","index":77,"values":[5.0,3.0],"result":2,"kind":"OPERATOR"}
record::{"arithmetic":"+","index":72,"values":[12,2],"result":14,"kind":"OPERATOR"}
record::{"arithmetic":"avg","index":50,"values":[8.0,3,14],"result":8.333333333333332,"kind":"FUNCTION"}
record::{"arithmetic":"min","index":40,"values":[5.0,6.0,8.333333333333332],"result":5.0,"kind":"FUNCTION"}
record::{"arithmetic":"sum","index":23,"values":[1.0,2.0,10,5.0],"result":18,"kind":"FUNCTION"}
record::{"arithmetic":"+","index":21,"values":[9,18],"result":27,"kind":"OPERATOR"}
```

- You can use the `calculationBoth` method to get an object that includes the calculation result and the context.
- The `context` contains the entire calculation history in the `recordList` field.
    - The order of calculation records corresponds to the actual calculation order.
    - `arithmetic` represents the original string used for the calculation.
    - `index` indicates the position in the formula.
    - `values` stores the values involved in the calculation (in the order they were passed for the operation).
    - `result` represents the result of this calculation.
    - `kind` represents the type of calculation.

#### Simple Variable Substitution

Example：

```java
public class NumberContext extends Context<Number> {

    @Override
    public Number getLiteralValue(String literal) {
        Number value = super.getLiteralValue(literal);
        value = value == null ? getNumberValue(literal) : value;
        if (value != null) {
            return value;
        }
        return null;
    }

    public static Number getNumberValue(String literal) {
        if ("π".equals(literal)) {
            return Math.PI;
        }
        return ValueUtil.getNumberByString(literal);
    }

}
```

- The provided example defines a context for numeric operations (you can extend `NumberContext` to customize the context
  you need).
- It includes handling the conversion of `π` to a numeric value.
- By following this approach, you can define which variables can be substituted into the formula for calculations.

#### Simple Function and Operator Extensions

- You can also add or adjust supported calculation functions and operators.
    - Override the corresponding retrieval methods in `NumberCalculator` or `MixedCalculator` to adjust what is involved
      in the calculation.
    - Alternatively, you can directly extend `Calculator` to design your own calculator utility class.
- For extending calculation functions, refer to the implementations in the `info.sunjune.solve.calculation.function`
  package, such as `info.sunjune.solve.calculation.function.NumberFunction`.
- For extending operators, refer to the implementations in the `info.sunjune.solve.calculation.operator` package, such
  as `info.sunjune.solve.calculation.operator.AdditionNumberOperator`.

#### Mixed Calculator

Example：

```java
public class MixedCalculatorTest {
    @Test
    void baseTest() throws Exception {
        MixedCalculator calculator = new MixedCalculator();

        assertEquals(calculator.calculation("-1 + -100 + 11 * 10 + \"abc\""), "9abc");
        assertEquals(calculator.calculation("if(1 * 10 > 5, 10, \"abc\") + 2"), 12);

        assertEquals(calculator.calculation("if(1 * 10 < 5, 10, \"abc\") + 2"), "abc2");
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2, 10, \"abc\") + 2"), 12);
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 || 10 < 3, 10, \"abc\") + 2"), 12);
        assertEquals(calculator.calculation("if(1 * 10 <= 5 * 2 && 1 == 1, 10, \"abc\") + 2"), 12);
    }
}
```

- In the mixed calculator implementation, `+` can be used for string operations (other non-numeric objects are also
  converted to strings for calculations).
- It supports comparison operators (`> >= < <= == !=`) and logical operators (`&& ||`).

## Future Plans

- 0.8.0
    - Enhance code comments
    - Add support for common calculation functions
    - Publish to the Maven Central Repository
- 0.9.0
    - Add support for chained calculations, as follows:
        - ProjectA, calculation formula: `num + 100`, where `num` is a custom variable
        - ProjectB, calculation formula: `ProjectA - 9`
        - ProjectC, calculation formula: `ProjectA + ProjectB`
    - Add checks for chained calculations to prevent cycles
    - Add features for recording chained calculations and more
- 1.0.0
    - Continue to enhance chained calculations
    - Add support for table-like data in chained calculations, including:
        - Calculations for each row of multi-row data, allowing the introduction of variables from outside the data
        - Limited support for accessing data outside the multi-row data
- Post 1.0.0
    - Routine maintenance
    - Introduce a TypeScript version, allowing Node.js or front-end applications to achieve the same functionality
    - Release Java 21 version (as a separate project), which may utilize features like virtual threads to accelerate
      computation