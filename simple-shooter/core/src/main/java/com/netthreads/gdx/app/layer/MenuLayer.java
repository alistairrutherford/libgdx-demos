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

package com.netthreads.gdx.app.layer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.netthreads.gdx.app.definition.AppActorEvents;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Scene layer.
 * 
 */
public class MenuLayer extends Layer
{
	private static final String UI_FILE = "data/uiskin60.json";
	private static final String URL_LABEL_FONT = "large-font";
	private static final String URL_LABEL_FONT_SMALL = "default-font";
	
	private Table table;
	private Skin skin;
	private Label titleLabelA;
	private Label titleLabelB;
	private Button startButton;
	private Button settingsButton;
	private Button aboutButton;

	/**
	 * The one and only director.
	 */
	private Director director;

	/**
	 * Construct the screen.
	 * 
	 * @param stage
	 */
	public MenuLayer(float width, float height)
	{
		setWidth(width);
		setHeight(height);

		director = AppInjector.getInjector().getInstance(Director.class);
		
		Gdx.input.setCatchBackKey(true);

		loadTextures();

		buildElements();
	}

	/**
	 * Load view textures.
	 * 
	 */
	private void loadTextures()
	{
		skin = new Skin(Gdx.files.internal(UI_FILE));
	}

	/**
	 * Build view elements.
	 * 
	 */
	private void buildElements()
	{
		// Title
		titleLabelA = new Label("Simple", skin, URL_LABEL_FONT, Color.YELLOW);
		titleLabelB = new Label("Shooter", skin, URL_LABEL_FONT_SMALL, Color.YELLOW);

		// ---------------------------------------------------------------
		// Buttons.
		// ---------------------------------------------------------------
		startButton = new TextButton("Start", skin);
		settingsButton = new TextButton("Settings", skin);
		aboutButton = new TextButton("About", skin);

		// ---------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------
		table = new Table();

		table.setSize(getWidth(), getHeight());

		table.row();
		table.add(titleLabelA).expandY().expandX();
		table.row();
		table.add(titleLabelB).expandY().expandX();
		table.row();
		table.add(startButton).expandY().expandX();
		table.row();
		table.add(settingsButton).expandY().expandX();
		table.row();
		table.add(aboutButton).expandY().expandX();

		table.setFillParent(true);

		table.pack();
		
		// Listener.
		startButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				director.sendEvent(AppActorEvents.EVENT_TRANSITION_TO_GAME_SCENE, event.getRelatedActor());
			}

		});

		// Listener.
		settingsButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				director.sendEvent(AppActorEvents.EVENT_TRANSITION_TO_SETTINGS_SCENE, event.getRelatedActor());
			}

		});

		// Listener.
		aboutButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				director.sendEvent(AppActorEvents.EVENT_TRANSITION_TO_ABOUT_SCENE, event.getRelatedActor());
			}

		});

		// Add table to view
		addActor(table);

	}

}
