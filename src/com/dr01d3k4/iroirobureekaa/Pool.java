package com.dr01d3k4.iroirobureekaa;



import java.util.ArrayList;
import java.util.List;



public final class Pool<T> {
	public interface PoolObjectFactory<T> {
		public T createObject();
	}
	
	
	
	private final List<T> freeObjects;
	private final PoolObjectFactory<T> factory;
	private final int maxSize;
	
	
	
	public Pool(final PoolObjectFactory<T> factory, final int maxSize) {
		this.factory = factory;
		this.maxSize = maxSize;
		this.freeObjects = new ArrayList<T>(maxSize);
	}
	
	
	
	public T newObject() {
		T object = null;
		
		if (freeObjects.isEmpty()) {
			object = factory.createObject();
		} else {
			object = freeObjects.remove(freeObjects.size() - 1);
		}
		return object;
	}
	
	
	
	public void free(final T object) {
		if (freeObjects.size() < maxSize) {
			freeObjects.add(object);
		}
	}
}
