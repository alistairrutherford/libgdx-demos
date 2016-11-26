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
import com.netthreads.gdx.app.definition.AppEvents;
import com.netthreads.gdx.core.Box2DBumpers;
import com.netthreads.libgdx.director.AppInjector;
import com.netthreads.libgdx.director.Director;
import com.netthreads.libgdx.scene.Layer;

/**
 * Menu layer.
 * 
 */
public class MenuLayer extends Layer
{

	private static final String UI_FILE = "data/uiskin.json";
	private static final String URL_LABEL_FONT = "default-font";

	private Table table;
	private Skin skin;
	private Label titleLabel;
	private Button startButton;
	private Button aboutButton;

	// Director of the action.
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
		titleLabel = new Label(Box2DBumpers.APPLICATION_NAME, skin, URL_LABEL_FONT, Color.YELLOW);

		// ---------------------------------------------------------------
		// Buttons.
		// ---------------------------------------------------------------
		startButton = new TextButton("Start", skin);
		aboutButton = new TextButton("About", skin);

		// ---------------------------------------------------------------
		// Table
		// ---------------------------------------------------------------
		table = new Table();

		table.setSize(getWidth(), getHeight());

		table.row();
		table.add(titleLabel).expandY().expandX();
		table.row();
		table.add(startButton).expandY().expandX();
		table.row();
		table.add(aboutButton).expandY().expandX();

		table.setFillParent(true);

		table.pack();
		
		// Listener.
		// Listener.
		startButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				director.sendEvent(AppEvents.EVENT_TRANSITION_TO_MAIN_SCENE, event.getRelatedActor());
			}

		});

		// Listener.
		// Listener.
		aboutButton.addListener(new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				director.sendEvent(AppEvents.EVENT_TRANSITION_TO_ABOUT_SCENE, event.getRelatedActor());
			}

		});

		table.pack();

		// Add table to view
		addActor(table);

	}
}
