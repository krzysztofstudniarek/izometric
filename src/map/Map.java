package map;

import org.lwjgl.opengl.GL11;

import Entities.AbstractEntity;
import Entities.Cell;

public class Map extends AbstractEntity {

	Cell[][] cells;
	int size;

	int X = 0, Y = 0;

	double xConvertion = 0, yConvertion = 0;

	public Map(double x, double y, int size, Cell[][] cells) {
		super(x, y, size * 10, size * 10);
		this.size = size;
		this.cells = cells;
	}

	@Override
	public void draw() {

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {

				if(cells[i][j].z != 0) {
					GL11.glColor4d(1 - 0.1 * cells[i][j].z, 1 - 0.1 * cells[i][j].z,
							1 - 0.1 * cells[i][j].z, 0.5);
					GL11.glRectd(x + i * 10 + xConvertion, y + j * 10
							+ yConvertion, x + (i + 1) * 10 + xConvertion, x
							+ (j + 1) * 10 + yConvertion);
				}
			}
		}

	}

	@Override
	public void update(int delta) {
		// TODO Auto-generated method stub

	}

	public void update(double xConvertion, double yConvertion, int X, int Y) {
		this.X = X;
		this.Y = Y;
		this.xConvertion = xConvertion;
		this.yConvertion = yConvertion;
	}

}
