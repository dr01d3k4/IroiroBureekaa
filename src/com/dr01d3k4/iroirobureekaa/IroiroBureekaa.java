package com.dr01d3k4.iroirobureekaa;



import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;



import com.dr01d3k4.iroirobureekaa.input.Input;
import com.dr01d3k4.iroirobureekaa.render.Graphics;
import com.dr01d3k4.iroirobureekaa.render.RenderView;
import com.dr01d3k4.iroirobureekaa.saving.FileIO;
import com.dr01d3k4.iroirobureekaa.saving.GameData;
import com.dr01d3k4.iroirobureekaa.saving.Settings;
import com.google.android.gms.common.SignInButton;
import com.google.example.games.basegameutils.BaseGameActivity;



public class IroiroBureekaa extends BaseGameActivity implements OnClickListener {
	private static final boolean DEBUG_BUILD = true;
	public int WINDOW_WIDTH = 0;
	public int WINDOW_HEIGHT = 0;
	public int CANVAS_WIDTH = 0;
	public int CANVAS_HEIGHT = 0;
	public float SCALE_X = 1;
	public float SCALE_Y = 1;
	
	private RenderView renderView;
	private Graphics graphics;
	private FileIO fileIO;
	private Input input;
	private Screen screen;
	public GameData gameData;
	
	private SignInButton signInButton;
	private Button signOutButton;
	
	
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		if (DEBUG_BUILD) {
			enableDebugLog(true);
		}
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
			.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		
		super.onCreate(savedInstanceState);
		
		final DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		
		WINDOW_WIDTH = metrics.widthPixels;
		WINDOW_HEIGHT = metrics.heightPixels;
		
		CANVAS_WIDTH = WINDOW_WIDTH;
		CANVAS_HEIGHT = WINDOW_HEIGHT;
		
		SCALE_X = (float) CANVAS_WIDTH / WINDOW_WIDTH;
		SCALE_Y = (float) CANVAS_HEIGHT / WINDOW_HEIGHT;
		
		final Bitmap frameBuffer = Bitmap.createBitmap(CANVAS_WIDTH, CANVAS_HEIGHT, Config.RGB_565);
		
		setContentView(R.layout.activity_iroiro_bureekaa);
		renderView = (RenderView) findViewById(R.id.rvRenderView);
		renderView.init(this, frameBuffer);
		
		signInButton = (SignInButton) findViewById(R.id.btSignInButton);
		signInButton.setOnClickListener(this);
		signOutButton = (Button) findViewById(R.id.btSignOutButton);
		signOutButton.setOnClickListener(this);
		
		graphics = new Graphics(getAssets(), frameBuffer);
		fileIO = new FileIO(this);
		input = new Input(this, renderView, SCALE_X, SCALE_Y);
		gameData = new GameData();
		
		changeScreen(new LoadingScreen(this));
	}
	
	
	
	public void update(final float deltaTime) {
		screen.update(deltaTime);
	}
	
	
	
	public void render(final float deltaTime) {
		screen.render(deltaTime);
	}
	
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		renderView.resume();
		screen.resume();
	}
	
	
	
	@Override
	public void onPause() {
		super.onPause();
		
		screen.pause();
		renderView.pause();
		
		if (isFinishing()) {
			screen.dispose();
		}
	}
	
	
	
	public void changeScreen(final Screen newScreen) {
		if (newScreen == null) {
			throw new IllegalArgumentException("Screen cannot be null");
		}
		if (screen != null) {
			screen.dispose();
		}
		screen = newScreen;
		screen.calculateSize();
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				setSignInButtonsVisible(screen.signInButtonsVisible());
			}
		});
	}
	
	
	
	public Graphics getGraphics() {
		return graphics;
	}
	
	
	
	public Input getInput() {
		return input;
	}
	
	
	
	public FileIO getFileIO() {
		return fileIO;
	}
	
	
	
	public void saveGame() {
		Settings.save(getFileIO(), gameData);
	}
	
	
	
	public void setSignInButtonShowing(final boolean visible) {
		if ((screen != null) && (!screen.signInButtonsVisible())) {
			setSignInButtonsVisible(false);
		} else if (visible) {
			signInButton.setVisibility(View.GONE);
			signOutButton.setVisibility(View.VISIBLE);
		} else {
			signInButton.setVisibility(View.VISIBLE);
			signOutButton.setVisibility(View.GONE);
		}
	}
	
	
	
	public void setSignInButtonsVisible(final boolean visible) {
		if (visible) {
			setSignInButtonShowing(isSignedIn());
		} else {
			signInButton.setVisibility(View.GONE);
			signOutButton.setVisibility(View.GONE);
		}
	}
	
	
	
	@Override
	public void onSignInSucceeded() {
		setSignInButtonShowing(true);
	}
	
	
	
	@Override
	public void onSignInFailed() {
		setSignInButtonShowing(false);
	}
	
	
	
	@Override
	public void onClick(final View view) {
		if (view.getId() == R.id.btSignInButton) {
			beginUserInitiatedSignIn();
		} else if (view.getId() == R.id.btSignOutButton) {
			signOut();
			
			setSignInButtonShowing(false);
		}
		
	}
	
	
	
	@Override
	public void onBackPressed() {
		screen.onBackPressed();
	}
}