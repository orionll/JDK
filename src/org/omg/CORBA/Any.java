/*
 * @(#)Any.java	1.24 99/04/22
 *
 * Copyright 1997-1999 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package org.omg.CORBA;

import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;
import org.omg.CORBA.portable.IDLEntity;


/**
 * Serves as a container for any data that can be
 * described in IDL or for any IDL primitive type.
 * An <code>Any</code> object is used as a component of a
 * <code>NamedValue</code> object, which provides information about
 * arguments or return values in requests, and which is used to define
 * name/value pairs in <code>Context</code> objects.
 <p>
 *
 * An <code>Any</code> object consists of two parts:
 * <OL>
 * <LI>a data value
 * <LI>a <code>TypeCode</code> object describing the type of the data
 * value contained in the <code>Any</code> object.  For example,
 * a <code>TypeCode</code> object for an array contains
 * a field for the length of the array and a field for
 * the type of elements in the array. (Note that in	this case, the
 * second field of the <code>TypeCode</code> object is itself a
 * <code>TypeCode</code> object.)
 *
 *
 * </OL>
 * <P>
 * <a name="anyOps">
 * A large part of the <code>Any</code> class consists of pairs of methods
 * for inserting values into and extracting values from an
 * <code>Any</code> object.
 * <P>
 * For a given primitive type X, these methods are:
 *  <dl>
 *	<dt><code><bold> void insert_X(X x)</bold></code>
 *	<dd> This method allows the insertion of
 *	  an instance <code>x</code> of primitive type <code>X</code>
 *    into the <code>value</code> field of the <code>Any</code> object.
 *    Note that the method
 *    <code>insert_X</code> also resets the <code>Any</code> object's
 *    <code>type</code> field if necessary.
 *	<dt> <code><bold>X extract_X()</bold></code>
 *	<dd> This method allows the extraction of an instance of
 *	  type <code>X</code> from the <code>Any</code> object.
 *    <BR>
 *    <P>
 *    This method throws the exception <code>BAD_OPERATION</code> under two conditions:
 *    <OL>
 *     <LI> the type of the element contained in the <code>Any</code> object is not
 *	   <code>X</code>
 *     <LI> the method <code>extract_X</code> is called before
 *     the <code>value</code> field of the <code>Any</code> object
 *     has been set
 *    </OL>
 * </dl>
 * <P>
 * There are distinct method pairs for each
 * primitive IDL data type (<code>insert_long</code> and <code>extract_long</code>,
 * <code>insert_string</code> and <code>extract_string</code>, and so on).<BR>
 * <P>
 * The class <code>Any</code> also has methods for
 * getting and setting the type code,
 * for testing two <code>Any</code> objects for equality,
 * and for reading an <code>Any</code> object from a stream or
 * writing it to a stream.
 * <BR>
 * @version 1.12, 09/09/97
 * @since   JDK1.2
 */
abstract public class Any implements IDLEntity {

    /**
     * Returns type information for the element contained in this
     * <code>Any</code> object.
     *
     * @return		the <code>TypeCode</code> object containing type information
     *                about the value contained in this <code>Any</code> object.
     */

    abstract public TypeCode type();

    /**
     * Sets this <code>Any</code> object's <code>type</code> field
     * to the given <code>TypeCode</code> object and clears its value.
     * <P>
     * Note that using this method to set the type code wipes out the
     * value if there is one. The method
     * is provided primarily so that the type may be set properly for
     * IDL <code>out</code> parameters.  Generally, setting the type
     * is done by the <code>insert_X</code> methods, which will set the type
     * to X if it is not already set to X.
     *
     * @param t       the <code>TypeCode</code> object giving
     *                information for the value in
     *                this <code>Any</code> object
     */

    abstract public void type(TypeCode t);

