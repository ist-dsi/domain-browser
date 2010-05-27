package pt.ist.utl.birdy.client.controls.svg;

import gwt.g2d.client.math.MathHelper;

import java.io.Serializable;
import java.util.Arrays;

import com.google.gwt.dev.util.collect.HashMap;

public class Color implements Serializable {
    protected static final double DEFAULT_ALPHA = 1.0;

    private static final long serialVersionUID = 5370658935618812361L;
    private final int red, green, blue;
    private final boolean hasAlpha;
    private final double alpha;

    /**
     * Constructor.
     * 
     * @param colorCode
     *            the string representing this color.
     * @param red
     *            red channel [0-255]
     * @param green
     *            green channel [0-255]
     * @param blue
     *            blue channel [0-255]
     * @param alpha
     *            alpha channel [0.0, 1.0]
     */
    public Color(int red, int green, int blue, double alpha) {
	this.red = red;
	this.green = green;
	this.blue = blue;
	this.alpha = alpha;
	if (alpha == 1) {
	    hasAlpha = false;
	} else {
	    hasAlpha = true;
	}
    }

    /**
     * Construct a web-safe color. The color code is represented in the form
     * "#RRGGBB" which represents an RGB color using hexadecimal numbers.
     * 
     * @param red
     *            red channel [0-255]
     * @param green
     *            green channel [0-255]
     * @param blue
     *            blue channel [0-255]
     */
    public Color(int red, int green, int blue) {
	this(red, green, blue, DEFAULT_ALPHA);
    }

    public Color(String code) {

	red = Integer.parseInt("" + code.subSequence(1, 1 + 2), 16);
	green = Integer.parseInt("" + code.subSequence(1 + 2, 1 + 4), 16);
	blue = Integer.parseInt("" + code.subSequence(1 + 4, 1 + 6), 16);
	alpha = DEFAULT_ALPHA;
	hasAlpha = false;

    }

    /**
     * Linear interpolation between two colors.
     * 
     * @param value1
     * @param value2
     * @param amount
     *            the amount to interpolate [0.0, 1.0].
     * @return a new interpolated color.
     */
    public static final Color lerp(Color value1, Color value2, double amount) {
	return new Color((int) MathHelper.lerp(value1.getR(), value2.getR(), amount), (int) MathHelper.lerp(value1.getG(), value2
		.getG(), amount), (int) MathHelper.lerp(value1.getB(), value2.getB(), amount), MathHelper.lerp(value1.getAlpha(),
		value2.getAlpha(), amount));
    }

    /**
     * Smooth interpolation between two colors.
     * 
     * @param value1
     * @param value2
     * @param amount
     *            the amount to interpolate [0.0, 1.0].
     * @return a new interpolated color.
     */
    public static final Color smoothStep(Color value1, Color value2, double amount) {
	return new Color((int) MathHelper.smoothStep(value1.getR(), value2.getR(), amount), (int) MathHelper.smoothStep(value1
		.getG(), value2.getG(), amount), (int) MathHelper.smoothStep(value1.getB(), value2.getB(), amount), MathHelper
		.smoothStep(value1.getAlpha(), value2.getAlpha(), amount));
    }

    /**
     * Gets the string representation of the color.
     */
    public final String getColorCode() {
	if (hasAlpha) {
	    return new StringBuilder(21).append("rgba(").append(red).append(',').append(green).append(',').append(blue).append(
		    ',').append(alpha).append(')').toString();
	} else {
	    StringBuilder stringBuilder = new StringBuilder("#000000");
	    String hexString = Integer.toHexString(getHexValue(red, green, blue));
	    return stringBuilder.replace(stringBuilder.length() - hexString.length(), stringBuilder.length(), hexString)
		    .toString();
	}
    }

    /**
     * Gets the value of the red channel.
     * 
     * @return a value between 0 to 255, inclusive.
     */
    public final int getR() {
	return red;
    }

    /**
     * Gets the value of the green channel.
     * 
     * @return a value between 0 to 255, inclusive.
     */
    public final int getG() {
	return green;
    }

    /**
     * Gets the value of the blue channel.
     * 
     * @return a value between 0 to 255, inclusive.
     */
    public final int getB() {
	return blue;
    }

    /**
     * Gets the value of the alpha channel.
     * 
     * @return a value between 0.0 to 1.0, inclusive.
     */
    public final double getAlpha() {
	return alpha;
    }

    @Override
    public final String toString() {
	return getColorCode();
    }

    @Override
    public final boolean equals(Object obj) {
	return (obj instanceof Color) ? equals((Color) obj) : false;
    }

    public final boolean equals(Color rhs) {
	return getR() == rhs.getR() && getG() == rhs.getG() && getB() == rhs.getB() && getAlpha() == rhs.getAlpha();
    }

    @Override
    public final int hashCode() {
	return Arrays.hashCode(new double[] { getHexValue(getR(), getG(), getB()), getAlpha() });
    }

    /**
     * Gets the integer value of the given rgb value.
     */
    private final int getHexValue(int r, int g, int b) {
	return ((r << 16) & 0xFF0000) | ((g << 8) & 0xFF00) | (b & 0xFF);
    }
}
