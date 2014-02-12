package menuObjects;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;

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
		//GL11.glEnable(GL11.GL_TEXTURE_2D);
		if (!this.intersects(pointer)) {
			GL11.glColor3d(1, 1, 1);
			GL11.glRectd(this.x - 10, this.y, this.x + width, this.y + height);
		} else {
			GL11.glColor3d(0.5, 0.5, 0.5);
			GL11.glRectd(this.x - 10, this.y, this.x + width, this.y + height);
		}
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
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
