public class Divide extends ComplexExpression{

    public Divide(Operation operation, NumarComplex[] args) {
        super(operation, args);
    }

    @Override
    public NumarComplex executeOneOperation(NumarComplex n1, NumarComplex n2) {
        return n1.impartire(n2);
    }
}
