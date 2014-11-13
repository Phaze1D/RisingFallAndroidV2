package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

/**
 * Created by davidvillarreal on 9/3/14. Rising Fall Android Version
 */
public class StoreBuyPanel extends Panel implements
		SimpleButton.SimpleButtonDelegate {

	public StoreBuyPanelDelegate delegate;

	public SimpleButton[] powerItem;

	private Label title;

	public TextureAtlas itemsAtlas;

	public StoreBuyPanel(Sprite panelSprite) {
		super(panelSprite);
	}

	public void createPanel() {
		powerItem = new SimpleButton[5];
		createTitle();
		createItems();
		createPosition();

	}

	public void createPosition() {
		float yOffset = (getHeight() - powerItem[0].getHeight() * 5 - title
				.getHeight()) / 7;
		title.setPosition((int) (getWidth() / 2 - title.getWidth()/1.5),
				(int) (getHeight() - yOffset - title.getHeight()));
		addActor(title);

		for (int i = 0; i < 5; i++) {
			float yPosition = yOffset + (yOffset + powerItem[0].getHeight())
					* i;
			powerItem[i].setPosition(
					(int) (getWidth() / 2 - powerItem[i].getWidth()/1.5f),
					(int) (yPosition));
			addActor(powerItem[i]);
		}
	}
	
	private void createTitle() {
		LocaleStrings strings = LocaleStrings.getOurInstance();
		title = new Label(strings.getValue("Powers"), new Label.LabelStyle(
				BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontStoreTitle()), new Color(0, .443f, .737f, 1)));
		title.setAlignment(Align.center);
	}

	private void createItems() {

		// Add item animation

		for (int i = 0; i < 5; i++) {
			SpriteDrawable spriteDrawable = new SpriteDrawable(
					itemsAtlas.createSprite("st" + (i + 1)));
			SimpleButton pbutton = new SimpleButton("",
					new ImageTextButton.ImageTextButtonStyle(spriteDrawable,
							null, null, BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontButtonL())));
			pbutton.type = i + 9;
			pbutton.delegate = this;
			SizeToAction up = Actions.sizeTo(pbutton.getWidth() * 1.1f,
					pbutton.getHeight() * 1.1f, .8f);
			SizeToAction down = Actions.sizeTo(pbutton.getWidth() / 1.1f,
					pbutton.getHeight() / 1.1f, .8f);
			SequenceAction seq = Actions.sequence(up, down);
			pbutton.addAction(Actions.forever(seq));
			addActor(pbutton);
			powerItem[i] = pbutton;
		}

	}

	public void disableButton() {
		for (SimpleButton button : powerItem) {
			button.setTouchable(Touchable.disabled);
		}
	}

	public void enableButton() {
		for (SimpleButton button : powerItem) {
			button.setTouchable(Touchable.enabled);
		}
	}

	@Override
	public void buttonPressed(int type) {
		delegate.pButtonPressed(type - 8);
	}

	public interface StoreBuyPanelDelegate {
		public void pButtonPressed(int powerTyped);
	}
}
