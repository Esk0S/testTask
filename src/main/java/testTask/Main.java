package testTask;

import org.apache.commons.cli.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;

import static java.nio.file.Files.*;
import static java.nio.file.StandardOpenOption.*;

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
            // parse the command line arguments
            CommandLine cmd = parser.parse(options, args);
            for (Option i : cmd.getOptions()) {
                System.out.println(i);
            }
            argList = new ArrayList<>(cmd.getArgList());
            System.out.println(cmd.getArgList());
//            if (cmd.hasOption('a')) {
//                desc = false;
//            }
//            if (cmd.hasOption('i')) {
//                string = false;
//            }
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
//            "app.jar -a|-d(optional) -s|-i out in1 in2 ..."
            formatter.printHelp("app.jar", options, true);
            System.exit(0);
        }
        System.out.println("desc: " + desc + "; string: " + string);


//        try {
//            if (args[0].equals("-a") || args[0].equals("-d")) {
//                if (args[0].equals("-d"))
//                    desc = true;
//                if (args[1].equals("-s")) {
//                    string = true;
//                } else if (!args[1].equals("-i")) {
//                    System.out.println("Argument 2 is unknown");
//                }
//            } else if (args[0].equals("-s") || args[0].equals("-i")) {
//                if (args[0].equals("-s")) {
//                    string = true;
//                }
//            } else {
//                System.out.println("Argument 1 is unknown");
//            }
//        } catch (ArrayIndexOutOfBoundsException ex) {
//            System.out.println("Example: app.jar -a|-d(optional) -s|-i out in1 in2 ...");
//            System.exit(0);
//        }

        String outFile = argList.remove(0);
        if (string) {
//            ArrayList<String> ss = readFromFile(argList.get(0), String.class);
//            MergeSort.mergeSort(ss, 0, ss.size() - 1, desc);
//            for (Object i : ss) {
//                System.out.println(i);
//            }
            mergeFiles(argList, outFile, desc, String.class);
        } else {
            mergeFiles(argList, outFile, desc, Integer.class);
//            ArrayList<Integer> ss1 = readFromFile(argList.get(0), Integer.class);
//            ArrayList<Integer> ss2 = readFromFile(argList.get(1), Integer.class);
//            ArrayList<Integer> aa = MergeSort.mergeFiles(ss1, ss2, desc);
//            System.out.println("------");
//            for (Integer i : aa) {
//                System.out.println(i);
//            }
////            mergeFiles(argList.get(0), argList.get(1), outFile, desc, Integer.class);
//
//
//            MergeSort.mergeSort(ss1, 0, ss1.size() - 1, desc);
//            for (Object i : ss1) {
//                System.out.println(i);
//            }
        }

    }

    public static <T> ArrayList<T> readFromFile(String filename, Class<T> type) {
        ArrayList<T> result = new ArrayList<>();

//        if (type == String.class) {
//        ArrayList<String> strings = new ArrayList<>();
        try (FileReader fr = new FileReader(filename)) {
            BufferedReader reader = new BufferedReader(fr);
            String line = reader.readLine();
            while (line != null) {
                if (type == String.class) {
                    if (line.contains(" "))
                        System.err.println("The string contains space character: \"" + line + "\"");
                    else
                        result.add(type.cast(line));
                } else if (type == Integer.class) {
                    try {
                        result.add(type.cast(Integer.parseInt(line)));
                    } catch (NumberFormatException exception) {
                        System.err.println("The file contains non-integer type: \"" + line + "\"");
                    }
                }
                line = reader.readLine();
            }
        } catch (FileNotFoundException exception) {
            System.err.println("The system cannot find the file " + filename + " \nCheck filename and try again");
        } catch (IOException exception) {
            System.err.println("File read error: " + filename);
        }
        return result;
    }
//            result = strings;
////        } else if (type == Integer.class) {
//            ArrayList<Integer> ints = new ArrayList<>();
//            try (FileReader fr = new FileReader(filename)) {
//                BufferedReader reader = new BufferedReader(fr);
//                String line = reader.readLine();
//
//                while (line != null) {
//                    try {
//                        ints.add(Integer.parseInt(line));
//                    } catch (NumberFormatException exception) {
//                        System.err.println("The file contains non-integer type: " + line);
//                    }
//                    line = reader.readLine();
//                }
//            } catch (FileNotFoundException exception) {
//                System.err.println("The system cannot find the file " + filename + " \nCheck filename and try again");
//            } catch (IOException exception) {
//                System.err.println("File read error: " + filename);
//            }
//            result = ints;
////        }
//        return result;
//    }

