/*
 * Copyright 2012 Nokia Siemens Networks 
 */
package com.cumulocity.sdk.agent.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * A queue wrapper that blocks on {@link #peek()} and {@link #element()} until the {@link #peek()} or {@link #remove()}.
 * @param <E> type of queue element. 
 */
public class PeekBlockingQueueWrapper<E> implements Queue<E>, Serializable {
	
	private static final long serialVersionUID = -37536519365737162L;
	
	private Queue<E> wrappedQueue;
	private Semaphore semaphore;
	
	public PeekBlockingQueueWrapper(Queue<E> wrappedQueue) {
		this.wrappedQueue = wrappedQueue;
		this.semaphore = new Semaphore(1);
	}
	
	@Override
	public boolean add(E e) {
		return wrappedQueue.add(e);
	}

	@Override
	public boolean offer(E e) {
		return wrappedQueue.offer(e);
	}

	@Override
	public E remove() {
		E e = wrappedQueue.remove();
		semaphore.release();
		return e;
	}

	@Override
	public E poll() {
		E e = wrappedQueue.poll();
		semaphore.release();
		return e;
	}

	@Override
	public E element() {
		if (semaphore.tryAcquire()) {
			try {
				return wrappedQueue.element();
			} catch (NoSuchElementException nse) {
				semaphore.release();
				throw nse;
			}
		} else {
			throw new NoSuchElementException("Queue is currently blocked!");
		}
	}

	@Override
	public E peek() {
		if (semaphore.tryAcquire()) {
			E e = wrappedQueue.peek();
			if (e == null) {
				semaphore.release();
			}
			return e;
		} else {
			return null;
		}
	}

	@Override
	public int size() {
		return wrappedQueue.size();
	}

	@Override
	public boolean isEmpty() {
		return wrappedQueue.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return wrappedQueue.contains(o);
	}

	@Override
	public Iterator<E> iterator() {
		return wrappedQueue.iterator();
	}

	@Override
	public Object[] toArray() {
		return wrappedQueue.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return wrappedQueue.toArray(a);
	}

	@Override
	public boolean remove(Object o) {
		return wrappedQueue.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return wrappedQueue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		return wrappedQueue.addAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return wrappedQueue.removeAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return wrappedQueue.retainAll(c);
	}

	@Override
	public void clear() {
		wrappedQueue.clear();
	}
}
