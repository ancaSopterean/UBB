public class Add extends ComplexExpression{

    public Add(Operation operation, NumarComplex[] args) {
        super(operation, args);
    }

    @Override
    public NumarComplex executeOneOperation(NumarComplex n1, NumarComplex n2) {

        return n1.adunare(n2);
    }
}


