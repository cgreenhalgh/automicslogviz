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

/**
 * @author cmg
 *
 */
public class UserData {
	private String trialid;
	private String trialuserid;
	private List<Position> positions = new LinkedList<Position>();
	private List<Event> events = new LinkedList<Event>();
	public UserData() {}
	/**
	 * @return the trialid
	 */
	public String getTrialid() {
		return trialid;
	}
	/**
	 * @param trialid the trialid to set
	 */
	public void setTrialid(String trialid) {
		this.trialid = trialid;
	}
	/**
	 * @return the trialuserid
	 */
	public String getTrialuserid() {
		return trialuserid;
	}
	/**
	 * @param trialuserid the trialuserid to set
	 */
	public void setTrialuserid(String trialuserid) {
		this.trialuserid = trialuserid;
	}
	/**
	 * @return the positions
	 */
	public List<Position> getPositions() {
		return positions;
	}
	/**
	 * @param positions the positions to set
	 */
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
	/**
	 * @return the events
	 */
	public List<Event> getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(List<Event> events) {
		this.events = events;
	}
	
}
