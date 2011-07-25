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

/** part of a timeline
 * @author cmg
 *
 */
public class TimelineSegment {
	private TimelinePoint start;
	private TimelinePoint end;
	private String type;
	public TimelineSegment() {}
	/**
	 * @param start
	 * @param end
	 * @param type
	 */
	public TimelineSegment(TimelinePoint start, TimelinePoint end, String type) {
		super();
		this.start = start;
		this.end = end;
		this.type = type;
	}
	/**
	 * @return the start
	 */
	public TimelinePoint getStart() {
		return start;
	}
	/**
	 * @param start the start to set
	 */
	public void setStart(TimelinePoint start) {
		this.start = start;
	}
	/**
	 * @return the end
	 */
	public TimelinePoint getEnd() {
		return end;
	}
	/**
	 * @param end the end to set
	 */
	public void setEnd(TimelinePoint end) {
		this.end = end;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	public double getX(double p) {
		if (p<0)
			return start.getX();
		//if (p>1)
		//	return end.getX();
		return start.getX()+p*(end.getX()-start.getX());
	}
	public double getY(double p) {
		if (p<0)
			return start.getY();
		//if (p>1)
		//return end.getY();
		return start.getY()+p*(end.getY()-start.getY());
	}
	public double getLength() {
		return Math.sqrt((end.getX()-start.getX())*(end.getX()-start.getX())+(end.getY()-start.getY())*(end.getY()-start.getY()));
	}
}