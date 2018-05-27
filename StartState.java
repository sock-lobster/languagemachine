import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 * 
 * StartState inherits from state and executes the same functionality
 * as State except that it draws differently, and is always the first 
 * state to be put into and stored by an NFA. 
 * 
 * StartState contains:
 * - all the same things State contains.
 */

public class StartState extends State
{
    /**
     * Constructor for StartState.  Same as constructor for state
     * except it assumes it is not selected.
     *
     * int xPos: the x position of the center of the state.
     * int yPos: the y position of the center of the state.
     * boolean acceptState: whether or not this is an accept state. 
     */
    public StartState(int xPos, int yPos, boolean acceptState)
    {
        super(xPos, yPos, acceptState);
        selected = false;
    }

    /**
     * Draws the start state and part of the arrow leading into it.
     *
     * Graphics g: graphics handler that draws in the workspace
     */
    public void draw(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(5));

        g2D.setColor(new Color(181, 27, 224));
        g2D.drawOval(xPos - RADIUS + 4, yPos - RADIUS + 4, 2*RADIUS - 8, 2*RADIUS - 8);

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
                g2D.setColor(Color.blue);
            }
        }
        else
        {
            if (selected)
            {
                g2D.setColor(Color.gray);
            }
            else
            {
                g2D.setColor(Color.black);
            }
        }
        g2D.drawOval(xPos - RADIUS, yPos - RADIUS, 2*RADIUS, 2*RADIUS);
        g2D.setColor(Color.black);

        g2D.setStroke(new BasicStroke(3));
        g2D.drawLine(xPos - RADIUS - 50, yPos, xPos - RADIUS, yPos);
        g2D.setColor(Color.gray);
        g2D.setColor(Color.black);
        drawArrowhead(g2D, xPos - RADIUS, yPos);
    }

    /**
     * Draws the arrow head pointing into the start state.
     *
     * Graphics2D g2D: graphics handler that draws in the workspace
     * int arrowheadx: the x position the arrow points to
     * int arrowheady: the y position the arrow points to
     */
    public void drawArrowhead(Graphics2D g2D, int arrowheadx, int arrowheady)
    {
        double theta = Math.PI/2;
        
        int point1x = (int) (arrowheadx - Transition.ARROW_LENGTH * Math.sin(theta - Transition.ARROW_ANGLE));
        int point1y = (int) (arrowheady - Transition.ARROW_LENGTH * Math.cos(theta - Transition.ARROW_ANGLE));
        int point2x = (int) (arrowheadx - Transition.ARROW_LENGTH * Math.sin(theta + Transition.ARROW_ANGLE));
        int point2y = (int) (arrowheady - Transition.ARROW_LENGTH * Math.cos(theta + Transition.ARROW_ANGLE));

        int[] xpoints = new int[3];
        xpoints[0] = arrowheadx;
        xpoints[1] = point1x;
        xpoints[2] = point2x;

        int[] ypoints = new int[3];
        ypoints[0] = arrowheady;
        ypoints[1] = point1y;
        ypoints[2] = point2y;      

        g2D.fillPolygon(xpoints, ypoints, 3);
    }
}
