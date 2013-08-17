/*
 * @(#)KeyboardManager.java	1.7 99/04/22
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
package javax.swing;


import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.beans.*;
import javax.swing.event.*;

/**
  * The KeyboardManager class is used to help dispatch keyboard actions for the
  * WHEN_IN_FOCUSED_WINDOW style actions.  Actions with other conditions are handled
  * directly in JComponent.
  *
  * Here's a description of the symantics of how keyboard dispatching should work
  * atleast as I understand it.
  *
  * KeyEvents are dispatched to the focused component.  The focus manager gets first
  * crack at processing this event.  If the focus manager doesn't want it, then
  * the JComponent calls super.processKeyEvent() this allows listeners a chance
  * to process the event.
  *
  * If none of the listeners "consumes" the event then the keybindings get a shot.
  * This is where things start to get interesting.  First, KeyStokes defined with the
  * WHEN_FOCUSED condition get a chance.  If none of these want the event, then the component
  * walks though it's parents looked for actions of type WHEN_ANCESTOR_OF_FOCUSED_COMPONENT.
  *
  * If no one has taken it yet, then it winds up here.  We then look for components registered
  * for WHEN_IN_FOCUSED_WINDOW events and fire to them.  Note that if none of those are found
  * then we pass the event to the menubars and let them have a crack at it.  They're handled differently.
  *
  * Lastly, we check if we're looking at an internal frame.  If we are and no one wanted the event
  * then we move up to the InternalFrame's creator and see if anyone wants the event (and so on and so on).
  * 
  *
  * @see JComponent#registerKeyboardAction
  */
class KeyboardManager {

    static KeyboardManager currentManager = new KeyboardManager();

    /**
      * maps top-level containers to a sub-hashtable full of keystrokes
      */
    Hashtable containerMap = new Hashtable();

    /**
      * Maps component/keystroke pairs to a topLevel container
      * This is mainly used for fast unregister operations
      */
    Hashtable componentKeyStrokeMap = new Hashtable();

    public static KeyboardManager getCurrentManager() {
        return currentManager;
    }

    public static void setCurrentManager(KeyboardManager km) {
        currentManager = km;
    }

    /**
      * register keystrokes here which are for the WHEN_IN_FOCUSED_WINDOW
      * case.
      * Other types of keystrokes will be handled by walking the heirarchy
      * That simplifies some potentially hairy stuff.
      */
     public void registerKeyStroke(KeyStroke k, JComponent c) {
         Container topContainer = getTopAncestor(c);
	 if (topContainer == null) {
	     return;
	 }
	 Hashtable keyMap = (Hashtable)containerMap.get(topContainer);

	 if (keyMap ==  null) {  // lazy evaluate one
	     keyMap = registerNewTopContainer(topContainer);
	 }

	 Object tmp = keyMap.get(k);
	 if (tmp == null) {
	     keyMap.put(k,c);
	 } else if (tmp instanceof Vector) {  // if there's a Vector there then add to it.
	     Vector v = (Vector)tmp;
	     if (!v.contains(c)) {  // only add if this keystroke isn't registered for this component
	         v.addElement(c);
	     }
	 } else if (tmp instanceof JComponent) {  
	   // if a JComponent is there then remove it and replace it with a vector
	   // Then add the old compoennt and the new compoent to the vector
	   // then insert the vector in the table
	   if (tmp != c) {  // this means this is already registered for this component, no need to dup
	       Vector v = new Vector();
	       v.addElement(tmp);
	       v.addElement(c);
	       keyMap.put(k, v);
	   } 
	 } else {
	     System.out.println("Unexpected condition in registerKeyStroke");
	     Thread.dumpStack();
	 }
	 
	 componentKeyStrokeMap.put(new ComponentKeyStrokePair(c,k), topContainer);

     }

     /**
       * find the top Window or Applet
       */
     private static Container getTopAncestor(JComponent c) {
        for(Container p = c.getParent(); p != null; p = p.getParent()) {
            if (p instanceof Window || p instanceof Applet || p instanceof JInternalFrame) {
                return p;
	    }
        }
        return null;
     }

