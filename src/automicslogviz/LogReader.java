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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;

/**
 * @author cmg
 *
 */
public class LogReader {
	static Logger logger = Logger.getLogger(LogReader.class.getName());
	public static Map<Integer,Zone> readZones(File file) {
		Map<Integer,Zone> zones = new HashMap<Integer,Zone>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			br.readLine();// header
			while (true) {
				String line = br.readLine();
				if (line==null)
					break;
				String values[] = line.split(",");
				Zone z= new Zone();
				//tagId	type	radius	comment	lat	lng	time	timestamp 
				int i=0;
				z.setTagId(Integer.parseInt(values[i++]));
				z.setType(values[i++]);
				z.setRadius(Float.parseFloat(values[i++]));
				z.setComment(values[i++]);
				z.setLat(Double.parseDouble(values[i++]));
				z.setLon(Double.parseDouble(values[i++]));
				zones.put(z.getTagId(), z);
			}
			br.close();
		}
		catch (Exception e) {
			logger.log(Level.SEVERE, "Error reading zone file "+file, e);
		}
		return zones;
	}
	public static List<UserData> readLogs(File dir) {
		// trials should be directories
		logger.log(Level.INFO, "read logs from "+dir);
		List<UserData> users = new LinkedList<UserData>();
		File tfs[] = dir.listFiles();
		for (int ti=0; ti<tfs.length; ti++) {
			File trialdir = tfs[ti];
			if (!trialdir.isDirectory())
				continue;
			String trialid = trialdir.getName();
			logger.log(Level.INFO, "process trial "+trialid);
			File pfs[] = trialdir.listFiles();
			for (int pi=0; pi<pfs.length; pi++) {
				File userdir = pfs[pi];
				if (!userdir.isDirectory())
					continue;
				String trialuserid = userdir.getName();
				File datadir = new File(userdir, "UserData");
				if (!datadir.exists() || !datadir.isDirectory()) {
					logger.log(Level.WARNING, "Apparent user "+userdir+" has no UserData directory");
					continue;
				}
				UserData user = new UserData();
				user.setTrialid(trialid);
				user.setTrialuserid(trialuserid);
				users.add(user);
				File behaviourfile = new File(datadir, "BehaviouralData.csv");
				File kmlfile = new File(datadir, "GpsTrace.kml");
				if (behaviourfile.exists()) {
					user.setEvents(readEvents(behaviourfile));
				}
				else
					logger.log(Level.WARNING, "No behaviour data file found for "+datadir);
				if (kmlfile.exists()) {
					user.setPositions(readPositions(kmlfile));
				}
				else
					logger.log(Level.WARNING, "No kml file found for "+datadir);
				logger.log(Level.INFO, "For user "+user.getTrialuserid()+" found "+user.getEvents().size()+" events and "+user.getPositions().size()+" positions ("+countTruncatedPositions(user.getPositions())+" truncated)");
			}
		}
		return users;
	}
	/**
	 * @param positions
	 * @return
	 */
	private static int countTruncatedPositions(List<Position> positions) {
		int c = 0;
		for (Position p : positions)
			if (p.isTruncated())
				c++;
		return c;
	}
	private static List<Position> readPositions(File kmlfile) {
		LinkedList<Position> positions = new LinkedList<Position>();
		try {
			SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			BufferedReader br = new BufferedReader(new FileReader(kmlfile));
			Position p = null;
			while (true) {
				String line = br.readLine();
				if (line==null)
					break;
				if (line.startsWith("<Placemark>")) {
					p = new Position();
				}
				else if (line.startsWith("<TimeStamp><when>")) {
					try {
						int ix = line.indexOf("<", "<TimeStamp><when>".length());
						String datetime = line.substring("<TimeStamp><when>".length(), ix);
						Date date = dateformat.parse(datetime);
						p.setTime(date.getTime());
						//logger.log(Level.INFO,"Time "+datetime+" -> "+date+": "+p.getTime()+" ("+Utils.getHourOfDay(p.getTime())+")");
						if (p.getLat()!=0) {
							positions.add(p);
							p = null;
						}
					}
					catch (Exception e ){
						logger.log(Level.WARNING, "Error parsing date line "+line, e);
					}
				}
				else if (line.startsWith("<Point><coordinates>")) {
					try {
						int ix = line.indexOf("<", "<Point><coordinates>".length());
						String coords = line.substring("<Point><coordinates>".length(), ix);
						String els[] = coords.split(",");
						if (coords.contains("E"))
							p.setTruncated(true);
						p.setLon(Double.parseDouble(els[0]));
						p.setLat(Double.parseDouble(els[1]));
						if (p.getTime()!=0) {
							positions.add(p);
							p = null;
						}
					}
					catch (Exception e ) {
						logger.log(Level.WARNING, "Error parsing position line "+line, e);
					}
				}
			}
			br.close();
		}
		catch (Exception e) {
			logger.log(Level.WARNING, "Error reading "+kmlfile,e);
		}
		return positions;
	}
	private static List<Event> readEvents(File behaviourfile) {
		LinkedList<Event> events = new LinkedList<Event>();
		try {
			//charset?
			BufferedReader br = new BufferedReader(new FileReader(behaviourfile));
			String headers = br.readLine();
			while(true) {
				String line = br.readLine();
				//logger.log(Level.INFO,"event line: "+line);
				if (line==null)
					break;
				Event e = new Event();
				String values[] = line.split(",");
//	//taskType,taskId,init,gpsTagId,notificationType,notificationTs,reminderCount,notficationAcceptTs,
				int i=0; 
				e.setTaskType(values[i++]);
				e.setTaskId(Integer.parseInt(values[i++]));
				e.setInit(values[i++]);
				e.setGpsTagId(Integer.parseInt(values[i++]));
				e.setNotificationType(values[i++]);
				e.setNotificationTs(i>=values.length || values[i++].length()==0 ? null : Long.parseLong(values[i-1]));
				e.setReminderCount(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				e.setNotficationAcceptTs(i>=values.length || values[i++].length()==0 ? null : Long.parseLong(values[i-1]));
//				acceptanceTime,taskAcceptTs,taskAcceptTime,taskComp,taskCompTs,taskCompTime,
				e.setAcceptanceTime(i>=values.length || values[i]==null || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				e.setTaskAcceptTs(i>=values.length || values[i++].length()==0 ? null : Long.parseLong(values[i-1]));
				e.setTaskAcceptTime(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				e.setTaskComp(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				e.setTaskCompTs(i>=values.length || values[i++].length()==0 ? null : Long.parseLong(values[i-1]));
				e.setTaskCompTime(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
//				responseTime,receptRating 
				e.setResponseTime(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				e.setReceptRating(i>=values.length || values[i++].length()==0 ? null : Integer.parseInt(values[i-1]));
				events.add(e);
			}
			br.close();
		}
		catch (Exception e) {
			logger.log(Level.WARNING, "Error reading behaviour file "+behaviourfile, e);
		}
		return events;
	}
	public static void main(String args[]) {
		if (args.length!=1) {
			logger.log(Level.SEVERE, "usage: <logdir>");
			System.exit(-1);			
		}
		readLogs(new File(args[0]));
	}
}
