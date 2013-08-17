/*
 * @(#)NotFoundHelper.java	1.5 98/09/21
 *
 * Copyright 1997, 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */
/*
 * File: ./org/omg/CosNaming/NamingContextPackage/NotFoundHelper.java
 * From: nameservice.idl
 * Date: Tue Aug 11 03:12:09 1998
 *   By: idltojava Java IDL 1.2 Aug 11 1998 02:00:18
 */

package org.omg.CosNaming.NamingContextPackage;
public class NotFoundHelper {
     // It is useless to have instances of this class
     private NotFoundHelper() { }

    public static void write(org.omg.CORBA.portable.OutputStream out, org.omg.CosNaming.NamingContextPackage.NotFound that) {
    out.write_string(id());

	org.omg.CosNaming.NamingContextPackage.NotFoundReasonHelper.write(out, that.why);
	{
	    out.write_long(that.rest_of_name.length);
	    for (int __index = 0 ; __index < that.rest_of_name.length ; __index += 1) {
	        org.omg.CosNaming.NameComponentHelper.write(out, that.rest_of_name[__index]);
	    }
	}
    }
    public static org.omg.CosNaming.NamingContextPackage.NotFound read(org.omg.CORBA.portable.InputStream in) {
        org.omg.CosNaming.NamingContextPackage.NotFound that = new org.omg.CosNaming.NamingContextPackage.NotFound();
         // read and discard the repository id
        in.read_string();

	that.why = org.omg.CosNaming.NamingContextPackage.NotFoundReasonHelper.read(in);
	{
	    int __length = in.read_long();
	    that.rest_of_name = new org.omg.CosNaming.NameComponent[__length];
	    for (int __index = 0 ; __index < that.rest_of_name.length ; __index += 1) {
	        that.rest_of_name[__index] = org.omg.CosNaming.NameComponentHelper.read(in);
	    }
	}
    return that;
    }
   public static org.omg.CosNaming.NamingContextPackage.NotFound extract(org.omg.CORBA.Any a) {
     org.omg.CORBA.portable.InputStream in = a.create_input_stream();
     return read(in);
   }
   public static void insert(org.omg.CORBA.Any a, org.omg.CosNaming.NamingContextPackage.NotFound that) {
     org.omg.CORBA.portable.OutputStream out = a.create_output_stream();
     write(out, that);
     a.read_value(out.create_input_stream(), type());
   }
   private static org.omg.CORBA.TypeCode _tc;
   synchronized public static org.omg.CORBA.TypeCode type() {
       int _memberCount = 2;
       org.omg.CORBA.StructMember[] _members = null;
          if (_tc == null) {
               _members= new org.omg.CORBA.StructMember[2];
               _members[0] = new org.omg.CORBA.StructMember(
                 "why",
                 org.omg.CosNaming.NamingContextPackage.NotFoundReasonHelper.type(),
                 null);

               _members[1] = new org.omg.CORBA.StructMember(
                 "rest_of_name",
                 org.omg.CORBA.ORB.init().create_sequence_tc(0, org.omg.CosNaming.NameComponentHelper.type()),
                 null);
             _tc = org.omg.CORBA.ORB.init().create_exception_tc(id(), "NotFound", _members);
          }
      return _tc;
   }
   public static String id() {
       return "IDL:omg.org/CosNaming/NamingContext/NotFound:1.0";
   }
}
