import javax.swing.*;
import javax.swing.border.*;
import java.awt.*; 
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 * 
 * LMApplication is the class that is called when starting up the Language
 * Machine.  It creates one of each large GUI component used by the 
 * Language Machine, and gives each of them the instance of each other 
 * large component it needs. Finally it packs them into a frame for the 
 * user to interact with. 
 */

public class LMApplication
{
    /**
     * The Language Machine's main method that builds the window and 
     * runs the program.
     */
    public static void main(String[] args)
    {
        // Tries to make the application look like it belongs within the 
        // operating system you are using.
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (java.lang.ClassNotFoundException e) {}
        catch (java.lang.InstantiationException e) {}
        catch (java.lang.IllegalAccessException e) {}
        catch (javax.swing.UnsupportedLookAndFeelException e) {}

        // Creates the workspace you draw in.
        // (center of frame)
        LMWorkspacePanel workspace = new LMWorkspacePanel();
        workspace.setPreferredSize(new Dimension(500, 500));

        // Creates the toolbar.
        // (left of frame)
        LMToolbar toolbar = new LMToolbar();
        toolbar.setPreferredSize(new Dimension(150, 200));

        // Creates the panel that object specific toolbars appear in.
        // (right of frame)
        LMOptionsPanel options = new LMOptionsPanel(new CardLayout());
        options.setPreferredSize(new Dimension(250, 250));

        // Creates the string tester area.
        // (top of frame)
        LMInputToolbar inputToolbar = new LMInputToolbar();
        inputToolbar.setPreferredSize(new Dimension(900, 50));

        // Creates the message bar.
        // (bottom of frame)
        LMMessagePanel messagePanel = new LMMessagePanel();
        messagePanel.setPreferredSize(new Dimension(900, 30));

        // Creates the menu bar.
        // (very top of frame)
        LMMenubar menu = new LMMenubar();

        // Gives the menu bar instances of the other GUI objects it needs.
        menu.setWorkspacePanel(workspace);
        menu.setOptionsPanel(options);
        menu.setInputToolbar(inputToolbar);
        menu.setMessagePanel(messagePanel);
 
        // Gives the workspace instances of the other GUI objects it needs.       
        workspace.setInputToolbar(inputToolbar);
        workspace.setToolbar(toolbar);
        workspace.setOptionsPanel(options);
        workspace.setMessagePanel(messagePanel);
        
        // Gives the toolbar instances of the other GUI objects it needs.       
        toolbar.setInputToolbar(inputToolbar);
        toolbar.setWorkspacePanel(workspace);
        toolbar.setOptionsPanel(options);
        toolbar.setMessagePanel(messagePanel);
 
        // Gives the options panel instances of the other GUI objects it needs.              
        options.setInputToolbar(inputToolbar);
        options.setWorkspacePanel(workspace);
        options.setMessagePanel(messagePanel);
        
        // Gives the test string input area instances of GUI objects in needs.
        inputToolbar.setWorkspacePanelAndNFA(workspace);
        inputToolbar.setMessagePanel(messagePanel);

        // Organizes all the large GUI components into a panel.
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(workspace, BorderLayout.CENTER);
        panel.add(toolbar, BorderLayout.LINE_START);
        panel.add(options, BorderLayout.LINE_END);
        panel.add(inputToolbar, BorderLayout.PAGE_START);
        panel.add(messagePanel, BorderLayout.PAGE_END);
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Packs all the large GUI components into the frame with which the 
        // user interacts.
        JFrame frame = new JFrame();
        frame.add(panel);
        frame.setJMenuBar(menu);
        frame.pack();
        frame.setLocation(200, 100);
        frame.setTitle("Language Machine");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(menu);

        // Puts the initial focus somewhere it won't be distracting.
        workspace.requestFocusInWindow();

        // Lets the user see the Language Machine.
        frame.setVisible(true);
    }
}
