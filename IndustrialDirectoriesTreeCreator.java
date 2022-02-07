package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * This class creates a tree of directories via recursion. Core directory mentioned below - is the root directory of the tree.
 * I divided each action the class executes in different methods. The class is using console only.
 * It asks you via console if you need to select core dir using method 'isCoreDirNeeded()'.
 * Then, depending on the result of the 'isCoreDirNeeded()' method, variable 'isCoreDirSelected' is initialised.
 * Afterwards, method 'initCoreDir()', depending on the result of the variable 'isCoreDirSelected', initialises core directory.
 * Method 'initAllEssentialProgramElements()' calls two other methods: 'initMaxNestingLvl()' and 'initElementsPerEachNestingLvl()'.
 * One of which initialises max nesting level and other asks for how many directories it is obliged to create on each nesting level.
 * Finally, method 'mkDir()' is called. Where all magic with recursion occurs.
 *
 * <h1>Example</h1> Lets assume that we have written 'yes' to the first request. Then we typed our root directory (in my case D:\ITM\Directory).
 * Afterwards, we need to type max nesting level. Let it be 3. Then 1-st, 2-nd and 3-rd nesting levels (in my case it is 2, 3, 4 respectively).
 * The program will create in selected core directory 2 dirs (testDir-1-0 and testDir-1-1).
 * Both of the dirs will contain 3 other dirs (testDir-2-0, testDir-2-1 and testDir-2-2).
 * Each of the three will contain four, in a similar manner named, dirs.
 *
 * <h1>Note</h1> The class was made as my own mini-pet project. Therefore, the class may contain several bugs which i missed.
 * In case you find one, I will be glad to fix it. Also, I will accept and consider all proposals about refactoring or improving this program.
 *
 * @author Volodymyr Yeikovych
 */
public class IndustrialDirectoriesTreeCreator {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private static int maxNestingLvl;
    private static final ArrayList<Integer> elementsPerEachNestingLvl = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        boolean isCoreDirSelected = isCoreDirNeeded();
        Path coreDir = initCoreDir(isCoreDirSelected);
        initAllEssentialProgramElements();
        mkDir(1, coreDir);
    }

    /** This method is core method of this class. The method is recursively called to create directories.
     *
     * @param curLvl - current nesting level.
     * @param motherDir - if method called for the first time it represents root directory.
     *                  Otherwise it represents previous elements of the dir tree.
     * @throws IOException - if method 'createFinalDir()' causes one.
     */
    private static void mkDir(int curLvl, Path motherDir) throws IOException {
        int dirNumInCurLvl = getElemNumInCurLvl(curLvl);
        String mask;
        if (curLvl < maxNestingLvl) {
            for (int i = 0; i < dirNumInCurLvl; i++) {
                mask = String.format("%s\\testDir-%d-%d", motherDir, curLvl, i);
                mkDir(curLvl + 1, Path.of(mask));
            }
        } else if (curLvl == maxNestingLvl) {
            for (int i = 0; i < dirNumInCurLvl; i++) {
                mask = String.format("%s\\testDir-%d-%d", motherDir, curLvl, i);
                createFinalDir(Path.of(mask));
            }
        }
    }

    /** The method counts number of elements the program is obliged to create in the current nesting level.
     *
     * @param curLvl - current nesting level.
     * @return - number of elements program needs to initialize in current nesting level.
     */
    private static int getElemNumInCurLvl(int curLvl) {
        if (curLvl < maxNestingLvl) return elementsPerEachNestingLvl.get(curLvl - 1);
        else return elementsPerEachNestingLvl.get(elementsPerEachNestingLvl.size() - 1);
    }

    /** The method creates directory which is given as param, recreating missing dir tree if needed.
     *
     * @param dir - directory to create.
     * @throws IOException - if method 'createDirectories()' of the Files class throws one.
     */
    private static void createFinalDir(Path dir) throws IOException {
        Files.createDirectories(dir);
    }

    /** Asks you via console if core directory is needed.
     *
     * @return - true or false if scanned sting equals 'yes' or 'no' respectively. Note that the case of answers is ignored.
     * @throws IOException - if method 'readLine()' of the BufferedReader class throws one.
     */
    private static boolean isCoreDirNeeded() throws IOException {
        while (true) {
            System.out.print("Select dir where to create files. Otherwise default dir will be used (Yes/No): ");
            String answer = reader.readLine();
            System.out.println();
            if (answer.equalsIgnoreCase("yes")) return true;
            else if (answer.equalsIgnoreCase("no")) return false;
            else System.out.println("Invalid answer. Try again.");
        }
    }

    /** The method initialize core directory - the root directory of the whole directory tree to be created.
     *
     * @param isCoreDirSelected - if false, default directory will be used as core directory.
     *                          Otherwise the method asks you to type a core directory via console.
     * @return - returns Path of core directory.
     * @throws IOException - if 'readLine()' method of the BufferedReaderClass throws one.
     */
    private static Path initCoreDir(boolean isCoreDirSelected) throws IOException {
        if (!isCoreDirSelected) return Path.of("D:");
        else {
            while (true) {
                System.out.print("Write your core dir here: ");
                String prospectiveCoreDir = reader.readLine();
                System.out.println();
                try {
                    return Paths.get(prospectiveCoreDir);
                } catch (InvalidPathException e) {
                    System.out.println("Selected Path is invalid. Try again.");
                }
            }
        }
    }

    /** Via console asks you to initialize max nesting level.
     *
     * @throws IOException - if method 'parseInt()' of the Integer class throws one.
     */
    private static void initMaxNestingLvl() throws IOException {
        while (true) {
            System.out.print("Write max nesting index: ");
            try {
                maxNestingLvl = Integer.parseInt(reader.readLine());
                System.out.println();
                return;
            } catch (NumberFormatException e) {
                System.out.println("Invalid nesting index. Try again");
            }
        }
    }

    /** Via console asks you to initialize each nesting level.
     *
     * @throws IOException - if method 'readLine()' of the BufferedReader class throws one.
     */
    private static void initElementsPerEachNestingLvl() throws IOException {
        int currNestLevel = 1;
        while (currNestLevel <= maxNestingLvl) {
            String elementsNumber;
            switch (currNestLevel) {
                case 1 -> {
                    System.out.print("Write how many elements to create on 1-st nestingLevel: ");
                    elementsNumber = reader.readLine();
                }
                case 2 -> {
                    System.out.print("Write how many elements to create on 2-nd nestingLevel: ");
                    elementsNumber = reader.readLine();
                }
                case 3 -> {
                    System.out.print("Write how many elements to create on 3-rd nestingLevel: ");
                    elementsNumber = reader.readLine();
                }
                default -> {
                    System.out.printf("Write how many elements to create on %s-th nestingLevel: ", currNestLevel);
                    elementsNumber = reader.readLine();
                }
            }
            try {
                int elements = Integer.parseInt(elementsNumber);
                elementsPerEachNestingLvl.add(elements);
                currNestLevel++;
            } catch (NumberFormatException e) {
                System.out.println("Invalid number. Try again.");
            }
        }
    }

    /** Calls two methods: 'initMaxNestingLvl()' and 'initElementsPerEachNestingLvl()';
     *
     * @throws IOException - if either of two methods throws one.
     */
    private static void initAllEssentialProgramElements() throws IOException {
        initMaxNestingLvl();
        initElementsPerEachNestingLvl();
    }

}


