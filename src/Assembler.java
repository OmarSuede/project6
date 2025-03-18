import java.io.FileWriter;
import java.io.IOException;

public class Assembler {
    private Parser parser;
    private SymbolTable symbolTable;
    private String inputFile;
    private String outputFile;
    private int nextVariableAddress;

    // Constructor: Initializes the assembler with input and output files
    public Assembler(String inputFile, String outputFile) {
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.symbolTable = new SymbolTable();
        this.nextVariableAddress = 16; // Start assigning variables from address 16
    }

    // Main method to run the assembler
    public void assemble() {
        firstPass(); // First pass: Populate the symbol table with labels
        secondPass(); // Second pass: Translate assembly code to binary
    }

    // First pass: Populates the symbol table with labels
    private void firstPass() {
        parser = new Parser(inputFile);
        int instructionAddress = 0;

        while (parser.hasMoreLines()) {
            parser.advance();

            if (parser.instructionType() == Parser.InstructionType.L_INSTRUCTION) {
                // Add the label to the symbol table with the current instruction address
                String label = parser.symbol();
                symbolTable.addEntry(label, instructionAddress);
            } else {
                // Increment the instruction address for A and C instructions
                instructionAddress++;
            }
        }
    }

    // Second pass: Translates assembly code to binary and writes to the output file
    private void secondPass() {
        parser = new Parser(inputFile); // Reset the parser for the second pass

        try (FileWriter writer = new FileWriter(outputFile)) {
            while (parser.hasMoreLines()) {
                parser.advance();

                String binaryInstruction = "";

                if (parser.instructionType() == Parser.InstructionType.A_INSTRUCTION) {
                    // Handle A-instruction
                    binaryInstruction = translateAInstruction(parser.symbol());
                } else if (parser.instructionType() == Parser.InstructionType.C_INSTRUCTION) {
                    // Handle C-instruction
                    binaryInstruction = translateCInstruction(parser.dest(), parser.comp(), parser.jump());
                }

                // Write the binary instruction to the output file
                if (!binaryInstruction.isEmpty()) {
                    writer.write(binaryInstruction + "\n");
                }
            }
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + outputFile);
            System.exit(1);
        }
    }

    // Translates an A-instruction to binary
    private String translateAInstruction(String symbol) {
        int address;

        if (symbol.matches("\\d+")) {
            // If the symbol is a number, use it directly
            address = Integer.parseInt(symbol);
        } else {
            // If the symbol is a variable or label, look it up in the symbol table
            if (!symbolTable.contains(symbol)) {
                // Add the variable to the symbol table if it doesn't exist
                symbolTable.addEntry(symbol, nextVariableAddress);
                nextVariableAddress++;
            }
            address = symbolTable.getAddress(symbol);
        }

        // Convert the address to a 16-bit binary string
        return "0" + String.format("%15s", Integer.toBinaryString(address)).replace(' ', '0');
    }

    // Translates a C-instruction to binary
    private String translateCInstruction(String dest, String comp, String jump) {
        // Translate dest, comp, and jump to binary using the Code class
        String destBinary = Code.dest(dest);
        String compBinary = Code.comp(comp);
        String jumpBinary = Code.jump(jump);

        // Combine into a 16-bit C-instruction: 111 + comp + dest + jump
        return "111" + compBinary + destBinary + jumpBinary;
    }
}