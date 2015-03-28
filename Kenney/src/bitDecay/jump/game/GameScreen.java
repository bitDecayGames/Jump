package bitDecay.jump.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.bitdecay.engine.utilities.ImageUtilities;


public class GameScreen implements Screen {

	Animation running;
	SpriteBatch batch = new SpriteBatch();
	float elapsed = 0;
	
	@Override
	public void show() {
		running = new Animation(.1f, ImageUtilities.getImagesByPrefix("yellow", "walk"));
		running.setPlayMode(PlayMode.LOOP);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.8f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		elapsed += delta;
		batch.draw(running.getKeyFrame(elapsed), 100, 100);
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}
