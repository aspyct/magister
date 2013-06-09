package org.aspyct.magister;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Magister {
	private class Invocation {
		private Object object;
		private Method method;
		
		public Invocation(Object object, Method method) {
			super();
			this.object = object;
			this.method = method;
		}
	}
	
	private Map<Class<? extends Event>, List<Invocation>> handlers;
	
	public Magister() {
		handlers = new HashMap<Class<? extends Event>, List<Invocation>>();
	}
	
	public void addEventHandler(Object handler) {
		for (Method method: handler.getClass().getMethods()) {
			Handler annotation = method.getAnnotation(Handler.class); 
			
			if (annotation != null) {
				Class<?>[] parameters = method.getParameterTypes();
				if (parameters.length == 1) {
					Class<?> uncheckedEventType = parameters[0];
					
					if (Event.class.isAssignableFrom(uncheckedEventType)) {
						@SuppressWarnings("unchecked")
						Class<? extends Event> eventType = (Class<? extends Event>) uncheckedEventType;
						
						List<Invocation> methodList = handlers.get(eventType);
						if (methodList == null) {
							methodList = new ArrayList<Invocation>();
							handlers.put(eventType, methodList);
						}
						
						methodList.add(new Invocation(handler, method));
					}
				}
				else {
					System.err.println("Invalid argument count for " + method);
				}
			}
 		}
	}
	
	public void dispatchEvent(Event event) {
		List<Invocation> invocations = handlers.get(event.getClass());
		
		if (invocations != null) {
			for (Invocation invocation: invocations) {
				try {
					invocation.method.invoke(invocation.object, event);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