//        ArrayList<Integer> list = new ArrayList<>();
//        for (String i : strings) {
//            try {
//                list.add(Integer.parseInt(i));
//            } catch (NumberFormatException exception) {
//                System.err.println("The file contains non-integer numbers");
//            }
//
//        }
//        return list;


    public static <T extends Comparable<T>> ArrayList<T> mergeFiles(String in1, String in2, String out, boolean mode, Class<T> type) {
        ArrayList<T> result = new ArrayList<>();

//        if (type == String.class) {
//        ArrayList<String> strings = new ArrayList<>();
        try (BufferedReader reader1 = newBufferedReader(Path.of(in1));
             BufferedReader reader2 = newBufferedReader(Path.of(in2));
             BufferedWriter writer = newBufferedWriter(Path.of(out), CREATE, WRITE, APPEND)
        )
         {
//            FileReader fr1 = new FileReader(in1);
//            FileReader fr2 = new FileReader(in2);
//            FileWriter fw = new FileWriter(out);

            String line1 = reader1.readLine();
            String line2 = reader2.readLine();
            PriorityQueue<Integer> pq = new PriorityQueue<>();
            T comparable1 = null;
            T comparable2 = null;
            while (line1 != null && line2 != null) {
                if (type == Integer.class) {
                    try {
                        comparable1 = type.cast(Integer.parseInt(line1));
                        comparable2 = type.cast(Integer.parseInt(line2));
                    } catch (NumberFormatException exception) {
                        System.err.println("The file contains non-integer type: \"" + line1 + "\"");
                    }
                }
//                else if (type == String.class) {
//
//                }
                if (type == String.class) {
                    if (line1.contains(" "))
                        System.err.println("The string contains space character: \"" + line1 + "\"");
                    else
                        result.add(type.cast(line1));
                } else if (type == Integer.class) {
                    if ((comparable1.compareTo(comparable2) <= 0) ^ mode) {
                        writer.write(comparable1.toString());
                        writer.newLine();
                        line1 = reader1.readLine();
                    } else {
                        writer.write(comparable2.toString());
                        writer.newLine();
                        line2 = reader2.readLine();
                    }
//                        result.add(type.cast(Integer.parseInt(line1)));
                }
            }
            while (line1 != null) {
                if (type == Integer.class) {
                    try {
                        comparable1 = type.cast(Integer.parseInt(line1));
                        writer.write(comparable1.toString());
                        writer.newLine();
                        line1 = reader1.readLine();
                    } catch (NumberFormatException exception) {
                        System.err.println("The file contains non-integer type: \"" + line1 + "\"");
                    }
                }
            }
            while (line2 != null) {
                if (type == Integer.class) {
                    try {
                        comparable2 = type.cast(Integer.parseInt(line2));
                        writer.write(comparable2.toString());
                        writer.newLine();
                        line2 = reader2.readLine();
                    } catch (NumberFormatException exception) {
                        System.err.println("The file contains non-integer type: \"" + line2 + "\"");
                    }
                }
            }
        } catch (FileNotFoundException exception) {
            System.err.println("The system cannot find the file " + in1 + " \nCheck filename and try again");
        } catch (IOException exception) {
            System.err.println("File read error: " + in1);
        }
        return result;
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
//                if (type == Integer.class)
//                    while (true)
//                        try {
//                            if (readers[i].ready()) {
//                                currentValue.set(i,
//                                        type.cast(Integer.parseInt(readers[i].readLine())));
//                            } else currentValue.set(i, null);
//                            break;
//                        } catch (NumberFormatException e) {
//                            System.err.println("The file " + in.get(i)
//                                    + " contains non-integer type " + e.getMessage());
//                        }
////                    currentValue.add(type.cast(Integer.parseInt(readers[i].readLine())));
//                else
//                    currentValue.add(type.cast(readers[i].readLine()));

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
                        // a должен быть больше для false
                        if (a == null) {
                            currentValue.set(minMaxObjIndex, null);
                            break;
                        } else if (compare(a, prevObj, mode) >= 0) {
                            currentValue.set(minMaxObjIndex, a);
                            break;
                        }
                        System.err.println("Skipped value \"" + a + "\" in file " + in.get(minMaxObjIndex));
                    }
//                    if (type == Integer.class)
//                        while (true)
//                            try {
//                                if (readers[minMaxObjIndex].ready()) {
//                                    currentValue.set(minMaxObjIndex,
//                                            type.cast(Integer.parseInt(readers[minMaxObjIndex].readLine())));
//                                } else currentValue.set(minMaxObjIndex, null);
//                                break;
//                            } catch (NumberFormatException e) {
//                                System.err.println("The file " + in.get(minMaxObjIndex)
//                                        + " contains non-integer type " + e.getMessage());
//                            }
//                    else
//                        currentValue.set(minMaxObjIndex, type.cast(readers[minMaxObjIndex].readLine()));
////                    if (compare(currentValue.get(minMaxObjIndex), prevObj, mode) < 0);
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
//        catch (NumberFormatException e) {
//            System.err.println("The file contains non-integer type " + e.getMessage());
//        }

    }

    private static <T extends Comparable<T>> int compare(T val1, T val2, boolean mode) {
        if (mode) {
            return val2.compareTo(val1);
        } else {
            return val1.compareTo(val2);
        }

    }

}