package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import static com.badlogic.gdx.Gdx.audio;

public class MyGdxGame extends ApplicationAdapter {

	class MyActor extends Actor {
		Texture texture = new Texture(Gdx.files.internal("badlogic.jpg"));

		@Override
		public void draw(Batch batch, float parentAlpha) {
			 batch.draw(texture,0,0);
		}
	}
	Stage stage;

	private SpriteBatch batch1;
	private Texture texture1;
	private Texture texture2;
	private Texture texture3;
	private Sprite sprite1;
	private Sprite sprite2;
	private Sprite sprite3;
	private OrthographicCamera camera;

	private TextureAtlas textureAtlas;
	private TextureAtlas textureAtlasgespiegeld;
	private Animation animationwalk;
	private Animation animationdead;
	private Animation animationjump;
	private Animation animationidle;
	private Animation animationslide;
	private Animation animationwalksp;
	private Animation animationdeadsp;
	private Animation animationjumpsp;
	private Animation animationidlesp;
	private Animation animationslidesp;
	private Animation currentanimation;
	private float elapsedTime = 0;

	private Rectangle rectanglefeet;
	private Rectangle schoorsteen;

	private ShapeRenderer platform;

	private Sound dead;

	private float xPos = 10;
	private float yPos = 0;
	private double veloy = 0.0;
	private double gravity = -0.5;
	boolean onGround = false;
	boolean goleft = false;
	boolean isSpacepressed = false;
	private float movingxPos;
	private float movingyPos;
	private ArrayList<Bullet> bullets;
	private float cooldown = 0;

	public class Bullet{
		private float movingxpos = 0;
		private float movingypos = 0;
		private float speed = 200;
		private Texture texture4;
		private Sprite sprite4;
		private Rectangle collisionBox;
		private float count = 1;
		public Bullet(){
			collisionBox = new Rectangle(0f,0f,5f,5f);
			texture4 = new Texture(Gdx.files.internal("Snowball.png"));
			sprite4 = new Sprite(texture4, 0, 0, 64, 64);
			sprite4.setPosition(movingxpos, movingypos);
			sprite4.setRotation(0);
			if(goleft == false){
				speed = 200;
			} else{
				speed = -200;
			}
		}
		public void setPosition(float x, float y){
			movingxpos = x;
			movingypos = y;
		}

		public void render(SpriteBatch batch1, float delta){
				movingxpos += speed * delta;

			collisionBox.setPosition(movingxpos, movingypos);
			batch1.draw(sprite4, movingxpos, movingypos);
		}
		public float getXpos(){
			return movingxpos;
		}
	}

	@Override
	public void create () {
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.translate(camera.viewportWidth/2, camera.viewportHeight/2);
		//camera = (OrthographicCamera) stage.getViewport().getCamera();
		//camera.position.set(xPos,300,0);
		//camera.update();
		stage = new Stage(new ScreenViewport());
		//MyActor actor = new MyActor();
		batch1 = new SpriteBatch();
		//stage.addActor(actor);

		Skin skin1 = new Skin(Gdx.files.internal("skin/comic-ui.json"));
		Label label1 = new Label("Hello GDX",skin1);
		label1.setPosition(500,450);
		stage.addActor(label1);

		texture1 = new Texture(Gdx.files.internal("pacman.png"));
		sprite1 = new Sprite(texture1, 0,0,128,128);
		sprite1.setPosition(xPos,300);
		sprite1.setRotation(0);

		texture2 = new Texture(Gdx.files.internal("schoorsteen.png"));
		sprite2 = new Sprite(texture2, 0, 0, 128, 128);
		sprite2.setPosition(150, -50);
		sprite2.setRotation(0);

		texture3 = new Texture(Gdx.files.internal("platform.jpg"));
		sprite3 = new Sprite(texture3, 0,0,128,128);
		sprite3.setPosition(0,0);
		sprite3.setRotation(0);

		textureAtlas = new TextureAtlas(Gdx.files.internal("animaties.atlas"));
		animationwalk = new Animation(1/60f, textureAtlas.findRegions("Walk"));
		animationdead = new Animation(1/60f, textureAtlas.findRegions("Dead"));
		animationjump = new Animation(1/60f, textureAtlas.findRegions("Jump"));
		animationidle = new Animation(1/60f, textureAtlas.findRegions("Idle"));
		animationslide = new Animation(1/60f, textureAtlas.findRegions("Slide"));

		textureAtlasgespiegeld = new TextureAtlas(Gdx.files.internal("animatiesgespiegeld.atlas"));
		animationwalksp = new Animation(1/60f, textureAtlasgespiegeld.findRegions("Walk"));
		animationdeadsp = new Animation(1/60f, textureAtlasgespiegeld.findRegions("Dead"));
		animationjumpsp = new Animation(1/60f, textureAtlasgespiegeld.findRegions("Jump"));
		animationidlesp = new Animation(1/60f, textureAtlasgespiegeld.findRegions("Idle"));
		animationslidesp = new Animation(1/60f, textureAtlasgespiegeld.findRegions("Slide"));

		schoorsteen = new Rectangle(sprite2.getX()+35,sprite2.getY(),texture2.getWidth()-65,texture2.getHeight());
		rectanglefeet = new Rectangle(xPos, 65, 40, 15);
		dead = Gdx.audio.newSound(Gdx.files.internal("dead.mp3"));

		bullets = new ArrayList<Bullet>();

		platform = new ShapeRenderer();
	}

