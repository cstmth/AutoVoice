package de.carldressler.autovoice.utilities;

public class ExecutionModeDeterminator {
    public boolean isProd(String[] args) {
        System.out.println(args[0]);
        return args.length > 1 || args[0].equals("prod");
    }
}
