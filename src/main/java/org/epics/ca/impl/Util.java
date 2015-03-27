package org.epics.ca.impl;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.lmax.disruptor.EventFactory;

public class Util {

	// TODO move type support to separate package
	
	public static interface TypeSupport {
		public Object newInstance();
		public int getCode();
		public void serialize(ByteBuffer buffer, Object object); 
		public Object deserialize(ByteBuffer buffer, Object object);
	}
	
	private static class DoubleTypeSupport implements TypeSupport {
		public static final DoubleTypeSupport INSTANCE = new DoubleTypeSupport();
		private DoubleTypeSupport() {};
		public Object newInstance() { return Double.valueOf(0); }
		public int getCode() { return 6; }
		public void serialize(ByteBuffer buffer, Object object) { buffer.putDouble((Double)object); }
		public Object deserialize(ByteBuffer buffer, Object object) { return buffer.getDouble(); }
	}

	private static class IntegerTypeSupport implements TypeSupport {
		public static final IntegerTypeSupport INSTANCE = new IntegerTypeSupport();
		private IntegerTypeSupport() {};
		public Object newInstance() { return Integer.valueOf(0); }
		public int getCode() { return 1; }
		public void serialize(ByteBuffer buffer, Object object) { buffer.putInt((Integer)object); }
		public Object deserialize(ByteBuffer buffer, Object object) { return buffer.getInt(); }
	}

	private static class StringTypeSupport implements TypeSupport {
		public static final StringTypeSupport INSTANCE = new StringTypeSupport();
		private StringTypeSupport() {};
		public Object newInstance() { return ""; }
		public int getCode() { return 0; }
		public void serialize(ByteBuffer buffer, Object object) { /* TODO */ }
		public Object deserialize(ByteBuffer buffer, Object object) { return object; }
	}

	static final Map<Class<?>, TypeSupport> typeSupportMap;

	static
	{
		Map<Class<?>, TypeSupport> map = new HashMap<Class<?>, Util.TypeSupport>();
		map.put(Double.class, DoubleTypeSupport.INSTANCE);
		map.put(Integer.class, IntegerTypeSupport.INSTANCE);
		map.put(String.class, StringTypeSupport.INSTANCE);
		
		typeSupportMap = Collections.unmodifiableMap(map);
	}
	
	static final TypeSupport getTypeSupport(Class<?> clazz)
	{
		return typeSupportMap.get(clazz);
	}
	
	// TODO move to TypeSupport
	static final <T> EventFactory<T> getEventFactory(Class<T> clazz)
	{
		// TODO cache factories using Map<Class<T>, EventFactory>
		return new EventFactory<T>() {
			@Override
			public T newInstance() {
				try {
					return clazz.newInstance();
				} catch (Throwable th) {
					throw new RuntimeException("failed to instantiate new instance of " + clazz, th);
				}
			}
		};
	}

}