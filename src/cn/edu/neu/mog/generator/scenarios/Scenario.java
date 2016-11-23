package cn.edu.neu.mog.generator.scenarios;

/**
 * A scenario translates from a physical area to a logical area.
 * 
 * @author Lukas Blunschi
 */
public class Scenario {

	public final String name;

	/**
	 * Physical maximum speed [m/s].
	 */
	public final double speedMaxMs;

	/**
	 * Factor to translate from m to logm.
	 */
	public final double ratio;

	/**
	 * Logical data space width (including the border).
	 */
	public final int widthLog;

	/**
	 * Logical data space with (excluding the border).
	 */
	public final int widthLogNoBorder;

	/**
	 * Speed in logical meters per second [logm/s].
	 */
	public final double speedMaxLog;

	/**
	 * Logical border width;
	 */
	public final int borderWidthLog;

	/**
	 * Description of this scenario.
	 */
	public final String desc;

	/**
	 * Instantiate.
	 * 
	 * @param name
	 * @param widthKm
	 * @param heightKm
	 * @param speedMaxMs
	 * @param widthLog
	 */
	public Scenario(String name, int widthKm, int heightKm, double speedMaxMs, int widthLog) {

		// name
		this.name = name;

		// physical parameters
		this.speedMaxMs = speedMaxMs;

		// physical border width
		// the maximum speed is given in m/s. Since the generator produces a
		// trace based on timestamps, we need to assume a maximum timestamp.
		// the horizon is time an object can be predicted forward.
		final double maxTimestampInS = 120.0;
		final double horizon = 3.0 * maxTimestampInS;
		final double borderWidthM = speedMaxMs * horizon;

		// physical to logical parameters
		double maxDimM = borderWidthM + (widthKm < heightKm ? heightKm * 1000.0 : widthKm * 1000.0);
		this.ratio = (double) widthLog / maxDimM;

		// logical parameters
		this.widthLog = widthLog;
		this.speedMaxLog = ratio * speedMaxMs;
		this.borderWidthLog = (int) (ratio * borderWidthM);
		this.widthLogNoBorder = widthLog - 2 * borderWidthLog;

		// description
		StringBuffer buf = new StringBuffer();
		buf.append(name).append("\n");
		buf.append("Physical: " + widthKm + " km x " + heightKm + " km @ " + speedMaxMs + " m/s.\n");
		buf.append("Logical: " + widthLog + " logm x " + widthLog + " logm @ " + speedMaxLog + " logm/s.\n");
		buf.append("Border width: " + borderWidthM + " m, " + borderWidthLog + " logm.\n");
		buf.append("Usable logical space side length: " + widthLogNoBorder + " logm.");
		desc = buf.toString();
	}

	public String toString() {
		return desc;
	}

}
