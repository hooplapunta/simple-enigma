package edu.cmu.rchew;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static String INPUT_MESSAGE = "Input message (all caps, no spaces or punctuation):";
    private static String OUTPUT_MESSAGE = "Encoded message: " ;

    private static char[] ROTOR_1 = "OBDFHJACPRTXVZNYEIWGLKMUSQ".toCharArray();
    private static char[] ROTOR_2 = "AJDKSIRUXBLHWTMCQGZNPYFVOE".toCharArray();
    private static char[] REFLECTOR = "YRUHQSLDPXNGOKMIEBFZCWVJAT".toCharArray();

    /**
     * A class to encapsulate the functions of each rotor
     */
    private static class Rotor {
        private List<Character> output;
        private int rotations;

        /**
         * Construct a new rotor with the following output letters
         * @param output
         */
        Rotor(char[] output) {
            this.output = new ArrayList<Character>(); // not convenient to convert primitive types unfortunately
            for (char c : output) {
                this.output.add(c);
            }
            this.rotations = 0;
        }

        /**
         * Rotate the rotor to the left
         * @return true if this rotation should cause a carry in the next rotor (26th rotation)
         */
        boolean rotate() {
            this.rotations++;

            this.output.add(this.output.remove(0));

            if (rotations == 26) {
                rotations = 0;
                return true;
            } else {
                return false;
            }
        }

        /**
         * Convert the input to this rotor to the correct output
         * @param input input char
         * @return output char
         */
        Character inputToOutput(char input) {
            checkChar(input);

            // return the alphabetical index, corrected for ASCII
            return this.output.get(input - 65);
        }

        /**
         * Convert the (reflected) output back to input
         * @param output output char
         * @return input char
         */
        Character outputToInput(char output) {
            checkChar(output);

            // first find the position of that character
            int index = this.output.indexOf(output);

            if (index == -1) {
                throw new IllegalArgumentException("The reflected letter was not found!");
            } else {
                return (char)(index + 65);
            }
        }

        /**
         * Checks if this is a valid char that can be converted, throws exception if not
         * @param c the char to check
         */
        void checkChar(char c) {
            if (c < 65 || c > 90) {
                throw new IllegalArgumentException("Input must be a capital letter!");
            }
        }
    }

    public static void main(String[] args) {

        // initialize our rotors
        Rotor rotor1 = new Rotor(ROTOR_1);
        Rotor rotor2 = new Rotor(ROTOR_2);
        Rotor reflector = new Rotor(REFLECTOR);

        char[] input = getString(INPUT_MESSAGE).toCharArray();
        System.out.print(OUTPUT_MESSAGE);

        // convert the input character by character
        for (char c: input) {

            // first do rotations and carries if necessary
            boolean carry = rotor1.rotate();
            if (carry) {
                rotor2.rotate();
            }

            // going through the rotors
            // just assigning variables here for clarity, could be collapsed into a single line
            char rotor1Output = rotor1.inputToOutput(c);
            char rotor2Output = rotor2.inputToOutput(rotor1Output);
            char reflectorOutput = reflector.inputToOutput(rotor2Output);

            // then reflected back
            char rotor2Reflected = rotor2.outputToInput(reflectorOutput);
            char rotor1Reflected = rotor1.outputToInput(rotor2Reflected);

            // print the converted output
            System.out.print(rotor1Reflected);
        }

        System.out.println();
    }


    /**
     * Get the next String from user input
     *
     * @param prompt String to prompt
     * @return String from user
     */
    private static String getString(String prompt) {
        boolean hasInput = false;
        String input = "";

        while (!hasInput) {
            try {
                System.out.println(prompt);
                Scanner scanner = new Scanner(System.in);
                input = scanner.nextLine();
                hasInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input! Enter some text.");
                hasInput = false;
            }
        }

        return input;
    }
}
