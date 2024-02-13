public class ExpressionFactory {
    private static ExpressionFactory instance;

    private ExpressionFactory(){}
    public static ExpressionFactory getInstance(){
        if(instance == null){
            instance = new ExpressionFactory();
        }
        return instance;
    }
    public ComplexExpression createExpression(Operation operation, NumarComplex[] args){
        return switch (operation){
            case ADDITION -> new Add(operation, args);
            case SUBSTRACTION -> new Substract(operation, args);
            case MULTIPLICATION -> new Multiply(operation, args);
            case DIVISION -> new Divide(operation, args);
            default -> throw new IllegalArgumentException("invalid operand");
        };
    }
}
