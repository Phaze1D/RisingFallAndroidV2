package com.Phaze1D.RisingFallAndroidV2.Scenes;

import com.Phaze1D.RisingFallAndroidV2.Actors.Ball;
import com.Phaze1D.RisingFallAndroidV2.Actors.Buttons.SimpleButton;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.SellItemPanel;
import com.Phaze1D.RisingFallAndroidV2.Actors.Panels.StoreBuyPanel;
import com.Phaze1D.RisingFallAndroidV2.Controllers.CorePaymentDelegate;
import com.Phaze1D.RisingFallAndroidV2.Controllers.SoundControllerDelegate;
import com.Phaze1D.RisingFallAndroidV2.Objects.Spawner;
import com.Phaze1D.RisingFallAndroidV2.Physics.PhysicsWorld;
import com.Phaze1D.RisingFallAndroidV2.Singletons.BitmapFontSizer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.RandomXS128;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;

import java.util.LinkedList;

/**
 * Created by davidvillarreal on 8/26/14. Rising Fall Android Version
 */
public class StoreScene extends Stage implements Screen,
		StoreBuyPanel.StoreBuyPanelDelegate, SimpleButton.SimpleButtonDelegate {

	public StoreDelegate delegate;

	private boolean isCreated;
	private boolean hasFinished;
	private boolean isSellCreated;

	public TextureAtlas sceneAtlas;
	public TextureAtlas buttonAtlas;
	public TextureAtlas ballsAtlas;
	public TextureAtlas itemsAtlas;

	private Spawner[] spawners;
	private LinkedList<Ball> ballQuene;

	// @property NSArray * productIdentifiers;
	// @property NSArray * products;

	private float spawnRate;
	private float nextSpawn;

	private Vector2 velocity;
	private Vector2 sidePosition;
	private Vector2 backButtonPostion;
	private Vector2 initPointSellPanel;

	private StoreBuyPanel buyPanel;
	private SellItemPanel sellItemPanel;

	private SimpleButton backB;

	private PhysicsWorld physicsWorld;
	private Group ballGroup;

	private Sprite itemsArea;
	private Sprite backButton;
	private Sprite sellItemArea;

	private RandomXS128 randomGen = new RandomXS128();

	public CorePaymentDelegate corePaymentDelegate;

	public SoundControllerDelegate soundDelegate;

	@Override
	public void render(float delta) {
		draw();
		act();
		if (hasFinished) {

			nextSpawn += delta;

			if (nextSpawn >= spawnRate) {
				nextSpawn = nextSpawn - spawnRate;
				spawnBall();
			}

			physicsWorld.evaluatePhysics(delta);
		}

	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		if (!isCreated) {
			Gdx.input.setInputProcessor(this);
			Gdx.input.setCatchBackKey(true);
			createScene();
			hasFinished = true;
		}

	}

	@Override
	public void hide() {
		corePaymentDelegate = null;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}
	
	

	@Override
	public void dispose() {
		corePaymentDelegate = null;
		super.dispose();
	}

	private void spawnBall() {

		if (spawners != null) {

			Ball ball = spawners[randomGen.nextInt(10)].spawnBall();
			ball.velocity.set(velocity);
			ball.isPhysicsActive = true;
			ball.setTouchable(Touchable.disabled);
			physicsWorld.addBody(ball);
			ballGroup.addActor(ball);
			ballQuene.addLast(ball);
		}

		if (ballQuene.size() > 0) {
			Ball ball = ballQuene.getFirst();
			if (ball.getY() < 0) {
				ballQuene.removeFirst();
				ball.remove();
				ball.isPhysicsActive = false;
				ball.body = null;
				ball.clear();
				ball.clearActions();
				ball.clearListeners();
				ball = null;
			}
		}

	}

	private void createScene() {

		initVariables();
		createBackground();
		createPositions();
		createSideView();
		createBackButton();
		// validateProductsID();

	}

	private void initVariables() {

		spawnRate = 1;
		ballQuene = new LinkedList<Ball>();
		velocity = new Vector2(0, -200);
		initPointSellPanel = new Vector2();
		physicsWorld = new PhysicsWorld();
		ballGroup = new Group();
		itemsArea = sceneAtlas.createSprite("itemsArea");
		backButton = buttonAtlas.createSprite("backButton");
		sellItemArea = sceneAtlas.createSprite("sellItemArea");

	}

	private void createBackground() {
		createSpawners();
		
		Image backbo = new Image(sceneAtlas.createSprite("backbox"));
		backbo.setCenterPosition((int) (this.getWidth() / 2),
				(int) (this.getHeight() / 2));
		addActor(backbo);
		
		addActor(ballGroup);
		
		Image backgroundUI = new Image(sceneAtlas.createSprite("background"));
		backgroundUI.setPosition(0, 0);
		backgroundUI.setSize((int) this.getWidth(), (int) this.getHeight());
		addActor(backgroundUI);
	}

	private void createSpawners() {

		float width = ballsAtlas.createSprite("ball0").getWidth();

		spawners = new Spawner[10];
		for (int i = 0; i < 10; i++) {
			Spawner spawners1 = new Spawner();
			float offsetX = (getWidth() - (width * 10)) / 11;
			float x = offsetX + (width + offsetX) * i;
			spawners1.position = new Vector2(x, getHeight());
			spawners1.powerUpProb = -1;
			spawners1.ballAtlas = ballsAtlas;
			spawners1.randGen = randomGen;
			spawners[i] = spawners1;

		}

	}

	private void createPositions() {

		float yOffset = (getHeight() - backButton.getHeight() - itemsArea
				.getHeight()) / 3f;

		backButtonPostion = new Vector2(0, yOffset);
		sidePosition = new Vector2(0, yOffset * 2 + backButton.getHeight());

	}

	private void createSideView() {

		buyPanel = new StoreBuyPanel(itemsArea);
		buyPanel.setPosition((int) sidePosition.x, (int) sidePosition.y);
		buyPanel.delegate = this;
		buyPanel.itemsAtlas = itemsAtlas;
		buyPanel.soundDelegate = soundDelegate;
		buyPanel.createPanel();
		addActor(buyPanel);

	}

	private void createBackButton() {

		backB = new SimpleButton("", new ImageTextButton.ImageTextButtonStyle(
				new SpriteDrawable(backButton), null, null,
				BitmapFontSizer.getFontWithSize(11, null)));
		backB.setPosition((int) backButtonPostion.x, (int) backButtonPostion.y);
		backB.delegate = this;
		backB.soundDelegate = soundDelegate;
		addActor(backB);

	}

	private String getPowerID(int powerType) {
		switch (powerType) {
		case 1:
			System.out.println("David villarreal    -------- " + powerType);
			return CorePaymentDelegate.POWER1_ID;
			
		case 2:
			System.out.println("David villarreal    -------- " + powerType);
			return CorePaymentDelegate.POWER2_ID;
		case 3:
			System.out.println("David villarreal    -------- " + powerType);
			return CorePaymentDelegate.POWER3_ID;
		case 4:
			System.out.println("David villarreal    -------- " + powerType);
			return CorePaymentDelegate.POWER4_ID;
		case 5:
			System.out.println("David villarreal    -------- " + powerType);
			return CorePaymentDelegate.POWER5_ID;

		default:
			return null;
		}
	}

	private void createSellPanel(final int powerType) {
		buyPanel.disableButton();
		float xOffset = (getWidth() - buyPanel.getWidth() - sellItemArea
				.getWidth()) / 2;
		initPointSellPanel.set(getWidth() / 2, -sellItemArea.getHeight());

		sellItemPanel = new SellItemPanel(sellItemArea, corePaymentDelegate);
		sellItemPanel.setPosition((int) initPointSellPanel.x,
				(int) initPointSellPanel.y);
		sellItemPanel.soundDelegate = soundDelegate;
		sellItemPanel.buttonAtlas = buttonAtlas;
		sellItemPanel.storeSceneAtlas = itemsAtlas;
		sellItemPanel.createPanel(powerType, true);
		sellItemPanel.powerID = getPowerID(powerType);
		sellItemPanel.addAction(Actions.alpha(0));
		sellItemPanel.addAction(Actions.scaleTo(0, 0));

		MoveToAction moveTo = Actions.moveTo(
				(int) (xOffset + buyPanel.getWidth()),
				(int) (getHeight() / 2 - sellItemPanel.getHeight() / 2), .3f);
		ScaleToAction scaleU = Actions.scaleTo(1, 1, .3f);
		AlphaAction alphaAction = Actions.alpha(1, .3f);
		ParallelAction group = Actions.parallel(moveTo, scaleU, alphaAction);
		Action complete = new Action() {
			@Override
			public boolean act(float delta) {
				buyPanel.enableButton();
				sellItemPanel.createTextArea(powerType);
				return true;
			}
		};

		sellItemPanel.addAction(Actions.sequence(group, complete));

		addActor(sellItemPanel);
	}

	private void removeSellPanel() {
		buyPanel.disableButton();
		sellItemPanel.textView.remove();
		sellItemPanel.textView = null;
		sellItemPanel.corePaymentDelegate = null;

		MoveToAction moveTo = Actions.moveTo(initPointSellPanel.x,
				initPointSellPanel.y, .25f);
		ScaleToAction scaleU = Actions.scaleTo(0, 0, .25f);
		AlphaAction alphaAction = Actions.alpha(0, .25f);
		ParallelAction group = Actions.parallel(moveTo, scaleU, alphaAction);
		Action complete = new Action() {
			@Override
			public boolean act(float delta) {
				sellItemPanel.remove();
				sellItemPanel.clear();
				sellItemPanel = null;
				buyPanel.enableButton();
				return true;
			}
		};

		sellItemPanel.addAction(Actions.sequence(group, complete));

	}

	private void replaceSellPanel(final int powerType) {

		if (powerType != sellItemPanel.powerType) {
			buyPanel.disableButton();
			sellItemPanel.textView.remove();

			MoveToAction moveTo = Actions.moveTo(initPointSellPanel.x,
					initPointSellPanel.y, .25f);
			ScaleToAction scaleU = Actions.scaleTo(0, 0, .25f);
			AlphaAction alphaAction = Actions.alpha(0, .25f);
			ParallelAction group = Actions
					.parallel(moveTo, scaleU, alphaAction);
			Action complete = new Action() {
				@Override
				public boolean act(float delta) {
					sellItemPanel.remove();
					sellItemPanel.clear();
					sellItemPanel = null;
					createSellPanel(powerType);
					return true;
				}
			};

			sellItemPanel.addAction(Actions.sequence(group, complete));
		} else {
			removeSellPanel();
			isSellCreated = false;
		}

	}
	
	public void disableBackButton(){
		backB.setTouchable(Touchable.disabled);
	}
	
	public void enableBackButton(){
		backB.setTouchable(Touchable.enabled);
		
	}

	@Override
	public void pButtonPressed(int powerTyped) {

		if (!isSellCreated) {
			createSellPanel(powerTyped);
			isSellCreated = true;
		} else {
			replaceSellPanel(powerTyped);
		}

	}
	
	@Override
	public boolean keyDown(int keyCode) {
		if(keyCode == Keys.BACK){
			buttonPressed(0);
		}
		return super.keyDown(keyCode);
	}

	@Override
	public void buttonPressed(int type) {

		if (isSellCreated) {
			removeSellPanel();
			isSellCreated = false;
		} else {
			delegate.storeBackPressed();
		}

	}

	public interface StoreDelegate {
		public void storeBackPressed();
	}
}
