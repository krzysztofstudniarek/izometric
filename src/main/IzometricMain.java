package main;

import java.io.IOException;
import java.util.Random;

//import map.HorizontalMap;
//import map.Map;
import menuObjects.Button;
import menuObjects.Pointer;
import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import Entities.Cell;

import static org.lwjgl.opengl.GL11.*;

public class IzometricMain {

	public static final int WIDTH = 800;
	public static final int HIGHT = 600;
	private boolean isRunning = true;

	private Texture texture;

	int size = 5;

	int X = size / 2;
	int Y = size / 2;

	int view = 0;

	double score = 0;
	double difficulty = 1025;
	int counter = 1;

	private double timeStamp = System.currentTimeMillis();
	private double timeStamp1 = System.currentTimeMillis();

	double translate_x = 0, translate_y = 0, xConvertion = 0,
			yConvertion = 150;

	private Cell[][] cells = new Cell[size][size];

	// private Map map;

	// private HorizontalMap xMap, yMap;

	private boolean lose = false;

	private static UnicodeFont font;
	private static UnicodeFont font1;
	private static UnicodeFont font2;

	private Pointer pointer;

	private Button gameOverButton;
	private Button startGame;

	private int gameMode = 0;

	public IzometricMain() {

		SetUpDisplay();
		SetUpOpenGL();
		SetUpEntities();

		while (isRunning) {
			GL11.glClear(GL_COLOR_BUFFER_BIT);
			GL11.glColor4d(1, 1, 1, 1);

			pointer.update(0);

			switch (gameMode) {
			case 0:

				pointer.update(0);

				render(0);

				if (this.gameOverButton.clicked()) {
					isRunning = false;
				}

				if (this.startGame.clicked()) {

					SetUpEntities();
					SetUpOpenGL();

					gameMode = 2;
				}

				break;
			case 1:

				cells[X][Y].visited = true;

				this.render(1);

				this.input();

				glTranslated(translate_x, translate_y, 0);

				if (cells[X][Y].getY() != HIGHT / 2 + yConvertion) {
					translate_y = -(float) (cells[X][Y].getY() - HIGHT / 2 - yConvertion);
					yConvertion -= translate_y;
				} else {
					translate_y = 0;
				}

				if (cells[X][Y].getX() != WIDTH / 2 + xConvertion) {
					translate_x = -(float) (cells[X][Y].getX() - WIDTH / 2 - xConvertion);
					xConvertion -= translate_x;
				} else {
					translate_x = 0;
				}

				gameLogic();

				if (lose) {
					double s = score;
					SetUpEntities();
					SetUpOpenGL();
					this.score = s;
					this.gameMode = 3;
				}

				break;

			case 2:

				pointer.update(0);

				render(2);

				if (this.startGame.clicked()) {

					SetUpEntities();
					SetUpOpenGL();

					gameMode = 1;
				}

				break;

			case 3:

				pointer.update(0);

				render(3);

				if (this.gameOverButton.clicked()) {
					SetUpEntities();
					SetUpOpenGL();
					gameMode = 0;
					pointer.setLocation(0, 0);
				}

				if (this.startGame.clicked()) {

					SetUpEntities();
					SetUpOpenGL();
					pointer.setLocation(0, 0);
					gameMode = 1;
				}

				break;
			}

			/*
			 * map.update(xConvertion, yConvertion, X, Y);
			 * xMap.update(xConvertion, yConvertion); yMap.update(xConvertion,
			 * yConvertion);
			 */

			Display.update();
			Display.sync(1000);
			if (Display.isCloseRequested()) {
				isRunning = false;

			}

		}
	}

	private void input() {
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if ((Keyboard.getEventKey() == Keyboard.KEY_A || Keyboard
						.getEventKey() == Keyboard.KEY_LEFT)) {

					if (Y > 0)
						Y--;
				}
			}
			if (Keyboard.getEventKeyState()) {
				if ((Keyboard.getEventKey() == Keyboard.KEY_D || Keyboard
						.getEventKey() == Keyboard.KEY_RIGHT)) {
					if (Y < size - 1)
						Y++;
				}
			}

			if (Keyboard.getEventKeyState()) {
				if ((Keyboard.getEventKey() == Keyboard.KEY_W || Keyboard
						.getEventKey() == Keyboard.KEY_UP)) {

					if (X > 0)
						X--;
				}
			}

