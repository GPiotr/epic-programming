/**
 *  This class holds two fields for particles.
 *  FrameNumber field contains frame number which should be animated when comes to redraw.
 *  Point is a coordinates of Caret at the moment when key was pressed.
 */
package pl.gieldon.epic.model;

import org.eclipse.swt.graphics.Point;

/**
 * @author Piotr Gie³don
 *
 */
public class Frame {

	private int frameNumber;
	private Point point;
	
	/**
	 * @return the frameNumber
	 */
	public int getFrameNumber() {
		return frameNumber;
	}
	/**
	 * @param frameNumber the frameNumber to set
	 */
	public void setFrameNumber(int frameNumber) {
		this.frameNumber = frameNumber;
	}
	/**
	 * @return the point
	 */
	public Point getPoint() {
		return point;
	}
	/**
	 * @param point the point to set
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	
	
}
