package c8y;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class RemoteAccessList extends ArrayList<RemoteAccess> {

    private static final long serialVersionUID = 8524431039567737503L;

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public RemoteAccess get(int index) {
        Object o = super.get(index);
        if (o instanceof Map) {
            return new RemoteAccess((Map) super.get(index));
        }
        return (RemoteAccess) o;
    }

    @Override
    public Iterator<RemoteAccess> iterator() {
        return new RemoteAccessIterator(super.iterator());
    }

    private class RemoteAccessIterator implements Iterator<RemoteAccess> {

        Iterator<RemoteAccess> iterator;

        public RemoteAccessIterator(Iterator<RemoteAccess> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @SuppressWarnings("unchecked")
        @Override
        public RemoteAccess next() {
            Object o = iterator.next();
            if (o instanceof Map) {
                return new RemoteAccess((Map<String, Object>) o);
            }
            return (RemoteAccess) o;

        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
