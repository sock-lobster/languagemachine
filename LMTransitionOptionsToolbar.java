import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 6 June 2012
 * 
 * Creates and implements a toolbar for the transition options. 
 * 
 * Toolbar contains:
 * - a title 
 * - a text field in which to enter rules
 * - a button to add epsilons to the textfield
 * - a delete button that deletes the selected transition
 */
public class LMTransitionOptionsToolbar
    extends JToolBar
    implements ActionListener 
{
    private LMOptionsPanel optionsPanel;

    private JTextField ruleTextField;
    private JButton enterButton;
    private JButton epsilonButton;
    private JButton deleteButton;
    
    private Transition transition;

    /**
     * The constructor for LMTransitionOptionsToolbar.
     *
     * LMOptionsPanel optionsPanel: the options panel that controls the 
     *      visible options toolbar in the panel
     */
    public LMTransitionOptionsToolbar(LMOptionsPanel optionsPanel)
    {
        super(JToolBar.VERTICAL);
        setFloatable(false);
        setLayout(new GridBagLayout());
        this.optionsPanel = optionsPanel;
        buildUI();
    }

    /**
     * Sets the rule that applies to the selected transition.
     *
     * Transition transition: the transition that is selected in order to 
     *      bring up the transition options panel
     */
    public void setTransitionInfo(Transition transition)
    {
        this.transition = transition;
        String ruleString = transition.buildStringFromRules();
        ruleTextField.setText(ruleString);
    }

    /**
     * Allows other classes access to the rule text field.
     *
     * Returns the rule text field.
     */
    public JTextField getRuleTextField()
    {
        return ruleTextField;
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

        if (command.equals("enter"))
        {
            enterRules();
        }
        else if (command.equals("epsilon"))
        {
            addAndEnterEpsilonRule();
        }
        else if (command.equals("delete"))
        {
            deleteSelectedTransition();
        }

        optionsPanel.getInputToolbar().clearAcceptanceText();
        optionsPanel.getMessagePanel().clearText();
        optionsPanel.getWorkspacePanel().setModified(true);
    }

    /**
     * Assigns the rules to the selected transition, paints them into the
     * workspace, and resets the tool to the select tool.
     */
    private void enterRules()
    {
        String ruleString = ruleTextField.getText();
        transition.buildRulesFromString(ruleString);
        ruleString = transition.buildStringFromRules();
        ruleTextField.setText(ruleString);
        optionsPanel.getWorkspacePanel().requestFocusInWindow();
        optionsPanel.getWorkspacePanel().repaint();
        // Daniel didn't like this automatic switching any more.
        // optionsPanel.getWorkspacePanel().setTool(LMWorkspacePanel.SELECT_TOOL);
    }

    /**
     * Adds an epsilon to the text field and performs the enter action on it.
     */
    private void addAndEnterEpsilonRule()
    {
        ruleTextField.setText(ruleTextField.getText() + "\u025B");
        enterRules();
    }

    /**
     * Deletes the selected transition when you click the delete button, and if
     * there are transitions going both directions, set the other one back to
     * pointing at the center of the states. Sets the options panel to blank, 
     * and sets the tool back to the select tool.
     */
    private void deleteSelectedTransition()
    {
        Transition oppositeTransition = optionsPanel.getWorkspacePanel().findTransitionBetween(transition.getToState(), transition.getFromState());
        if (oppositeTransition != null)
        {
            oppositeTransition.setTangent(false);
        }
        optionsPanel.getWorkspacePanel().repaint();
        optionsPanel.getWorkspacePanel().getNFA().removeTransition(transition);
        optionsPanel.getWorkspacePanel().setTool(LMWorkspacePanel.SELECT_TOOL);
        optionsPanel.changeOptions("blank");
    }

    /**
     * Builds the transition options panel.
     */
    private void buildUI()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;

        // Adds the title to the panel, changes the font, and centers it
        c.gridwidth = 2;
        Font displayFont = new Font("Serif", Font.BOLD, 18);
        JLabel cardTitle = new JLabel("Transition Options");
        cardTitle.setFont(displayFont);
        cardTitle.setHorizontalAlignment(JLabel.CENTER);
        add(cardTitle, c);

        // Adds a separator between the title and the rules text field
        c.gridy++;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(0, 15));
        add(separator, c);

        // Adds a label for the rules field
        c.gridy++;
        c.gridwidth = 1;
        JLabel rulesLabel = new JLabel("Rules:");
        add(rulesLabel, c);

        // Sets the insets to 3 on every side
        c.insets = new Insets(3, 3, 3, 3);

        // Creates the rules text field of size 100, sets the action command
        // to enter
        c.gridy++;
        c.ipadx = 100;
        ruleTextField = new JTextField();
        ruleTextField.setActionCommand("enter");
        ruleTextField.addActionListener(this);
        add(ruleTextField, c);

        // Creates an enter button, resets padding to 0
        c.gridx = 1;
        c.ipadx = 0;
        enterButton = new JButton("Enter");
        enterButton.setActionCommand("enter");
        enterButton.addActionListener(this);
        add(enterButton, c);

        // Creates a button that adds an epsilon transition to the rule text field
        c.gridy++;
        c.gridx = 0;
        c.gridwidth = 2;
        epsilonButton = new JButton("Add Epsilon Transition");
        epsilonButton.setActionCommand("epsilon");
        epsilonButton.addActionListener(this);
        add(epsilonButton, c);

        // Adds a separator between the rule text field items and the delete button
        c.gridy++;
        separator = new JSeparator();
        separator.setPreferredSize(new Dimension(0, 15));
        add(separator, c);

        // Creates a delete button
        c.gridy++;
        deleteButton = new JButton("Delete Transition");
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