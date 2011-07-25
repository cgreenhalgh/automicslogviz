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

import org.omg.PortableServer.LIFESPAN_POLICY_ID;

/**
 * @author cmg
 *
 */
public class DrawTimelines {
	static Logger logger = Logger.getLogger(DrawTimelines.class.getName());
	public static float SCALE = 1000;
	public static float BIG_MARGIN = 100;
	public static float SMALL_MARGIN = 10;
	public static double MIN_HOD = 8; // 8?
	public static double MAX_HOD = 16;
	private static final int LANE_WIDTH = 80;
	private static final String NONE = "none";
	static final String DASHED = "dashed";
	static final String SOLID = "solid";
	private static final String NOTIFICATION = "notification";
	private static final String ACCEPTANCE = "acceptance";
	private static final float TEXT_HEIGHT = 15;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length!=3) {
			logger.log(Level.SEVERE, "usage: <logdir> <zonefile> <outdir>");
			System.exit(-1);			
		}
		try {
			List<UserData> users = LogReader.readLogs(new File(args[0]));
			Map<Integer,Zone> zones = LogReader.readZones(new File(args[1]));
			logger.log(Level.INFO,"Read "+zones.size()+" zones");
			File outdir = new File(args[2]);
			if (!outdir.isDirectory()) {
				logger.log(Level.SEVERE, "Output directory not valid: "+outdir);
				System.exit(-1);
			}
			
//			svg = createBackgroundFile(new File(outdir,"allusers.svg"));
//			drawPositions(svg, users, null, false);
//			svg.close();
			SvgFile svg;
			svg = createTimelineFile(new File(outdir,"timelinetest.svg"));
			
			for (int hod=(int)Math.ceil(MIN_HOD); hod<MAX_HOD; hod++) {
				double x = BIG_MARGIN+(SCALE-BIG_MARGIN-SMALL_MARGIN)*(hod-MIN_HOD)/(MAX_HOD-MIN_HOD);
				svg.line((float)x, (float)SMALL_MARGIN, (float)x, (float)(SCALE-BIG_MARGIN), "#888", 0.5f, " stroke-dasharray=\"2 4\"");
				svg.text(hod+":00", (float) x, SCALE-BIG_MARGIN+TEXT_HEIGHT, 11, "#000", null);
			}
			
			int ui =0;
			for (UserData user : users) {
				Timeline tl = new Timeline();
				float y = SMALL_MARGIN+LANE_WIDTH/2+LANE_WIDTH*ui;
				TimelinePoint lasttp = new TimelinePoint(MIN_HOD, (float)(BIG_MARGIN), y);
				boolean first = true;
				boolean truncated = false;
				for (Position p : user.getPositions()) {
					TimelinePoint tp = new TimelinePoint(p.getHourOfDay(), (float)(BIG_MARGIN+(SCALE-BIG_MARGIN-SMALL_MARGIN)*(p.getHourOfDay()-MIN_HOD)/(MAX_HOD-MIN_HOD)), y);
					if (lasttp==null)
						lasttp = tp;
					else {
						if (first || !p.isTruncated()) {
							TimelineSegment s1 =new TimelineSegment(lasttp, tp, first ? NONE : (truncated || p.isTruncated() ? DASHED : SOLID));
							tl.getSegments().add(s1);
							lasttp = tp;
							truncated = p.isTruncated();
							first = false;
						}
						else
							truncated = true;
					}
				}
				if (truncated && lasttp!=null) {
					// last line
					Position p = user.getPositions().get(user.getPositions().size()-1);
					TimelinePoint tp = new TimelinePoint(p.getHourOfDay(), (float)(BIG_MARGIN+(SCALE-BIG_MARGIN-SMALL_MARGIN)*(p.getHourOfDay()-MIN_HOD)/(MAX_HOD-MIN_HOD)), y);
					TimelineSegment s1 =new TimelineSegment(lasttp, tp, DASHED);
					tl.getSegments().add(s1);					
					lasttp = tp;
				}
				{ 
					// last last line :-)
					// last line
					TimelinePoint tp = new TimelinePoint(MAX_HOD, (float)(SCALE-SMALL_MARGIN), y);
					TimelineSegment s1 =new TimelineSegment(lasttp, tp, NONE);
					tl.getSegments().add(s1);					
				}
				drawTimeline(svg, tl);
				svg.text(user.getTrialid()+"/"+user.getTrialuserid(), 0, y, 11, "#000", null);
				
				for (Event e : user.getEvents()) {
					// init==sys => notificationTs & acceptanceTime (seconds, 0==not)
					// init==user => taskAcceptTs
					if (!"newImages".equals(e.getTaskType()))
						drawEvent(svg, e, tl);
				}
				
				ui++;
				
			}
			svg.close();
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error creating "+args[1], e);
		}		
	}
	static void drawEvent(SvgFile svg, Event e, Timeline tl) {
		String stroke = getEventStroke(e);
		// TODO Auto-generated method stub
		if (e.getNotificationTs()!=null && e.getNotificationTs()!=0) {
			float tod = Utils.getHourOfDay(e.getNotificationTs());
			TimelineSegment s = getTimelineSegment(tl, tod);
			if (s==null)
				logger.log(Level.WARNING,"COuld not find TimelineSegment for event "+e);
			else {
				drawGlyph(svg, s, tod, NOTIFICATION, stroke);
			}
		}
		if (e.getNotficationAcceptTs()!=null && e.getNotficationAcceptTs()!=0) {
			float tod = Utils.getHourOfDay(e.getNotficationAcceptTs());
			TimelineSegment s = getTimelineSegment(tl, tod);
			if (s==null)
				logger.log(Level.WARNING,"COuld not find TimelineSegment for event "+e);
			else {
				drawGlyph(svg, s, tod, ACCEPTANCE, stroke);
			}
		}
		if (e.getTaskAcceptTs()!=null && e.getTaskAcceptTs()!=0) {
			float tod = Utils.getHourOfDay(e.getTaskAcceptTs());
			float tod2 = tod;
			if (e.getTaskCompTs()!=null && e.getTaskCompTs()!=0 && e.getTaskCompTs()>e.getTaskAcceptTs())
				tod2 = Utils.getHourOfDay(e.getTaskCompTs());
			drawDuration(svg, tl, tod, tod2, stroke);
		}
	}
	private static void drawDuration(SvgFile svg, Timeline tl, float tod,
			float tod2, String stroke) {
		for (TimelineSegment s : tl.getSegments()) {
			if (tod2<s.getStart().getTime())
				continue;
			if (tod>=s.getEnd().getTime())
				continue;
			double p1 = 0;
			if (tod > s.getStart().getTime() && s.getEnd().getTime()>s.getStart().getTime())
				p1 = (tod-s.getStart().getTime())/(s.getEnd().getTime()-s.getStart().getTime());
			double p2 = 1;
			if (tod2 < s.getEnd().getTime() && s.getEnd().getTime()>s.getStart().getTime())
				p2 = (tod2-s.getStart().getTime())/(s.getEnd().getTime()-s.getStart().getTime());
			double length = s.getLength()*(p2-p1);
			if (s.getLength()==0) {
				if (tod>=s.getStart().getTime() || tod2<s.getEnd().getTime())
					svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p1)+1, (float)s.getY(p1), stroke, 10, " stroke-opacity=\"100%\"");		
				else
					svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p1)+1, (float)s.getY(p1), stroke, 10, " stroke-opacity=\"30%\"");		
				svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p2)+1, (float)s.getY(p2), stroke, 1, " stroke-opacity=\"100%\"");		
			}
			else {
				//if (length<1) {
				//	p2 = p1+1/s.getLength();
				//}
				if (tod>=s.getStart().getTime())
					svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p1+1/s.getLength()), (float)s.getY(p1+1/s.getLength()), stroke, 10, " stroke-opacity=\"100%\"");		
				if (tod2<s.getEnd().getTime())
					svg.line((float)s.getX(p2), (float)s.getY(p2), (float)s.getX(p2+1/s.getLength()), (float)s.getY(p2+1/s.getLength()), stroke, 10, " stroke-opacity=\"100%\"");		
				svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p2), (float)s.getY(p2), stroke, 10, " stroke-opacity=\"30%\"");
				svg.line((float)s.getX(p1), (float)s.getY(p1), (float)s.getX(p2), (float)s.getY(p2), stroke, 1, " stroke-opacity=\"100%\"");
			}
		}

		// TODO Auto-generated method stub
		
	}
	private static void drawGlyph(SvgFile svg, TimelineSegment s, double time,
			String type, String stroke) {
		if (time<s.getStart().getTime())
			time = s.getStart().getTime();
		else if (time>s.getEnd().getTime())
			time = s.getEnd().getTime();
		double x = s.getStart().getX();
		double y = s.getStart().getY();
		double angle = 0;
		if (s.getStart().getTime()<s.getEnd().getTime()) {
			x = s.getStart().getX()+(s.getEnd().getX()-s.getStart().getX())*(time-s.getStart().getTime())/(s.getEnd().getTime()-s.getStart().getTime());
			y = s.getStart().getY()+(s.getEnd().getY()-s.getStart().getY())*(time-s.getStart().getTime())/(s.getEnd().getTime()-s.getStart().getTime());
		}
		angle = 180*Math.atan2(s.getEnd().getY()-s.getStart().getY(), s.getEnd().getX()-s.getStart().getX())/Math.PI;
		svg.print("<g transform=\"translate("+x+" "+y+") rotate("+angle+")\">");
		if (NOTIFICATION.equals(type))
			svg.print("<path fill-opacity=\"0%\" stroke=\""+stroke+"\" stroke-width=\"0.5\" d=\"M -5 -5 L 0 0 -5 5 -5 -5\"/>");
		else if (ACCEPTANCE.equals(type)) 
			svg.print("<path fill-opacity=\"0%\" stroke=\""+stroke+"\" stroke-width=\"0.5\" d=\"M -5 0 L 0 5 5 0 0 -5 -5 0\"/>");
		else
			svg.print("<path fill-opacity=\"0%\" stroke=\""+stroke+"\" stroke-width=\"0.5\" d=\"M -5 -5 L 5 5 M -5 5 L 5 -5\"/>");

		svg.print("</g>");
		// TODO Auto-generated method stub
		
	}
	private static String getEventStroke(Event e) {
		String fill = "#000";
		if ("photo opportunity".equals(e.getTaskType())) 
			fill = "#00f";
		else if ("lunch/break".equals(e.getTaskType()) || "photo_story".equals(e.getTaskType())) 
			fill = "#0f0";
		else if ("Q-Zone".equals(e.getTaskType()) || "annotate".equals(e.getTaskType()))
			fill = "#f00";
		else if ("end of ride".equals(e.getTaskType()))
			fill = "#ff0";
		else if ("photo".equals(e.getTaskType()) || "newImage".equals(e.getTaskType()))			
			fill = "#f0f";
		return fill;
	}
	private static TimelineSegment getTimelineSegment(Timeline tl, double time) {
		for (TimelineSegment s : tl.getSegments()) {
			if (s.getEnd().getTime()>time && s.getStart().getTime()<=time)
				return s;
		}
		return null;
	}
	private static void drawTimeline(SvgFile svg, Timeline tl) {
		for (TimelineSegment s : tl.getSegments()) {
			String stroke = getTimelineSegmentStroke(s);
			float strokeWidth = getTimelineSegmentStrokeWidth(s);
			String extra = getTimeSegmentExtra(s);
			//logger.log(Level.INFO, "Line "+s.getStart().getX()+","+s.getStart().getY()+" to "+s.getEnd().getX()+","+s.getEnd().getY()+", stroke="+stroke+" (width="+ strokeWidth+", extra="+extra+")");

			svg.line(s.getStart().getX(), s.getStart().getY(), s.getEnd().getX(), s.getEnd().getY(), stroke, strokeWidth, extra);
		}
	}
	private static String getTimeSegmentExtra(TimelineSegment s) {
		if (DASHED.equals(s.getType()))
			return " stroke-dasharray=\"3 3\"";
		if (NONE.equals(s.getType()))
			return " stroke-opacity=\"0%\"";
		return null;
	}
	private static float getTimelineSegmentStrokeWidth(TimelineSegment s) {
		return 0.5f;
	}
	private static String getTimelineSegmentStroke(TimelineSegment s) {
		return "#000";
	}
	private static SvgFile createTimelineFile(File file) throws UnsupportedEncodingException, FileNotFoundException {
		SvgFile svg = new SvgFile(file);
		svg.desc("DrawTimelines of automics data, "+new Date());
		svg.rect(BIG_MARGIN, SMALL_MARGIN, SCALE-BIG_MARGIN-SMALL_MARGIN, SCALE-BIG_MARGIN-SMALL_MARGIN, "#888", 0.5f, "#fff");
		return svg;
	}

}
