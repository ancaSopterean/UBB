import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionParser {
    public static ComplexExpression parser(String[] args){
        ArrayList<NumarComplex> listaNr = new ArrayList<>();
        Operation op = null;
        for(int i = 0; i < args.length; i++){
            if(i % 2 == 0){
                listaNr.add(numberParser(args[i]));
            }
            else{
                op = operationParser(args[i]);
            }
        }
        if (op == null){
            throw new IllegalArgumentException("operation not found");
        }

        NumarComplex[] arrNr = listaNr.toArray(new NumarComplex[0]);
        ExpressionFactory fact = ExpressionFactory.getInstance();

        return fact.createExpression(op, arrNr);
    }

    public static Operation operationParser(String arg){
        return switch(arg){
            case "+" -> Operation.ADDITION;
            case "-" -> Operation.SUBSTRACTION;
            case "*" -> Operation.MULTIPLICATION;
            case "/" -> Operation.DIVISION;
            default -> throw new IllegalArgumentException("invalid operand");
        };
    }
    public static NumarComplex numberParser(String arg){
        String doubleRegex = "[-+]?(\\d+(\\.\\d*)?|\\.\\d+)";
        Pattern doublePattern = Pattern.compile(doubleRegex);
        double re =0.0;
        double im = 0.0;
        Matcher m = doublePattern.matcher(arg);
        if(m.lookingAt()) {
            re = Double.parseDouble(m.group());
            arg = arg.substring(m.end());
            m = doublePattern.matcher((arg));
            if (arg.matches("[-+].*") && m.lookingAt()) {
                im = Double.parseDouble(m.group());
                arg = arg.substring(m.end());
            } else if (arg.matches("[-+]i")) {
                im = arg.startsWith("-") ? -1.0 : 1.0;
            } else {
                throw new NumberFormatException("invalid complez number");
            }
        }
        else if (arg.matches("[-+]i")) {
            im =arg.startsWith("-") ? -1.0 : 1.0;
        }
        else {
            throw new NumberFormatException("invalid complex number");
        }
        return new NumarComplex(re,im);
    }
}
