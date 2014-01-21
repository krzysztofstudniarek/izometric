package map;

import org.lwjgl.opengl.GL11;

import Entities.AbstractEntity;
import Entities.Cell;

public class HorizontalMap extends AbstractEntity {

	Cell[][] cells;
	int size;

	double xConvertion = 0, yConvertion = 0;

	int view;

	public HorizontalMap(double x, double y, int size, Cell[][] cells, int view) {
		super(x, y, size * 10, size * 10);
		this.size = size;
		this.cells = cells;
		this.view = view;
	}

	@Override
	public void draw() {
		switch (view) {
		case 0:
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					GL11.glColor4d(0.05 * i, 0.05 * i, 0.05 * i, 1.0);

					GL11.glRectd(x + j * 10 + xConvertion, y + 100
							+ yConvertion, x + (j + 1) * 10 + xConvertion, y
							+ 100 - cells[i][j].z * 10 + yConvertion);

				}
			}
			break;
		case 1:
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					GL11.glColor4d(0.05 * j, 0.05 * j, 0.05 * j, 1.0);

					GL11.glRectd(x + i * 10 + xConvertion, y + 100
							+ yConvertion, x + (i + 1) * 10 + xConvertion, y
							+ 100 - cells[i][j].z * 10 + yConvertion);

				}
			}
			break;
		}

	}

	@Override
	public void update(int delta) {

	}

	public void update(double xConvertion, double yConvertion) {
		this.xConvertion = xConvertion;
		this.yConvertion = yConvertion;
	}

}
