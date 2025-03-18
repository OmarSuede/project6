import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Integer> symbolTable;
    private int nextVariableAddress;

    // Constructor: Initializes the symbol table with predefined symbols
    public SymbolTable() {
        symbolTable = new HashMap<>();
        nextVariableAddress = 16;

        // Add predefined symbols
        addPredefinedSymbols();
    }

    // Adds predefined symbols to the symbol table
    private void addPredefinedSymbols() {
        // R0-R15
        for (int i = 0; i < 16; i++) {
            symbolTable.put("R" + i, i);
        }

        // Other predefined symbols
        symbolTable.put("SP", 0);
        symbolTable.put("LCL", 1);
        symbolTable.put("ARG", 2);
        symbolTable.put("THIS", 3);
        symbolTable.put("THAT", 4);
        symbolTable.put("SCREEN", 16384);
        symbolTable.put("KBD", 24576);
    }

    // Adds a symbol to the symbol table
    public void addEntry(String symbol, int address) {
        symbolTable.put(symbol, address);
    }

    // Checks if the symbol table contains a given symbol
    public boolean contains(String symbol) {
        return symbolTable.containsKey(symbol);
    }

    // Returns the address associated with a symbol
    public int getAddress(String symbol) {
        return symbolTable.get(symbol);
    }

    // Returns the next available address for a variable
    public int getNextVariableAddress() {
        return nextVariableAddress++;
    }
}