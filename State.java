import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 *
 * Implements a state in the NFA. Handles both the drawing of the state and
 * storing of transitions for running the NFA.
 *
 * state final int RADIUS: the globally defined state radius which is used
 *      for finding states and drawing transitions
 * ints xPos, yPos: the center of the state
 * boolean acceptState: true if accept state, else false
 * boolean selected: true if last clicked object, else false
 * boolean transitionSelected: true if clicked for the initial state in a
 *      transition (uses different highlight color)
 * ArrayList<Transition>s transitionsIn, transitionsOut: lists of the
 *      transitions in and out of the state
 */
public class State
{
    public static final int RADIUS = 25;

    protected int xPos;
    protected int yPos;
    protected boolean acceptState;
    protected boolean selected;
    protected boolean transitionSelected;
    protected ArrayList<Transition> transitionsIn = new ArrayList<Transition>();
    protected ArrayList<Transition> transitionsOut = new ArrayList<Transition>();

    /**
     * Constructor for a state. Initializes a state with a position and tells
     * whether or not the state is an accept state. Sets the state to be
     * selected.
     *
     * int xPos, yPos: the center of the state
     * boolean acceptState: true if accept state and false otherwise
     */
    public State(int xPos, int yPos, boolean acceptState)
    {
        this.xPos = xPos;
        this.yPos = yPos;
        this.acceptState = acceptState;
        this.selected = true;
        this.transitionSelected = false;
    }

    /**
     * Returns the x position of the state.
     */
    public int getXPos()
    {
        return xPos;
    }

    /**
     * Returns the y position of the state.
     */
    public int getYPos()
    {
        return yPos;
    }

    /**
     * Returns true if the state is an accept state and false otherwise.
     */
    public boolean isAcceptState()
    {
        return acceptState;
    }

    /**
     * Sets the state to be an accept state or not.
     *
     * boolean b: true to be an accept state, false for not
     */
    public void setAcceptState(boolean b)
    {
        acceptState = b;
    }

    /**
     * Sets the state to be selected.
     *
     * boolean b: true to be selected, false to not
     */
    public void setSelected(boolean b)
    {
        selected = b;
    }

    /**
     * Sets the state to be selected as the first state in a transition.
     *
     * boolean b: true to be transition selected, false to not
     */
    public void setTransitionSelected(boolean b)
    {
        transitionSelected = b;
    }

    /**
     * Returns the array list of transitions pointing to the state.
     */
    public ArrayList<Transition> getTransitionsIn()
    {
        return transitionsIn;
    }

    /**
     * Returns the array list of transitions pointing away from the state.
     */
    public ArrayList<Transition> getTransitionsOut()
    {
        return transitionsOut;
    }

    /**
     * Draws the state. Color depends on class booleans.
     *
     * Graphics g: graphics object passed from paint to draw the state
     */
    public void draw(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));
        if (transitionSelected)
        {
            g2D.setColor(new Color(204, 0, 0));
        }
        else if (acceptState)
        {
            if (selected)
            {
                g2D.setColor(new Color(30, 144, 255));
            }
            else
            {
                g2D.setColor(Color.BLUE);
            }
        }
        else
        {
            if (selected)
            {
                g2D.setColor(Color.GRAY);
            }
            else
            {
                g2D.setColor(Color.BLACK);
            }
        }
        g2D.drawOval(xPos - RADIUS, yPos - RADIUS, 2*RADIUS, 2*RADIUS);
        g2D.setColor(Color.BLACK);
    }

    /**
     * Moves the state and changes the x and y coordinates of the state and
     * appropriately adjusts the x and y positions of the transitions
     * associated with the state.
     *
     * int x, y: the new location of the center of the state
     */
    public void move(int x, int y)
    {
        xPos = x;
        yPos = y;
    }

    /**
     * Adds a transition to the ArrayList of transitions pointing to the state.
     *
     * Transition t: the transition to add
     */
    public void addTransitionIn(Transition t)
    {
        transitionsIn.add(t);
    }

    /**
     * Adds a transition to the ArrayListt of transitions pointing away from the state.
     *
     * Transition t: the transition to add
     */
    public void addTransitionOut(Transition t)
    {
        transitionsOut.add(t);
    }

    /**
     * Removes a transition from the ArrayList of transitions pointing to the state.
     *
     * Transition t: the transition to remove
     */
    public void removeTransitionIn(Transition t)
    {
        transitionsIn.remove(t);
    }

    /**
     * Removes a transition from the ArrayList of transitions pointing away from the state.
     *
     * Transition t: the transition to remove
     */
    public void removeTransitionOut(Transition t)
    {
        transitionsOut.remove(t);
    }
}