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
import com.github.jotask.neat.engine.entity.Enemy;
import com.github.jotask.neat.neat.JotaNeat;
import com.github.jotask.neat.neat.JotaNeatRenderer;

import java.util.LinkedList;

public class Neat extends ApplicationAdapter {

	private static Neat instance;
	public static final Neat get(){ return instance; }

	final Color b = Color.WHITE;

	final int POPULATION = 5;
	final int FOOD = 10;

	SpriteBatch sb;
	ShapeRenderer sr;

	Camera camera;

	World world;
	Box2DDebugRenderer renderer;

	EntityManager entityManager;

	BitmapFont font;

	Gui gui;

	Factory factory;

	JotaNeat jNeat;
	Thread thread;

	JotaNeatRenderer renderNeat;

	LinkedList<Enemy> toDelete;
	
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

		for(int i = 0; i < FOOD; i++){
			factory.food();
		}

		toDelete = new LinkedList<Enemy>();

		jNeat = new JotaNeat(this);
		thread = new Thread(jNeat);
		thread.start();

		renderNeat = new JotaNeatRenderer(this, jNeat);

	}

	private void input(){ }

	public void update(){
		world.step(Gdx.graphics.getDeltaTime(), 6, 3);
		entityManager.update();
		for(Enemy e: toDelete){
			if(!(e.isDie())){
				System.out.println("SomeError");
				continue;
			}
			Neat.get().getWorld().destroyBody(e.getBody());
		}
		toDelete.clear();
	}

	@Override
	public void render () {
		input();
		update();
		Gdx.gl.glClearColor(b.r, b.g, b.b, b.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		sb.setProjectionMatrix(camera.combined);
		sb.begin();
		entityManager.render(sb);
		sb.end();
		renderer.render(world, camera.combined);
		sr.setProjectionMatrix(camera.combined);
		sr.begin();
		sr.set(ShapeRenderer.ShapeType.Filled);
		entityManager.debug(sr);
		sr.end();
		gui.render(sb);
		renderNeat.render(sb, sr);
	}
	
	@Override
	public void dispose () {
		jNeat.finish = true;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
			thread.interrupt();
		}
		sb.dispose();
		sr.dispose();
		world.dispose();
		EntityManager.get().dispose();
		font.dispose();
		Neat.instance = null;
	}

	public LinkedList<Enemy> getToDelete() { return toDelete; }

	public World getWorld() { return world; }

	public Factory getFactory() { return factory; }

	public BitmapFont getFont() { return font; }

	public JotaNeat getjNeat() { return jNeat; }
}
