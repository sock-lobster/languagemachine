import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import javax.swing.JOptionPane;
import javax.swing.JFileChooser;
import java.awt.Component;
import java.lang.NumberFormatException;

/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 *
 * Stores states and transitions and implements an NFA.
 *
 * ArrayList<State> states: the arraylist containing all the states of the NFA
 * StartState startState: the start state of the NFA
 */
public class NFA
{
    private ArrayList<State> states = new ArrayList<State>();
    private StartState startState;

    /**
     * Constructor for NFA. Initializes the NFA with the given start state.
     *
     * StartState startState: the start state to set for the NFA
     */
    public NFA(StartState startState)
    {
        this.startState = startState;
        addState(startState);
    }

    /**
     * Alternate constructor for NFA. Used when loading an NFA from file.
     */
    public NFA() {}

    /**
     * Returns the arraylist of states.
     */
    public ArrayList<State> getStates()
    {
        return states;
    }

    /**
     * Returns the start state of the NFA.
     */
    public State getStartState()
    {
        return startState;
    }

    /**
     * Adds a state to the NFA.
     *
     * State s: the state to add to the NFA
     */
    public void addState(State s)
    {
        states.add(s);
    }

    /**
     * Removes a state from the NFA. Removing a state also requires that all
     * transitions too and from that state are removed.
     *
     * State s: the state to remove from the NFA
     */
    public void removeState(State s)
    {
        for (Transition t : s.getTransitionsOut())
        {
            State toState = t.getToState();
            toState.getTransitionsIn().remove(t);
        }
        for (Transition t : s.getTransitionsIn())
        {
            State fromState = t.getFromState();
            fromState.getTransitionsOut().remove(t);
        }
        states.remove(s);
    }

    /**
     * Removes a transition from the NFA by removing the transition from both
     * its initial state and final state.
     *
     * Transition t: the transition to remove from the NFA
     */
    public void removeTransition(Transition t)
    {
        for (State s : states)
        {
            s.getTransitionsOut().remove(t);
            s.getTransitionsIn().remove(t);
        }
    }

    /**
     * Wrapper for recursive run() method. Parses the given input string into
     * an arraylist of single character strings and passes that list and the
     * start state to the recursive method.
     *
     * String inputString: the string to run the NFA on
     */
    public boolean run(String inputString)
    {
        ArrayList<String> inputList = new ArrayList<String>();
        for (int i = 0; i < inputString.length(); i++)
        {
            inputList.add(Character.toString(inputString.charAt(i)));
        }
        return run(startState, inputList);
    }

