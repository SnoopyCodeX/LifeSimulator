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
import com.github.jotask.neat.jneat.Jota;

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

	private Jota jota;

	private Player player;
	
	@Override
	public void create () {
		Neat.instance = this;

		this.font = new BitmapFont();
		this.font.setColor(Color.BLACK);

		this.gui = new Gui(this);

		this.sb = new SpriteBatch();
		this.sr = new ShapeRenderer();
		this.sr.setAutoShapeType(true);

		this.camera = new Camera();

		this.entityManager = EntityManager.get();

		this.world = new World(new Vector2(), true);
		this.world.setContactListener(new WorldListener());
		this.renderer = new Box2DDebugRenderer();

		this.factory = new Factory(this);

		this.factory.createWalls();

		this.player = factory.getPlayer();

		this.jota = new Jota();

	}

	private void input(){ }

	public void update(){
		this.world.step(Gdx.graphics.getDeltaTime(), 6, 3);
		this.player.update();
		this.jota.eval();
		this.entityManager.update();
		this.jota.learn();
	}

	@Override
	public void render () {
		this.input();
		this.update();
		gl.glClearColor(b.r, b.g, b.b, b.a);

		gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.renderer.render(world, camera.combined);

		this.sb.setProjectionMatrix(camera.combined);

		this.sb.begin();
		this.entityManager.render(sb);
		this.player.render(sb);
		this.sb.end();

		this.sr.setProjectionMatrix(camera.combined);

		this.sr.begin();
		this.sr.set(ShapeRenderer.ShapeType.Filled);
		this.entityManager.debug(sr);
		this.player.debug(sr);
		this.sr.end();

		gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		this.sb.setProjectionMatrix(this.getGui().getCamera().combined);
		this.sr.setProjectionMatrix(this.getGui().getCamera().combined);

		this.jota.render(sb);
		this.jota.debug(sr);

		this.gui.render(sb);

	}
	
	@Override
	public void dispose () {
		this.sb.dispose();
		this.sr.dispose();
		this.jota.dispose();
		this.world.dispose();
		EntityManager.get().dispose();
		this.font.dispose();
		Neat.instance = null;
	}

	public World getWorld() { return world; }

	public Factory getFactory() { return factory; }

	public BitmapFont getFont() { return font; }

	public Player getPlayer() { return player; }

	public Gui getGui() { return gui; }

}
