package com.cumulocity.me.lang;

import java.util.Enumeration;
import java.util.Hashtable;

public class HashMap implements Map {
	
	private final Hashtable backend;
	
	public HashMap() {
		this(16);
	}
	
	public HashMap(int initialCapacity) {
		this.backend = new Hashtable(initialCapacity);
	}

	public int size() {
		return backend.size();
	}

	public boolean isEmpty() {
		return backend.isEmpty();
	}
	
	public boolean containsKey(Object key) {
	    return backend.containsKey(key);
	}

	public Object get(Object key) {
		return backend.get(key);
	}

	public Object put(Object key, Object value) {
		return backend.put(key, value);
	}
	
	public void putAll(Map map) {
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry entry = (Entry) iterator.next();
			backend.put(entry.getKey(), entry.getValue());
		}
	}

	public Set keySet() {
		return new KeySet(backend);
	}

	public Set entrySet() {
		return new EntrySet(backend);
	}
    
    public int hashCode() {
        return backend.hashCode();
    }
    
    public boolean equals(Object obj) {
        return backend.equals(obj);
    }
    
    public String toString() {
        return backend.toString();
    }
    
    private static class KeySet extends BaseCollection implements Set {

        private final Hashtable backend;

        public KeySet(Hashtable backend) {
            this.backend = backend;
        }
        
        public int size() {
            throw new UnsupportedOperationException();
        }

        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        public boolean contains(Object o) {
            throw new UnsupportedOperationException();
        }

        public Iterator iterator() {
            return new EnumerationIterator(backend.keys());
        }

        public boolean add(Object e) {
            throw new UnsupportedOperationException();
        }
    }
	
	private static class EntrySet extends BaseCollection implements Set {
		
		private final Hashtable backend;

		public EntrySet(Hashtable backend) {
			this.backend = backend;
		}

		public int size() {
			return backend.size();
		}

		public boolean isEmpty() {
			return backend.isEmpty();
		}

		public boolean contains(Object o) {
			throw new UnsupportedOperationException();
		}

		public Iterator iterator() {
			return new EntryIterator(backend);
		}

		public boolean add(Object e) {
			throw new UnsupportedOperationException();
		}
	}
	
	private static class EntryIterator implements Iterator {

		private final Hashtable backend;
		private Enumeration keys;

		public EntryIterator(Hashtable backend) {
			this.backend = backend;
			this.keys = backend.keys();
		}
		
		public boolean hasNext() {
			return this.keys.hasMoreElements();
		}

		public Object next() {
			Object key = keys.nextElement();
			Object value = backend.get(key);
			return new MapEntry(key, value);
		}
	}
}
