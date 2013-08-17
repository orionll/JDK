/*
 * @(#)WindowsSplitPaneDivider.java	1.10 98/08/28
 *
 * Copyright 1998 by Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Sun Microsystems, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * you entered into with Sun.
 */

package com.sun.java.swing.plaf.windows;

import java.awt.*;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;


/**
 * Divider used for Windows split pane.
 * <p>
 * <strong>Warning:</strong>
 * Serialized objects of this class will not be compatible with
 * future Swing releases.  The current serialization support is appropriate
 * for short term storage or RMI between applications running the same
 * version of Swing.  A future release of Swing will provide support for
 * long term persistence.
 *
 * @version 1.2 10/25/97
 * @author Jeff Dinkins
 */
public class WindowsSplitPaneDivider extends BasicSplitPaneDivider
{

    /**
     * Creates a new Windows SplitPaneDivider
     */
    public WindowsSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    } 

    /**
      * Paints the divider.
      */
    public void paint(Graphics g) {
        Color bgColor = (splitPane.hasFocus()) ?
                            UIManager.getColor("SplitPane.shadow") :
                            getBackground();
        Dimension size = getSize();

        if(bgColor != null) {
            g.setColor(bgColor);
            g.fillRect(0, 0, size.width, size.height);
        }
        super.paint(g);
    }
}
