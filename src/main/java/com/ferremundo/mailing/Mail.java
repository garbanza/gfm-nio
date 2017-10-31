package com.ferremundo.mailing;

public interface Mail {
	
	public boolean send( String subject, String text,String[] recipients);
	public boolean send( String subject, String text,String[] recipients, String[] paths, String[] fileNames);

}