class _TestBytecode {

    public enum Type { NULL, NOT_NULL}

    private Type x;

    public void method(){
        boolean b = (x == Type.NULL);
    }
}
