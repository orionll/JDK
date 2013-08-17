/*
 * @(#)CommentView.java	1.5 98/11/19
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
package javax.swing.text.html;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.text.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.util.*;

/**
 * CommentView subclasses HiddenTagView to contain a JTextArea showing
 * a comment. When the textarea is edited the comment is
 * reset. As this inherits from EditableView if the JTextComponent is
 * not editable, the textarea will not be visible.
 *
 * @author  Scott Violet
 * @version 1.5, 11/19/98
 */
class CommentView extends HiddenTagView {
    CommentView(Element e) {
	super(e);
    }

    protected Component createComponent() {
	JTextArea ta = new JTextArea(getRepresentedText());
	Document doc = getDocument();
	Font font;
	if (doc instanceof StyledDocument) {
	    font = ((StyledDocument)doc).getFont(getAttributes());
	    ta.setFont(font);
	}
	else {
	    font = ta.getFont();
	}
	updateYAlign(font);
	ta.setBorder(CBorder);
	ta.getDocument().addDocumentListener(this);
	return ta;
    }

    void resetBorder() {
    }

    void pushTextToModel() {
	if (!isSettingAttributes) {
	    SimpleAttributeSet sas = new SimpleAttributeSet();
	    String text = getTextComponent().getText();
	    isSettingAttributes = true;
	    try {
		sas.addAttribute(HTML.Attribute.COMMENT, text);
		((StyledDocument)getDocument()).setCharacterAttributes
		    (getStartOffset(), getEndOffset() -
		     getStartOffset(), sas, false);
	    }
	    finally {
		isSettingAttributes = false;
	    }
	}
    }

    JTextComponent getTextComponent() {
	return (JTextComponent)getComponent();
    }

    String getRepresentedText() {
	AttributeSet as = getElement().getAttributes();
	if (as != null) {
	    Object comment = as.getAttribute(HTML.Attribute.COMMENT);
	    if (comment instanceof String) {
		return (String)comment;
	    }
	}
	return "";
    }

    static final Border CBorder = new CommentBorder();
    static final int commentPadding = 3;
    static final int commentPaddingD = commentPadding * 3;

    static class CommentBorder extends LineBorder {
	CommentBorder() {
	    super(Color.black, 1);
	}

	public void paintBorder(Component c, Graphics g, int x, int y,
				int width, int height) {
	    super.paintBorder(c, g, x + commentPadding, y,
			      width - commentPaddingD, height);
	}

	public Insets getBorderInsets(Component c) {
	    Insets retI = super.getBorderInsets(c);

	    retI.left += commentPadding;
	    retI.right += commentPadding;
	    return retI;
	}

	public boolean isBorderOpaque() {
	    return false;
	}
    } // End of class CommentView.CommentBorder
} // End of CommentView
