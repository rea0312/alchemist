package com.htjf.common.lutil.codeutil;

import java.util.Random;

public class RandomString {

	/**
	 * <p>Random object used by random method. This has to be not local
	 * to the random method so as to not return the same value in the 
	 * same millisecond.</p>
	 */
	private static final Random RANDOM = new Random();

	/**
	 * <p>Creates a random string based on a variety of options, using
	 * supplied source of randomness.</p>
	 *
	 * <p>If start and end are both <code>0</code>, start and end are set
	 * to <code>' '</code> and <code>'z'</code>, the ASCII printable
	 * characters, will be used, unless letters and numbers are both
	 * <code>false</code>, in which case, start and end are set to
	 * <code>0</code> and <code>Integer.MAX_VALUE</code>.
	 *
	 * <p>If set is not <code>null</code>, characters between start and
	 * end are chosen.</p>
	 *
	 * <p>This method accepts a user-supplied {@link Random}
	 * instance to use as a source of randomness. By seeding a single 
	 * {@link Random} instance with a fixed seed and using it for each call,
	 * the same random sequence of strings can be generated repeatedly
	 * and predictably.</p>
	 *
	 * @param count  the length of random string to create
	 * @param start  the position in set of chars to start at
	 * @param end  the position in set of chars to end before
	 * @param letters  only allow letters?
	 * @param numbers  only allow numbers?
	 * @param chars  the set of chars to choose randoms from.
	 *  If <code>null</code>, then it will use the set of all chars.
	 * @param random  a source of randomness.
	 * @return the random string
	 * @throws ArrayIndexOutOfBoundsException if there are not
	 *  <code>(end - start) + 1</code> characters in the set array.
	 * @throws IllegalArgumentException if <code>count</code> &lt; 0.
	 * @since 2.0
	 */
	public static String random(int count, int start, int end, boolean letters, boolean numbers,
			char[] chars, Random random) {
		if (count == 0) {
			return "";
		} else if (count < 0) {
			throw new IllegalArgumentException("Requested random string length " + count
					+ " is less than 0.");
		}
		if ((start == 0) && (end == 0)) {
			end = 'z' + 1;
			start = ' ';
			if (!letters && !numbers) {
				start = 0;
				end = Integer.MAX_VALUE;
			}
		}
	
		StringBuffer buffer = new StringBuffer();
		int gap = end - start;
	
		while (count-- != 0) {
			char ch;
			if (chars == null) {
				ch = (char) (random.nextInt(gap) + start);
			} else {
				ch = chars[random.nextInt(gap) + start];
			}
			if ((letters && numbers && Character.isLetterOrDigit(ch))
					|| (letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch))
					|| (!letters && !numbers)) {
				buffer.append(ch);
			} else {
				count++;
			}
		}
		return buffer.toString();
	}

	public static String random(int count) 
	{
		return random(count, 0, 0, false, true, null, RANDOM);
	}

}
