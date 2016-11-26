package com.netthreads.gdx.app.sprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.netthreads.gdx.app.layer.element.DisplayContactElement;
import com.netthreads.libgdx.sprite.SimpleSprite;

/**
 * Texture and text sprite.
 * 
 */
public class LabeledSprite extends SimpleSprite
{
	private static final String FONT_FILE_LARGE = "data/font.fnt";
	private static final String FONT_IMAGE_LARGE = "data/font.png";

	private TextSprite labelSprite;
	private DisplayContactElement contactElement;

	/**
	 * Construct sprite.
	 * 
	 * @param textureRegion
	 *            The sprite texture.
	 */
	public LabeledSprite(TextureRegion textureRegion)
	{
		super(textureRegion);

		labelSprite = new TextSprite(Gdx.files.internal(FONT_FILE_LARGE), Gdx.files.internal(FONT_IMAGE_LARGE), Color.WHITE);

		addActor(labelSprite);
	}

	/**
	 * Assign text.
	 * 
	 * @param text
	 */
	public void setText(CharSequence text)
	{
		if (labelSprite != null)
		{
			labelSprite.setText(text);

			labelSprite.setOriginX(getOriginX() - labelSprite.getWidth() / 2);
			labelSprite.setOriginY(getOriginY() + labelSprite.getHeight() / 2);
		}
	}

	/**
	 * Assign text.
	 * 
	 * @param text
	 */
	public void setText(Character character)
	{
		if (labelSprite != null)
		{
			labelSprite.getText().setLength(0);
			labelSprite.getText().append(character);

			labelSprite.setOriginX(getOriginX() - labelSprite.getWidth() / 2);
			labelSprite.setOriginY(getOriginY() + labelSprite.getHeight() / 2);
		}
	}

	/**
	 * Return associated text sprite.
	 * 
	 * @return The associated text sprite.
	 */
	public TextSprite getLabelSprite()
	{
		return labelSprite;
	}

	/**
	 * Associated collision handler.
	 * 
	 * @return
	 */
	public DisplayContactElement getContactElement()
	{
		return contactElement;
	}

	/**
	 * Associated collision handler.
	 * 
	 * @param contactElement
	 */
	public void setContactElement(DisplayContactElement contactElement)
	{
		this.contactElement = contactElement;
	}

}
