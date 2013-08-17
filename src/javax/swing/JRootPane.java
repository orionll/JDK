/*
 * @(#)JRootPane.java	1.47 99/04/22
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

import java.awt.*;
import java.awt.event.*;
import javax.accessibility.*;
import java.util.Vector;
import java.io.Serializable;


/** 
 * The fundamental component in the container hierarchy. In the same way that
 * JComponent is fundamental to the JFC/Swing components, JRootPane is fundamental
 * to the JFC/Swing window, frame, and pane containers.
 * However, while other classes use inheritance to take advantage of JComponent's
 * capabilities, the container classes delegate operations to a JRootPane instance.
 * Those containers are the heavyweight containers: JFrame, JDialog, JWindow, and 
 * JApplet, as well as the lightweight container, JInternalFrame.
 * <p>
 * The following image shows these relationships:
 * <p align=center><img src="doc-files/JRootPane-1.gif" HEIGHT=484 WIDTH=629></p>
 * The &quot;heavyweight&quot; components (those that delegate to a peer, or native
 * component on the host system) are shown with a darker, heavier box. The four
 * heavyweight JFC/Swing containers (JFrame, JDialog, JWindow, and JApplet) are 
 * shown in relation to the AWT classes they extend. These four components are the
 * only heavyweight containers in the Swing library. The lightweight container, 
 * JInternalPane, is also shown. All 5 of these JFC/Swing containers implement the
 * RootPaneContainer interface, and they all delegate their operations to a 
 * JRootPane (shown with a little "handle" on top).
 * <blockquote>
 * <b>Note:</b> The JComponent method <code>getRootPane</code> can be used to
 * obtain the JRootPane that contains a given component.  
 * </blockquote>
 * <table align="right" border="0">
 * <tr>
 * <td align="center">
 * <img src="doc-files/JRootPane-2.gif" HEIGHT=386 WIDTH=349>
 * </td>
 * </tr>
 * </table>
 * The diagram at right shows the structure of a JRootPane.
 * A JRootpane is made up of a glassPane, an optional menuBar, and
 * a contentPane. (The JLayeredPane manages the menuBar and the contentPane.)
 * The glassPane sits over the top of everything, where it is in a position
 * to intercept mouse movements. Since the glassPane (like the contentPane)
 * can be an arbitrary component, it is also possible to set up the 
 * glassPane for drawing. Lines and images on the glassPane can then range
 * over the frames underneath without being limited by their boundaries. 
 * <p>
 * Although the menuBar component is optional, the layeredPane, contentPane,
 * and glassPane always exist. Attempting to set them to null generates an
 * exception. 
 * <p>
 * The <code>contentPane</code> must be the parent of any children of 
 * the JRootPane. Rather than adding directly to a JRootPane, like this:
 * <PRE>
 *       rootPane.add(child);
 * </PRE>
 * You instead add to the contentPane of the JRootPane, like this:
 * <PRE>
 *       rootPane.getContentPane().add(child);
 * </PRE>
 * The same priniciple holds true for setting layout managers, removing 
 * components, listing children, etc. All these methods are invoked on  
 * the <code>contentPane</code> instead of on the JRootPane.
 * <blockquote>
 * <b>Note:</b> The default layout manager for the <code>contentPane</code> is
 *  a BorderLayout manager. However, the JRootPane uses a custom LayoutManager.
 *  So, when you want to change the layout manager for the components you added
 *  to a JRootPane, be sure to use code like this:<PRE>
 *    rootPane.getContentPane().setLayout(new BoxLayout());
 * </PRE></blockquote>
 * If a JMenuBar component is set on the JRootPane, it is positioned 
 * along the upper edge of the frame. The <code>contentPane</code> is
 * adjusted in location and size to fill the remaining area. 
 * (The JMenuBar and the <code>contentPane</code> are added to the 
 * <code>layeredPane</code> component at the JLayeredPane.FRAME_CONTENT_LAYER 
 * layer.) 
 * <p>
 * The <code>layeredPane</code> is the parent of all children in the JRootPane.
 * It is an instance of JLayeredPane, which provides the ability to add components 
 * at several layers. This capability is very useful when working with menu popups,
 * dialog boxes, and dragging -- situations in which you need to place a component
 * on top of all other components in the pane.
 * <p>
 * The <code>glassPane</code> sits on top of all other components in the JRootPane.
 * That provides a convenient place to draw above all other components, and makes
 * it possible to intercept mouse events, which is useful both for dragging and
 * for drawing. Developers can use <code>setVisible</code> on the glassPane
 * to control when the <code>glassPane</code> displays over the other children. 
 * By default the <code>glassPane</code> is not visible. 
 * <p>
 * The custom LayoutManager used by JRootPane ensures that:
 * <OL>
 * <LI>The <code>glassPane</code>, if present, fills the entire viewable
 *     area of the JRootPane (bounds - insets).
 * <LI>The <code>layeredPane</code> fills the entire viewable area of the
 *     JRootPane. (bounds - insets)
 * <LI>The <code>menuBar</code> is positioned at the upper edge of the 
 *     layeredPane().
 * <LI>The <code>contentPane</code> fills the entire viewable area, 
 *     minus the MenuBar, if present.
 * </OL>
 * Any other views in the JRootPane view hierarchy are ignored.
 * <p>
 * If you replace the LayoutManager of the JRootPane, you are responsible for 
 * managing all of these views. So ordinarily you will want to be sure that you
 * change the layout manager for the <code>contentPane</code> rather than 
 * for the JRootPane itself!
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @see JLayeredPane
 * @see JMenuBar
 * @see JWindow
 * @see JFrame
 * @see JDialog
 * @see JApplet
 * @see JInternalFrame
 * @see JComponent
 * @see BoxLayout
 *
 * @see <a href="http://java.sun.com/products/jfc/swingdoc-archive/mixing.html">
 * Mixing Heavy and Light Components</a>
 *
 * @version 1.47 04/22/99
 * @author David Kloba
 */
