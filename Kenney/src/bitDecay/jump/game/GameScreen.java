package bitDecay.jump.game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.bitdecay.engine.utilities.ImageUtilities;


public class GameScreen implements Screen {

//	Animation running;
	
	@Override
	public void show() {
//		running = new Animation(.1f, ImageUtilities.getImagesByPrefix("player", "run"));
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0.0f, 0.8f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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
