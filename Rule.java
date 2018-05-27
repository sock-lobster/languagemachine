/**
 * Written by Daniel Barter, Alby Himelick, and Grace Whitmore.
 * For CS204 - Software Design
 * 4 June 2012
 * 
 * Rule is the class that contains the individual rules that 
 * combined govern a transition's behavior.  As it is, it seems
 * silly to have such a class when it is just a single character 
 * string, but we did it this way so we could easily accomodate 
 * PDA and possibly Turing Machine rules if we wanted to. 
 * 
 * Rule contains:
 * - String inputChar: the character this rule represents.
 */

public class Rule
{
    private String inputChar;

    /**
     * Constructor for Rule.
     *
     * String inputChar: the single character string this rule represents.
     */
    public Rule(String inputChar)
    {
        this.inputChar = inputChar;
    }

    /**
     * Returns the input character
     */
    public String getInputChar()
    {
        return inputChar;
    }
}