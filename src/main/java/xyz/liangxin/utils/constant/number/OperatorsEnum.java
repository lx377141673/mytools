package xyz.liangxin.utils.constant.number;


/**
 * 运算符枚举
 *
 * @author liangxin
 */
public enum OperatorsEnum {
    /**
     * 加号 [+]
     */
    ADD("+"),
    /**
     * 减号 [-]
     */
    SUBTRACT("-"),
    /**
     * 乘号 [*]
     */
    MULTIPLY("*"),
    /**
     * 除号 [/]
     */
    DIVIDE("/");

    /**
     * 运算符
     */
    private final String operators;

    OperatorsEnum(String operators) {
        this.operators = operators;
    }

    public String getOperators() {
        return this.operators;
    }

    public String value() {
        return getOperators();
    }

    public static OperatorsEnum of(String operators) {
        switch (operators) {
            case "+":
                return ADD;
            case "-":
                return SUBTRACT;
            case "*":
                return MULTIPLY;
            case "/":
                return DIVIDE;
            default:
                return null;
        }
    }

}
