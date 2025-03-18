import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Parser {
    private List<String> lines;
    private int currentLineIndex;

    public enum InstructionType {
        A_INSTRUCTION, L_INSTRUCTION, C_INSTRUCTION;
    }

    public Parser(String inputFile) {
        // Read the input file and store lines in a list
        lines = new ArrayList<>();
        currentLineIndex = -1;

        try (Scanner scanner = new Scanner(new File(inputFile))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();

                // Remove comments (anything after "//")
                int commentIndex = line.indexOf("//");
                if (commentIndex != -1) {
                    line = line.substring(0, commentIndex).trim();
                }

                // Add non-empty lines to the list
                if (!line.isEmpty()) {
                    lines.add(line);
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + inputFile);
            System.exit(1);
        }
    }

    public boolean hasMoreLines() {
        return currentLineIndex < lines.size() - 1;
    }

    public void advance() {
        if (hasMoreLines()) {
            currentLineIndex++;
        }
    }

    public InstructionType instructionType() {
        System.out.println("Current line index: " + currentLineIndex); // Debug
        System.out.println("Total lines: " + lines.size()); // Debug

        String currentLine = lines.get(currentLineIndex); // This is where the error occurs

        if (currentLine.startsWith("@")) {
            return InstructionType.A_INSTRUCTION;
        } else if (currentLine.startsWith("(") && currentLine.endsWith(")")) {
            return InstructionType.L_INSTRUCTION;
        } else {
            return InstructionType.C_INSTRUCTION;
        }
    }

    public String symbol() {
        // Extract the symbol or value from an A-instruction or L-instruction
        String currentLine = lines.get(currentLineIndex);

        if (instructionType() == InstructionType.A_INSTRUCTION) {
            return currentLine.substring(1); // Remove "@"
        } else if (instructionType() == InstructionType.L_INSTRUCTION) {
            return currentLine.substring(1, currentLine.length() - 1); // Remove "(" and ")"
        } else {
            throw new IllegalStateException("Current instruction is not an A-instruction or L-instruction.");
        }
    }

    public String dest() {
        // Extract the dest part of a C-instruction
        String currentLine = lines.get(currentLineIndex);

        if (instructionType() == InstructionType.C_INSTRUCTION) {
            int equalsIndex = currentLine.indexOf("=");
            if (equalsIndex != -1) {
                return currentLine.substring(0, equalsIndex);
            }
        }
        return ""; // No dest part
    }

    public String comp() {
        // Extract the comp part of a C-instruction
        String currentLine = lines.get(currentLineIndex);

        if (instructionType() == InstructionType.C_INSTRUCTION) {
            int equalsIndex = currentLine.indexOf("=");
            int semicolonIndex = currentLine.indexOf(";");

            if (equalsIndex != -1 && semicolonIndex != -1) {
                return currentLine.substring(equalsIndex + 1, semicolonIndex);
            } else if (equalsIndex != -1) {
                return currentLine.substring(equalsIndex + 1);
            } else if (semicolonIndex != -1) {
                return currentLine.substring(0, semicolonIndex);
            }
        }
        return ""; // No comp part
    }

    public String jump() {
        // Extract the jump part of a C-instruction
        String currentLine = lines.get(currentLineIndex);

        if (instructionType() == InstructionType.C_INSTRUCTION) {
            int semicolonIndex = currentLine.indexOf(";");
            if (semicolonIndex != -1) {
                return currentLine.substring(semicolonIndex + 1);
            }
        }
        return ""; // No jump part
    }
}