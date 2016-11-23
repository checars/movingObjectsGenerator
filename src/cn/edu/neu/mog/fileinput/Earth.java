package cn.edu.neu.mog.fileinput;

/**
 * Some information about our blue planet.
 * 
 * @author CheQingShou
 */
public class Earth {

	/**
	 * Polar radius in m.<br>
	 * http://en.wikipedia.org/wiki/Earth
	 */
	public static final double POLAR_RADIUS = 6356.8 * 1000;

	/**
	 * Equatorial radius in m.<br>
	 * http://en.wikipedia.org/wiki/Earth
	 */
	public static final double EQUATORIAL_RADIUS = 6378.1 * 1000;

	/**
	 * Polar circumference in m.
	 */
	public static final double POLAR_CIRCUMFERENCE = POLAR_RADIUS * 2.0 * Math.PI;

	public static final double EQUATORIAL_CIRCUMFERENCE = EQUATORIAL_RADIUS * 2.0 * Math.PI;

	public static void main(String[] args) {
		System.out.println("Polar radius: " + POLAR_RADIUS);
		System.out.println("Equatorial radius: " + EQUATORIAL_RADIUS);
		System.out.println("Polar circumference: " + POLAR_CIRCUMFERENCE);
		System.out.println("Equatorial circumference: " + EQUATORIAL_CIRCUMFERENCE);
	}
}
