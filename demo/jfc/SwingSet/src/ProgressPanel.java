/*
 * @(#)ProgressPanel.java	1.10 99/04/23
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
import javax.swing.text.*;
import javax.accessibility.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;


/**
 * Demo the Progress Bar
 *
 * @version 1.10 04/23/99
 * @author Jeff Dinkins
 # @author Peter Korn (accessibility support)
 */
public class ProgressPanel extends JPanel implements ActionListener {
    SwingSet swing;
    JProgressBar progressBar;
    JTextArea progressTextArea;
    JButton loadButton;
    JButton stopButton;
    Timer timer;
    Object  lock = new Object();

    public ProgressPanel(SwingSet swing) {
	this.swing = swing;

	setLayout(new BorderLayout());

	JPanel textWrapper = new JPanel(new BorderLayout());
	textWrapper.setBorder(swing.loweredBorder);
	textWrapper.setAlignmentX(LEFT_ALIGNMENT);
	progressTextArea = new MyTextArea();
	progressTextArea.getAccessibleContext().setAccessibleName("Text progressively being loaded in");
	progressTextArea.getAccessibleContext().setAccessibleDescription("This JTextArea is being filled with text from a buffer progressively a character at a time while the progress bar a the bottom of the window shows the loading progress");
	textWrapper.add(progressTextArea, BorderLayout.CENTER);

	add(textWrapper, BorderLayout.CENTER);

	JPanel progressPanel = new JPanel();
	add(progressPanel, BorderLayout.SOUTH);

	progressBar = new JProgressBar(JProgressBar.HORIZONTAL,
				       0, text.length()) {
	    public Dimension getPreferredSize() {
		return new Dimension(300, super.getPreferredSize().height);
	    }
	};
	progressBar.getAccessibleContext().setAccessibleName("Text loading progress");
	progressPanel.add(progressBar);

	loadButton = new JButton("Start Loading Text");
	loadButton.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		startLoading();
	    }
	});
	progressPanel.add(loadButton);

        stopButton = new JButton("Stop Loading Text");
        stopButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                stopLoading();
            }
        });
        stopButton.setEnabled(false);
	progressPanel.add(stopButton);
    }

    public Insets getInsets() {
	return new Insets(10,10,10,10);
    }

    public void startLoading() {
        if(timer == null) {
            loadButton.setEnabled(false);
            stopButton.setEnabled(true);
	    timer = new Timer(25, this);
	    timer.start();
        }
    }

    public void stopLoading() {
	if(timer != null) {
	   timer.stop();
	   timer = null;
	}
        loadButton.setEnabled(true);
        stopButton.setEnabled(false);
    }

    int textLocation = 0;

    String text =
 	 "      The saying goes: if an infinite number of monkeys\n" +
         "   typed on an infinite number of typewriters, eventually\n" +
         "   all the great works of mankind would emerge. \n\n " +
 	 "      Now, with today's high speed computers, we can\n " +
         "   finally test the theory...\n\n" +
 	 "      Lzskd jfy 92y;ho4 th;qlh sd 6yty;q2 hnlj 8sdf. Djfy\n " +
         "   92y;ho4, th;qxhz d7yty; Q0hnlj 23&^ (# ljask djf y92y;h\n " +
         "   fy92y; Sd6y ty;q2h nl jk la gfa harvin garvil lasdfsd\n " +
         "   a83sl la8z 2 be or not to be... that is the question. Hath\n" +
	 "   forth not to want a banana, or to be a banana. Banana, I \n" +
	 "   knew him banana. Banana banana banana.\n\n" +
    	 "          Well... it seemed like a good idea...\n\n\n\n\n\n\n\n\n\n" +
 	 "             Hi Ewan and Montana!";

    public void actionPerformed (ActionEvent e) {
	    if(progressBar.getValue() < progressBar.getMaximum()) {
		progressBar.setValue(progressBar.getValue() + 1);
		progressTextArea.append(text.substring(textLocation, textLocation+1));
		textLocation++;
	    } else {
	        stopLoading();
            }
    }

    class MyTextArea extends JTextArea {
        public MyTextArea() {
            super(null, 0, 0);
	    setEditable(false);
	    setText("");
        }

        public float getAlignmentX () {
            return LEFT_ALIGNMENT;
        }
 
        public float getAlignmentY () {
            return TOP_ALIGNMENT;
        }
    }
}
