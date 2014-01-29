/**
 * User: Dim-S (Dmitriy Zayceff)
 * Date: 28.01.14
 */
public class Test {

    public static int test(){
        try {
            try {
                System.gc();
            } catch (RuntimeException e){
                System.out.print("done");
            }
        } catch (NullPointerException e){
            System.out.print("nothing");
        }
        return 0;
    }
}