    /**
     * Checks for equality between this <code>Any</code> object and the
     * given <code>Any</code> object.  Two <code>Any</code> objects are
     * equal if both their values and type codes are equal.
     *
     * @param a	the <code>Any</code> object to test for equality
     * @return	<code>true</code> if the <code>Any</code> objects are equal;
     * <code>false</code> otherwise
     */

    abstract public boolean equal(Any a);

    ///////////////////////////////////////////////////////////////////////////
    // marshalling/unmarshalling routines

    /**
     * Reads off (unmarshals) the value of an <code>Any</code> object from
     * the given input stream using the given typecode.
     *
     * @param is the <code>org.omg.CORBA.portable.InputStream</code>
     *                object from which to read
     *                the value contained in this <code>Any</code> object
     *
     * @param t	 a <code>TypeCode</code> object containing type information
     *           about the value to be read
     *
     * @exception MARSHAL when the given <code>TypeCode</code> object is
     *                    not consistent with the value that was contained
     *                    in the input stream
     */

    abstract public void   read_value(InputStream is, TypeCode t)
      throws MARSHAL;

    /**
     * Writes out to the given output stream the typecode and value
     * of this <code>Any</code> object.
     * <P>
     * If this method is called on an <code>Any</code> object that has not
     * had a value inserted into its <code>value</code> field, it will throw
     * the exception <code>java.lang.NullPointerException</code>.
     *
     * @param os	the <code>org.omg.CORBA.portable.OutputStream</code>
     *                object into which to marshal the value and typecode
     *                of this <code>Any</code> object
     *
     */

    abstract public void   write_value(OutputStream os);

    /**
     * Creates an output stream into which this <code>Any</code> object's
     * value can be marshalled.
     *
     * @return		the newly-created <code>OutputStream</code>
     */

    abstract public OutputStream  create_output_stream();

    /**
     * Creates an input stream from which this <code>Any</code> object's value
     * can be unmarshalled.
     *
     * @return		the newly-created <code>InputStream</code>
     */

    abstract public InputStream  create_input_stream();

    ///////////////////////////////////////////////////////////////////////////
    // insertion of streamables

    /**
     * Inserts the given <code>Streamable</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     * This method allows the insertion of non-primitive IDL types.
     *
     * @param s		the <code>Streamable</code> object to insert into this
     *                <code>Any</code> object; may be a non-primitive
	 *                IDL type
     */

    abstract public void insert_Streamable(Streamable s);


    ///////////////////////////////////////////////////////////////////////////
    // basic insertion/extraction methods

    /**
     * Extracts the <code>short</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>short</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>short</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public short    extract_short() throws BAD_OPERATION;

    /**
     * Inserts the given <code>short</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param s		the <code>short</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_short(short s);

    /**
     * Extracts the <code>int</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>int</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than an <code>int</code> or the
	 *              <code>value</code> field has not yet been set
     */

    abstract public int      extract_long() throws BAD_OPERATION;

    /**
     * Inserts the given <code>int</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param l		the <code>int</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_long(int l);


    /**
     * Extracts the <code>long</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>long</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>long</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public long     extract_longlong() throws BAD_OPERATION;

    /**
     * Inserts the given <code>long</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param l		the <code>long</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_longlong(long l);


    /**
     * Extracts the <code>short</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>short</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>short</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public short    extract_ushort() throws BAD_OPERATION;

    /**
     * Inserts the given <code>short</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param s		the <code>short</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_ushort(short s);


    /**
     * Extracts the <code>int</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>int</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than an <code>int</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public int      extract_ulong() throws BAD_OPERATION;

    /**
     * Inserts the given <code>int</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param l		the <code>int</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_ulong(int l);


    /**
     * Extracts the <code>long</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>long</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>long</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public long     extract_ulonglong() throws BAD_OPERATION;

    /**
     * Inserts the given <code>long</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param l		the <code>long</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_ulonglong(long l);


    /**
     * Extracts the <code>float</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>float</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>float</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public float    extract_float() throws BAD_OPERATION;

    /**
     * Inserts the given <code>float</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param f		the <code>float</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_float(float f);


    /**
     * Extracts the <code>double</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>double</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>double</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public double   extract_double() throws BAD_OPERATION;

    /**
     * Inserts the given <code>double</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param d		the <code>double</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_double(double d);


    /**
     * Extracts the <code>boolean</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>boolean</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>boolean</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public boolean  extract_boolean() throws BAD_OPERATION;

    /**
     * Inserts the given <code>boolean</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param b		the <code>boolean</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_boolean(boolean b);


    /**
     * Extracts the <code>char</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>char</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>char</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public char     extract_char() throws BAD_OPERATION;

    /**
     * Inserts the given <code>char</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param c		the <code>char</code> to insert into this
     *                <code>Any</code> object
	 * @exception <code>DATA_CONVERSION</code>
     */
    abstract public void     insert_char(char c) throws DATA_CONVERSION;


