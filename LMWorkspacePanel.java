import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.lang.Math;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 *
 * Builds and runs the workspace where the NFA is drawn. Uses Java Graphics
 * to draw the NFA. Implements MouseListener and MouseMotionListener to
 * allow the drawing of the NFA with the mouse.
 *
 * "LM" objects: allow interaction between the workspace and those parts of
 *      the GUI
 * State selectedState: the state most recently selected/clicked on by the user
 * State initialState: for transitions, the first state selected for the
 *      transition
 * NFA nfa: the NFA represented by the drawing
 * int tool: the ID of the currently selected tool in the toolbar
 * boolean modified: true if the NFA has been modified since the last save,
 *      otherwise false
 * static final ints: public names for each of the tools to help readability
 */
public class LMWorkspacePanel
    extends JPanel
    implements MouseListener, MouseMotionListener
{
    private LMOptionsPanel optionsPanel;
    private LMToolbar toolbar;
    private LMInputToolbar inputToolbar;
    private LMMessagePanel messagePanel;

    private State selectedState;
    private State initialState;
    private NFA nfa;
    private int tool = 0;
    private boolean modified;
    
    public static final int 
        SELECT_TOOL = 0,
        STATE_TOOL = 1,
        ACCEPT_STATE_TOOL = 2,
        TRANSITION_TOOL_INITIAL = 3,
        TRANSITION_TOOL_FINAL = 4,
        DELETE_TOOL = 5;

    /**
     * Constructor for the workspace. Calls JPanel's constructor. Sets the NFA
     * to be a new, unmodified NFA, sets the mouse listeners, and repaints the
     * workspace to display the new NFA.
     */
    public LMWorkspacePanel()
    {
        super();
        nfa = new NFA(new StartState(100, 250, false));
        modified = false;
        addMouseListener(this);
        addMouseMotionListener(this);
        repaint();
    }

    /**
     * Returns the current NFA.
     */
    public NFA getNFA()
    {
        return nfa;
    }

    /**
     * Sets the current NFA and sets the input toolbar's NFA.
     */
    public void setNFA(NFA nfa)
    {
        this.nfa = nfa;
        inputToolbar.setNFA(nfa);
    }

    /**
     * Gets the modified value.
     *
     * Returns modified, so true if the workspace has been modified since the
     * last save and false otherwise.
     */
    public boolean isModified()
    {
        return modified;
    }

    /**
     * Sets the modified value.
     */
    public void setModified(boolean b)
    {
        modified = b;
    }

    /**
     * Sets the messagePanel. Allows the workspace to interact with this part
     * of the GUI.
     *
     * LMMessagePanel messagePanel: the message panel of the frame
     */
    public void setMessagePanel(LMMessagePanel messagePanel)
    {
        this.messagePanel = messagePanel;
    }

    /**
     * Sets the optionsPanel. Allows the workspace to interact with this part
     * of the GUI.
     *
     * LMOptionsPanel optionsPanel: the options panel of the frame
     */
    public void setOptionsPanel(LMOptionsPanel optionsPanel)
    {
        this.optionsPanel = optionsPanel;
    }

    /**
     * Sets the toolbar. Allows the workspace to interact with this part
     * of the GUI.
     *
     * LMToolbar toolbar: the toolbar of the frame
     */
    public void setToolbar(LMToolbar toolbar)
    {
        this.toolbar = toolbar;
    }

    /**
     * Sets the inputToolbar. Allows the workspace to interact with this part
     * of the GUI.
     *
     * LMInputToolbar inputToolbar: the input toolbar of the frame
     */
    public void setInputToolbar(LMInputToolbar inputToolbar)
    {
        this.inputToolbar = inputToolbar;
    }

    /**
     * Adds a state to the workspace and repaints.
     *
     * State s: the state to add
     */
    public void addState(State s)
    {
        nfa.addState(s);
        repaint();
    }

    /**
     * Adds a transition between states and repaints.
     *
     * Transition t: the transition to add
     * State sf: the initial state ('from state')
     * State st: the final state ('to state')
     */
    public void addTransition(Transition t, State sf, State st)
    {
        sf.addTransitionOut(t);
        st.addTransitionIn(t);
        repaint();
    }

    /**
     * Sets the current tool being used in the workspace and highlights that
     * tool in the toolbar.
     *
     * int x: the ID of the tool to use
     */
    public void setTool(int x)
    {
        for (int i = 0; i <= 5; i++)
        {
            JPanel toolButtonPanel = toolbar.getToolButtonPanel(tool);
            toolButtonPanel.setBorder(new LineBorder(new Color(237, 237, 237), 2));
        }

        if (x >= 0 && x <= 5)
        {
            tool = x;
            JPanel toolButtonPanel = toolbar.getToolButtonPanel(tool);
            toolButtonPanel.setBorder(new LineBorder(Color.GRAY, 2));
            requestFocusInWindow();
        }

        else
        {
            messagePanel.setText("ERROR: Bad tool int, how?");
        }
    }

    /**
     * Paints the current NFA (states, transitions, and rules). Called by
     * repaint().
     */
    public void paint(Graphics g)
    {
        g.setColor(Color.DARK_GRAY);
        g.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
        g.setColor(Color.WHITE);
        g.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 10, 10);

        for (State s : nfa.getStates())
        {
            s.draw(g);
        }
        for (State s : nfa.getStates())
        {
            for (Transition t : s.getTransitionsOut())
            {
                t.draw(g);
            }
        }
        for (State s : nfa.getStates())
        {
            for (Transition t : s.getTransitionsOut())
            {
                t.drawRule(g);
            }
        }
    }

    /**
     * Performs an action when the mouse is pressed. The action is determined
     * by the current tool. Part of the MouseListener implementation.
     *
     * MouseEvent e: the event generate by the mouse press
     */
    public void mousePressed(MouseEvent e)
    {
        int x = Math.min(getWidth() - State.RADIUS - 5, Math.max(State.RADIUS + 5, e.getX()));
        int y = Math.min(getHeight() - State.RADIUS - 5, Math.max(State.RADIUS + 5, e.getY()));

        unselectAll();
        inputToolbar.clearAcceptanceText();
        messagePanel.clearText();

        if (tool == SELECT_TOOL)
        {
            selectStateOrTransitionWithinRadius(x, y);
        }
        else if (tool == STATE_TOOL)
        {
            createStateOrConvertStateWithinRadius(x, y);
        }
        else if (tool == ACCEPT_STATE_TOOL)
        {
            createAcceptStateOrConvertStateWithinRadius(x, y);
        }
        else if (tool == TRANSITION_TOOL_INITIAL)
        {
            selectInitialStateForTransition(x, y);
        }
        else if (tool == TRANSITION_TOOL_FINAL)
        {
            selectFinalStateForTransitionOrEditExistingTransition(x, y);
        }
        else if (tool == DELETE_TOOL)
        {
            deleteStateOrTransition(x, y);
        }
    }

    /**
     * Performs an action when the mouse is dragged. Part of the MouseMotionListener
     * implementation.
     *
     * MouseEvent e: the event generated by the mouse drag
     */
    public void mouseDragged(MouseEvent e)
    {
        int x = Math.min(getWidth() - State.RADIUS - 5, Math.max(State.RADIUS + 5, e.getX()));
        int y = Math.min(getHeight() - State.RADIUS - 5, Math.max(State.RADIUS + 5, e.getY()));

        if (tool == SELECT_TOOL || tool == STATE_TOOL ||
            tool == ACCEPT_STATE_TOOL)
        {
            dragState(x, y);
        }
    }

    /**
     * Uses the distance forumla to find a state within the given radius of the
     * given point.
     *
     * int x: the x coordinate of the point
     * int y: the y coordinate of the point
     * int r: the radius to search
     *
     * Returns a state if one is found and null otherwise.
     */
    private State findStateInRadius(int x, int y, int r)
    {
        for (State s : nfa.getStates())
        {
            if (Math.sqrt(Math.pow(x - s.getXPos(), 2) + Math.pow(y - s.getYPos(), 2)) < r)
            {
                return s;
            }
        }
        return null;
    }

    /**
     * Uses the distance formula to find a select circle of a transition within
     * the given radius of the given point.
     *
     * int x: the x coordinate of the point
     * int y: the y coordinate of the point
     * int r: the radius to search
     *
     * Returns a transition if one is found and null otherwise.
     */
    private Transition findTransitionInRadius(int x, int y, int r)
    {
        for (State s : nfa.getStates())
        {
            for (Transition t : s.getTransitionsOut())
            {
                if (Math.sqrt(Math.pow(x - t.getSelectOvalX(), 2) + Math.pow(y - t.getSelectOvalY(), 2)) < r)
                {
                    return t;
                }
            }
        }
        return null;
    }

    /**
     * Finds a transition between two given states.
     *
     * State from: the first state
     * State to: the second state
     *
     * Returns a transition if one is found and null otherwise.
     */
    public Transition findTransitionBetween(State from, State to)
    {
        for (Transition t : from.getTransitionsOut())
        {
            if (t.getToState() == to)
            {
                return t;
            }
        }
        return null;
    }

    /**
     * Unselects any selected object in the workspace. Called whenever the
     * mouse is pressed in the workspace. Repaints the workspace.
     */
    public void unselectAll()
    {
        for (State s : nfa.getStates())
        {
            s.setSelected(false);
            s.setTransitionSelected(false);
            for (Transition t : s.getTransitionsOut())
            {
                t.setSelected(false);
            }
        }
        optionsPanel.changeOptions("blank");
        repaint();
    }

    /**
     * Selects an object within a certain radius of the given point. Only
     * called in mousePressed().
     *
     * int x: the x coordinate of the point
     * int y: the y coordinate of the point
     */
    private void selectStateOrTransitionWithinRadius(int x, int y)
    {
        selectedState = findStateInRadius(x, y, State.RADIUS + 2);
        Transition selectedTransition = findTransitionInRadius(x, y, Transition.SELECT_RADIUS + 2);

        if (selectedTransition != null)
        {
            selectedTransition.setSelected(true);
            optionsPanel.changeOptions("transition");
            optionsPanel.getTransitionToolbar().setTransitionInfo(selectedTransition);
            optionsPanel.getTransitionToolbar().getRuleTextField().requestFocusInWindow();
        }

        else if (selectedState != null)
        {
            selectedState.setSelected(true);
            optionsPanel.changeOptions("state");
            optionsPanel.getStateToolbar().setStateInfo(selectedState);
            requestFocusInWindow();
        }
    }

    /**
     * Creates a new (non-accept) state or selects a state if you click on one.
     * Converts accept states to non-accept states. Only called in mousePressed().
     *
     * int x: the x coordinate of the clicked point
     * int y: the y coordinate of the clicked point
     */
    private void createStateOrConvertStateWithinRadius(int x, int y)
    {
        if (findStateInRadius(x, y, 30) == null && findTransitionInRadius(x, y, 35) == null)
        {
            selectedState = new State(x, y, false);
            addState(selectedState);
            optionsPanel.changeOptions("state");
            optionsPanel.getStateToolbar().setStateInfo(selectedState);
            modified = true;
            messagePanel.setFileModified();
        }
        else if (findStateInRadius(x, y, 30) != null)
        {
            selectedState = findStateInRadius(x, y, 30);
            if (selectedState.isAcceptState())
            {
                selectedState.setAcceptState(false);
            }
            selectedState.setSelected(true);
            optionsPanel.changeOptions("state");
            optionsPanel.getStateToolbar().setStateInfo(selectedState);
            modified = true;
            messagePanel.setFileModified();
        }
        else
        {
            messagePanel.setText("ERROR: Don't overlap these objects.");
        }
        requestFocusInWindow();
    }

    /**
     * Creates a new accept state or selects a state if you click on one.
     * Converts non-accept states to accept states. Only called in mousePressed().
     *
     * int x: the x coordinate of the clicked point
     * int y: the y coordinate of the clicked point
     */
    private void createAcceptStateOrConvertStateWithinRadius(int x, int y)
    {
        if (findStateInRadius(x, y, 60) == null && findTransitionInRadius(x, y, 35) == null)
        {
            selectedState = new State(x, y, true);
            addState(selectedState);
            optionsPanel.changeOptions("state");
            optionsPanel.getStateToolbar().setStateInfo(selectedState);
            modified = true;
            messagePanel.setFileModified();
        }
        else if (findStateInRadius(x, y, 30) != null)
        {
            selectedState = findStateInRadius(x, y, 30);
            if (!selectedState.isAcceptState())
            {
                selectedState.setAcceptState(true);
            }
            selectedState.setSelected(true);
            optionsPanel.changeOptions("state");
            optionsPanel.getStateToolbar().setStateInfo(selectedState);
            modified = true;
            messagePanel.setFileModified();
        }
        else
        {
            messagePanel.setText("ERROR: Don't overlap objects.");
        }
        requestFocusInWindow();
    }

    /**
     * Selects the first state when drawing a transition. Also allows selecting
     * existing transitions. Only called in mousePressed().
     *
     * int x: the x coordinate of the clicked point
     * int y: the y coordinate of the clicked point
     */
    private void selectInitialStateForTransition(int x, int y)
    {
        initialState = findStateInRadius(x, y, 25);
        Transition selectedTransition = findTransitionInRadius(x, y, Transition.SELECT_RADIUS + 2);
        if (initialState != null)
        {
            tool = TRANSITION_TOOL_FINAL;
            initialState.setTransitionSelected(true);
        }
        else if (selectedTransition != null)
        {
            selectedTransition.setSelected(true);
            optionsPanel.changeOptions("transition");
            optionsPanel.getTransitionToolbar().setTransitionInfo(selectedTransition);
            optionsPanel.getTransitionToolbar().getRuleTextField().requestFocusInWindow();
        }
    }

    /**
     * Selects the final state when drawing a transition. Even if no state is
     * clicked, sets the tool back to the initial transition select tool. Only
     * called in mousePressed().
     *
     * int x: the x coordinate of the clicked point
     * int y: the y coordinate of the clicked point
     */
    private void selectFinalStateForTransitionOrEditExistingTransition(int x, int y)
    {
        selectedState = findStateInRadius(x, y, 25);        
        if (selectedState != null)
        {
            Transition transition = findTransitionBetween(initialState, selectedState);
            if (transition != null)
            {
                transition.setSelected(true);
                messagePanel.setText("You may modify this transition in the Transition Options panel.");
                optionsPanel.changeOptions("transition");
                optionsPanel.getTransitionToolbar().setTransitionInfo(transition);
                optionsPanel.getTransitionToolbar().getRuleTextField().requestFocusInWindow();
            }
            else 
            {
                transition = new Transition(initialState, selectedState);
                Transition oppositeTransition = findTransitionBetween(selectedState, initialState);
                if (oppositeTransition != null && selectedState != initialState)
                {
                    transition.setTangent(true);
                    oppositeTransition.setTangent(true);
                }
                addTransition(transition, initialState, selectedState);
                optionsPanel.changeOptions("transition");
                optionsPanel.getTransitionToolbar().setTransitionInfo(transition);
                optionsPanel.getTransitionToolbar().getRuleTextField().requestFocusInWindow();
                modified = true;
                messagePanel.setFileModified();
            }
        }
        tool = TRANSITION_TOOL_INITIAL;
    }

    /**
     * Deletes the clicked object. Only called in mousePressed().
     *
     * int x: the x coordinate of the clicked point
     * int y: the y coordinate of the clicked point
     */
    private void deleteStateOrTransition(int x, int y)
    {
        selectedState = findStateInRadius(x, y, State.RADIUS + 2);
        Transition selectedTransition = findTransitionInRadius(x, y, Transition.SELECT_RADIUS + 2);
        if (selectedTransition != null)
        {
            if (selectedTransition.isTangent())
            {
                Transition oppositeTransition = findTransitionBetween(selectedTransition.getToState(), selectedTransition.getFromState());
                oppositeTransition.setTangent(false);
            }
            nfa.removeTransition(selectedTransition);
            modified = true;
            messagePanel.setFileModified();
        }

        else if (selectedState != null && selectedState != nfa.getStartState())
        {
            nfa.removeState(selectedState);
            modified = true;
            messagePanel.setFileModified();
        }
        requestFocusInWindow();
    }

    /**
     * Drags the clicked state to a new point. Only called in mouseDragged().
     *
     * int x: the x coordinate of the new point
     * int y: the y coordinate of the new point
     */
    private void dragState(int x, int y)
    {
        if (selectedState != null)
        {
            selectedState.move(x, y);
            for (Transition t : selectedState.getTransitionsOut())
            {
                t.moveStart();
            }
            for (Transition t : selectedState.getTransitionsIn())
            {
                t.moveEnd();
            }
            modified = true;
            messagePanel.setFileModified();
            repaint();
        }
    }

    /**
     * Unimplemented MouseListener and MouseMotionListener methods.
     */
    public void mouseMoved(MouseEvent e) {}

    public void mouseReleased(MouseEvent e) {}

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}