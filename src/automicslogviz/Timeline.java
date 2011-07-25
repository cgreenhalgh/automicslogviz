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

import java.util.LinkedList;
import java.util.List;

/** a line along which time changes :-)
 * @author cmg
 *
 */
public class Timeline {
	private List<TimelineSegment> segments = new LinkedList<TimelineSegment>();
	public Timeline() {}
	/**
	 * @param segments
	 */
	public Timeline(List<TimelineSegment> segments) {
		super();
		this.segments = segments;
	}
	/**
	 * @return the segments
	 */
	public List<TimelineSegment> getSegments() {
		return segments;
	}
	/**
	 * @param segments the segments to set
	 */
	public void setSegments(List<TimelineSegment> segments) {
		this.segments = segments;
	}
	
}
