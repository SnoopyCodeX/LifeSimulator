package com.github.jotask.neat;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.github.jotask.neat.engine.*;
import com.github.jotask.neat.engine.entity.Player;
import com.github.jotask.neat.jneat.Constants;
import com.github.jotask.neat.jneat.JNeat;

import static com.badlogic.gdx.Gdx.gl;

public class Neat extends ApplicationAdapter {

	private static Neat instance;
	public static Neat get(){ return instance; }

	final Color b = Color.WHITE;

	SpriteBatch sb;
	ShapeRenderer sr;

	Camera camera;

	World world;
	Box2DDebugRenderer renderer;

	EntityManager entityManager;

	BitmapFont font;

	Gui gui;

	Factory factory;

	JNeat neat;

	private Player player;
	
	@Override
	public void create () {
		Neat.instance = this;

		font = new BitmapFont();
		font.setColor(Color.BLACK);

		gui = new Gui(this);

		sb = new SpriteBatch();
		sr = new ShapeRenderer();
		sr.setAutoShapeType(true);

		camera = new Camera();

		entityManager = EntityManager.get();

		world = new World(new Vector2(), true);
		world.setContactListener(new WorldListener());
		renderer = new Box2DDebugRenderer();

		factory = new Factory(this);

		factory.createWalls();

		player = factory.getPlayer();

		neat = new JNeat(this);

	}

	private void input(){ }

	public void update(){
		world.step(Gdx.graphics.getDeltaTime(), 6, 3);
		player.update();
		neat.evaluate();
		entityManager.update();
		neat.learn();
	}

	@Override
	public void render () {
		input();
		update();
		gl.glClearColor(b.r, b.g, b.b, b.a);

		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.render(world, camera.combined);

		sb.setProjectionMatrix(camera.combined);

		sb.begin();
		entityManager.render(sb);
		player.render(sb);
		sb.end();

		sr.setProjectionMatrix(camera.combined);

		sr.begin();
		sr.set(ShapeRenderer.ShapeType.Filled);
		entityManager.debug(sr);
		player.debug(sr);
		sr.end();


		gl.glEnable(GL20.GL_BLEND);
		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);


		sb.setProjectionMatrix(this.getGui().getCamera().combined);
		sr.setProjectionMatrix(this.getGui().getCamera().combined);

		if(Constants.DRAW) {
			neat.render(sb);
			neat.debug(sr);
			gui.render(sb);
		}

	}
	
	@Override
	public void dispose () {
		sb.dispose();
		sr.dispose();
		neat.dispose();
		world.dispose();
		EntityManager.get().dispose();
		font.dispose();
		Neat.instance = null;
	}

	public World getWorld() { return world; }

	public Factory getFactory() { return factory; }

	public BitmapFont getFont() { return font; }

	public JNeat getNeat() { return neat; }

	public Camera getCamera() { return camera; }

	public Player getPlayer() { return player; }

	public Gui getGui() { return gui; }

}