/// PENDING(klobad) Who should be opaque in this component?
public class JRootPane extends JComponent implements Accessible {

    /** The menu bar. */
    protected JMenuBar menuBar;

    /** The content pane. */
    protected Container contentPane;

    /** The layered pane that manages the menu bar and content pane. */
    protected JLayeredPane layeredPane;

    /**
     * The glass pane that overlays the menu bar and content pane,
     *  so it can intercept mouse movements and such.
     */
    protected Component glassPane;
    /** 
     * The button that gets activated when the pane has the focus and
     * a UI-specific action like pressing the Enter key occurs.
     */
    protected JButton defaultButton;
    /** The action to take when the defaultButton is pressed.
     *  @see #defaultButton
     */ 
    protected DefaultAction defaultPressAction;   
    /** The action to take when the defaultButton is released.
     *  @see #defaultButton
     */ 
    protected DefaultAction defaultReleaseAction;

    /** 
     * Create a JRootPane, setting up its glassPane, LayeredPane, and contentPane.
     */
    public JRootPane() {
        setGlassPane(createGlassPane());
        setLayeredPane(createLayeredPane());
        setContentPane(createContentPane());
        setLayout(createRootLayout());
        setDoubleBuffered(true);
    }

    /** Called by the constructor methods to create the default layeredPane. 
      * Bt default it creates a new JLayeredPane. 
      */
    protected JLayeredPane createLayeredPane() {
        JLayeredPane p = new JLayeredPane();
        p.setName(this.getName()+".layeredPane");
        return p;
    }

    /** Called by the constructor methods to create the default contentPane. 
     * By default this method creates a new JComponent add sets a 
     * BorderLayout as its LayoutManager.
     */
    protected Container createContentPane() {
        JComponent c = new JPanel();
        c.setName(this.getName()+".contentPane");
        c.setLayout(new BorderLayout() {
            /* This BorderLayout subclass maps a null constraint to CENTER.
             * Although the reference BorderLayout also does this, some VMs
             * throw an IllegalArgumentException.
             */
            public void addLayoutComponent(Component comp, Object constraints) {
                if (constraints == null) {
                    constraints = BorderLayout.CENTER;
                }
                super.addLayoutComponent(comp, constraints);
            }
        });
        return c;
    }

