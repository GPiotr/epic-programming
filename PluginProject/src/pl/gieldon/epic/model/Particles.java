/**
 * This class holds Frame object, and also all particle locations for all frames
 * It keeps also the font color.
 */
package pl.gieldon.epic.model;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;

/**
 * @author Piotr Gie³don
 *
 */
public class Particles {

	private Frame frame;
	private Point[][] topParticles;
	private Point[][] bottomParticles;
	private Color myColor;
	
	/**
	 * @return the frame
	 */
	public Frame getFrame() {
		return frame;
	}
	/**
	 * @param frame the frame to set
	 */
	public void setFrame(Frame frame) {
		this.frame = frame;
	}
	/**
	 * @return the topParticles
	 */
	public Point[][] getTopParticles() {
		return topParticles;
	}
	/**
	 * @param topParticles the topParticles to set
	 */
	public void setTopParticles(Point[][] topParticles) {
		this.topParticles = topParticles;
	}
	/**
	 * @return the bottomParticles
	 */
	public Point[][] getBottomParticles() {
		return bottomParticles;
	}
	/**
	 * @param bottomParticles the bottomParticles to set
	 */
	public void setBottomParticles(Point[][] bottomParticles) {
		this.bottomParticles = bottomParticles;
	}
	/**
	 * @return the myColor
	 */
	public Color getMyColor() {
		return myColor;
	}
	/**
	 * @param myColor the myColor to set
	 */
	public void setMyColor(Color myColor) {
		this.myColor = myColor;
	}

	
	
	
	
}
