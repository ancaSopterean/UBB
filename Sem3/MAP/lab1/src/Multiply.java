public class Multiply extends ComplexExpression{
    public Multiply(Operation operation, NumarComplex[] args) {
        super(operation, args);
    }

    @Override
    public NumarComplex executeOneOperation(NumarComplex n1, NumarComplex n2) {
        return n1.inmultire(n2);
    }
}
