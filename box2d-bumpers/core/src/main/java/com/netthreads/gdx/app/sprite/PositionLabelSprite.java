package com.netthreads.gdx.app.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;

/**
 * Displays position.
 * 
 * Add this to an existing Sprite to have the sprite position displayed on the view.
 * 
 * 
 */
public class PositionLabelSprite extends TextSprite
{
	private static final String FONT_FILE_LARGE = "data/font.fnt";
	private static final String FONT_IMAGE_LARGE = "data/font.png";

	private static final String TEXT_POSITION_SEPARATOR = ",";

	/**
	 * Default parameters.
	 * 
	 */
	public PositionLabelSprite()
	{
		super(Gdx.files.internal(FONT_FILE_LARGE), Gdx.files.internal(FONT_IMAGE_LARGE), Color.WHITE);
	}

	/**
	 * Construct position label sprite.
	 * 
	 * @param fontFile
	 * @param imageFile
	 * @param color
	 */
	public PositionLabelSprite(FileHandle fontFile, FileHandle imageFile, Color color)
	{
		super(fontFile, imageFile, color);
	}

	/**
	 * Draw touch message.
	 * 
	 */
	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		StringBuilder text = getText();
		text.setLength(0);
		text.append(Math.round(getParent().getX()));
		text.append(TEXT_POSITION_SEPARATOR);
		text.append(Math.round(getParent().getY()));

		super.draw(batch, parentAlpha);
	}

}
