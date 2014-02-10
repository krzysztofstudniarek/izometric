package menuObjects;

import org.lwjgl.input.Mouse;

import Entities.AbstractEntity;

public class Pointer extends AbstractEntity{

	public Pointer(double x, double y) {
		super(x, y, 1, 1);
		
	}

	@Override
	public void draw() {
		
	}

	@Override
	public void update(int delta) {
		this.x = Mouse.getX();
		this.y = 600-Mouse.getY();
	}

}
