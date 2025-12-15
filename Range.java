import java.util.List;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Collection;

public class Range implements List<Integer>
{
    int start, stop, step;
    
    private Range(int start, int stop, int step) throws Exception
    {
        if(step==0 || (start<stop && step<0) || (start>stop && step>0))
        {
            throw new Exception("Invalid range");
        }
        
        this.start=start;
        this.stop=stop;
        this.step=step;
    }
    
    public static Range range(int stop) throws Exception
    {
        return range(0, stop);
    }
    
    public static Range range(int start, int stop) throws Exception
    {
        return range(start, stop, 1);
    }
    
    public static Range range(int start, int stop, int step) throws Exception
    {
        return new Range(start, stop, step);
    }
    
    public int size()
    {
        if (step > 0) {
            return Math.max(0, (stop - start + step - 1) / step);
        } else {
            return Math.max(0, (start - stop - step - 1) / (-step));
        }
    }
    
    public boolean isEmpty()
    {
        return size()<=0;
    }
    
    public int indexOf(Integer e)
    {
         
        
        // Check if (e - start) is divisible by step
        if ((e - start) % step != 0) {
            return -1; // Element not in sequence
        }
        
        int position = (e - start) / step;
        
        // Check if position is valid (non-negative and within bounds)
        if (position < 0) {
            return -1;
        }
        
        // Check if element is before stop
        if (step > 0 && e >= stop) {
            return -1;
        }
        if (step < 0 && e <= stop) {
            return -1;
        }
        
        return position;
    }
    
    public int lastIndexOf(Integer e)
    {
        return indexOf(e);
    }
    
    public boolean contains(Integer e)
    {
        return indexOf(e)!=-1;
    }
    
    public Integer[] toArray()
    {
        Integer arr[]=new Integer[size()];
        for(int i=0;i<arr.length;i++) arr[i]=i==0?start:arr[i-1]+step;
        return arr;
    }

    public Iterator<Integer> iterator()
    {
        return new Itr();
    }
    
    public Range subList(int start, int stop)
    {
        return null;
    }
    
    public Integer get(int index){
        if(index>=size())
        {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: "+ size());
        }
        return start+(step*index);
    }
    
    private void notImplemented() throws Exception{ throw new Exception("Invalid function for range");}
    public ListIterator<Integer> listIterator() {return null;}
    public ListIterator<Integer> listIterator(int x) {return null;}
    public int lastIndexOf(Object o){return -1;}
    public int indexOf(Object o){return -1;}
    public boolean remove(Object o){return false;}
    public Integer remove(int o){return -1;}
    public boolean add(Integer o){return false;}
    public void add(int o, Integer e){}
    public Integer set(int o, Integer e){return null;}
    public void clear(){}
    public boolean retainAll(Collection<?> c) { return false;}
    public boolean removeAll(Collection<?> c) { return false;}
    public boolean addAll(Collection<? extends Integer> c) { return false;}
    public boolean addAll(int o, Collection<? extends Integer> c) { return false;}
    public boolean containsAll(Collection<?> c) { return false;}
    public <T> T[] toArray(T[] a){ return null;}
    public boolean contains(Object o){return false;}
    
    
    private class Itr implements Iterator<Integer>
    {
        int lastRet=start-step;
        public boolean hasNext()
        {
            return step>0?(lastRet+step)<stop:(lastRet+step)>stop;
        }
        public Integer next()
        {
            return lastRet+step;
        }
    }
}
