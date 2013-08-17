/*
 * @(#)Boolean.java	1.32 98/09/21
 *
 * Copyright 1994-1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package java.lang;

/**
 * The Boolean class wraps a value of the primitive type 
 * <code>boolean</code> in an object. An object of type 
 * <code>Boolean</code> contains a single field whose type is 
 * <code>boolean</code>. 
 * <p>
 * In addition, this class provides many methods for 
 * converting a <code>boolean</code> to a <code>String</code> and a 
 * <code>String</code> to a <code>boolean</code>, as well as other 
 * constants and methods useful when dealing with a 
 * <code>boolean</code>. 
 *
 * @author  Arthur van Hoff
 * @version 1.29, 07/23/98
 * @since   JDK1.0
 */
public final
class Boolean implements java.io.Serializable {
    /** 
     * The <code>Boolean</code> object corresponding to the primitive 
     * value <code>true</code>. 
     */
    public static final Boolean TRUE = new Boolean(true);

    /** 
     * The <code>Boolean</code> object corresponding to the primitive 
     * value <code>false</code>. 
     */
    public static final Boolean FALSE = new Boolean(false);

    /**
     * The Class object representing the primitive type boolean.
     *
     * @since   JDK1.1
     */
    public static final Class	TYPE = Class.getPrimitiveClass("boolean");

    /**
     * The value of the Boolean.
     *
     * @serial
     */
    private boolean value;

    /** use serialVersionUID from JDK 1.0.2 for interoperability */
    private static final long serialVersionUID = -3665804199014368530L;

    /**
     * Allocates a <code>Boolean</code> object representing the 
     * <code>value</code> argument. 
     *
     * @param   value   the value of the <code>Boolean</code>.
     */
    public Boolean(boolean value) {
	this.value = value;
    }

    /**
     * Allocates a <code>Boolean</code> object representing the value 
     * <code>true</code> if the string argument is not <code>null</code> 
     * and is equal, ignoring case, to the string <code>"true"</code>. 
     * Otherwise, allocate a <code>Boolean</code> object representing the 
     * value <code>false</code>. Examples:<p>
     * <tt>new&nbsp;Boolean("True")</tt> produces a <tt>Boolean</tt> object 
     * that represents <tt>true</tt>.<br>
     * <tt>new&nbsp;Boolean("yes")</tt> produces a <tt>Boolean</tt> object 
     * that represents <tt>false</tt>.
     *
     * @param   s   the string to be converted to a <code>Boolean</code>.
     */
    public Boolean(String s) {
	this(toBoolean(s));
    }

    /**
     * Returns the value of this <tt>Boolean</tt> object as a boolean 
     * primitive.
     *
     * @return  the primitive <code>boolean</code> value of this object.
     */
    public boolean booleanValue() {
	return value;
    }

    /**
     * Returns the boolean value represented by the specified String.
     * A new <code>Boolean</code> object is constructed. This 
     * <code>Boolean</code> represents the value <code>true</code> if the 
     * string argument is not <code>null</code> and is equal, ignoring 
     * case, to the string <code>"true"</code>. <p>
     * Example: <tt>Boolean.valueOf("True")</tt> returns <tt>true</tt>.<br>
     * Example: <tt>Boolean.valueOf("yes")</tt> returns <tt>false</tt>.
     *
     * @param   s   a string.
     * @return  the <code>Boolean</code> value represented by the string.
     */
    public static Boolean valueOf(String s) {
	return new Boolean(toBoolean(s));
    }

    /**
     * Returns a String object representing this Boolean's value.
     * If this object represents the value <code>true</code>, a string equal 
     * to <code>"true"</code> is returned. Otherwise, a string equal to 
     * <code>"false"</code> is returned. 
     *
     * @return  a string representation of this object. 
     */
    public String toString() {
	return value ? "true" : "false";
    }

    /**
     * Returns a hash code for this <tt>Boolean</tt> object.
     *
     * @return  the integer <tt>1231</tt> if this object represents 
     * <tt>true</tt>; returns the integer <tt>1237</tt> if this 
     * object represents <tt>false</tt>. 
     */
    public int hashCode() {
	return value ? 1231 : 1237;
    }

    /**
     * Returns <code>true</code> if and only if the argument is not 
     * <code>null</code> and is a <code>Boolean </code>object that 
     * represents the same <code>boolean</code> value as this object. 
     *
     * @param   obj   the object to compare with.
     * @return  <code>true</code> if the Boolean objects represent the 
     *          same value; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
	if ((obj != null) && (obj instanceof Boolean)) {
	    return value == ((Boolean)obj).booleanValue();
	} 
	return false;
    }

    /**
     * Returns <code>true</code> if and only if the system property 
     * named by the argument exists and is equal to the string 
     * <code>"true"</code>. (Beginning with version 1.0.2 of the 
     * Java<font size="-2"><sup>TM</sup></font> platform, the test of 
     * this string is case insensitive.) A system property is accessible 
     * through <code>getProperty</code>, a method defined by the 
     * <code>System</code> class. 
     *
     * @param   name   the system property name.
     * @return  the <code>boolean</code> value of the system property.
     * @see     java.lang.System#getProperty(java.lang.String)
     * @see     java.lang.System#getProperty(java.lang.String, java.lang.String)
     */
    public static boolean getBoolean(String name) {
	return toBoolean(System.getProperty(name));
    }

    private static boolean toBoolean(String name) { 
	return ((name != null) && name.toLowerCase().equals("true"));
    }
}
