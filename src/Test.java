/**
 * User: Dim-S (Dmitriy Zayceff)
 * Date: 28.01.14
 */
public class Test {

    public static int test(){
        try {
            System.out.print("0");

            return 0;
        } catch (RuntimeException e){
            int y = 30;
            return 0;
        } finally {
            int x = 20;
            System.out.print("Test");
        }
    }
}
