class _TestBytecode {

    public enum Type { NULL, NOT_NULL}

    private Type x;

    public int method(boolean arg){
        int x;
        if (arg){
            x = 20;
            return 9999;
        }
        x = 30;
        return 6666;
    }
}
