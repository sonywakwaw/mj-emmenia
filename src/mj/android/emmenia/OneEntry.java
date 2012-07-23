package mj.android.emmenia;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OneEntry implements Serializable{
	
	private long id;
	private long date;
	private String title;
	private int icon;
	private int days;
	
	public OneEntry (long id, long date, String title, int icon) {
		this.id = id;
		this.date = date;
		this.title = title;
		this.icon = icon;
	}
	
	public long getID () {return id;}
	public long getDate () {return date;}
	public String getTitle () {return title;}
	public int getIcon () {return icon;}
	public int getDays () {return days;}
	public void setDays (int days) {this.days = days;}
	
	@Override
	public String toString () {return "id: " + id + " date: " + date;}
}