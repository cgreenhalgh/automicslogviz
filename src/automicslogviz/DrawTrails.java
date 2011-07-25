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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author cmg
 *
 */
public class DrawTrails {
	static Logger logger = Logger.getLogger(DrawTrails.class.getName());
	public static double BORDER_M = 100;
	public static double SCALE = 1000;
	public static int MAG = 10;
//	private static double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE, maxX=-Double.MAX_VALUE, maxY=-Double.MAX_VALUE;
//	private static double minLat=Double.MAX_VALUE, minLon=Double.MAX_VALUE, maxLat=-Double.MAX_VALUE, maxLon=-Double.MAX_VALUE;
	private static String colors [] = new String [] { "#f00", "#ff0", "#0f0", "#0ff", "#00f", "#f0f", "#f80", "#f08", "#0f8", "#8f0", "#80f", "#08f","#800", "#880", "#080", "#088", "#008", "#808" };
	private static double width;
	private static double cX, cY, cLat, cLon;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length!=3) {
			logger.log(Level.SEVERE, "usage: <logdir> <zonefile> <outdir>");
			System.exit(-1);			
		}
		try {
			cLat=52.987253;
			cLon=-1.8895595;
			cX = Mercator.mercX(cLon);
			cY = Mercator.mercY(cLat);
			//http://maps.googleapis.com/maps/api/staticmap?center=40.714728,-73.998672&zoom=12&size=400x400&maptype=satellite&sensor=false
			//INFO: lon: -1.896922 to -1.882197, lat: 52.984308 to 52.990198
			//http://maps.googleapis.com/maps/api/staticmap?center=52.987253,-1.8895595&zoom=14&size=640x640&maptype=satellite&sensor=false
			List<UserData> users = LogReader.readLogs(new File(args[0]));
			Map<Integer,Zone> zones = LogReader.readZones(new File(args[1]));
			logger.log(Level.INFO,"Read "+zones.size()+" zones");
			File outdir = new File(args[2]);
			if (!outdir.isDirectory()) {
				logger.log(Level.SEVERE, "Output directory not valid: "+outdir);
				System.exit(-1);
			}
			
			width = 2000;
			
			SvgFile svg = createBackgroundFile(new File(outdir,"park.svg"));
			{
				double minX=Double.MAX_VALUE, minY=Double.MAX_VALUE, maxX=-Double.MAX_VALUE, maxY=-Double.MAX_VALUE;
				double minLat=Double.MAX_VALUE, minLon=Double.MAX_VALUE, maxLat=-Double.MAX_VALUE, maxLon=-Double.MAX_VALUE;
				float minHOD=Float.MAX_VALUE, maxHOD=-Float.MAX_VALUE;
				float minHOD2=Float.MAX_VALUE, maxHOD2=-Float.MAX_VALUE;
				for (UserData user : users) {
					for (Position p : user.getPositions()) {
						if (p.isTruncated())
							continue;

						double x= p.getX();
						double y= p.getY();
						if (x<minX)
							minX = x;
						if (x>maxX)
							maxX = x;
						if (y<minY)
							minY = y;
						if (y>maxY)
							maxY = y;
						double lat= p.getLat();
						double lon= p.getLon();
						if (lat<minLat)
							minLat = lat;
						if (lat>maxLat)
							maxLat = lat;
						if (lon<minLon)
							minLon = lon;
						if (lon>maxLon)
							maxLon = lon;
						float hod = p.getHourOfDay();
						if (hod<minHOD)
							minHOD=hod;
						if (hod>maxHOD)
							maxHOD=hod;
					}
					for (Event e : user.getEvents()) {
						Float hod = e.getHourOfDay();
						if (hod==null)
							continue;
						if (hod<minHOD2)
							minHOD2=hod;
						if (hod>maxHOD2)
							maxHOD2=hod;
						
					}
				}
				logger.log(Level.INFO,"Ranges, x: "+minX+" to "+maxX+", y: "+minY+" to "+maxY);
				logger.log(Level.INFO,"Ranges, lon: "+minLon+" to "+maxLon+", lat: "+minLat+" to "+maxLat);
				logger.log(Level.INFO,"Ranges, HOD: "+minHOD+" to "+maxHOD);
				logger.log(Level.INFO,"Ranges, HOD2: "+minHOD2+" to "+maxHOD2);
				minX -= BORDER_M;
				minY -= BORDER_M;
				maxX += BORDER_M;
				maxY += BORDER_M;
			}
			//String stroke = "#ff0000";
			drawPositions(svg, users, "#eee", false);
			drawZones(svg, zones);
			
			svg.close();
			svg = createBackgroundFile(new File(outdir,"allusers.svg"));
			drawPositions(svg, users, null, false);
			svg.close();
			
			svg = createBackgroundFile(new File(outdir,"allevents.svg"));
			drawPositions(svg, users, null, false);
			drawEvents(svg, users, null, zones);
			svg.close();

			svg.close();
			svg = createBackgroundFile(new File(outdir,"allusershod.svg"));
			drawPositionsHod(svg, users, false);
			drawEventsHod(svg, users, zones);
			svg.close();
			
			svg = createBackgroundFile(new File(outdir,"allusershodtl.svg"));
			drawZones2(svg, zones);
			drawPositionsHod(svg, users, false);
			drawTimelineEventsHod(svg, users, zones);
			svg.close();
			
			Set<String> groups  = new HashSet<String>();
			for (UserData u : users) {
				groups.add(u.getTrialid());
			}
			for (String group : groups) {
				logger.info("Group "+group);
				svg = createBackgroundFile(new File(outdir,"allusershod-"+group+".svg"));
				List<UserData> members = new LinkedList<UserData>();
				for (UserData u : users) 
					if (u.getTrialid().equals(group))
						members.add(u);
				drawPositionsHod(svg, members, false);
				drawEventsHod(svg, members, zones);
				svg.close();
				
				svg = createBackgroundFile(new File(outdir,"allusershodtl-"+group+".svg"));
				drawZones2(svg, zones);
				drawPositionsHod(svg, members, false);
				drawTimelineEventsHod(svg, members, zones);
				svg.close();
			}
			for (UserData user : users) {
				logger.info("User "+user.getTrialid()+" "+user.getTrialuserid());
				svg = createBackgroundFile(new File(outdir,"allusershod-"+user.getTrialid()+"-"+user.getTrialuserid()+".svg"));
				List<UserData> members = new LinkedList<UserData>();
				members.add(user);
				drawPositionsHod(svg, members, false);
				drawEventsHod(svg, members, zones);
				svg.close();

				svg = createBackgroundFile(new File(outdir,"allusershodtl-"+user.getTrialid()+"-"+user.getTrialuserid()+".svg"));
				drawZones2(svg, zones);
				drawPositionsHod(svg, members, false);
				drawTimelineEventsHod(svg, members, zones);
				svg.close();
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating "+args[1], e);
		}		
	}
	private static void drawTimelineEventsHod(SvgFile svg,
			List<UserData> members, Map<Integer, Zone> zones) {
		// TODO Auto-generated method stub
		for (UserData user : members) {
			Timeline tl = getTimeline(user);
			for (Event e : user.getEvents()) {
				//if (!"newImages".equals(e.getTaskType()))
				DrawTimelines.drawEvent(svg, e, tl, zones);				
			}
		}
	}
	private static Timeline getTimeline(UserData user) {
		Timeline tl = new Timeline();
		TimelinePoint lasttp = null;
		boolean truncated = false;
		for (Position p : user.getPositions()) {
			TimelinePoint tp = new TimelinePoint(p.getHourOfDay(), scaleX(p.getX()), scaleY(p.getY()));
			if (lasttp==null)
				lasttp = tp;
			else {
				if (!p.isTruncated()) {
					TimelineSegment s1 =new TimelineSegment(lasttp, tp, (truncated || p.isTruncated() ? DrawTimelines.DASHED : DrawTimelines.SOLID));
					tl.getSegments().add(s1);
					lasttp = tp;
					truncated = p.isTruncated();
				}
				else
					truncated = true;
			}
		}
		return tl;
	}
	/**
	 * @param svg
	 * @param users
	 * @param b
	 */
	private static void drawPositions(SvgFile svg, List<UserData> users,
			String overrideStroke, boolean showPoints) {
		float strokeWidth = 0.5f;
		int ci = 0;
		for (UserData user : users) {
			ci = ci+1 % colors.length;
			String stroke = overrideStroke!=null ? overrideStroke : colors[ci];
			drawPositions(svg, user, stroke, strokeWidth, showPoints);
			for (Event e : user.getEvents()) {
//				e.get
			}
		}
		
	}
	private static void drawPositions(SvgFile svg, UserData user,
			String stroke, float strokeWidth, boolean showPoints) {
		Position lp = null;
		Position lastGoodp = null;
		for (Position p : user.getPositions()) {
			if (lp!=null && !lp.isTruncated() && !p.isTruncated()) {
				svg.line(scaleX(lp.getX()), scaleY(lp.getY()), scaleX(p.getX()), scaleY(p.getY()), stroke, strokeWidth);
			}
			else if (lastGoodp!=null && !p.isTruncated()) {
				svg.line(scaleX(lastGoodp.getX()), scaleY(lastGoodp.getY()), scaleX(p.getX()), scaleY(p.getY()), stroke, strokeWidth, "stroke-dasharray=\""+MAG*3+" "+MAG*3+"\"");					
				lastGoodp = null;
			}
			lp = p;
			if (!p.isTruncated())
				lastGoodp = p;
			if (showPoints)
				svg.circle(scaleX(p.getX()), scaleY(p.getY()), 3, stroke, 0.4f, null);
		}		
	}
	private static String getColour(float value) {
		if(value<0)
			value = 0;
		if (value>1)
			value = 1;
		return "#"+getHex(1-value)+"f0"+getHex(value);
	}
	private static String getHex(float f) {
		String red = Integer.toHexString((int)(255*f));
		if (red.length()==1)
			red = "0"+red;
		return red;
	}
	/** colour by hour of day */
	private static void drawPositionsHod(SvgFile svg, List<UserData> users,
			boolean showPoints) {
		float strokeWidth = 0.5f;
		for (UserData user : users) {
			Position lp = null;
			Position lastGoodp = null;
			for (Position p : user.getPositions()) {
				String stroke = getColour(scaleHod(p.getHourOfDay()));
				if (lp!=null && !lp.isTruncated() && !p.isTruncated()) {
					svg.line(scaleX(lp.getX()), scaleY(lp.getY()), scaleX(p.getX()), scaleY(p.getY()), stroke, strokeWidth);
				}
				else if (lastGoodp!=null && !p.isTruncated()) {
					svg.line(scaleX(lastGoodp.getX()), scaleY(lastGoodp.getY()), scaleX(p.getX()), scaleY(p.getY()), stroke, strokeWidth, "stroke-dasharray=\""+MAG*3+" "+MAG*3+"\"");					
					lastGoodp = null;
				}
				lp = p;
				if (!p.isTruncated())
					lastGoodp = p;
				if (showPoints)
					svg.circle(scaleX(p.getX()), scaleY(p.getY()), 3, stroke, 0.4f, null);
			}
		}		
	}
	private static float scaleHod(float hourOfDay) {
		return (float)(hourOfDay-6)/(16-6);
	}
	private static void drawEvents(SvgFile svg, List<UserData> users,
			String overrideStroke, Map<Integer,Zone> zones) {
		float strokeWidth = 0.5f;
		int ci = 0;
		for (UserData user : users) {
			ci = ci+1 % colors.length;
			String stroke = overrideStroke!=null ? overrideStroke : colors[ci];
			for (Event e : user.getEvents()) {
//				e.get
				if (e.getGpsTagId()!=0) {
					Zone z= zones.get(e.getGpsTagId());
					if (z!=null) {
						String fill = getZoneFill(z);
						float rx = (float)((Math.random()-0.5)*JITTER);
						float ry = (float)((Math.random()-0.5)*JITTER);
						svg.circle(rx+scaleX(Mercator.mercX(z.getLon())),ry+scaleY(Mercator.mercY(z.getLat())), scaleD(z.getRadius()), stroke, 1.0f, fill,"fill-opacity=\"0.1\"");
					}
				}
			}
		}
		
	}
	private static void drawEventsHod(SvgFile svg, List<UserData> users,
			Map<Integer,Zone> zones) {
		float strokeWidth = 0.5f;
		int ci = 0;
		for (UserData user : users) {
			for (Event e : user.getEvents()) {
//				e.get
				Float hod = e.getHourOfDay();
				if (hod==null)
					continue;
				String stroke = getColour(scaleHod(hod));
				if (e.getGpsTagId()!=0) {
					Zone z= zones.get(e.getGpsTagId());
					if (z!=null) {
						String fill = getZoneFill(z);
						float rx = (float)((Math.random()-0.5)*JITTER);
						float ry = (float)((Math.random()-0.5)*JITTER);
						float size = scaleD(z.getRadius());
						if (size<1)
							size = 1;
						svg.circle(rx+scaleX(Mercator.mercX(z.getLon())),ry+scaleY(Mercator.mercY(z.getLat())), size, stroke, 1.0f, fill,"fill-opacity=\"0.1\"");
					}
				}
			}
		}
		
	}
	static final double JITTER = 5;
	/**
	 * @param svg
	 * @param zones
	 */
	
	private static void drawZones(SvgFile svg, Map<Integer, Zone> zones) {
		for (Zone z : zones.values()) {
			String fill = getZoneFill(z);
			float size = scaleD(z.getRadius());
			if (size<1)
				size = 1;
			svg.circle(scaleX(Mercator.mercX(z.getLon())),scaleY(Mercator.mercY(z.getLat())), size, fill, 1.0f, fill, " fill-opacity=\"30%\"");
		}
	}
	private static void drawZones2(SvgFile svg, Map<Integer, Zone> zones) {
		for (Zone z : zones.values()) {
			String fill = getZoneFill(z);
			float size = scaleD(z.getRadius());
			if (size<1)
				size = 1;
			svg.circle(scaleX(Mercator.mercX(z.getLon())),scaleY(Mercator.mercY(z.getLat())), size, fill, 0.0f, fill, " fill-opacity=\"15%\" stroke-opacity=\"20%\"");
		}
	}
	/**
	 * @param z
	 * @return
	 */
	public static String getZoneFill(Zone z) {
		String fill = "#000";
		if ("photo opportunity".equals(z.getType())) 
			fill = "#00f";
		else if ("lunch/break".equals(z.getType())) 
			fill = "#0f0";
		else if ("Q-Zone".equals(z.getType())) 
			fill = "#f00";
		else if ("end of ride".equals(z.getType()))
			fill = "#ff0";
		return fill;
	}
	/**
	 * @param file
	 * @return
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	private static SvgFile createBackgroundFile(File file) throws UnsupportedEncodingException, FileNotFoundException {
		SvgFile svg = new SvgFile(file, MAG);
		svg.desc("DrawTrails of automics data, "+new Date());
		svg.print("<filter id=\"desaturate\"  primitiveUnits=\"objectBoundingBox\">");
//		svg.print("<feImage x=\""+(500-width/2)+"\" y=\""+(500-width/2)+"\" width=\""+width+"\" height=\""+width+"\" xlink:href=\"altontowers-zoom15.png\"/>");
		svg.print("<feImage x=\"0%\" y=\"0%\" width=\"100%\" height=\"100%\" xlink:href=\"altontowers-zoom15.png\"/>");
		svg.print("<feColorMatrix type=\"matrix\" values=\"0.3333 0.3333 0.3333 0 0 "+
                "0.3333 0.3333 0.3333 0 0 "+
              "0.3333 0.3333 0.3333 0 0 "+
            "0      0      0      1 0\"/>");
		svg.print("<feColorMatrix type=\"matrix\" values=\"0.05 0 0 0 0 "+
                "0 0.05 0 0 0 "+
              "0 0 0.05 0 0 "+
            "0      0      0      1 0\"/>");
		svg.print("</filter>");
		// zoom level - 2^zoom level tiles in x & y
		int zoom = 15;
		double imageWidth = 2*Mercator.mercX(180)/Math.pow(2, zoom)*640/256;
		svg.print("<rect x=\""+MAG*SCALE*0.5*(1-imageWidth/width)+"\" y=\""+MAG*SCALE*0.5*(1-imageWidth/width)+"\" width=\""+MAG*SCALE*(imageWidth/width)+"\" height=\""+MAG*SCALE*(imageWidth/width)+"\" filter=\"url(#desaturate)\"/>");
		return svg;
	}
	private static float scaleX(double x) {
		return (float)(SCALE*(0.5+(x-cX)/width));
//		return (float)(SCALE*(x-minX)/(maxX-minX));
	}
	private static float scaleY(double y) {
		return (float)(SCALE*(0.5-(y-cY)/width));
//		return (float)(SCALE*(y-minY)/(maxY-minY));
	}
	/** real metres! */
	private static float scaleD(double d) {
		return (float)(SCALE*d/width/Math.cos(Math.PI*cLat/180));
	}
}
