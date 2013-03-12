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

package com.netthreads.gdx.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.netthreads.gdx.app.Box2DTest;


public class Box2DTestDesktop
{
	public static void main(String[] args)
	{
		// last parameter false = use OpenGL 1.1 and not 2.1+
		new LwjglApplication(new Box2DTest(), Box2DTest.APPLICATION_NAME, Box2DTest.DEFAULT_WIDTH, Box2DTest.DEFAULT_HEIGHT, false);
		
	}
}
