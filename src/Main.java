public class Main {
    public static void main(String[] args) {
        // Check if the input file is provided
        if (args.length < 1) {
            System.out.println("Usage: java Main <input.asm>");
            return;
        }

        // Get the input file name from the command line arguments
        String inputFile = args[0];
        // Generate the output file name by replacing .asm with .hack
        String outputFile = inputFile.replace(".asm", ".hack");

        // Create an instance of the Assembler
        Assembler assembler = new Assembler(inputFile, outputFile);
        // Run the assembler to translate the assembly code to binary
        assembler.assemble();

        // Print a success message
        System.out.println("Assembly completed. Output written to " + outputFile);
    }
}