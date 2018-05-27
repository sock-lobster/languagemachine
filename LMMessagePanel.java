import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 6 June 2012
 * 
 * Creates and implements a panel that holds error/warning messages and
 * the file name.
 */
public class LMMessagePanel
    extends JPanel
{
    private JLabel message;
    private JLabel curFilename;
    private JLabel blankLabel;

    private String filename = "Untitled NFA";

    /**
     * The constructor for LMMessagePanel.
     */
    public LMMessagePanel()
    {
        super();
        setLayout(new GridBagLayout());
        buildUI();
    }

    /**
     * Sets the string that is displayed as the error/warning message.
     *
     * String s: the string that should be displayed
     */
    public void setText(String s)
    {
        message.setText(s);
    }

    /**
     * Clears the error/warning message from the panel.
     */
    public void clearText()
    {
        message.setText("");
    }

    /**
     * Sets the filename that is to be displayed in the panel.
     *
     * String s: the filename that should be displayed
     */
    public void setFilename(String s)
    {
        filename = s;
        curFilename.setText(s);
    }

    /**
     * Sets the filename to be the filename + (Unsaved) if the file has been
     * modified since the file has last been saved. It is set whenever the
     * workspace is modified in any way.
     */
    public void setFileModified()
    {
        curFilename.setText(filename + " (Unsaved)");
    }

    /**
     * Builds the message panel
     */
    public void buildUI()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;

        // Adds a blank label to the beginning of the row in order to properly
        // place the error message
        blankLabel = new JLabel();
        blankLabel.setPreferredSize(new Dimension(157, 25));
        add(blankLabel, c);

        // Adds a new red label with the error message
        c.gridx++;
        message = new JLabel();
        message.setForeground(Color.red);
        add(message, c);

        // Pushes the filename to the right side
        c.gridx++;
        c.weightx = 1;
        JLabel blank = new JLabel();
        add(blank, c);

        // Adds the title of the NFA
        c.gridx++;
        c.weightx = 0;
        curFilename = new JLabel("Untitled NFA");
        add(curFilename, c);
    }
}