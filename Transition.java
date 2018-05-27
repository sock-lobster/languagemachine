import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 * 
 * Transition is the class for creating transitions between states.
 * This class handles both the visual aspects of transitions ie drawing
 * them, and thier workings within an NFA ie tracking thier rules and the
 * state they are a transition to.
 * 
 * Transition contains:
 * - an int SELECT_RADIUS: the pixel radius of the selctor circle
 * - a double ARROW_ANGLE: half the angle of the pointy end of the arrows 
 * - an int ARROW_LENGTH: the pixel length of the long edges of the arrows
 * - an int startXPos: the x position of the center of the state this 
 *      transition comes out of.
 * - an int startYPos: the y position of the center of the state this 
 *      transition comes out of.
 * - an int endXPos: the x position of the center of the state this 
 *      transition goes to.
 * - an int endYPos: the y position of the center of the state this 
 *      transition goes to.
 * - an int selectOvalX: the x position of the center of the selector circle
 * - an int selectOvalY: the y position of the center of the selector circle
 * - a State fromState: the state this transition comes out of.
 * - a State toState: the state this transition goes to.\
 * - an ArrayList of rules rules: a collection of all the rules that make
 *      this transition be followed.
 * - a String ruleDisplay: a string representation of the transition's rules
 * - a boolean selected: knows if the user is looking at this transition
 * - a boolean tangent: knows if there is another transition going the 
 *      opposite direction from this transition between this transitions two
 *      states.  It is called tangent because when this is the case, we draw
 *      transitions differently so they are easier to see. 
 */

public class Transition
{

    public static final int SELECT_RADIUS = 8;
    public static double ARROW_ANGLE = Math.PI/8;
    public static int ARROW_LENGTH = 20;

    private int startXPos;
    private int startYPos;
    private int endXPos;
    private int endYPos;
    private int selectOvalX;
    private int selectOvalY;
    private State fromState;
    private State toState;
    private ArrayList<Rule> rules;
    private String ruleDisplay;
    private boolean selected;
    private boolean tangent;

    /**
     * Constructor for Transition.
     *
     * State from: the state the transition starts at.
     * State to: the state the transition goes to.
     */
    public Transition(State from, State to)
    {
        fromState = from;
        toState = to;
        startXPos = fromState.getXPos();
        startYPos = fromState.getYPos();
        endXPos = toState.getXPos();
        endYPos = toState.getYPos();
        rules = new ArrayList<Rule>();
        ruleDisplay = "";
        selected = true;
        tangent = false;
    }

    /**
     * Returns the x coordinate of the selector "button".
     */
    public int getSelectOvalX()
    {
        return selectOvalX;
    }

    /**
     * Returns the y coordinate of the selection "button".
     */
    public int getSelectOvalY()
    {
        return selectOvalY;
    }

    /**
     * Returns the state the transition is starting at.
     */
    public State getFromState()
    {
        return fromState;
    }

    /**
     * Returns the state the transition is ending at.
     */
    public State getToState()
    {
        return toState;
    }

    /**
     * Returns an array list of rules the transition is storing about itself.
     */
    public ArrayList<Rule> getRules()
    {
        return rules;
    }

    /**
     * Returns whether or not the transition is tangent to its states, which 
     * is only the case if there is another transition going the other
     * direction between the same two states.  Such transitions are drawn
     * tangent to their states so that they are more visible.
     */
    public boolean isTangent()
    {
        return tangent;
    }

    /**
     * Sets the transition to being selected or not selected.
     *
     * boolean b: true if setting selected, false if not selected
     */
    public void setSelected(boolean b)
    {
        selected = b;
    }

    /**
     * Sets the transition to being drawn tangent to its states or not.
     *
     * boolean b: true if drawing tangent false if not.
     */
    public void setTangent(boolean b)
    {
        tangent = b;
    }

    /**
     * Updates the LinkedHashSet of rules and sets the ruleDisplay string to 
     * a propperly formatted string representing the transition's rules
     *
     * String ruleString: a string representing the transition's rules 
     */
    public void buildRulesFromString(String ruleString)
    {
        rules.clear();

        ruleString = ruleString.replaceAll(", ", "");
        ruleString = ruleString.replaceAll(" ", "");
        ruleString = ruleString.replaceAll(",", "");

        LinkedHashSet<String> ruleSet = new LinkedHashSet<String>();
        for (int i = 0; i < ruleString.length(); i++)
        {
            ruleSet.add(Character.toString(ruleString.charAt(i)));
        }

        for (String s : ruleSet)
        {
            Rule r = new Rule(s.replaceAll(" ", ""));
            rules.add(r);
        }
        ruleDisplay = buildStringFromRules();
    }

    /**
     * Writes a propperly formatted string describing the transition's rules.
     * 
     * Returns the propperly formatted string.
     */
    public String buildStringFromRules()
    {
        String ruleString = "";
        if (!rules.isEmpty())
        {
            for (Rule rule : rules)
            {
                ruleString += rule.getInputChar() + ", ";
            }
            ruleString = ruleString.substring(0, ruleString.length() - 2);
        }
        return ruleString;
    }

