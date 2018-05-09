package c8y;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class WlanSsidInformation extends ArrayList<SsidInformation> {
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public SsidInformation get(int index) {
        Object o = super.get(index);
        if (o instanceof Map) {
            return new SsidInformation((Map)super.get(index));
        }
        return (SsidInformation) o;
    }

    @Override
    public Iterator<SsidInformation> iterator() {
        return new SsidInformationIterator(super.iterator());
    }

    private class SsidInformationIterator implements Iterator<SsidInformation> {

        Iterator<SsidInformation> iterator;

        public SsidInformationIterator(Iterator<SsidInformation> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public SsidInformation next() {
            Object o = iterator.next();
            if (o instanceof Map) {
                return new SsidInformation((Map)o);
            }
            return (SsidInformation)o;

        }

        @Override
        public void remove() {
            iterator.remove();
        }

    }
}
