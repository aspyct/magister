package org.aspyct.magister;

import static org.junit.Assert.*;

import org.aspyct.magister.Event;
import org.aspyct.magister.Handler;
import org.aspyct.magister.Magister;
import org.junit.Before;
import org.junit.Test;

public class MagisterTest {
	private Magister magister;
	private CustomEvent1 event1;
	private CustomEvent2 event2;
	private CustomEventHandler handler;
	
	@Before
	public void setUp() throws Exception {
		magister = new Magister();
		event1 = new CustomEvent1();
		event2 = new CustomEvent2();
		handler = new CustomEventHandler();
	}

	@Test
	public void magisterShouldCallDeclaredHandlers() {
		magister.addEventHandler(handler);
		
		magister.dispatchEvent(event1);
		
		assertTrue(handler.called);
	}
	
	@Test
	public void magisterShouldFilterByEventType() {
		magister.addEventHandler(handler);
		
		magister.dispatchEvent(event2);
		
		assertFalse(handler.called);
	}
	
	private class CustomEvent1 extends Event {}
	private class CustomEvent2 extends Event {}
	
	private class CustomEventHandler {
		boolean called;
		
		@Handler
		public void onEvent1(CustomEvent1 event1) {
			called = true;
		}
	}
} 