package com.netthreads.gdx.java;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.netthreads.gdx.core.Box2DBumpers;

public class Box2DBumpersDesktop
{
	public static void main(String[] args)
	{
		new LwjglApplication(new Box2DBumpers(), 
				Box2DBumpers.APPLICATION_NAME, 
				Box2DBumpers.DEFAULT_WIDTH, 
				Box2DBumpers.DEFAULT_HEIGHT);

	}
}
