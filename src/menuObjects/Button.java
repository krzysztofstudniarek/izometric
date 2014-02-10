package menuObjects;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import Entities.AbstractEntity;

public class Button extends AbstractEntity {

	Pointer pointer;

	public Button(double x, double y, double height, double width,
			Pointer pointer) {
		super(x, y, height, width);

		this.pointer = pointer;
	}

	@Override
	public void draw() {
		if (!this.intersects(pointer)) {
			GL11.glColor4d(1, 1, 1, 1);
			GL11.glRectd(this.x - 10, this.y, this.x + width, this.y + height);
		} else {
			GL11.glColor4d(0.5, 0.5, 0.5, 1);
			GL11.glRectd(this.x - 10, this.y, this.x + width, this.y + height);
		}
	}

	@Override
	public void update(int delta) {

	}
	
	public boolean isOn(){
		if(this.intersects(pointer)){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean clicked(){
		if(this.intersects(pointer) && Mouse.isButtonDown(0)){
			return true;
		}else{
			return false;
		}
	}

}
