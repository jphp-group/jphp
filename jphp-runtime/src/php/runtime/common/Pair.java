package php.runtime.common;

public class Pair<A, B> {
    private A sideA;
    private B sideB;

    public Pair(A a, B b) {
        this.sideA = a;
        this.sideB = b;
    }

    public boolean hasA() {
        return sideA != null;
    }

    public boolean hasB() {
        return sideB != null;
    }

    public A getA() {
        return sideA;
    }

    public B getB() {
        return sideB;
    }
}
