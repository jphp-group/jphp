package php.runtime.common;

public class Pair<A, B> {
    private A sideA;
    private B sideB;

    public Pair(A a, B b) {
        this.sideA = a;
        this.sideB = b;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
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
