/**
 * Copyright 2010 The University of Nottingham
 * 
 * This file is part of automicslogviz.
 *
 *  automicslogviz is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  automicslogviz is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with automicslogviz.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */
package automicslogviz;

/**
 * @author cmg
 *
 */
public class Utils {
	public static float getHourOfDay(long time) {
		return (float)((time % MS_PER_DAY)*1.0f/MS_PER_HOUR);
	}
	public static final long MS_PER_DAY = 1000*60*60*24;
	public static final long MS_PER_HOUR = 1000*60*60;
	public static int getDay(long time) {
		return (int)(time / MS_PER_DAY);
	}
}
