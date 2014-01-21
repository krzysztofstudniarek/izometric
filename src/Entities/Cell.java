package Entities;

import org.lwjgl.opengl.GL11;

public class Cell extends AbstractEntity {

	public int type = 0;

	public int z = 0;

	public boolean visited = false;

	public Cell(double x, double y, double width, double height) {
		super(x, y, width, height);
	}

	@Override
	public void draw() {

		if (visited) {

			GL11.glColor4d(0.8, 0.8, 0.8, 0.6);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x - (width / 2), y);
			GL11.glVertex2d(x, y + (height / 2));
			GL11.glColor4d(0.8, 0.8, 0.8, 0.0);
			GL11.glVertex2d(x, y + (height / 2) - 400);
			GL11.glVertex2d(x - (width / 2), y - 400);
			GL11.glEnd();

			GL11.glColor4d(1.0, 1.0, 1.0, 0.6);

			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x + (width / 2), y);
			GL11.glVertex2d(x, y + (height / 2));
			GL11.glColor4d(1.0, 1.0, 1.0, 0.0);
			GL11.glVertex2d(x, y + (height / 2) - 400);
			GL11.glVertex2d(x + (width / 2), y - 400);
			GL11.glEnd();

			GL11.glColor4d(1.0, 1.0, 1.0, 1.0);

		} else if (z > 0) {
			switch (type) {
			case 0:
				GL11.glColor4d(0.9, 0.9, 0.9, 1.0);
				break;
			case 1:

				GL11.glColor4d(0.5, 1.0, 0.5, 1.0);
				break;
			case 2:

				GL11.glColor4d(1.0, 0.5, 0.5, 1.0);
				break;
			case 3:

				GL11.glColor4d(0.5, 0.5, 1.0, 1.0);
				break;
			}
		} else if (z <= 0) {
			GL11.glColor4d(0.2, 0.8, 0.2, 1.0);
		}

		// top
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x - (width / 2), y - z * 25);
		GL11.glVertex2d(x, y + (height / 2) - z * 25);
		GL11.glVertex2d(x + (width / 2), y - z * 25);
		GL11.glVertex2d(x, y - (height / 2) - z * 25);
		GL11.glEnd();

		if (visited) {
			GL11.glColor3d(0.8, 0.8, 0.8);
		} else {

			switch (type) {
			case 0:
				GL11.glColor3d(0.5, 0.5, 0.5);
				break;
			case 1:
				GL11.glColor4d(0.3, 0.5, 0.3, 1.0);
				break;
			case 2:
				GL11.glColor4d(0.5, 0.3, 0.3, 1.0);
				break;
			case 3:
				GL11.glColor4d(0.3, 0.3, 0.5, 1.0);
				break;
			}

		}

		// left
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x - (width / 2), y);
		GL11.glVertex2d(x - (width / 2), y - z * 25);
		GL11.glVertex2d(x, y + (height / 2) - z * 25);
		GL11.glVertex2d(x, y + (height / 2));
		GL11.glEnd();

		if (visited) {
			GL11.glColor3d(0.9, 0.9, 0.9);
		} else {

			switch (type) {
			case 0:
				GL11.glColor3d(0.7, 0.7, 0.7);
				break;
			case 1:
				GL11.glColor4d(0.5, 0.7, 0.5, 1.0);
				break;
			case 2:
				GL11.glColor4d(0.7, 0.5, 0.5, 1.0);
				break;
			case 3:
				GL11.glColor4d(0.5, 0.5, 0.7, 1.0);
				break;
			}
		}

		// right
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glVertex2d(x + (width / 2), y);
		GL11.glVertex2d(x + (width / 2), y - z * 25);
		GL11.glVertex2d(x, y + (height / 2) - z * 25);
		GL11.glVertex2d(x, y + (height / 2));
		GL11.glEnd();
	}

	public boolean addHeight() {
		if (z <= 10) {
			z++;
			return false;
		} else {
			return true;
		}

	}

	public boolean removeHeight() {
		if (z > 0) {
			z--;
			return true;
		}

		return false;

	}

	@Override
	public void update(int delta) {

	}

}
