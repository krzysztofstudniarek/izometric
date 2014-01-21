package Entities;

import java.awt.Rectangle;



public abstract class AbstractEntity implements Entity {

	protected double x, y, height, width;
	protected Rectangle hitbox = new Rectangle();
	
	public AbstractEntity(double x, double y, double width, double height){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	

	@Override
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;

	}

	@Override
	public void setX(double x) {
		this.x = x;

	}

	@Override
	public void setY(double y) {
		this.y = y;

	}

	@Override
	public void setWidth(double width) {
		this.width = width;

	}

	@Override
	public void setHeight(double height) {
		this.height = height;

	}

	@Override
	public double getX() {
		return this.x;
	}

	@Override
	public double getY() {
		return this.y;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public double getHeight() {
		return this.height;
	}
	
	public Rectangle getHitbox() {
		return this.hitbox;
	}

	@Override
	public boolean intersects(Entity other) {
		
		hitbox.setBounds((int) this.x, (int) this.y , (int) this.width, (int) this.height);
		
		return hitbox.intersects(other.getX(), other.getY(), other.getWidth(), other.getHeight());
	}


}
