import com.gam.nocr.ems.util.Computation;

public class ComputingEngine
{
    public static void main(String[] args)
    {
        int x = 15;
        ComputingEngine engine = new ComputingEngine();
        engine.modify(x);
        System.out.println("The value of x after passing by value "+x);
    }
    public  void modify(int x)
    {
        x = 12;
    }
}
