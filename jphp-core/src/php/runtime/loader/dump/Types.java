package php.runtime.loader.dump;

public interface Types {
    public final static int CLASS    = 1;
    public final static int METHOD   = 2;
    public final static int PROPERTY = 3;
    public final static int CONSTANT = 4;
    public final static int FUNCTION = 5;
    public final static int CLOSURE  = 6;
    public final static int GENERATOR = 7;

    public final static int PARAMETER = 100;

    public final static int MODULE   = 255;
}
