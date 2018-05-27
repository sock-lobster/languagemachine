import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 * 
 * LMInputToolbar is the class that displays executes appropriate behaviors
 * for the part of the GUI where the user inputs strings to test on thier 
 * NFA or DFA.
 * 
 * LMInputToolbar contains:
 * - LM Objects workspacePanel, messagePanel: allows the Input toolbar to 
 *      interact with these other GUI components.
 * - JTextField inputText: here, the user inputs strings to test with his/her
 *      NFA
 * - JButton enterButton: allows the user to submit text in the above field 
 *      for processing by the NFA
 * - JLabel acceptanceLabel: this tells the user whether or not their string
 *      is accepted by the NFA
 * - NFA nfa: the NFA created by the user that is displayed in the workspace.
 */

public class LMInputToolbar
    extends JToolBar
    implements ActionListener
{
    private LMWorkspacePanel workspacePanel;
    private LMMessagePanel messagePanel;

    private JTextField inputText;
    private JButton enterButton;
    private JLabel acceptanceLabel;
    
    private NFA nfa;

    /**
     * Constructor for LMInputToolbar.
     */
    public LMInputToolbar()
    {
        super();
        setFloatable(false);
        setLayout(new GridBagLayout());
        buildUI();
    }

    /**
     * Returns the acceptanceLabel.
     */
    public JLabel getAcceptanceLabel()
    {
        return acceptanceLabel;
    }

    /**
     * Returns the inputTextField.
     */
    public JTextField getInputTextField()
    {
        return inputText;
    }

    /**
     * Sets the workspace and nfa.
     *
     * LMWorkspacePanel workspacePanel: the workspace being drawn in
     */
    public void setWorkspacePanelAndNFA(LMWorkspacePanel workspacePanel)
    {
        this.workspacePanel = workspacePanel;
        nfa = workspacePanel.getNFA();
    }

    /**
     * Sets the message pannel to the one displayed in the frame.
     *
     * LMMessagePanel messagePanel: the message panel in the frame
     */
    public void setMessagePanel(LMMessagePanel messagePanel)
    {
        this.messagePanel = messagePanel;
    }

    /**
     * Sets the nfa to the one displayed.
     *
     * NFA nfa: the NFA currently being worked on by the user
     */
    public void setNFA(NFA nfa)
    {
        this.nfa = nfa;
    }

    /**
     * Stops displaying the result of the user's last test.
     */
    public void clearAcceptanceText()
    {
        acceptanceLabel.setText("");
    }

    /**
     * Performs actions based on user input, here the primary action is
     * testing strings against NFAs and displaying the result.
     * 
     * ActionEvent e: the action the user performed in the input toolbar
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();
        acceptanceLabel.setText("");

        if (command.equals("enter"))
        {
            submitInputTextToNFAAndDisplayOutput();
        }
    }

    /**
     * Runs the user's NFA on the user's input and displays the result
     */
    private void submitInputTextToNFAAndDisplayOutput()
    {
        for (State s : nfa.getStates())
        {
            for (Transition t : s.getTransitionsOut())
            {
                if (t.getRules().size() == 0)
                {
                    messagePanel.setText("WARNING: You have transition(s) without any rules.");
                    break;
                }
            }
        }
        String inputString = inputText.getText();
        boolean accepts = nfa.run(inputString);

        if (accepts)
        {
            acceptanceLabel.setForeground(Color.BLUE);
            acceptanceLabel.setText("Accepts");
        }
        else
        {
            acceptanceLabel.setForeground(Color.RED);
            acceptanceLabel.setText("Rejects");
        }
        inputText.requestFocusInWindow();
        inputText.selectAll();
    }

    /**
     * Places GUI objects within the input toolbar.
     */
    private void buildUI()
    {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.insets = new Insets(3, 3, 3, 3);

        JLabel title = new JLabel("Input your test string:");
        //title.setHorizontalAlignment(JLabel.CENTER);
        add(title, c);

        c.gridx++;
        c.ipadx = 497;
        inputText = new JTextField();
        inputText.setActionCommand("enter");
        inputText.addActionListener(this);
        inputText.setPreferredSize(new Dimension(0, 100));
        add(inputText, c);

        c.gridx++;
        c.ipadx = 0;
        enterButton = new JButton("Enter");
        enterButton.setActionCommand("enter");
        enterButton.addActionListener(this);
        add(enterButton, c);

        c.gridx++;
        Font displayFont = new Font("Serif", Font.BOLD, 18);
        acceptanceLabel = new JLabel();
        acceptanceLabel.setFont(displayFont);
        add(acceptanceLabel, c);

        // I hate Alby for making this work.
        c.gridx++;
        c.weightx = 1;
        JLabel blank = new JLabel();
        add(blank, c);
    }
} 