    /**
     * Runs the NFA. Recursively tries paths through the NFA until an accepting
     * path is found. If one is found, returns true and otherwise returns false.
     *
     * State state: the current state in the recursion
     * ArrayList<String> inputList: the current list of characters in the input
     *
     * Returns true if the NFA accepts the input string and false otherwise.
     */
    public boolean run(State state, ArrayList<String> inputList)
    {
        boolean branchAccepts;
        // see if there is an epsilon from here before going to base case
        if (inputList.size() == 0)
        {
            if (state.isAcceptState())
            {
                return true;
            }
            for (Transition t : state.getTransitionsOut())
            {
                for (Rule r : t.getRules())
                {
                    // epsilon is U+025B
                    if (r.getInputChar().equals("\u025B") && t.getFromState() != t.getToState())
                    {
                        // recurse, consuming no input for the epsilon transition
                        branchAccepts = run(t.getToState(), inputList);
                        if (branchAccepts)
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
        else
        {
            for (Transition t : state.getTransitionsOut())
            {
                for (Rule r : t.getRules())
                {
                    if (r.getInputChar().equals("\u025B") && t.getFromState() != t.getToState())
                    {
                        // recurse, consuming no input for the epsilon transition
                        branchAccepts = run(t.getToState(), inputList);
                        if (branchAccepts)
                        {
                            return true;
                        }
                    }
                    else if (r.getInputChar().equals(inputList.get(0)))
                    {
                        // recurse, consuming the first character of the input list
                        ArrayList<String> nextInput = new ArrayList<String>();
                        nextInput.addAll(inputList);
                        nextInput.remove(0);
                        branchAccepts = run(t.getToState(), nextInput);
                        if (branchAccepts)
                        {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    /**
     * Saves the NFA with the name filename. File existence problems are dealt
     * with in LMMenubar, so the given filename should be good. Encodes in UTF-8.
     *
     * Format of saved files:
     * startstate xpos ypos accept
     * state xpos ypos accept
     * ...
     * #
     * transition from to tangent rulseString
     * ...
     * For transitions, from and to are indexes into the state list (based on
     * the order the states are entered).
     *
     * String filename: the name to give the file
     */
    public void save(String filename)
    {
        try
        {
            // initialize output stream
            File file = new File(filename);
            FileOutputStream fop = new FileOutputStream(file);
            OutputStreamWriter out = new OutputStreamWriter(fop, "utf-8");

            // write start state
            int x = startState.getXPos();
            int y = startState.getYPos();
            boolean acceptState = startState.isAcceptState();

            String line = "startstate " + x + " " + y + " " + acceptState + "\n";

            out.write(line);
            
            // write the rest of the states
            for (int i = 1; i < states.size(); i++)
            {
                x = states.get(i).getXPos();
                y = states.get(i).getYPos();
                acceptState = states.get(i).isAcceptState();

                line = "state " + x + " " + y + " " + acceptState + "\n";

                out.write(line);
            }

            // write state/transition separator
            line = "#\n";

            out.write(line);

            // write transitions 
            for (State s : states)
            {
                for (Transition t : s.getTransitionsOut())
                {
                    int from = states.indexOf(t.getFromState());
                    int to = states.indexOf(t.getToState());
                    boolean tangent = t.isTangent();
                    String ruleString = t.buildStringFromRules().replaceAll(" ", "");

                    line = "transition " + from + " " + to + " " + tangent + " " + ruleString + "\n";

                    out.write(line);
                }
            }

            // flush the buffer and close the file
            out.flush();
            out.close();
        }
        catch (IOException e)
        {
            // this should be caught in LMMenubar, though it might be possible to
            // receive a permission error
            System.err.println("Something failed while saving: " + e.getMessage());
        }
    }

    /**
     * Loads a previously saved NFA. Returns false if an NFA can't be loaded
     * from the specified filename. Attempts to decode from UTF-8.
     *
     * Returns true if the file is loaded successfully and false otherwise.
     */
    public boolean load(String filename)
    {
        Scanner scanner;
        File file = new File(filename);

        try
        {
            scanner = new Scanner(file, "utf-8");
        }
        catch (IOException e)
        {   
            // should be caught in LMMenubar, though it might be possible to
            // receive a permission error
            System.err.println("Something failed while loading: " + e.getMessage());
            return false;
        }

        // construct start state
        if (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String[] args = line.split(" ");

            if (args.length != 4)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nLength of arguments expected to be 4, found " + args.length,
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!args[0].equals("startstate"))
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nExpected 'startstate', found " + args[0],
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int x, y;
            try
            {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nUnable to parse string as integer.",
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            boolean acceptState = Boolean.parseBoolean(args[3]);

            StartState start = new StartState(x, y, acceptState);
            addState(start);
            startState = start;
        }

        // construct other states
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            
            if (line.equals("#"))
            {
                break;
            }

            String[] args = line.split(" ");
            
            if (args.length != 4)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nLength of arguments expected to be 4, found " + args.length,
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!args[0].equals("state"))
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nExpected 'state', found " + args[0],
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            int x, y;
            try
            {
                x = Integer.parseInt(args[1]);
                y = Integer.parseInt(args[2]);
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nUnable to parse string as integer.",
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }
            boolean acceptState = Boolean.parseBoolean(args[3]);

            State state = new State(x, y, acceptState);
            state.setSelected(false);
            addState(state);
        }

        // construct transitions
        while (scanner.hasNextLine())
        {
            String line = scanner.nextLine();
            String[] args = line.split(" ");

            if (args.length != 4 && args.length != 5)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nLength of arguments expected to be 4 or 5, found " + args.length,
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            if (!args[0].equals("transition"))
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nExpected 'transition', found " + args[0],
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            State from, to;
            try
            {
                from = states.get(Integer.parseInt(args[1]));
                to = states.get(Integer.parseInt(args[2]));
            }
            catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog((Component) null,
                        "Cannot open specified file due to invalid file format.\nUnable to parse string as integer.",
                        "Invalid file format",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            Transition transition = new Transition(from, to);
            if (args.length == 5)
            {
                transition.buildRulesFromString(args[4]);
            }
            transition.setTangent(Boolean.parseBoolean(args[3]));
            transition.setSelected(false);
            from.addTransitionOut(transition);
            to.addTransitionIn(transition);
        }
        return true;
    }
}