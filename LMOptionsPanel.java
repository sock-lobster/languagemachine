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
public class LMOptionsPanel
    extends JPanel
{
    private LMWorkspacePanel workspacePanel;
    private LMInputToolbar inputToolbar;
    private LMMessagePanel messagePanel;

    private LMStateOptionsToolbar stateCard;
    private LMTransitionOptionsToolbar transitionCard;
   
    private CardLayout cardLayout;
    private Transition transition;

    /**
     * The constructor for LMOptionsPanel.
     *
     * CardLayout cardLayout: makes this panel able to switch between different
     *      options panels
     */
    public LMOptionsPanel(CardLayout cardLayout)
    {
        super(cardLayout);
        this.cardLayout = cardLayout;

        buildUI();
    }
    
    /**
     * Sets the workspace panel so that it can be accessed.
     *
     * LMWorkspacePanel workspace: the workspace panel to be set
     */
    public void setWorkspacePanel(LMWorkspacePanel workspace)
    {
        workspacePanel = workspace;
    }

    /**
     * Sets the input toolbar so that it can be accessed.
     *
     * LMInputToolbar inputToolbar: an input toolbar to be set
     */
    public void setInputToolbar(LMInputToolbar inputToolbar)
    {
        this.inputToolbar = inputToolbar;
    }

    /**
     * Sets the message panel so that it can be accessed.
     *
     * LMMessagePanel messagePanel: the message panel to be set
     */
    public void setMessagePanel(LMMessagePanel messagePanel)
    {
        this.messagePanel = messagePanel;
    }

    /**
     * Gets the current workspace panel.
     *
     * Returns a workspace panel
     */
    public LMWorkspacePanel getWorkspacePanel()
    {
        return workspacePanel;
    }

    /**
     * Gets the current input toolbar.
     *
     * Returns an input toolbar
     */
    public LMInputToolbar getInputToolbar()
    {
        return inputToolbar;
    }

    /**
     * Gets the current message panel.
     *
     * Returns a message panel
     */
    public LMMessagePanel getMessagePanel()
    {
        return messagePanel;
    }

    /**
     * Gets the current state toolbar.
     *
     * Returns a state toolbar
     */
    public LMStateOptionsToolbar getStateToolbar()
    {
        return stateCard;
    }

    /**
     * Gets the current transition toolbar.
     * 
     * Returns a transition toolbar
     */
    public LMTransitionOptionsToolbar getTransitionToolbar()
    {
        return transitionCard;
    }

    /**
     * Changes the options panel between transition options, state options
     * and a blank panel.
     *
     * String s: the string of what panel is supposed to be changed to
     */
    public void changeOptions(String s) 
    {
        cardLayout.show(this, s);
    }

    /**
     * Builds the options panel.
     */
    private void buildUI()
    {
        JPanel blankCard = new JPanel();

        stateCard = new LMStateOptionsToolbar(this);

        transitionCard = new LMTransitionOptionsToolbar(this);
        
        this.add(blankCard, "blank");
        this.add(stateCard, "state");
        this.add(transitionCard, "transition");
    }
}
