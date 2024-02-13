public class Substract extends ComplexExpression{

    public Substract(Operation operation, NumarComplex[] args) {
        super(operation, args);
    }

    @Override
    public NumarComplex executeOneOperation(NumarComplex n1, NumarComplex n2) {
        return n1.scadere(n2);
    }
}
