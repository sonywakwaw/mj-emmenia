package mj.android.emmenia;

import java.io.Serializable;

@SuppressWarnings("serial")
public class OnePhase implements Serializable{
	
	private long id;
	private int from;
	private int to;
	private String desc;
	private int icon;
	
	public OnePhase (long id, int from, int to, String desc, int icon) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.desc = desc;
		this.icon = icon;
	}
	
	public long getID () {return id;}
	public long getFrom () {return from;}
	public long getTo () {return to;}
	public String getDesc () {return desc;}
	public int getIcon () {return icon;}
}