    /** Called by the constructor methods to create the default glassPane. 
      * By default this method creates a new JComponent with visibility 
      * set to false.
      */
    protected Component createGlassPane() {
        JComponent c = new JPanel();
        c.setName(this.getName()+".glassPane");
        c.setVisible(false);
        ((JPanel)c).setOpaque(false);
        return c;
    }

    /** Called by the constructor methods to create the default layoutManager. */
    protected LayoutManager createRootLayout() {
        return new RootLayout();
    } 

    /** 
     * Adds or changes the menu bar used in the layered pane. 
     * @param menu the JMenuBar to add
     */
    public void setJMenuBar(JMenuBar menu) {
        if(menuBar != null && menuBar.getParent() == layeredPane)
            layeredPane.remove(menuBar);
        menuBar = menu;

        if(menuBar != null)
            layeredPane.add(menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
    }

    /**
     * Specifies the menu bar value.
     * @deprecated As of Swing version 1.0.3
     *  replaced by <code>setJMenuBar(JMenuBar menu)</code>.
     */
    public void setMenuBar(JMenuBar menu){
        if(menuBar != null && menuBar.getParent() == layeredPane)
            layeredPane.remove(menuBar);
        menuBar = menu;

        if(menuBar != null)
            layeredPane.add(menuBar, JLayeredPane.FRAME_CONTENT_LAYER);
    }
    
    /** 
     * Returns the menu bar from the layered pane. 
     * @return the JMenuBar used in the pane
     */
    public JMenuBar getJMenuBar() { return menuBar; }

    /**
     * Returns the menu bar value.
     * @deprecated As of Swing version 1.0.3
     *  replaced by <code>getJMenubar()</code>.
     */
    public JMenuBar getMenuBar() { return menuBar; }

    /** 
     * Sets the content pane -- the container that holds the components
     * parented by the root pane.
     *  
     * @param content the Container to use for component-contents
     * @exception java.awt.IllegalComponentStateException (a runtime
     *            exception) if the content pane parameter is null
     */
    public void setContentPane(Container content) {
        if(content == null)
            throw new IllegalComponentStateException("contentPane cannot be set to null.");
        if(contentPane != null && contentPane.getParent() == layeredPane)
            layeredPane.remove(contentPane);
        contentPane = content;

        layeredPane.add(contentPane, JLayeredPane.FRAME_CONTENT_LAYER);
    }

    /** 
     * Returns the content pane -- the container that holds the components
     * parented by the root pane.
     *  
     * @return the Container that holds the component-contents
     */
    public Container getContentPane() { return contentPane; }

// PENDING(klobad) Should this reparent the contentPane and MenuBar?
    /**
     * Set the layered pane for the root pane. The layered pane
     * typically holds a content pane and an optional JMenuBar.
     *
     * @param layered  the JLayeredPane to use.
     * @exception java.awt.IllegalComponentStateException (a runtime
     *            exception) if the layered pane parameter is null
     */
    public void setLayeredPane(JLayeredPane layered) {
        if(layered == null)
            throw new IllegalComponentStateException("layeredPane cannot be set to null.");
        if(layeredPane != null && layeredPane.getParent() == this)
            this.remove(layeredPane);
        layeredPane = layered;

        this.add(layeredPane, -1);
    }
    /**
     * Get the layered pane used by the root pane. The layered pane
     * typically holds a content pane and an optional JMenuBar.
     *
     * @return the JLayeredPane currently in use
     */
    public JLayeredPane getLayeredPane() { return layeredPane; }

    /**
     * Sets a specified Component to be the glass pane for this
     * root pane.  The glass pane should normally be a lightweight,
     * transparent component, because it will be made visible when
     * ever the root pane needs to grab input events.  For example,
     * only one JInternalFrame is ever active when using a
     * DefaultDesktop, and any inactive JInternalFrames' glass panes
     * are made visible so that clicking anywhere within an inactive
     * JInternalFrame can activate it.
     * @param glass the Component to use as the glass pane for this
     *              JRootPane.
     */
    public void setGlassPane(Component glass) {
        if (glass == null) {
            throw new NullPointerException("glassPane cannot be set to null.");
        }

        boolean visible = false;
        if (glassPane != null && glassPane.getParent() == this) {
            this.remove(glassPane);
            visible = glassPane.isVisible();
        }

        glass.setVisible(visible);
        glassPane = glass;
        this.add(glassPane, 0);
        if (visible) {
            repaint();
        }
    }

    /**
     * Returns the current glass pane for this JRootPane.
     * @return the current glass pane.
     * @see #setGlassPane
     */
    public Component getGlassPane() { 
        return glassPane; 
    }

    /**
     * Make JRootPane be the root of a focus cycle.
     * That means that, by default, tabbing within the root
     * pane will move between components of the pane,
     * but not out of the pane.

     * @see JComponent#isFocusCycleRoot
     * @return true
     */
    public boolean isFocusCycleRoot() {
        return true;
    }

    /**
     * If a descendant of this JRootPane calls revalidate, validate
     * from here on down.
     *<p>
     * Deferred requests to relayout a component and it's descendants,
     * i.e. calls to revalidate(), are pushed upwards to either a JRootPane 
     * or a JScrollPane because both classes override isValidateRoot() to 
     * return true.
     * 
     * @see JComponent#isValidateRoot
     * @return true
     */
    public boolean isValidateRoot() {
	return true;
    }

    /**
     * Register ourselves with the SystemEventQueueUtils as a new
     * root pane. 
     */
    public void addNotify() {
	SystemEventQueueUtilities.addRunnableCanvas(this);
        super.addNotify();
        enableEvents(AWTEvent.KEY_EVENT_MASK);
    }

    // Note: These links don't work because the target
    //       class is package private
    // @see SystemEventQueueUtilities#addRunnableCanvas
    // @see SystemEventQueueUtilities#removeRunnableCanvas

    /**
     * Unregister ourselves from SystemEventQueueUtils.
     */
    public void removeNotify() {
	SystemEventQueueUtilities.removeRunnableCanvas(this);
        super.removeNotify();
    }


    /**
     * Sets the current default button for this JRootPane.
     * The default button is the button which will be activated 
     * when a UI-defined activation event (typically the Enter key) 
     * occurs in the RootPane regardless of whether or not the button 
     * has keyboard focus (unless there is another component within 
     * the RootPane which consumes the activation event, such as a JTextPane).
     * For default activation to work, the button must be an enabled
     * descendent of the RootPane when activation occurs.
     * To remove a default button from this RootPane, set this
     * property to null.
     *
     * @see JButton#isDefaultButton 
     * @param default the JButton which is to be the default button
     */
    public void setDefaultButton(JButton defaultButton) { 
        JButton oldDefault = this.defaultButton;

        if (oldDefault != defaultButton) {
            this.defaultButton = defaultButton;

            if (defaultButton != null) {
                if (defaultPressAction == null) {
                    defaultPressAction = new DefaultAction(this, true);
                    defaultReleaseAction = new DefaultAction(this, false);
                   // Eventually we should get the KeyStroke from the UI
                   // but hardcode it for now....
                    registerKeyboardAction(defaultPressAction, 
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false), 
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(defaultReleaseAction, 
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true), 
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(defaultPressAction, 
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 
                                                          InputEvent.CTRL_MASK, false), 
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);

                    registerKeyboardAction(defaultReleaseAction, 
                                   KeyStroke.getKeyStroke(KeyEvent.VK_ENTER,            
                                                          InputEvent.CTRL_MASK, true), 
                                   JComponent.WHEN_IN_FOCUSED_WINDOW);
                }
                defaultPressAction.setOwner(defaultButton);
                defaultReleaseAction.setOwner(defaultButton);
            } else {
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, false));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0, true));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 
                                                                InputEvent.CTRL_MASK, false));
                unregisterKeyboardAction(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 
                                                                InputEvent.CTRL_MASK, true));
                defaultPressAction = null;
                defaultReleaseAction = null;
            }

            if (oldDefault != null) {
                oldDefault.repaint();
            }
            if (defaultButton != null) {
                defaultButton.repaint();
            } 
        }

        firePropertyChange("defaultButton", oldDefault, defaultButton);        
    }

    /**
     * Returns the current default button for this JRootPane.
     * @return the JButton which is currently the default button
     */
    public JButton getDefaultButton() { 
        return defaultButton;
    }

    static class DefaultAction extends AbstractAction {
        JButton owner;
        JRootPane root;
        boolean press;
        DefaultAction(JRootPane root, boolean press) {
            this.root = root;
            this.press = press;
        }
        public void setOwner(JButton owner) {
            this.owner = owner;
        }
        public void actionPerformed(ActionEvent e) {
            if (owner != null && SwingUtilities.getRootPane(owner) == root) {
                ButtonModel model = owner.getModel();
                if (press) {
                    model.setArmed(true);
                    model.setPressed(true);
                } else {
                    model.setPressed(false);
                }
            }
        }
        public boolean isEnabled() {
            return owner.getModel().isEnabled();
        }
    }


    /** Overridden to enforce the position of the glass component as the zero child. */
    protected void addImpl(Component comp, Object constraints, int index) {
        super.addImpl(comp, constraints, index);
        
        /// We are making sure the glassPane is on top. 
        if(glassPane != null 
            && glassPane.getParent() == this
            && getComponent(0) != glassPane) {
            add(glassPane, 0);
        }
    }

    /**
     * Locates the visible child component that contains the specified
     * position.  The top-most child component is returned in the case 
     * where there is overlap in the components.  If the containing child 
     * component is a Container, this method will continue searching for 
     * the deepest nested child component.  Components which are not
     * visible are ignored during the search.<p>
     *
     * This method is similar to Container.findComponentAt, except that
     * the JRootPane.glassPane is ignored during the search.  This
     * is needed to to support drag&drop.  Since the glassPane is the
     * same size as the JRootPane instance, if there's a need to
     * test whether the mouse is over the glassPane, just test that it's
     * over the JRootPane instead.
     *
     * @param x the <i>x</i> coordinate
     * @param y the <i>y</i. coordinate
     * @return null if the component does not contain the position.
     * If there is no child component at the requested point and the 
     * point is within the bounds of this instance, then this 
     * JRootPane itself is returned.
     * @see Container.findComponentAt
     * @see Container.getComponentAt
     */

    public Component findComponentAt(int x, int y) {
	if (!contains(x, y)) {
	    return null;
	}
        int ncomponents = countComponents();
        Component component[] = getComponents();
        for (int i = 0 ; i < ncomponents ; i++) {
            Component comp = component[i];
            if (comp != null) {
		if (comp == glassPane) {  // don't use equals()
		    continue;
                } else if (comp instanceof Container) {
                    comp = ((Container)comp).findComponentAt(x - comp.getX(),
							     y - comp.getY());
                } else {
                    comp = comp.locate(x - comp.getX(), y - comp.getY());
                }
                if (comp != null && comp.isVisible()) {
                    return comp;
                }
            }
        }
	return this;
    }
 

