package com.mygdx.game;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class myDocumentListener implements DocumentListener {
    public void changedUpdate(DocumentEvent e) {

    }

    public void insertUpdate(DocumentEvent e) {
        Document document = e.getDocument();
        try {
            String s = document.getText(0, document.getLength());

        } catch (BadLocationException e1) {
            e1.printStackTrace();
            return;
        }

    }

    public void removeUpdate(DocumentEvent e) {
    }
}
