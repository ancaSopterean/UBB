

public class Main {
    public static void main(String[] args) {
        try{
            ComplexExpression cex = ExpressionParser.parser(args);
            System.out.println(cex.execute());
        }
        catch(Exception exc){
            System.out.println(exc.toString());
        }
    }
}