    /**
     * Calls appropriate specific draw methods for the transition based 
     * on its characteristics and orientation in the workspace.
     *
     * Graphics g: graphics handler that draws in the workspace.
     */
    public void draw(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));

        if (selected)
        {
            g2D.setColor(Color.gray);
        }
        if (fromState == toState)
        {
            drawHelperSelfTransition(g2D);
        }
        else if (startYPos < endYPos)
        {
            drawHelper(g2D, 1);
        }
        else if (startYPos > endYPos)
        {
            drawHelper(g2D, -1);
        }
        else if (startYPos == endYPos)
        {
            if (startXPos > endXPos)
            {
                drawHelper(g2D, -1);
            }
            else if (startXPos < endXPos)
            {
                drawHelper(g2D, 1);
            }
        }
    }

    /**
     * Draws transitions between two different states accounting for all 
     * possible combinations of orientations and traits like tangentness.
     * 
     * Graphics2D g2D: 2D graphics handler that draws in the workspace
     * int sign: either 1 or -1.  this helps deal with trigonometry relating
     *      to the transition's orientation
     */
    private void drawHelper(Graphics2D g2D, int sign)
    {
        double theta = Math.PI/2;
        if (endYPos != startYPos)
        {
            theta = Math.atan((double) (endXPos - startXPos) / (double) (endYPos - startYPos));
        }
        if (tangent)
        {
            // we actually mean 45 degrees from tangent.
            theta += Math.PI/4;
        }
        int startx = (int) (startXPos + sign*State.RADIUS*Math.sin(theta));
        int starty = (int) (startYPos + sign*State.RADIUS*Math.cos(theta));
        if (tangent)
        {
            sign = - sign;
            theta += Math.PI/2;
        }
        int endx = (int) (endXPos - sign*State.RADIUS*Math.sin(theta));
        int endy = (int) (endYPos - sign*State.RADIUS*Math.cos(theta));

        selectOvalX = (2 * startx + endx * 3)/5;
        selectOvalY = (2 * starty + endy * 3)/5;

        g2D.drawLine(startx, starty, endx, endy);
        g2D.fillOval(selectOvalX - SELECT_RADIUS, selectOvalY - SELECT_RADIUS, 2*SELECT_RADIUS, 2*SELECT_RADIUS);
        drawArrowhead(g2D, endx, endy, sign);
        g2D.setColor(Color.gray);
        g2D.drawOval(selectOvalX - SELECT_RADIUS, selectOvalY - SELECT_RADIUS, 2*SELECT_RADIUS, 2*SELECT_RADIUS);
        g2D.setColor(Color.black);
    }

    /**
     * Draws transitions that loop over one state.
     * 
     * Graphics2D g2D: 2D graphics handler that draws in the workspace
     */
    public void drawHelperSelfTransition(Graphics2D g2D)
    {
        g2D.drawOval(startXPos - 15, startYPos - 68, 30, 40);
        g2D.fillOval(startXPos - SELECT_RADIUS, startYPos - 73, 2*SELECT_RADIUS, 2*SELECT_RADIUS);
        drawArrowhead(g2D, endXPos, endYPos - State.RADIUS, 1);
        g2D.setColor(Color.gray);
        g2D.drawOval(startXPos - SELECT_RADIUS, startYPos - 73, 2*SELECT_RADIUS, 2*SELECT_RADIUS);
        g2D.setColor(Color.black);

        selectOvalX = startXPos;
        selectOvalY = startYPos - State.RADIUS - 40;
    }

    /**
     * Draws arrow heads on transitions to indicate its direction.
     * 
     * Graphics2D g2D: 2D graphics handler that draws in the workspace
     * int endx: the x position of the arrow end of the transition
     * int endy: the y position of the arrow end of the transition
     * int sign: either 1 or -1 to helps deal with trigonometry relating
     *      to the transition's orientation
     */
    public void drawArrowhead(Graphics2D g2D, int endx, int endy, int sign)
    {
        double theta = Math.PI/2;
        if (fromState == toState)
        {
            theta = - Math.PI/4;
        }
        else if (endYPos != startYPos)
        {
            theta = Math.atan((double) (endXPos - startXPos) / (double) (endYPos - startYPos));
        }
        if (tangent)
        {
            sign = - sign;
        }
        int point1x = (int) (endx - sign * ARROW_LENGTH * Math.sin(theta - ARROW_ANGLE));
        int point1y = (int) (endy - sign * ARROW_LENGTH * Math.cos(theta - ARROW_ANGLE));
        int point2x = (int) (endx - sign * ARROW_LENGTH * Math.sin(theta + ARROW_ANGLE));
        int point2y = (int) (endy - sign * ARROW_LENGTH * Math.cos(theta + ARROW_ANGLE));

        int[] xpoints = new int[3];
        xpoints[0] = endx;
        xpoints[1] = point1x;
        xpoints[2] = point2x;

        int[] ypoints = new int[3];
        ypoints[0] = endy;
        ypoints[1] = point1y;
        ypoints[2] = point2y;      

        g2D.fillPolygon(xpoints, ypoints, 3);
    }

    /**
     * Prints the ruleDisplay string on transitions above their 
     * selector circle.
     * 
     * Graphics g: graphics handler that draws in the workspace.
     */
    public void drawRule(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(3));
        FontMetrics metrics = g2D.getFontMetrics(g2D.getFont());
        int strWidth = metrics.stringWidth(ruleDisplay);
        int strHeight = metrics.getHeight();
        g2D.setColor(Color.white);
        g2D.fillRect(selectOvalX - (strWidth / 2), selectOvalY - SELECT_RADIUS - strHeight + 1, strWidth, strHeight - 2);
        g2D.setColor(Color.black);
        g2D.drawString(ruleDisplay, selectOvalX - (strWidth / 2), selectOvalY - SELECT_RADIUS - 4);
    }

    /**
     * Moves the start of the transition.
     */
    public void moveStart()
    {
        startXPos = fromState.getXPos();
        startYPos = fromState.getYPos();
    }

    /**
     * Moves the end of the transition.
     */
    public void moveEnd()
    {
        endXPos = toState.getXPos();
        endYPos = toState.getYPos();
    }
}