    /**
     * Extracts the <code>byte</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>byte</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>byte</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public byte     extract_octet() throws BAD_OPERATION;

    /**
     * Inserts the given <code>byte</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param b		the <code>byte</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_octet(byte b);


    /**
     * Extracts the <code>char</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>char</code> stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>char</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public char     extract_wchar() throws BAD_OPERATION;

    /**
     * Inserts the given <code>char</code> 
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param c		the <code>char</code> to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_wchar(char c);


    /**
     * Extracts the <code>Any</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>Any</code> object stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this <code>Any</code> object 
	 *              contains something other than an <code>Any</code> object or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public Any      extract_any() throws BAD_OPERATION;

    /**
     * Inserts the given <code>Any</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param a		the <code>Any</code> object to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_any(Any a);


    /**
     * Extracts the <code>String</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>String</code> object stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>String</code> object or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public String   extract_string() throws BAD_OPERATION;

    /**
     * Inserts the given <code>String</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param s		the <code>String</code> object to insert into this
     *                <code>Any</code> object
	 * @exception <code>DATA_CONVERSION</code>
	 * @exception <code>MARSHAL</code>
     */
    abstract public void     insert_string(String s) throws DATA_CONVERSION, MARSHAL;


    /**
     * Extracts the <code>String</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>String</code> object stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>String</code> object or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public String   extract_wstring() throws BAD_OPERATION;

    /**
     * Inserts the given <code>String</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param s		the <code>String</code> object to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_wstring(String s) throws MARSHAL;


    /**
     * Extracts the <code>org.omg.CORBA.Object</code> in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>org.omg.CORBA.Object</code> stored in 
	 *         this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than an 
	 *              <code>org.omg.CORBA.Object</code> or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public org.omg.CORBA.Object extract_Object() throws BAD_OPERATION;

    /**
     * Inserts the given <code>org.omg.CORBA.Object</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param o		the <code>org.omg.CORBA.Object</code> object to insert into this
     *                <code>Any</code> object
     */
    abstract public void     insert_Object(org.omg.CORBA.Object o);

    /**
     * Inserts the given <code>org.omg.CORBA.Object</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param o		the <code>org.omg.CORBA.Object</code> instance to insert into this
     *                <code>Any</code> object
	 * @param t     the <code>TypeCode</code> object that is to be inserted into
	 *              this <code>Any</code> object and that describes
	 *              the <code>Object</code> being inserted
	 *              
     */
    abstract public void     insert_Object(org.omg.CORBA.Object o, 
					   TypeCode t) 
	throws BAD_OPERATION;

    /**
     * Extracts the <code>TypeCode</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>TypeCode</code> object stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>TypeCode</code> object or the
	 *              <code>value</code> field has not yet been set
     */
    abstract public TypeCode extract_TypeCode() throws BAD_OPERATION;

    /**
     * Inserts the given <code>TypeCode</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param t		the <code>TypeCode</code> object to insert into this
     *                <code>Any</code> object
     */
    abstract public void	   insert_TypeCode(TypeCode t);


