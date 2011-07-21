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
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;

/**
 * @author cmg
 *
 */
public class SvgFile {
	private PrintWriter pw;
	public SvgFile(File file) throws UnsupportedEncodingException, FileNotFoundException {
		//logger.log(Level.INFO,"Test output to "+file);
		pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));
		pw.println("<?xml version=\"1.0\" standalone=\"no\"?>");
		pw.println("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">");
		// svg file
		pw.println("<svg opacity=\"20%\" width=\"30cm\" height=\"30cm\" version=\"1.1\" viewBox=\"0 0 1000 1000\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\">");
	}
	public void close() {
		pw.println("</svg>");
		pw.close();
	}
	public void desc(String desc) {
		pw.println("<desc>"+escape(desc)+"</desc>");
	}
	private String escape(String desc) {
//		StringBuilder sb = new StringBuilder();
		
		return desc;
	}
	public void print(String s) {
		pw.println(s);
	}
	public void rect(float x, float y, float width, float height, String stroke, float strokeWidth,
			String fill) {
		pw.println("<rect x=\""+x+"\" y=\""+y+"\" width=\""+width+"\" height=\""+height+"\" "+(stroke!=null ? "stroke=\""+stroke+"\" ": "")+"stroke-width=\""+strokeWidth+"\" "+(fill!=null ? "fill=\""+fill+"\" " : "")+"/>");
	}
	public void circle(float cx, float cy, float r, String stroke, float strokeWidth,
			String fill) {
		circle(cx, cy, r, stroke, strokeWidth, fill, null);
	}
	public void circle(float cx, float cy, float r, String stroke, float strokeWidth,
				String fill, String extra) {
		pw.println("<circle cx=\""+cx+"\" cy=\""+cy+"\" r=\""+r+"\" "+(stroke!=null ? "stroke=\""+stroke+"\" ": "")+"stroke-width=\""+strokeWidth+"\" "+(fill!=null ? "fill=\""+fill+"\" " : "")+(extra!=null ? extra : "")+"/>");
	}
	public void line(float x1, float y1, float x2, float y2, String stroke, float strokeWidth) {
		pw.println("<line x1=\""+x1+"\" y1=\""+y1+"\" x2=\""+x2+"\" y2=\""+y2+"\" "+(stroke!=null ? "stroke=\""+stroke+"\" ": "")+"stroke-width=\""+strokeWidth+"\"/>");
	}
}
