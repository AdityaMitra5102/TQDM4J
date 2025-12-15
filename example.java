import java.util.List;
import java.util.ArrayList;

class example
{
    public static void main(String args[]) throws Exception
    {
        List<Integer> arr=Range.range(0,100);
       
        System.out.println("Starting");
        for(int x: TQDM.tqdm(Range.range(0,100)))
        {
            Thread.sleep(50);
        }

        for(int x:TQDM.tqdm(Range.range(0,30),'#','.'))
        {
            Thread.sleep(100);
        }
        System.out.println("End");
    }
}