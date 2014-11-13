package com.Phaze1D.RisingFallAndroidV2.Actors.Panels;

import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.Phaze1D.RisingFallAndroidV2.Singletons.LocaleStrings;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

/**
 * Created by davidvillarreal on 8/26/14. Rising Fall Android Version
 */
public class ObjectivePanel extends Panel {

	public double time;
	public double futureTime;

	public int ballsLeft;
	public int gameType;
	public int fontSize;

	public Label titleNode;

	public BitmapFont font;
	private LocaleStrings strings;

	public ObjectivePanel(Sprite panelSprite) {
		super(panelSprite);
		strings = LocaleStrings.getOurInstance();
	}

	public void createPanel() {


		String titleString;
		if (gameType == 2) {
			titleString = strings.getValue("BallsLeft") + " " + ballsLeft;
		} else {
			String objectString;
			int minutes = (int) time / (60);
			int seconds = (int) (time - minutes * 60);
			objectString = String.format("%02d:%02d", minutes, seconds);
			titleString = strings.getValue("TimeLeft") + " " + objectString;
		}

		font = BitmapFontSizer.getFontWithSize((int)BitmapFontSizer.sharedInstance().fontObjectivePanel());
		titleNode = new Label(titleString, new Label.LabelStyle(font,
				Color.YELLOW));
		titleNode.setPosition((int)(getWidth()/8),
				(int) (getHeight() / 2 - titleNode.getHeight() / 2));
		addActor(titleNode);

	}

	public boolean updateObjective(double currentTime) {
		if (gameType == 2) {
			titleNode.setText(strings.getValue("BallsLeft") + " " + ballsLeft);
		} else {
			String titleString;
			String objectString;
			int minutes = (int) time / (60);
			int seconds = (int) (time - minutes * 60);
			objectString = String.format("%02d:%02d", minutes, seconds);
			titleString = strings.getValue("TimeLeft") + " " + objectString;
			titleNode.setText(titleString);
			time = futureTime - currentTime;

		}

		if (gameType == 2 && ballsLeft <= 0) {
			ballsLeft = 0;
			titleNode.setText(strings.getValue("BallsLeft") + " " + ballsLeft);
			return true;
		} else if (gameType == 1 && time < 0) {
			time = 0;
			String titleString;
			String objectString;
			int minutes = (int) time / (60);
			int seconds = (int) (time - minutes * 60);
			objectString = String.format("%02d:%02d", minutes, seconds);
			titleString = strings.getValue("TimeLeft") + " " + objectString;
			titleNode.setText(titleString);
			time = futureTime - currentTime;
			return true;
		}
		return false;
	}
}
