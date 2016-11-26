package com.netthreads.gdx.app.definition;

/**
 * Define application events
 * 
 */
public class AppEvents
{
	private static final int EVENT_BASE = 0;
	
	public static final int EVENT_TRANSITION_TO_MAIN_SCENE = EVENT_BASE + 1;
	public static final int EVENT_TRANSITION_TO_MENU_SCENE = EVENT_BASE + 2;
	public static final int EVENT_TRANSITION_TO_ABOUT_SCENE = EVENT_BASE + 3;
	
	public static final int EVENT_BODY_TOUCH_DOWN = EVENT_BASE + 4;
	public static final int EVENT_BODY_TOUCH_DRAGGED = EVENT_BASE + 5;
	public static final int EVENT_BODY_TOUCH_UP = EVENT_BASE + 6;

	public static final int EVENT_ITEM_ADD = EVENT_BASE + 7;
	public static final int EVENT_ITEM_CLEAR = EVENT_BASE + 8;

	public static final int EVENT_POINTER_FLARE = EVENT_BASE + 9;
	
	public static final int EVENT_EXIT = EVENT_BASE + 99;
}
