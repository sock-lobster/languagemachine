import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.*;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 *
 * Builds the toolbar which contains buttons for the different tools used to
 * build an NFA. Implements ActionListener to handle button presses.
 *
 * "LM" objects: allow interaction between the toolbar and those parts of
 *      the GUI
 * JButtons: buttons for each tool
 * JPanels: panels for each button to cleanly draw a border around the selected
 *      tool
 */
public class LMToolbar
    extends JToolBar
    implements ActionListener
{
    private LMWorkspacePanel workspacePanel;
    private LMInputToolbar inputToolbar;
    private LMOptionsPanel optionsPanel;
    private LMMessagePanel messagePanel;

    private JButton mouseButton;
    private JButton stateButton;
    private JButton acceptStateButton;
    private JButton transitionButton;
    private JButton deleteButton;

    private JPanel mouseButtonPanel;
    private JPanel stateButtonPanel;
    private JPanel acceptStateButtonPanel;
    private JPanel transitionButtonPanel;
    private JPanel deleteButtonPanel;

    /**
     * Constructor for the toolbar. Calls JToolBar's constructor to set up a
     * vertical toolbar. Sets the toolbar to be unable to float and gives it
     * a bag layout. Calls buildUI() to fill the toolbar with buttons.
     */
    public LMToolbar()
    {
        super(JToolBar.VERTICAL);
        setFloatable(false);
        setLayout(new GridBagLayout());
        buildUI();
    }

    /**
     * Sets the workspacePanel. Allows the toolbar to interact with this
     * part of the GUI.
     *
     * LMWorkspacePanel workspacePanel: the workspace of the frame
     */
    public void setWorkspacePanel(LMWorkspacePanel workspacePanel)
    {
        this.workspacePanel = workspacePanel;
    }

    /**
     * Sets the inputToolbar. Allows the toolbar to interact with this
     * part of the GUI.
     *
     * LMInputToolbar inputToolbar: the input toolbar of the frame
     */
    public void setInputToolbar(LMInputToolbar inputToolbar)
    {
        this.inputToolbar = inputToolbar;
    }

    /**
     * Sets the optionsPanel. Allows the toolbar to interact with this
     * part of the GUI.
     *
     * LMOptionsPanel optionsPanel: the options panel of the frame
     */
    public void setOptionsPanel(LMOptionsPanel optionsPanel)
    {
        this.optionsPanel = optionsPanel;
    }

    /**
     * Sets the messagePanel. Allows the toolbar to interact with this
     * part of the GUI.
     *
     * LMMessagePanel messagePanel: the message panel of the frame
     */
    public void setMessagePanel(LMMessagePanel messagePanel)
    {
        this.messagePanel = messagePanel;
    }

    /**
     * Gets the panel for containing the button corresponding to the given tool.
     * Used to set a border for the panel to highlight the currently selected
     * tool. If somehow a bad tool ID is passed, returns the mouseButtonPanel.
     *
     * int tool: the ID of the tool
     *
     * Returns the panel of the corresponding tool button.
     */
    public JPanel getToolButtonPanel(int tool)
    {
        if (tool == LMWorkspacePanel.SELECT_TOOL)
        {
            return mouseButtonPanel;
        }

        else if (tool == LMWorkspacePanel.STATE_TOOL)
        {
            return stateButtonPanel;
        }

        else if (tool == LMWorkspacePanel.ACCEPT_STATE_TOOL)
        {
            return acceptStateButtonPanel;
        }

        else if (tool == LMWorkspacePanel.TRANSITION_TOOL_INITIAL ||
            tool == LMWorkspacePanel.TRANSITION_TOOL_FINAL)
        {
            return transitionButtonPanel;
        }

        else if (tool == LMWorkspacePanel.DELETE_TOOL)
        {
            return deleteButtonPanel;
        }
        else
        {
            return mouseButtonPanel;
        }
    }

    /**
     * Called when a button is pressed. Chooses an action based on the button
     * pressed. Also clears the various text fields and unselects any selected
     * objects in the workspace. Part of the ActionListener implementation.
     *
     * ActionEvent e: the event generated when a button is clicked
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        inputToolbar.clearAcceptanceText();
        messagePanel.clearText();
        optionsPanel.changeOptions("blank");
        workspacePanel.unselectAll();
        workspacePanel.repaint();

        if (command.equals("mouse"))
        {
            workspacePanel.setTool(LMWorkspacePanel.SELECT_TOOL);
        }

        else if (command.equals("state"))
        {
            workspacePanel.setTool(LMWorkspacePanel.STATE_TOOL);
        }

        else if (command.equals("accept state"))
        {
            workspacePanel.setTool(LMWorkspacePanel.ACCEPT_STATE_TOOL);
        }

        else if (command.equals("transition"))
        {
            workspacePanel.setTool(LMWorkspacePanel.TRANSITION_TOOL_INITIAL);
        }

        else if (command.equals("delete"))
        {
            workspacePanel.setTool(LMWorkspacePanel.DELETE_TOOL);
        }
    }

    /**
     * Creates buttons and their panels and builds the UI.
     */
    private void buildUI()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.insets = new Insets(3, 3, 3, 3);

        mouseButton = new JButton("Select", new ImageIcon("images/select32.png"));
        mouseButton.setDisabledIcon(new ImageIcon("images/select32d.png"));
        mouseButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        mouseButton.setHorizontalTextPosition(AbstractButton.CENTER);
        mouseButton.setMnemonic(KeyEvent.VK_E);
        mouseButton.setActionCommand("mouse");
        mouseButton.addActionListener(this);

        mouseButtonPanel = new JPanel(new GridLayout());
        mouseButtonPanel.setBorder(new LineBorder(Color.GRAY, 2));
        mouseButtonPanel.add(mouseButton);

        add(mouseButtonPanel, c);

        c.gridy++;
        stateButton = new JButton("State", new ImageIcon("images/state32.png"));
        stateButton.setDisabledIcon(new ImageIcon("images/state32d.png"));
        stateButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        stateButton.setHorizontalTextPosition(AbstractButton.CENTER);
        stateButton.setMnemonic(KeyEvent.VK_S);
        stateButton.setActionCommand("state");
        stateButton.addActionListener(this);

        stateButtonPanel = new JPanel(new GridLayout());
        stateButtonPanel.setBorder(new LineBorder(new Color(237, 237, 237), 2));
        stateButtonPanel.add(stateButton);

        add(stateButtonPanel, c);

        c.gridy++;
        acceptStateButton = new JButton("Accept State", new ImageIcon("images/acceptstate32.png"));
        acceptStateButton.setDisabledIcon(new ImageIcon("images/state32d.png"));
        acceptStateButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        acceptStateButton.setHorizontalTextPosition(AbstractButton.CENTER);
        acceptStateButton.setMnemonic(KeyEvent.VK_A);
        acceptStateButton.setActionCommand("accept state");
        acceptStateButton.addActionListener(this);

        acceptStateButtonPanel = new JPanel(new GridLayout());
        acceptStateButtonPanel.setBorder(new LineBorder(new Color(237, 237, 237), 2));
        acceptStateButtonPanel.add(acceptStateButton);

        add(acceptStateButtonPanel, c);

        c.gridy++;
        transitionButton = new JButton("Transition", new ImageIcon("images/transition32.png"));
        transitionButton.setDisabledIcon(new ImageIcon("images/transition32d.png"));
        transitionButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        transitionButton.setHorizontalTextPosition(AbstractButton.CENTER);
        transitionButton.setMnemonic(KeyEvent.VK_T);
        transitionButton.setActionCommand("transition");
        transitionButton.addActionListener(this);

        transitionButtonPanel = new JPanel(new GridLayout());
        transitionButtonPanel.setBorder(new LineBorder(new Color(237, 237, 237), 2));
        transitionButtonPanel.add(transitionButton);

        add(transitionButtonPanel, c);

        c.gridy++;
        deleteButton = new JButton("Delete", new ImageIcon("images/delete32.png"));
        deleteButton.setDisabledIcon(new ImageIcon("images/delete32d.png"));
        deleteButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        deleteButton.setHorizontalTextPosition(AbstractButton.CENTER);
        deleteButton.setMnemonic(KeyEvent.VK_D);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);

        deleteButtonPanel = new JPanel(new GridLayout());
        deleteButtonPanel.setBorder(new LineBorder(new Color(237, 237, 237), 2));
        deleteButtonPanel.add(deleteButton);

        add(deleteButtonPanel, c);

        c.gridy++;
        c.weighty = 1;
        JLabel blank = new JLabel();
        add(blank, c);
    }
}