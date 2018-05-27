import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 6 June 2012
 * 
 * Creates and implements a toolbar for the state options. 
 * 
 * Toolbar contains:
 * - a title 
 * - buttons to switch between an accept state and a non-accept state
 * - a delete button
 */
public class LMStateOptionsToolbar
    extends JToolBar
    implements ActionListener 
{
    private LMOptionsPanel optionsPanel;

    private JButton acceptButton;
    private JButton nonAcceptButton;
    private JButton deleteButton;

    private State state;

    /**
     * The constructor for LMStateOptionsToolbar.
     *
     * LMOptionsPanel optionsPanel: the options panel that controls the 
     *      visible options toolbar in the panel
     */
    public LMStateOptionsToolbar(LMOptionsPanel optionsPanel)
    {
        super(JToolBar.VERTICAL);
        setFloatable(false);
        setLayout(new GridBagLayout());
        this.optionsPanel = optionsPanel;
        buildUI();
    }

    /**
     * Sets the buttons that apply to the selected state. Sets buttons to
     * be enabled or not depending on whether the state is an accept state
     * or not.
     *
     * State s: the state that is selected in order to 
     *      bring up the state options panel
     */
    public void setStateInfo(State s)
    {
        deleteButton.setEnabled(true);
        state = s;
        if (state.isAcceptState())
        {
            acceptButton.setEnabled(false);
            nonAcceptButton.setEnabled(true);
        }
        else
        {
            acceptButton.setEnabled(true);
            nonAcceptButton.setEnabled(false);
        }
        if (s == optionsPanel.getWorkspacePanel().getNFA().getStartState())
        {
            deleteButton.setEnabled(false);
        }
    }

    /**
     * Performs all the actions that are possible in the toolbar. Clears the 
     * acceptance text and message panel text, and sets setModified to true.
     * 
     * ActionEvent e: an action event that allows us to handle actions 
     *      performed in the toolbar
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("accept state"))
        {
            changeStateToAcceptState();
        }
        else if (command.equals("non-accept state"))
        {
            changeStateToNonAcceptState();
        }
        else if (command.equals("delete"))
        {
            deleteSelectedState();
        }

        optionsPanel.getInputToolbar().clearAcceptanceText();
        optionsPanel.getMessagePanel().clearText();
        optionsPanel.getWorkspacePanel().setModified(true);
    }

    /**
     * Changes the selected state from a non-accept state to an accept state,
     * then repaints the workspace panel and sets the tool back to the select tool.
     */
    private void changeStateToAcceptState()
    {
        state.setAcceptState(true);
        acceptButton.setEnabled(false);
        nonAcceptButton.setEnabled(true);
        optionsPanel.getWorkspacePanel().repaint();
        optionsPanel.getWorkspacePanel().setTool(LMWorkspacePanel.SELECT_TOOL);
    }

    /**
     * Changes the selected state from a non-accept state to an accept state,
     * then repaints the workspace panel and sets the tool back to the select tool.
     */
    private void changeStateToNonAcceptState()
    {
        state.setAcceptState(false);
        acceptButton.setEnabled(true);
        nonAcceptButton.setEnabled(false);
        optionsPanel.getWorkspacePanel().repaint();
        optionsPanel.getWorkspacePanel().setTool(LMWorkspacePanel.SELECT_TOOL);
    }

    /**
     * Deletes the selected state when you click the delete button. Sets the 
     * options panel to blank, and sets the tool back to the select tool.
     */
    private void deleteSelectedState()
    {
        optionsPanel.getWorkspacePanel().getNFA().removeState(state);
        optionsPanel.getWorkspacePanel().repaint();
        optionsPanel.getWorkspacePanel().setTool(LMWorkspacePanel.SELECT_TOOL);
        optionsPanel.changeOptions("blank");
    }

    /**
     * Builds the state options panel.
     */
    private void buildUI()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

        // Adds the title to the panel, changes the font, and centers it
        Font displayFont = new Font("Serif", Font.BOLD, 18);
        JLabel cardTitle = new JLabel("State Options");
        cardTitle.setFont(displayFont);
        cardTitle.setHorizontalAlignment(JLabel.CENTER);
        add(cardTitle, c);

        // Adds a separator between the title and the accept/non-accept buttons
        c.gridy++;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(0, 17));
        add(separator, c);

        // Sets the insets to 3 on every side
        c.insets = new Insets(3, 3, 3, 3);

        // Adds a make accepting button
        c.gridy++;
        acceptButton = new JButton("Make Accepting", new ImageIcon("images/acceptstate32.png"));
        acceptButton.setDisabledIcon(new ImageIcon("images/state32d.png"));
        acceptButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        acceptButton.setHorizontalTextPosition(AbstractButton.CENTER);
        acceptButton.setActionCommand("accept state");
        acceptButton.addActionListener(this);
        add(acceptButton, c);

        // Adds a make non-accepting button
        c.gridy++;
        nonAcceptButton = new JButton("Make Non-Accepting", new ImageIcon("images/state32.png"));
        nonAcceptButton.setDisabledIcon(new ImageIcon("images/state32d.png"));
        nonAcceptButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        nonAcceptButton.setHorizontalTextPosition(AbstractButton.CENTER);
        nonAcceptButton.setActionCommand("non-accept state");
        nonAcceptButton.addActionListener(this);
        add(nonAcceptButton, c);

        // Sets the insets back to 0 on every side
        c.insets = new Insets(0, 0, 0, 0);

        // Adds a separator between the accept/non-accept buttons and the delete button
        c.gridy++;
        separator = new JSeparator();
        separator.setPreferredSize(new Dimension(0, 12));
        add(separator, c);

        // Adds a delete button
        c.gridy++;
        deleteButton = new JButton("Delete State");
        deleteButton.setVerticalTextPosition(AbstractButton.BOTTOM);
        deleteButton.setHorizontalTextPosition(AbstractButton.CENTER);
        deleteButton.setActionCommand("delete");
        deleteButton.addActionListener(this);
        add(deleteButton, c);

        // I hate Alby for making this work.
        // Pushes the panel to the top
        c.gridy++;
        c.weighty = 1;
        JLabel blank = new JLabel();
        add(blank, c);
    }
}