     public void unregisterKeyStroke(KeyStroke ks, JComponent c) {

       // component may have already been removed from the heirarchy, we
       // need to look up the container using the componentKeyStrokeMap.

         ComponentKeyStrokePair ckp = new ComponentKeyStrokePair(c,ks);
	 
	 Object topContainer = componentKeyStrokeMap.get(ckp);

	 if (topContainer == null) {  // never heard of this pairing, so bail
	     return;
	 }

	 Hashtable keyMap = (Hashtable)containerMap.get(topContainer);
	 if  (keyMap == null) { // this should never happen, but I'm being safe
	     Thread.dumpStack();
	     return;
	 }

	 Object tmp = keyMap.get(ks);
	 if (tmp == null) {  // this should never happen, but I'm being safe
	     Thread.dumpStack();
	     return;
	 }

	 if (tmp instanceof JComponent && tmp == c) {
	     keyMap.remove(ks);  // remove the KeyStroke from the Map
	     //System.out.println("removed a stroke" + ks);
	 } else if (tmp instanceof Vector ) {  // this means there is more than one component reg for this key
	     Vector v = (Vector)tmp;
	     v.removeElement(c);
	     if ( v.isEmpty() ) {
	         keyMap.remove(ks);  // remove the KeyStroke from the Map
		 //System.out.println("removed a ks vector");
	     }
	 }
	 
	 if ( keyMap.isEmpty() ) {  // if no more bindings in this table
	     containerMap.remove(topContainer);  // remove table to enable GC
	     //System.out.println("removed a container");
	 }

	 componentKeyStrokeMap.remove(ckp);
     }

    /**
      * This method is called when the focused component (and none of
      * its ancestors) want the key event.  This will look up the keystroke
      * to see if any chidren (or subchildren) of the specified container
      * want a crack at the event.
      * If one of them wants it, then it will "DO-THE-RIGHT-THING"
      */
    public boolean fireKeyboardAction(KeyEvent e, boolean pressed, Container topAncestor) {

         if (e.isConsumed()) {
	      System.out.println("Aquired pre-used event!");
	      Thread.dumpStack();
         }

         KeyStroke ks;


	 if(e.getID() == KeyEvent.KEY_TYPED) {
               ks=KeyStroke.getKeyStroke(e.getKeyChar());
         } else {
               ks=KeyStroke.getKeyStroke(e.getKeyCode(), e.getModifiers(), !pressed);
	 }

	 Hashtable keyMap = (Hashtable)containerMap.get(topAncestor);
	 if (keyMap != null) { // this container isn't registered, so bail

	     Object tmp = keyMap.get(ks);

	     if (tmp == null) {
	       // don't do anything
	     } else if ( tmp instanceof JComponent) {
	         JComponent c = (JComponent)tmp;
		 if ( c.isShowing() && c.isEnabled() ) { // only give it out if enabled and visible
		     fireBinding(c, ks, e);
		 }
	     } else if ( tmp instanceof Vector) { //more than one comp registered for this
	         Vector v = (Vector)tmp;
		 Enumeration iter = v.elements();
		 while (iter.hasMoreElements()) {
		     JComponent c = (JComponent)iter.nextElement();
		     //System.out.println("Trying collision: " + c + " vector = "+ v.size());
		     if ( c.isShowing() && c.isEnabled() ) { // don't want to give these out
		         fireBinding(c, ks, e);
			 if (e.isConsumed())
			     return true;
		     }
		 }
	     } else  {
	         System.out.println( "Unexpected condition in fireKeyboardAction " + tmp);
		 // This means that tmp wasn't null, a JComponent, or a Vector.  What is it?
		 Thread.dumpStack();
	     }
	 }

	 if (e.isConsumed()) {
	     return true;
	 }
	 // if no one else handled it, then give the menus a crack
	 // The're handled differently.  The key is to let any JMenuBars 
	 // process the event
	 if ( keyMap != null) {     
	     Vector v = (Vector)keyMap.get(JMenuBar.class);
	     if (v != null) {
	         Enumeration iter = v.elements();
		 while (iter.hasMoreElements()) {
		     JMenuBar mb = (JMenuBar)iter.nextElement();
		     if ( mb.isShowing() && mb.isEnabled() ) { // don't want to give these out
		         fireBinding(mb, ks, e);
			 if (e.isConsumed()) {
			     return true;
			 }
		     }
		 }
	     }
	 }

	 // if no one has handled it yet, and the container we're working in is
	 // an internal frame, then move on up to it's top container.
	 if (topAncestor instanceof JInternalFrame) {
	     Container newTopContainer = getTopAncestor((JInternalFrame)topAncestor);
	     if (newTopContainer == null) {  
	       return false; // this case can occur in rare cases where a key action removes
	                     // the internal frame from the heirarchy.
	     }
	     fireKeyboardAction( e, pressed, newTopContainer );
	 }
	
	 return e.isConsumed();
    }