			if (Keyboard.getEventKeyState()) {
				if ((Keyboard.getEventKey() == Keyboard.KEY_S || Keyboard
						.getEventKey() == Keyboard.KEY_DOWN)) {
					if (X < size - 1)
						X++;
				}
			}

			if (Keyboard.getEventKeyState()) {
				if ((Keyboard.getEventKey() == Keyboard.KEY_SPACE) && !lose) {

					switch (cells[X][Y].type) {
					case 0:
						if (cells[X][Y].removeHeight()) {
							score += 100;
						}
						break;
					case 1:
						for (int i = 0; i < size; i++) {
							for (int j = 0; j < size; j++) {
								if (cells[i][j].removeHeight()) {
									score += 100;
								}
							}
						}
						cells[X][Y].type = 0;
						break;
					case 2:
						score += 100 * cells[X][Y].z;
						cells[X][Y].z = 0;
						cells[X][Y].type = 0;
						break;
					case 3:
						for (int i = 0; i < size; i++) {
							if (i != Y && cells[X][i].removeHeight()) {
								score += 100;
							}

							if (i != X && cells[i][Y].removeHeight()) {
								score += 100;
							}
						}

						if (cells[X][Y].removeHeight()) {
							score += 100;
						}
						cells[X][Y].type = 0;
						break;
					}

				}
			}

		}
	}

	private void SetUpDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HIGHT));
			Display.setInitialBackground(1f, 1f, 1f);
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

	}

	private void SetUpOpenGL() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glClearDepth(1);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glViewport(0, 0, WIDTH, HIGHT);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HIGHT, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}

	private void SetUpEntities() {

		int startX = WIDTH / 2;
		int startY = 100;

		lose = false;
		xConvertion = 0;
		yConvertion = 150;
		this.score = 0;
		this.difficulty = 1025;
		this.counter = 1;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell(startX + j * 20, startY + j * 10, 40, 20);
				cells[i][j].z = 5;
			}

			startX -= 20;
			startY += 10;
		}

		try {
			texture = TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("controls.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		java.awt.Font awtFont = new java.awt.Font("Verdana",
				java.awt.Font.BOLD, 25);
		font = new UnicodeFont(awtFont);
		font.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		font.addAsciiGlyphs();
		try {
			font.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		java.awt.Font awtFont1 = new java.awt.Font("Verdana",
				java.awt.Font.BOLD, 100);
		font1 = new UnicodeFont(awtFont1);
		font1.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		font1.addAsciiGlyphs();
		try {
			font1.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		java.awt.Font awtFont2 = new java.awt.Font("Verdana",
				java.awt.Font.BOLD, 30);
		font2 = new UnicodeFont(awtFont2);
		font2.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
		font2.addAsciiGlyphs();
		try {
			font2.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

		pointer = new Pointer(Mouse.getX(), Mouse.getY());

		gameOverButton = new Button(0, 0, 200, 50, pointer);
		this.startGame = new Button(0, 0, 200, 50, pointer);

	}

	private void render(int view) {

		Color.white.bind();

		switch (view) {
		case 1:
			GL11.glColor4d(0, 0.5, 0, 1.0);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 10);
			GL11.glVertex2d(cells[0][0].getX() - cells[0][0].getWidth() * size
					/ 2, cells[0][0].getY() + cells[0][0].getHeight() * size
					/ 2 - 10);

			GL11.glVertex2d(cells[0][0].getX() - cells[0][0].getWidth() * size
					/ 2, cells[0][0].getY()
					+ (cells[0][0].getHeight() * size / 2) - 300);
			GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 300);

			GL11.glEnd();

			GL11.glColor4d(0, 0.2, 0, 1.0);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 10);
			GL11.glVertex2d(cells[0][0].getX() + cells[0][0].getWidth() * size
					/ 2, cells[0][0].getY() + cells[0][0].getHeight() * size
					/ 2 - 10);

			GL11.glVertex2d(cells[0][0].getX() + cells[0][0].getWidth() * size
					/ 2, cells[0][0].getY()
					+ (cells[0][0].getHeight() * size / 2) - 300);
			GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 300);

			GL11.glEnd();

			double d = (System.currentTimeMillis() - this.timeStamp1)
					/ (30000 - 5000 * Math.min(counter, 5));

			GL11.glColor4d(d, 1 - d, 0.2, 1.0);
			GL11.glRectd(0 + xConvertion, 400 + yConvertion, this.WIDTH
					* (1 - d) + xConvertion, this.HIGHT + yConvertion);

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					cells[i][j].draw();
					cells[i][j].visited = false;
				}
			}

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			font.drawString((float) WIDTH / 2 - 50, (float) -250, (int) score
					+ " pts.", Color.gray);

			font.drawString((float) (WIDTH / 2 - 50 + xConvertion),
					(float) (410 + yConvertion), (String) ("level " + counter),
					Color.gray);
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			break;

		case 2:

			this.startGame.setX(WIDTH / 2 - 100);
			this.startGame.setY(450);
			this.startGame.draw();

			GL11.glColor4d(1, 1, 1, 1);

			texture.bind();
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
			GL11.glVertex3d(100, 150, 0.0);
			GL11.glTexCoord2f(0.65f, 0);
			GL11.glVertex3d(300, 150, 0.0);
			GL11.glTexCoord2f(0.65f, 0.7f);
			GL11.glVertex3d(300, 350, 0.0);
			GL11.glTexCoord2f(0, 0.7f);
			GL11.glVertex3d(100, 350, 0.0);
			GL11.glEnd();

			font2.drawString((float) (WIDTH / 2 - 80), (float) (50), "Controls");
			font.drawString(WIDTH / 2, 210, "- motion");
			font.drawString(WIDTH / 2, 310, "- remove block");
			font.drawString(WIDTH / 2 - 80, 460, "Start Game");
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			break;
		case 0:

			gameOverButton.setX(WIDTH / 2 - 100);
			gameOverButton.setY(400);
			gameOverButton.draw();

			this.startGame.setX(WIDTH / 2 - 100);
			this.startGame.setY(340);
			this.startGame.draw();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			font1.drawString((float) (90 + xConvertion), (float) (30),
					"IZOMETRIC\n    GAME");
			font.drawString(WIDTH / 2 - 80, 350, "Start Game");
			font.drawString(WIDTH / 2 - 40, 410, "Quit");
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			break;
		case 3:
			gameOverButton.setX(WIDTH / 2 - 100);
			gameOverButton.setY(450);
			gameOverButton.draw();

			this.startGame.setX(WIDTH / 2 - 100);
			this.startGame.setY(390);
			this.startGame.draw();

			GL11.glEnable(GL11.GL_TEXTURE_2D);
			font1.drawString((float) (90 + xConvertion), (float) (30),
					"GAME OVER");
			font2.drawString((float) (WIDTH / 2 - 130), (float) 250,
					(String) ("SCORE: " + score));
			font.drawString(WIDTH / 2 - 80, 400, "Start Game");
			font.drawString(WIDTH / 2 - 100, 460, "Back to menu");
			GL11.glDisable(GL11.GL_TEXTURE_2D);

			break;

		}

	}

	private void gameLogic() {

		Random r = new Random();

		if (!lose && System.currentTimeMillis() - timeStamp >= (difficulty)) {

			timeStamp = System.currentTimeMillis();

			Cell c = cells[r.nextInt(size)][r.nextInt(size)];

			if (c.addHeight()) {
				lose = true;
			} else {
				float f = r.nextFloat();
				if (c.type == 0) {
					if (f >= 0.88 && f < 0.91) {
						c.type = 1;
					} else if (f >= 0.91 && f < 0.95) {
						c.type = 2;
					} else if (f >= 0.95 && f < 1.0) {
						c.type = 3;
					}
				}
			}
		}
		if (System.currentTimeMillis() - timeStamp1 >= 30000 - 5000 * Math.min(
				counter, 5)) {
			timeStamp1 = System.currentTimeMillis();

			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					if (cells[i][j].addHeight()) {
						lose = true;
					}
					float f = r.nextFloat();
					if (cells[i][j].type == 0) {
						if (f >= 0.95 && f < 0.952) {
							cells[i][j].type = 1;
						} else if (f >= 0.952 && f < 0.975) {
							cells[i][j].type = 2;
						} else if (f >= 0.975 && f < 1.0) {
							cells[i][j].type = 3;
						}
					}
				}
			}

			counter++;

			if (difficulty > 500) {
				difficulty -= 100;
			}

		}

	}

	public static void main(String[] args) {
		new IzometricMain();

	}

}