	private void StartJump () {
		if (onGround) {
			veloy = 12.0;
			onGround = false;
		}
	}

	private void EndJump () {
		//if (veloy < 6.0) {
		//	veloy = 6.0;
		//}
	}

	private void shoot(float delta){
		isSpacepressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);

		//System.out.print(movingxPos);
		//System.out.print(movingypos);
		if(isSpacepressed && cooldown <= 0){
			cooldown = 0.2f;
			Bullet b = new Bullet();
			bullets.add(b);
			b.setPosition(xPos, yPos/2);
		}
		if(cooldown>0){
			cooldown-=delta;
		}
	}

	@Override
	public void render () {
		//camera.update();

		//batch1.setProjectionMatrix(camera.combined);

		//camera.rotate(250);
		camera.update();

		boolean isApressed = Gdx.input.isKeyPressed(Input.Keys.A) || Gdx.input.isKeyPressed(Input.Keys.LEFT);
		boolean isDpressed = Gdx.input.isKeyPressed(Input.Keys.D) || Gdx.input.isKeyPressed(Input.Keys.RIGHT);
		boolean isWpressed = Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP);
		boolean isSpacepressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
		boolean isOverlapping = rectanglefeet.overlaps(schoorsteen);
		float d = Gdx.graphics.getDeltaTime();

		shoot(d);

		//System.out.print(bullets);
		for (Bullet b : bullets){
			if(b.getXpos() > Gdx.graphics.getWidth() || b.getXpos() < 0){
				bullets.remove(b);
				break;
			}
			/*
			if (b.collisionBox.overlaps(schoorsteen)){
				bullets.remove(b);
				bullets.remove(sprite2);
			}*/
		}

		if(!isOverlapping) {
			if(isDpressed) {
				xPos = xPos + (200 * (Gdx.graphics.getDeltaTime()));
				currentanimation = animationwalk;
				goleft = false;
			}
			else if(isApressed){
				xPos = xPos - (200 * (Gdx.graphics.getDeltaTime()));
				currentanimation = animationwalksp;
				goleft = true;
			}
			else if (!onGround){
				currentanimation = goleft ? animationjumpsp : animationjump;
			}
			else if (isSpacepressed){
				currentanimation = goleft ? animationslidesp : animationslide;
			}
			else {
				currentanimation = goleft ? animationidlesp : animationidle;
			}
		}
		else if(!(currentanimation == animationdead || currentanimation == animationdeadsp))
		{
			currentanimation = goleft ? animationdeadsp : animationdead;
			dead.play(10.0f);
		}

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1,0,0,1);

		/*platform.begin(ShapeRenderer.ShapeType.Filled);
		platform.setColor(355,355,355,1);
		platform.rect(300, 300, 100,50);
		platform.rect(sprite2.getX(),sprite2.getY(),texture2.getWidth(),texture2.getHeight());
		platform.end();*/

		batch1.begin();
		batch1.setProjectionMatrix(camera.combined);

		elapsedTime += Gdx.graphics.getDeltaTime();
		batch1.draw(sprite1, xPos,300);
		batch1.draw(sprite2, 150, -10);
		if(!isOverlapping) {
			batch1.draw((TextureRegion) currentanimation.getKeyFrame(elapsedTime, true), xPos, yPos);
		}
		else if (!onGround){
			batch1.draw((TextureRegion) currentanimation.getKeyFrame(elapsedTime, false), xPos, yPos);
		}
		else
		{
			batch1.draw((TextureRegion) currentanimation.getKeyFrame(elapsedTime, false), xPos, yPos);
		}

		for(Bullet b : bullets){
			b.render(batch1, d);
		}
		batch1.draw(sprite3,0,0,Gdx.graphics.getWidth(), 10);
		batch1.end();
		stage.act();
		stage.draw();

		rectanglefeet.setPosition(xPos+20, yPos);

		if (xPos >= 640){
			xPos = -128;
		}
		if (xPos <= -128){
			xPos = 640;
		}

		if (isWpressed) {
			StartJump();
		}
		else {
			EndJump();
		}
		veloy += gravity;
		yPos += veloy;

		if (yPos <0){
			yPos = 0;
			veloy = 0.0;
			onGround = true;
		}
	}
	
	@Override
	public void dispose () {

		batch1.dispose();
		textureAtlas.dispose();
		dead.dispose();
	}
}