    void fireBinding(JComponent c, KeyStroke ks, KeyEvent e) {
      //System.out.println("Firing on: " + c);
	 JComponent.KeyboardBinding binding = c.bindingForKeyStroke(ks, JComponent.WHEN_IN_FOCUSED_WINDOW);
	 if(binding != null) {  // this block of code stolen from JComponent.processKeyBinding
	     ActionListener listener = binding.getAction();
	     if(listener != null) {
	       
	         if (listener instanceof Action && ((Action)listener).isEnabled() == false) {
		   // this case handles when we try to dispatch to a disbled action
		   // instead of sending the event we return, thus giving a chance to
		   // other components registered for this stroke.
		     return;
		 }
	         listener.actionPerformed(new ActionEvent(c,ActionEvent.ACTION_PERFORMED,binding.getCommand()));
		 e.consume();
	     }
	 } else {
	   // System.out.println("Binding NULL");
	 }
    }

    public void registerMenuBar(JMenuBar mb) {
        Container top = getTopAncestor(mb);
	Hashtable keyMap = (Hashtable)containerMap.get(top);

	if (keyMap ==  null) {  // lazy evaluate one
	     keyMap = registerNewTopContainer(top);
	}
	// use the menubar class as the key
	Vector menuBars = (Vector)keyMap.get(JMenuBar.class); 

	if (menuBars == null) {  // if we don't have a list of menubars, 
	                         // then make one.
	    menuBars = new Vector();
	    keyMap.put(JMenuBar.class, menuBars);
	}

	if (!menuBars.contains(mb)) {
	    menuBars.addElement(mb);
	}
    }


    public void unregisterMenuBar(JMenuBar mb) { 
	Object topContainer = getTopAncestor(mb);
	Hashtable keyMap = (Hashtable)containerMap.get(topContainer);
	if (keyMap!=null) {
	    Vector v = (Vector)keyMap.get(JMenuBar.class);
	    if (v != null) {
		v.removeElement(mb);
		if (v.isEmpty()) {
		    keyMap.remove(JMenuBar.class);
		    if (keyMap.isEmpty()) {
			// remove table to enable GC
			containerMap.remove(topContainer);  
		    }
		} 
	    }
	}
    }
    protected Hashtable registerNewTopContainer(Container topContainer) {
	     Hashtable keyMap = new Hashtable();
	     containerMap.put(topContainer, keyMap);
	     return keyMap;
    }

    /**
      * This class is used to create keys for a hashtable
      * which looks up topContainers based on component, keystroke pairs
      * This is used to make unregistering KeyStrokes fast
      */
    class ComponentKeyStrokePair {
        Object component;
        Object keyStroke;
        
        public ComponentKeyStrokePair(Object comp, Object key) {
	    component = comp;
	    keyStroke = key;
	}

        public boolean equals(Object o) {
	    if ( !(o instanceof ComponentKeyStrokePair)) {
	        return false;
	    }
	    ComponentKeyStrokePair ckp = (ComponentKeyStrokePair)o;
	    return ((component.equals(ckp.component)) && (keyStroke.equals(ckp.keyStroke)));
	}

        public int hashCode() {
	    return component.hashCode() * keyStroke.hashCode();
	}

    }

} // end KeyboardManager
