import java.util.Iterator;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.NoSuchElementException;

class TQDM<E> implements Iterable<E>
{

    private char style='=';
    private char empty=' ';

    private Iterable<E> iterator=null;

    private TQDM(Iterable<E> iterator, char style, char empty)
    {
        this.iterator=iterator;
        this.style=style;
        this.empty=empty;

    }

    public static <T> TQDM<T> tqdm(Iterable<T> iterator)
    {
        return tqdm(iterator, '=');
    }

    public static <T> TQDM<T> tqdm(Iterable<T> iterator, char style)
    {
        return tqdm(iterator, style, ' ');
    }

    public static <T> TQDM<T> tqdm(Iterable<T> iterator, char style, char empty)
    {
        return new TQDM(iterator, style, empty);
    }

    public void setStyle(char c)
    {
        style=c;
    }

    public char getStyle()
    {
        return style;
    }

    public void setEmpty(char c)
    {
        empty=c;
    }

    public char getEmpty()
    {
        return empty;
    }

    public Iterator<E> iterator() {
        return new Itr();
    }

    private int size()
    {
        return ((Collection<E>) iterator).size();
    }

    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return

        int size=size();
        long prevtime=System.currentTimeMillis();
        private Iterator<E> internalIterator=iterator.iterator();
        // prevent creating a synthetic constructor

        Itr() {}

        private String calcTime(long timems)
        {
            long hour=timems/3600000l;
            timems=timems%3600000l;
            long min=timems/60000;
            timems=timems%60000;
            long sec=timems/1000;
            timems=timems%1000;
            long msec=timems%100;
            StringBuilder k=new StringBuilder();
            k.append(hour+":");
            k.append(min+":");
            k.append(sec+".");
            k.append(msec);
            return k.toString();
        }

        private String getProgress(int cursor)
        {
            int current=cursor+1;
            int total=50;
            int prog=(int) (current>=size?50: ((current*total*1.0)/size));
            StringBuilder k=new StringBuilder("[");
            for(int z=0;z<prog;z++) k.append(style);
            for(int z=0;z<50-prog;z++) k.append(empty);
            k.append("] "+current+" / "+size);

            long difftime=Math.max(System.currentTimeMillis()-prevtime,1);
            float speed= 1000.0f/difftime;
            long timeleftms=(size-current)*difftime;
            k.append(String.format(" %.2fit/s", speed));
            k.append(" Remaining "+calcTime(timeleftms));
            if(current==size) k.append("\n");
            if(current==1) k.insert(0,"\n");
            return k.toString();
        }

        public boolean hasNext() {
            return cursor != size;
        }

        @SuppressWarnings("unchecked")
        public E next() {

            int i = cursor;
            if (i >= size)
                throw new NoSuchElementException();
            cursor = i + 1;
            System.out.print("\r\033[K");
            System.out.print(getProgress(i));

            return (E) internalIterator.next();
        }

    }
}
