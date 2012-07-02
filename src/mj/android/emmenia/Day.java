package mj.android.emmenia;

import java.io.Serializable;
import java.util.Calendar;

@SuppressWarnings("serial")
public class Day implements Serializable{
	
	private Calendar date;
	private int phase;
	
	public Day (Calendar date, int phase) {
		this.date = date;
		this.phase = phase;
	}
	
	public Calendar getDate () {return date;}
	public int getPhase () {return phase;}
	
	@Override
	public String toString () {
		return "date: " + date + " phase: " + phase;
		
	}
}