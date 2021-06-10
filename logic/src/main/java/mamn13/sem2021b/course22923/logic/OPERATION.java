package mamn13.sem2021b.course22923.logic;

public enum OPERATION {
	
    ENCRYPT("e"), DECRYPT("d");

    private String alias;

    OPERATION(String alias) {
        this.alias = alias;
    }

    public static OPERATION getOperation(String operation) {
        for (OPERATION op : OPERATION.values()) {
            if (op.name().equalsIgnoreCase(operation) || op.getAlias().equalsIgnoreCase(operation)) {
                return op;
            }
        }
        throw new IllegalArgumentException(operation);
    }

    private String getAlias() {
        return alias;
    }
}
