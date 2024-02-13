import java.util.ArrayList;

public abstract class ComplexExpression {
    protected Operation operation;
    private NumarComplex[] args;


    public ComplexExpression(Operation operation, NumarComplex[] args){
        this.operation = operation;
        this.args = args;
    }
    public abstract NumarComplex executeOneOperation(NumarComplex n1, NumarComplex n2);

    public NumarComplex execute(){
        NumarComplex rez = args[0];
        for(int i = 1; i < args.length; i++){
            rez = executeOneOperation(rez, args[i]);
        }
        return rez;
    }

}