///////////////////////////////////////////////////////////////////////////////
//// Begin Inner Classes
///////////////////////////////////////////////////////////////////////////////


    /** 
     * A custom layout manager that is responsible for the layout of 
     * layeredPane, glassPane, and menuBar.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     */
    protected class RootLayout implements LayoutManager2, Serializable
    {
        /**
         * Returns the amount of space the layout would like to have.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's preferred size
         */ 
        public Dimension preferredLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();
        
            if(contentPane != null) {
                rd = contentPane.getPreferredSize();
            } else {
                rd = parent.getSize();
            }
            if(menuBar != null) {
                mbd = menuBar.getPreferredSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right, 
                                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the minimum amount of space the layout needs.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's minimum size
         */ 
        public Dimension minimumLayoutSize(Container parent) {
            Dimension rd, mbd;
            Insets i = getInsets();
            if(contentPane != null) {
                rd = contentPane.getMinimumSize();
            } else {
                rd = parent.getSize();
            }
            if(menuBar != null) {
                mbd = menuBar.getMinimumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            return new Dimension(Math.max(rd.width, mbd.width) + i.left + i.right, 
                        rd.height + mbd.height + i.top + i.bottom);
        }

        /**
         * Returns the maximum amount of space the layout can use.
         *
         * @param the Container for which this layout manager is being used
         * @return a Dimension object containing the layout's maximum size
         */ 
        public Dimension maximumLayoutSize(Container target) {
            Dimension rd, mbd;
            Insets i = getInsets();
            if(menuBar != null) {
                mbd = menuBar.getMaximumSize();
            } else {
                mbd = new Dimension(0, 0);
            }
            if(contentPane != null) {
                rd = contentPane.getMaximumSize();
            } else {
                // This is silly, but should stop an overflow error
                rd = new Dimension(Integer.MAX_VALUE, 
                        Integer.MAX_VALUE - i.top - i.bottom - mbd.height - 1);
            }
            return new Dimension(Math.min(rd.width, mbd.width) + i.left + i.right,
                                         rd.height + mbd.height + i.top + i.bottom);
        }
        
        /**
         * Instructs the layout manager to perform the layout for the specified
         * container.
         *
         * @param the Container for which this layout manager is being used
         */ 
        public void layoutContainer(Container parent) {
            Rectangle b = parent.getBounds();
            Insets i = getInsets();
            int contentY = 0;
            int w = b.width - i.right - i.left;
            int h = b.height - i.top - i.bottom;
        
            if(layeredPane != null) {
                layeredPane.setBounds(i.left, i.top, w, h);
            }
            if(glassPane != null) {
                glassPane.setBounds(i.left, i.top, w, h);
            }
            // Note: This is laying out the children in the layeredPane,
            // technically, these are not our chilren.
            if(menuBar != null) {
                Dimension mbd = menuBar.getPreferredSize();
                menuBar.setBounds(0, 0, w, mbd.height);
                contentY += mbd.height;
            }
            if(contentPane != null) {
                contentPane.setBounds(0, contentY, w, h - contentY);
            }
        }
        
        public void addLayoutComponent(String name, Component comp) {}
        public void removeLayoutComponent(Component comp) {}
        public void addLayoutComponent(Component comp, Object constraints) {}
        public float getLayoutAlignmentX(Container target) { return 0.0f; }
        public float getLayoutAlignmentY(Container target) { return 0.0f; }
        public void invalidateLayout(Container target) {}
    }


    /**
     * Returns a string representation of this JRootPane. This method 
     * is intended to be used only for debugging purposes, and the 
     * content and format of the returned string may vary between      
     * implementations. The returned string may be empty but may not 
     * be <code>null</code>.
     * 
     * @return  a string representation of this JRootPane.
     */
    protected String paramString() {
	return super.paramString();
    }

/////////////////
// Accessibility support
////////////////

    /**
     * Get the AccessibleContext associated with this JComponent
     *
     * @return the AccessibleContext of this JComponent
     */
    public AccessibleContext getAccessibleContext() {
        if (accessibleContext == null) {
            accessibleContext = new AccessibleJRootPane();
        }
        return accessibleContext;
    }

    /**
     * The class used to obtain the accessible role for this object.
     * <p>
     * <strong>Warning:</strong>
     * Serialized objects of this class will not be compatible with
     * future Swing releases.  The current serialization support is appropriate
     * for short term storage or RMI between applications running the same
     * version of Swing.  A future release of Swing will provide support for
     * long term persistence.
     */
    protected class AccessibleJRootPane extends AccessibleJComponent {
        /**
         * Get the role of this object.
         *
         * @return an instance of AccessibleRole describing the role of 
         * the object
         */
        public AccessibleRole getAccessibleRole() {
            return AccessibleRole.ROOT_PANE;
        }
    } // inner class AccessibleJRootPane

}
