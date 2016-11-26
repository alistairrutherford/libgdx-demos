/*
 * -----------------------------------------------------------------------
 * Copyright 2012 - Alistair Rutherford - www.netthreads.co.uk
 * -----------------------------------------------------------------------
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */

package com.netthreads.gdx.app.definition;

public class AppEvents
{
	private static final int EVENT_BASE = 0;
	
	public static final int EVENT_TRANSITION_TO_SIMULATION_SCENE = EVENT_BASE + 1;
	public static final int EVENT_TRANSITION_TO_MENU_SCENE = EVENT_BASE + 2;
	public static final int EVENT_TRANSITION_TO_ABOUT_SCENE = EVENT_BASE + 3;
	public static final int EVENT_DROP_SHAPE = EVENT_BASE + 4;
	public static final int EVENT_EXIT = EVENT_BASE + 5;
}
