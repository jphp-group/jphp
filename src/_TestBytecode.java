import ru.regenix.jphp.runtime.lang.BaseException;

class _TestBytecode {

    public void test(){
        int x = 2;
        try {
            x = 3;
            throw new BaseException(null, null);
        } catch (BaseException e){
            System.out.append("aaa");
        } catch (RuntimeException e){

        }
    }
}
