class _TestBytecode {

    public static void mock(){
        int x = 333;
        try {
            x = 20;
        } finally {
            x = 3333;
        }
    }
}
