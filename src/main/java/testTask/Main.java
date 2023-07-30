package testTask;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        boolean desc = false;
        boolean string = false;
        ArrayList<String> argList;
        Options options = new Options();
        Option ascOpt = new Option("a", "asc", false, "Sort ascending");
        ascOpt.setRequired(false);
        Option descOpt = new Option("d", "desc", false, "Sort descending");
        descOpt.setRequired(false);
        Option stringOpt = new Option("s", "string", false, "Data type: string");
        stringOpt.setRequired(true);
        Option integerOpt = new Option("i", "integer", false, "Data type: integer");
        integerOpt.setRequired(true);

        OptionGroup isOptGroup = new OptionGroup();
        isOptGroup.addOption(stringOpt);
        isOptGroup.addOption(integerOpt);
        isOptGroup.setRequired(true);

        OptionGroup adOptGroup = new OptionGroup();
        adOptGroup.addOption(ascOpt);
        adOptGroup.addOption(descOpt);

        options.addOptionGroup(isOptGroup)
                .addOptionGroup(adOptGroup);
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            argList = new ArrayList<>(cmd.getArgList());
            if (cmd.hasOption('d')) {
                desc = true;
            }
            if (cmd.hasOption('s')) {
                string = true;
            }

        }
        catch (ParseException exp) {
            argList = new ArrayList<>();
            System.err.println("Argument parsing error. Reason: " + exp.getMessage());
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("app.jar", options, true);
            System.exit(0);
        }

        String outFile = argList.remove(0);
        if (string) {
            mergeFiles(argList, outFile, desc, String.class);
        } else {
            mergeFiles(argList, outFile, desc, Integer.class);
        }
    }


    private static <T> T readNextObj(Class<T> type, BufferedReader reader, String inName) {
        try {
            if (type == Integer.class)
                while (true)
                    try {
                        if (reader.ready()) {
                            return type.cast(Integer.parseInt(reader.readLine()));
                        } else return null;
                    } catch (NumberFormatException e) {
                        System.err.println("The file " + inName
                                + " contains non-integer type " + e.getMessage());
                    }
            else if (type == String.class) {
                String strTemp;
                while ((strTemp = reader.readLine()) != null) {
                    if (strTemp.contains(" ") || strTemp.equals("")) {
                        System.err.println("The string contains space character or does not contain any character: \""
                                + strTemp + "\""
                                + " in file " + inName);
                    }
                    else
                        return type.cast(strTemp);
                }

            }
                return type.cast(reader.readLine());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T extends Comparable<T>> void mergeFiles(List<String> in, String out, boolean mode, Class<T> type) {
        try {
            BufferedReader[] readers = new BufferedReader[in.size()];
            for (int i = 0; i < in.size(); i++) {
                readers[i] = new BufferedReader(new FileReader(in.get(i)));
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(out));

            ArrayList<T> currentValue = new ArrayList<>();
            for (int i = 0; i < in.size(); i++) {
                currentValue.add(readNextObj(type, readers[i], in.get(i)));
            }

            while (true) {
                T prevObj = null;
                int minMaxObjIndex = -1;
                for (int i = 0; i < currentValue.size(); i++) {
                    if (currentValue.get(i) != null) {
                        if (minMaxObjIndex == -1 || compare(currentValue.get(i), currentValue.get(minMaxObjIndex), mode) < 0) {
                            minMaxObjIndex = i;
                            prevObj = currentValue.get(i);
                        }
                    }
                }

                if (minMaxObjIndex == -1) {
                    break;
                }
                writer.write(currentValue.get(minMaxObjIndex).toString());
                writer.newLine();
                if (readers[minMaxObjIndex].ready()) {
                    while (true) {
                        T a = readNextObj(type, readers[minMaxObjIndex], in.get(minMaxObjIndex));
                        if (a == null) {
                            currentValue.set(minMaxObjIndex, null);
                            break;
                        } else if (compare(a, prevObj, mode) >= 0) {
                            currentValue.set(minMaxObjIndex, a);
                            break;
                        }
                        System.err.println("Skipped value \"" + a + "\" in file " + in.get(minMaxObjIndex));
                    }
                } else {
                    currentValue.set(minMaxObjIndex, null);
                }
            }

            for (BufferedReader i : readers) {
                i.close();
            }
            writer.close();

        } catch (IOException e) {
            System.err.println("File error: " + e.getMessage());
        }

    }

    private static <T extends Comparable<T>> int compare(T val1, T val2, boolean mode) {
        if (mode) {
            return val2.compareTo(val1);
        } else {
            return val1.compareTo(val2);
        }

    }

}