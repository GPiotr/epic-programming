package pl.gieldon.epic.util;

import java.util.Random;

import org.eclipse.swt.graphics.Point;

/**
 * Particle Generator. After every keypress it generates 90 frames of Particle animation and store it in two dimensional array.
 * It creates frames in 3 phases.
 * 1st phase: Particles go fast on Y axis, and only a little bit on X axis. (Frames 1-30).
 * 2nd phase: Particles slow down on Y axis, and move more on X axis. (Frames 31-60).
 * 3rd phase: Particles almost no move on Y axis, and move a lot on X axis. (Frames 61-90).
 * In 1st phase the animation effect is done by Randomizing start and finish point coordinates, and trying to fulfill path between those points.
 * In 2nd phase start point is finish point of 1st phase, but we still randomizing finish point of 2nd phase.
 * In 3rd phase start point is finish point of 2nd phase, but we also need to randomize the finish point of 3rd phase.
 * 
 * 1st phase shortened for one particle:
 *  |  x-finish (index 29)  |  x  |  x |
 * 	|      		  			|     |  o |
 * 	|		      			| o   | o  |
 *  | x-start (index 0)	    | x   | x  |
 *   
 * 2nd phase shortened for one particle:
 *  |    x - finish (index 59)       |    x  |    x 
 *  |                                |  o    |  oo
 *  | x - start (index 30 = index 29)| x     | x   
 *  
 * 3rd phase shortened for one particle:   
 *  |       x - finish (index 89)    |     x |       x |       x |     o x |     oox |
 *  | x - start (index 60 = index 59)| xo    | xoo     | xooo    | xooo    | xooo    |
 *  
 * ---------------------------------------------------- 
 *  In sum up we got 90 frames firework-like animation:
 *                                    
 *              xx x         xx         ----- Phase 3 end
 *               x  x x     xx  x
 *                    x  xx             ----- Phase 2 end
 *                    x x
 *                     x                ----- Phase 1 end
 *  				  x	                
 *                    x
 * ----------------------------------------------------
 *
 * 
 * 
 * @author Piotr Gie³don
 *
 */
public class ParticleGenerator {
	
	public static synchronized Point[][] generateParticles(int prefParticleAmount) {
		
		Point[][] particles = new Point[prefParticleAmount][90];
		Random r = new Random();
		
		/*
		 * PHASE 1
		 */
		
		//Let's get random starting positions X=<0,2> Y = 0
		for (int i = 0; i < prefParticleAmount; i++) {
			particles[i][0] = new Point(r.nextInt(2), 0);
		}
		
		//Now it is time to randomize finish positions X = <0,5> Y = <20,40>
		for (int j = 0; j < prefParticleAmount; j++) {
			particles[j][29] = new Point(r.nextInt(5), 20 + r.nextInt(20));
		}

		//Fulfill paths between start and finish point
		for (int k = 0; k < prefParticleAmount; k++) {
			for (int l = 1; l < 29; l++) {
				particles[k][l] = new Point((int)(((double)particles[k][29].x / 29) * l), (int)(((double) particles[k][29].y / 29) * l));
			}
		}
		
		/*
		 * PHASE 2
		 */
		
		//Let's set finish point for Phase 2, X = X + X, Y = Y + Y/2
		for (int m = 0; m < prefParticleAmount; m++) {
			particles[m][59] = new Point(particles[m][29].x + particles[m][29].x, particles[m][29].y + particles[m][29].y/2);
		}
		
		//Fulfill paths between start and finish point
		for(int n = 0; n < prefParticleAmount; n++){
			for(int o = 30; o < 59; o++){
				particles[n][o] = new Point(particles[n][29].x + (int)(((double)(particles[n][59].x - particles[n][29].x)/ 29) * (o - 29)), particles[n][29].y + (int)(((double) (particles[n][59].y - particles[n][29].y) / 29) * (o - 29)));
			}
		}
		
		
		/*
		 * PHASE 3
		 */
		
		//Let's set finish point for Phase 3,  X = X + X/2, Y = Y + Y/4
		for (int m = 0; m < prefParticleAmount; m++) {
			particles[m][89] = new Point(particles[m][59].x + particles[m][59].x/2, particles[m][59].y + particles[m][59].y/4);
		}
		
		//Fulfill paths between start and finish point
		for(int n = 0; n < prefParticleAmount; n++){
			for(int o = 60; o< 89; o++){
				particles[n][o] = new Point(particles[n][59].x + (int)(((double)(particles[n][89].x - particles[n][59].x) / 29) * (o - 59)),particles[n][59].y +  (int)(((double) (particles[n][89].y - particles[n][59].y) / 29) * (o-59)));
			}
		}
		
		
		return particles;

	}
}
