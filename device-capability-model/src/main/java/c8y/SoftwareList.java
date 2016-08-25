package c8y;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class SoftwareList extends ArrayList<SoftwareListEntry> {
    
    private static final long serialVersionUID = -2808870422985393963L;
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public SoftwareListEntry get(int index) {
        Object o = super.get(index);
        if (o instanceof Map) {
            return new SoftwareListEntry((Map)super.get(index));
        }
        return (SoftwareListEntry) o;
    }
    
    @Override
    public Iterator<SoftwareListEntry> iterator() {
        return new SoftwareListIterator(super.iterator());
    }
    
    private class SoftwareListIterator implements Iterator<SoftwareListEntry> {

        Iterator<SoftwareListEntry> iterator;
        
        public SoftwareListIterator(Iterator<SoftwareListEntry> iterator) {
            this.iterator = iterator;
        }
        
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public SoftwareListEntry next() {
            Object o = iterator.next();
            if (o instanceof Map) {
                return new SoftwareListEntry((Map)o);
            }
            return (SoftwareListEntry)o;
            
        }

        @Override
        public void remove() {
            iterator.remove();
        }
        
    }
    
    
    
}
