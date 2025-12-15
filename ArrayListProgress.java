import java.util.*;
import java.util.function.Consumer;

class ArrayListProgress<E> extends ArrayList<E> implements Iterable<E>
{
    private char style='=';
    private char empty=' ';
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
    
    
    Object[] getElementData()
    {
        Object elementData[]=toArray();
        return elementData;
    }

    @SuppressWarnings("unchecked")
    static <E> E elementAt(Object[] es, int index) {
        return (E) es[index];
    }

        public Iterator<E> iterator() {
        return new Itr();
    }
    
    /**
     * An optimized version of AbstractList.Itr
     * Idk man just copied this inner class off the JDK source code
     */
    private class Itr implements Iterator<E> {
        int cursor;       // index of next element to return
        int lastRet = -1; // index of last element returned; -1 if no such
        int expectedModCount = modCount;
        int size=size();
        long prevtime=System.currentTimeMillis();
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
            return cursor != size();
        }

        @SuppressWarnings("unchecked")
        public E next() {
            
            checkForComodification();
            int i = cursor;
            if (i >= size())
                throw new NoSuchElementException();
            Object[] elementData = getElementData();

            if (i >= elementData.length)
                throw new ConcurrentModificationException();
            cursor = i + 1;
            System.out.print("\r\033[K");
            System.out.print(getProgress(i));
            
            return (E) get(lastRet = i);
        }

        public void remove() {
            if (lastRet < 0)
                throw new IllegalStateException();
            checkForComodification();

            try {
                ArrayListProgress.this.remove(lastRet);
                cursor = lastRet;
                lastRet = -1;
                expectedModCount = modCount;
            } catch (IndexOutOfBoundsException ex) {
                throw new ConcurrentModificationException();
            }
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            final int size = size();
            int i = cursor;
            if (i < size) {
                final Object[] es = getElementData();
                if (i >= es.length)
                    throw new ConcurrentModificationException();
                for (; i < size && modCount == expectedModCount; i++)
                    action.accept(elementAt(es, i));
                // update once at end to reduce heap write traffic
                cursor = i;
                lastRet = i - 1;
                checkForComodification();
            }
        }

        final void checkForComodification() {
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
        }
    }

}
