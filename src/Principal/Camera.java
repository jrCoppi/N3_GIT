package Principal;
/**
 * Classe de camera
 */
public class Camera {
	private int xMin;
	private int xMax;
	private int yMin;
	private int yMax;

	public Camera() {
		this.xMin = 0;
		this.xMax = 600;
		this.yMin = 600;
		this.yMax = 0;
	}

	public int getxMin() {
		return xMin;
	}

	public void setxMin(int xMin) {
		this.xMin = xMin;
	}

	public int getxMax() {
		return xMax;
	}

	public void setxMax(int xMax) {
		this.xMax = xMax;
	}

	public int getyMin() {
		return yMin;
	}

	public void setyMin(int yMin) {
		this.yMin = yMin;
	}

	public int getyMax() {
		return yMax;
	}

	public void setyMax(int yMax) {
		this.yMax = yMax;
	}
	
}
