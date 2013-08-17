/*
 * @(#)InternalWindowPanel.java	1.16 99/04/23
 *
 * Copyright (c) 1997-1999 by Sun Microsystems, Inc. All Rights Reserved.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license to use,
 * modify and redistribute this software in source and binary code form,
 * provided that i) this copyright notice and license appear on all copies of
 * the software; and ii) Licensee does not utilize the software in a manner
 * which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE
 * LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING
 * OR DISTRIBUTING THE SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS
 * LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF THE USE OF
 * OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control of
 * aircraft, air traffic, aircraft navigation or aircraft communications; or in
 * the design, construction, operation or maintenance of any nuclear
 * facility. Licensee represents and warrants that it will not use or
 * redistribute the Software for such purposes.
 */

import javax.swing.*;
import javax.swing.border.*;
import javax.accessibility.*;

import java.awt.Panel;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Container;

/*
 * 1.16 99/04/23
 * @author Dave Kloba
 * @author Peter Korn (accessibility support)
 */
public class InternalWindowPanel extends JPanel implements ActionListener     {
    // Maker values
    JCheckBox closeBox;
    JCheckBox maxBox;
    JCheckBox iconBox;
    JCheckBox resizeBox;
    JTextField titleField;
    JTextField layerField;
    JButton closeAllButton;
    JButton makeButton;
    JLayeredPane lc;
    int makeCount = 0;
    JInternalFrame maker;

    public InternalWindowPanel()    {
        setLayout(new BorderLayout());
        lc = new JDesktopPane();
	lc.setOpaque(false);
        maker = createMakerFrame();
        lc.add(maker, JLayeredPane.PALETTE_LAYER);  
        
        add("Center", lc);
    }

    public JInternalFrame createMakerFrame() {
        JInternalFrame w;
        JPanel tp;
	Container contentPane;
        
        w = new JInternalFrame("Frame Creator");
	contentPane = w.getContentPane();
        contentPane.setLayout(new GridLayout(0, 1));

        tp = new JPanel();
        tp.setLayout(new GridLayout(2, 2));
        closeBox = new JCheckBox( "is Closable ");
        closeBox.setSelected(true);
        tp.add(closeBox);
        maxBox = new JCheckBox(   "is Maxable  ");
        maxBox.setSelected(true);
        tp.add(maxBox);
        iconBox = new JCheckBox(  "is Iconifiable ");
        iconBox.setSelected(true);
        tp.add(iconBox);
        resizeBox = new JCheckBox("is Resizable");
        resizeBox.setSelected(true);
        tp.add(resizeBox);
        contentPane.add(tp);
        
        tp = new JPanel();
	tp.setBorder(BorderFactory.createTitledBorder("Title"));
        tp.setLayout(new BorderLayout());
        titleField = new JTextField();
        titleField.setText("");
        titleField.setMinimumSize(new Dimension(50, 25));
	titleField.setEditable(true);
	titleField.getAccessibleContext().setAccessibleName("Title for created frame");
        tp.add(titleField, "Center");
        contentPane.add(tp);              

        tp = new JPanel();
	tp.setBorder(BorderFactory.createTitledBorder("Layer"));
        tp.setLayout(new BorderLayout());
        layerField = new JTextField();
        layerField.setMinimumSize(new Dimension(50, 25));
	layerField.setEditable(true);
        layerField.setText("5");
	layerField.getAccessibleContext().setAccessibleName("Layer for created frame");
	layerField.getAccessibleContext().setAccessibleDescription("This must be an Integer value, which determines which layer in the stacking order to place the newly created Internal Frame");
        tp.add(layerField, "Center");
        contentPane.add(tp);              

        tp = new JPanel();
        tp.setLayout(new GridLayout(1, 2));
        closeAllButton = new JButton("Clear");
        closeAllButton.addActionListener(this);
        tp.add(closeAllButton);
        makeButton = new JButton("Make");
        makeButton.addActionListener(this);
        tp.add(makeButton);     
        contentPane.add(tp);
        
        w.setBounds(360, 10, 270, 250);
        w.setResizable(true);
        return w;
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == closeAllButton) {
            lc.removeAll();
            lc.add(maker);
            lc.repaint();
            makeCount = 0;
        } else if(e.getSource() == makeButton) {
            JInternalFrame w;
	    int layer;
            w = new JInternalFrame();
            w.setClosable(closeBox.isSelected());
            w.setMaximizable(maxBox.isSelected());
            w.setIconifiable(iconBox.isSelected());
	    String title = titleField.getText();
	    if(title.equals("")) {
                w.setTitle("Internal Frame " + (makeCount+1));
	    } else {
                w.setTitle(title);
	    }
            w.setResizable(resizeBox.isSelected());
	    try { 
                layer = Integer.parseInt(layerField.getText()); 
	    } catch (NumberFormatException e2) {
	        layer = 0;
	    }
            makeCount++;
            w.setBounds(20*(makeCount%10), 20*(makeCount%10), 225, 150);
            w.setContentPane(new MyScrollPane(layer, makeCount));
                
            lc.add(w, new Integer(layer));  
            try { w.setSelected(true); } catch (java.beans.PropertyVetoException e2) {}
        }
    }

}

class MyScrollPane extends JScrollPane
{
    static ImageIcon[] icon = new ImageIcon[5];

    public MyScrollPane(int layer, int count)
    {
	super();
        if(icon[0] == null) {
           icon[0] = SwingSet.sharedInstance().loadImageIcon("images/ImageClub/misc/horn.gif", "Horn");
           icon[1] = SwingSet.sharedInstance().loadImageIcon("images/ImageClub/misc/fish.gif", "Fish");
           icon[2] = SwingSet.sharedInstance().loadImageIcon("images/ImageClub/misc/moon.gif", "Moon");
           icon[3] = SwingSet.sharedInstance().loadImageIcon("images/ImageClub/misc/sun.gif",  "Sun");
           icon[4] = SwingSet.sharedInstance().loadImageIcon("images/ImageClub/misc/cab.gif",  "Yellow Cab");
        }
	JPanel p = new JPanel();
       	p.setOpaque(false);
	p.setLayout(new BorderLayout() );
	JLabel layerLabel = new JLabel("Layer "+layer);
	layerLabel.setOpaque(false);
        
	int it = count%5;
        p.add(new JLabel(icon[count%5]), BorderLayout.CENTER);
	p.add(layerLabel, BorderLayout.NORTH);

	getViewport().add(p);

    }
    
    public Dimension getMinimumSize() {
	return new Dimension(25, 25);
    }
    
    public boolean isOpaque() {
        return true;
    }
}