    /**
    * Extracts the <code>Principal</code> object in this
    * <code>Any</code> object's <code>value</code> field.
    * Note that the class <code>Principal</code> has been deprecated.
    *
    * @return the <code>Principal</code> object stored in this <code>Any</code> object
    * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object
    *              contains something other than a
    *              <code>Principal</code> object or the
    *              <code>value</code> field has not yet been set
    * @deprecated Deprecated by CORBA 2.2.
    */
    abstract public Principal extract_Principal() throws BAD_OPERATION;

    /**
    * Inserts the given <code>Principal</code> object
    * into this <code>Any</code> object's <code>value</code> field.
    * Note that the class <code>Principal</code> has been deprecated.
    *
    * @param p		the <code>Principal</code> object to insert into this
    *                <code>Any</code> object
    * @deprecated Deprecated by CORBA 2.2.
    */
    abstract public void	   insert_Principal(Principal p);


    /**
     * Extracts the <code>java.math.BigDecimal</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>java.math.BigDecimal</code> object 
	 *         stored in this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a
	 *              <code>java.math.BigDecimal</code> object or the
	 *              <code>value</code> field has not yet been set
     */
    public java.math.BigDecimal extract_fixed() {
        throw new org.omg.CORBA.NO_IMPLEMENT();
    }


    /**
     * Inserts the given <code>java.math.BigDecimal</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param value		the <code>java.math.BigDecimal</code> object
	 *                  to insert into this <code>Any</code> object
     */
    public void insert_fixed(java.math.BigDecimal value) {
	throw new org.omg.CORBA.NO_IMPLEMENT();
    }


    /**
     * Inserts the given <code>java.math.BigDecimal</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param value		the <code>java.math.BigDecimal</code> object
	 *                  to insert into this <code>Any</code> object
	 * @param type     the <code>TypeCode</code> object that is to be inserted into
	 *              this <code>Any</code> object's <code>type</code> field
	 *              and that describes the <code>java.math.BigDecimal</code> 
	 *              object being inserted
     */
    public void insert_fixed(java.math.BigDecimal value,
			     org.omg.CORBA.TypeCode type) {
	throw new org.omg.CORBA.NO_IMPLEMENT();
    }

    // Changes due to orbos/98-01-18: Objects-by-Value


    /**
     * Extracts the <code>java.io.Serializable</code> object in this
     * <code>Any</code> object's <code>value</code> field.
     *
     * @return the <code>java.io.Serializable</code> object stored in
	 *         this <code>Any</code> object
	 * @exception <code>BAD_OPERATION</code> if this  <code>Any</code> object 
	 *              contains something other than a <code>java.io.Serializable</code> 
	 *              object or the
	 *              <code>value</code> field has not yet been set
     */
    public java.io.Serializable extract_Value() throws BAD_OPERATION {
	throw new org.omg.CORBA.NO_IMPLEMENT();
    }


    /**
     * Inserts the given <code>java.io.Serializable</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param v		the <code>java.io.Serializable</code> object to insert into this
     *                <code>Any</code> object
     */
    public void insert_Value(java.io.Serializable v) {
	throw new org.omg.CORBA.NO_IMPLEMENT();
    }


    /**
     * Inserts the given <code>java.io.Serializable</code> object
     * into this <code>Any</code> object's <code>value</code> field.
     *
     * @param v		the <code>java.io.Serializable</code> object to insert into this
     *                <code>Any</code> object
	 * @param t     the <code>TypeCode</code> object that is to be inserted into
	 *              this <code>Any</code> object's <code>type</code> field
	 *              and that describes the <code>java.io.Serializable</code> 
	 *              object being inserted
     */
    public void insert_Value(java.io.Serializable v, TypeCode t)
	throws MARSHAL
    {
	throw new org.omg.CORBA.NO_IMPLEMENT();
    }
}
