/*
 * @(#)Date.java	1.18 98/09/30
 *
 * Copyright 1996-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.sql;

/**
 * <P>A thin wrapper around a millisecond value that allows
 * JDBC to identify this as a SQL DATE.  A milliseconds value represents
 * the number of milliseconds that have passed since January 1, 1970
 * 00:00:00.000 GMT.
 * <p>
 * To conform with the definition of SQL DATE, the millisecond values 
 * wrapped by a java.sql.Date instance must be 'normalized' by setting the 
 * hours, minutes, seconds, and milliseconds to zero in the particular
 * time zone with which the instance is associated.
 */
public class Date extends java.util.Date {

    /**
     * Constructs a <code>Date</code> object initialized with the given
	 * year, month, and day.
     *
     * @param year year-1900
     * @param month 0 to 11 
     * @param day 1 to 31
     * @deprecated instead use the constructor <code>Date(long date)</code>
     */
    public Date(int year, int month, int day) {
	super(year, month, day);
    }

    /**
     * Constructs a <code>Date</code> object 
	 * using a milliseconds time value.  If the given millisecond 
	 * value contains time information, the driver will set the
	 * time components to zero.
     *
     * @param date milliseconds since January 1, 1970, 00:00:00 GMT.
				   A negative number indicates the number of milliseconds
				   before January 1, 1970, 00:00:00 GMT.
     */
    public Date(long date) {
	// If the millisecond date value contains time info, mask it out.
	super(date);
	
    }

    /**
     * Sets an existing <code>Date</code> object 
	 * using the given milliseconds time value.  If the given milliseconds 
	 * value contains time information, the driver will set the
	 * time components to zero.
     *
     * @param date milliseconds since January 1, 1970, 00:00:00 GMT.
				   A negative number indicates the number of milliseconds
				   before January 1, 1970, 00:00:00 GMT.
     */
    public void setTime(long date) {
	// If the millisecond date value contains time info, mask it out.
	super.setTime(date);	 
    }

    /**
     * Converts a string in JDBC date escape format to
	 * a <code>Date</code> value.
     *
     * @param s date in format "yyyy-mm-dd"
     * @return a <code>Date</code> object representing the given date
     */
    public static Date valueOf(String s) {
	int year;
	int month;
	int day;
	int firstDash;
	int secondDash;

	if (s == null) throw new java.lang.IllegalArgumentException();

	firstDash = s.indexOf('-');
	secondDash = s.indexOf('-', firstDash+1);
	if ((firstDash > 0) & (secondDash > 0) & (secondDash < s.length()-1)) {
	    year = Integer.parseInt(s.substring(0, firstDash)) - 1900;
	    month = Integer.parseInt(s.substring(firstDash+1, secondDash)) - 1;
	    day = Integer.parseInt(s.substring(secondDash+1));	 
	} else {
	    throw new java.lang.IllegalArgumentException();
	}
			
	return new Date(year, month, day);
    }

    /**
     * Formats a date in JDBC date escape format.  
     *
     * @return a String in yyyy-mm-dd format
     */
    public String toString () {
	int year = super.getYear() + 1900;
	int month = super.getMonth() + 1;
	int day = super.getDate();
	String yearString;
	String monthString;
	String dayString;

		
	yearString = Integer.toString(year);

	if (month < 10) {
	    monthString = "0" + month;
	} else {		
	    monthString = Integer.toString(month);
	}

	if (day < 10) {
	    dayString = "0" + day;
	} else {		
	    dayString = Integer.toString(day);
	}

	return ( yearString + "-" + monthString + "-" + dayString);
    }

    // Override all the time operations inherited from java.util.Date;

   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public int getHours() {
	throw new java.lang.IllegalArgumentException();
    }

   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public int getMinutes() {
	throw new java.lang.IllegalArgumentException();
    }
    
   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public int getSeconds() {
	throw new java.lang.IllegalArgumentException();
    }

   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public void setHours(int i) {
	throw new java.lang.IllegalArgumentException();
    }

   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public void setMinutes(int i) {
	throw new java.lang.IllegalArgumentException();
    }

   /**
    * This method is deprecated and should not be used because SQL Date 
    * values do not have a time component.
    *
    * @deprecated
	* @exception java.lang.IllegalArgumentException if this method is invoked
    */
    public void setSeconds(int i) {
	throw new java.lang.IllegalArgumentException();
    }
}

