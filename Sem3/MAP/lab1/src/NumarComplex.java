public class NumarComplex {
    private double re;
    private double im;

    public NumarComplex(double re, double im){
        this.re = re;
        this.im = im;
    }

    public double getRe() {
        return re;
    }

    public void setRe(double re) {
        this.re = re;
    }

    public double getIm() {
        return im;
    }

    public void setIm(double im) {
        this.im = im;
    }


    public NumarComplex adunare(NumarComplex n){
        this.re = this.re + n.getRe();
        this.im = this.im + n.getIm();
        return new NumarComplex(this.re,this.im);
    }

    public NumarComplex scadere(NumarComplex n){
        this.re = this.re - n.getRe();
        this.im = this.im - n.getIm();
        return new NumarComplex(this.re,this.im);
    }


    public NumarComplex inmultire(NumarComplex n){
        this.re = this.re*n.getRe() - this.im*n.getIm();
        this.im = this.re*n.getIm() + this.im*n.getRe();
        return new NumarComplex(this.re,this.im);
    }


    public NumarComplex impartire(NumarComplex n){
        double numitor = n.getRe()*n.getRe() + n.getIm()*n.getIm();
        this.re = (this.re*n.getRe() + this.im*n.getIm())/numitor;
        this.im = (this.im*n.getRe() - this.re*n.getIm())/numitor;
        return new NumarComplex(this.re,this.im);
    }

    @Override
    public String toString() {
        if(im >0){
            return re + "+" + im + "*i";
        }
        return re + "-" + im + "*i";
    }
}
