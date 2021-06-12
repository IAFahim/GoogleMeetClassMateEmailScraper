import java.io.*;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);


    public static Set<String> validateAndGrabFromFile(String filePath) {
        Set<String> set = new HashSet<>();
        try (Scanner sc = new Scanner(new FileReader(filePath))) {
            Matcher matcher;

            while (sc.hasNextLine()) {
                matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(sc.nextLine());
                while (matcher.find()) {
                    set.add(matcher.group());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    public static Set<String> plainGrabEmailFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Set<String> set = new HashSet<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                set.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void writeToFile(Set<String> set, String filePath) {
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (var o : set) {
                fileWriter.write(o + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String storeFilePath = "Math130", extension = ".txt";

        Set<String> previousSet = plainGrabEmailFromFile(storeFilePath + extension);
        Set<String> newSet = validateAndGrabFromFile("html.txt");
        Set<String> commonSet = new HashSet<>();

        commonSet.addAll(previousSet);
        commonSet.addAll(newSet);

        newSet.removeAll(previousSet);

        writeToFile(commonSet, storeFilePath + extension);
        if (newSet.size() > 0 && newSet.size() != commonSet.size()) {
            writeToFile(newSet, storeFilePath + "_new" + extension);
        }
        System.out.println(newSet.size());
        for (var o : newSet) {
            System.out.println(o);
        }


    }
}
