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

/** GPS report
 * @author cmg
 *
 */
public class Position {
	private long time;
	private double lat;
	private double lon;
	private boolean truncated;
	private Double x;
	private Double y;
	public Position() {}
	/**
	 * @param time
	 * @param lat
	 * @param lon
	 * @param truncated
	 */
	public Position(long time, double lat, double lon, boolean truncated) {
		super();
		this.time = time;
		this.lat = lat;
		this.lon = lon;
		this.truncated = truncated;
	}
	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}
	public float getHourOfDay() {
		return Utils.getHourOfDay(time);
	}
	/**
	 * @param time the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}
	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public double getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(double lon) {
		this.lon = lon;
	}
	/**
	 * @return the truncated
	 */
	public boolean isTruncated() {
		return truncated;
	}
	/**
	 * @param truncated the truncated to set
	 */
	public void setTruncated(boolean truncated) {
		this.truncated = truncated;
	}
	public synchronized double getX() {
		if (x==null)
			x = Mercator.mercX(lon);
		return x;
	}
	public synchronized double getY() {
		if (y==null)
			y = Mercator.mercY(lat);
		return y;
	}
}
