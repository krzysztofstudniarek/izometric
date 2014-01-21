package main;

import java.util.Random;

import map.HorizontalMap;
import map.Map;

import org.lwjgl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.*;
import org.newdawn.slick.Color;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import Entities.Cell;

import static org.lwjgl.opengl.GL11.*;

public class IzometricMain {

	public static final int WIDTH = 800;
	public static final int HIGHT = 600;
	private boolean isRunning = true;
//	private long lastFrame = getTime();

	int size = 5;

	int X = size / 2;
	int Y = size / 2;

	int view = 0;

	double score = 0;
	double difficulty = 1625;
	int counter = 1;

	private double timeStamp = System.currentTimeMillis();
	private double timeStamp1 = System.currentTimeMillis();

	double translate_x = 0, translate_y = 0, xConvertion = 0,
			yConvertion = 150;

	private Cell[][] cells = new Cell[size][size];

	private Map map;

	private HorizontalMap xMap, yMap;

	private boolean lose = false;

	private static UnicodeFont font;
	private static UnicodeFont font1;

/*	private long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}*/

/*	private int getDelta() {
		long currentTime = getTime();
		int delta = (int) (currentTime - lastFrame);
		lastFrame = getTime();
		return delta;
	}*/

	public IzometricMain() {

		SetUpDisplay();
		SetUpOpenGL();
		SetUpEntities();
//		SetUpTimer();

		while (isRunning) {

			GL11.glClear(GL_COLOR_BUFFER_BIT);
			
			cells[X][Y].visited = true;

			GL11.glClear(GL_COLOR_BUFFER_BIT);

//			int delta = getDelta();

			this.render(this.view);

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

			map.update(xConvertion, yConvertion, X, Y);
			xMap.update(xConvertion, yConvertion);
			yMap.update(xConvertion, yConvertion);

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
		
		
		
		
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING);  
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HIGHT, 0, -1, 1);
	}

	private void SetUpEntities() {

		int startX = WIDTH / 2;
		int startY = 100;

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j] = new Cell(startX + j * 20, startY + j * 10, 40, 20);
			}

			startX -= 20;
			startY += 10;
		}

		map = new Map(10, 10, size, cells);
		xMap = new HorizontalMap(10, 400, size, cells, 1);
		yMap = new HorizontalMap(WIDTH - (size + 1) * 10, 400, size, cells, 0);

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
		font1.getEffects().add(new ColorEffect(java.awt.Color.RED));
		font1.addAsciiGlyphs();
		try {
			font1.loadGlyphs();
		} catch (SlickException e) {
			e.printStackTrace();
		}

	}

/*	public void SetUpTimer() {
		lastFrame = getTime();
	}*/

	private void render(int view) {

		// font.drawString((float)(WIDTH-150+xConvertion),
		// (float)(30+yConvertion), "score: "+(int)score, Color.black);

		GL11.glColor4d(0, 0.5, 0, 1.0);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 10);
		GL11.glVertex2d(cells[0][0].getX() - cells[0][0].getWidth() * size / 2,
				cells[0][0].getY() + cells[0][0].getHeight() * size / 2 - 10);

		GL11.glVertex2d(cells[0][0].getX() - cells[0][0].getWidth() * size / 2,
				cells[0][0].getY() + (cells[0][0].getHeight() * size / 2) - 300);
		GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 300);

		GL11.glEnd();

		GL11.glColor4d(0, 0.2, 0, 1.0);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 10);
		GL11.glVertex2d(cells[0][0].getX() + cells[0][0].getWidth() * size / 2,
				cells[0][0].getY() + cells[0][0].getHeight() * size / 2 - 10);

		GL11.glVertex2d(cells[0][0].getX() + cells[0][0].getWidth() * size / 2,
				cells[0][0].getY() + (cells[0][0].getHeight() * size / 2) - 300);
		GL11.glVertex2d(cells[0][0].getX(), cells[0][0].getY() - 300);

		GL11.glEnd();

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				cells[i][j].draw();
				cells[i][j].visited = false;
			}
		}
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);

		if (lose) {
			font1.drawString((float)(50+xConvertion), (float)(yConvertion+50), "GAME OVER", new Color(1, 0, 0, 0.7f));
		}

		
		font.drawString((float)WIDTH/2-50, (float)-250, (int)score+" pts.", Color.gray);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		// map.draw();
		// xMap.draw();
		// yMap.draw();

	}

	private void gameLogic() {

		if (!lose && System.currentTimeMillis() - timeStamp >= (difficulty)) {

			timeStamp = System.currentTimeMillis();

			Random r = new Random();

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

			if (System.currentTimeMillis() - timeStamp1 >= 60000 - 5000 * counter) {
				timeStamp1 = System.currentTimeMillis();
				
				
				for (int i = 0; i < size; i++) {
					for (int j = 0; j < size; j++) {
						if(counter <= 5){
							cells[i][j].z += counter;
						}else{
							cells[i][j].z += 5;
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
				
				if (counter * 5000 <= 55000)
					counter++;
				

				if (difficulty > 100) {
					difficulty -= 100;
				}

				
			}
		}

	}

	public static void main(String[] args) {
		new IzometricMain();

	}

}
