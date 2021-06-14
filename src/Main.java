import java.io.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        String storeFilePath = "Math130.txt";
        String htmlFilePath = "html.txt";
//        build(storeFilePath, htmlFilePath);
        texts("Teachers", "Students");
    }

    static void texts(String... string) {
        try (Scanner sc = new Scanner(new FileReader("html.txt"))) {
            Matcher matcher;
            int i = 0;
            boolean[] found = new boolean[string.length];
            Arrays.fill(found, false);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int from = line.indexOf(string[i]);
                if (from > 0) {
                    if (i - 2 > string.length) break;
                    String str = line.substring(from);
                    int to = str.indexOf(string[i + 1]);
                    if (to > 0) {
                        String limitedString = new String(str.substring(0, to));
                        matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(limitedString);
                        while (matcher.find()) {
                            if (!found[i]) {
                                System.out.println(string[i] + "---");
                            }
                            System.out.println(matcher.group());
                            found[i] = true;
                        }
                        str = new String(str.substring(to));
                        i++;
                    }
                    matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
                    while (matcher.find()) {
                        if (!found[i]) {
                            System.out.println(string[i] + "---");
                        }
                        System.out.println(matcher.group());
                        found[i]=true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void statistics(boolean[] booleans, String[] strings) {
        for (int i = 0; i < booleans.length; i++) {
            System.out.println((booleans[i]) ? "Found" : "");

        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);

    public static Set<String> validateAndGrabFromFile(String filePath) {
        Set<String> set = new HashSet<>();
        String teachers = "Teachers";
        String classmates = "Classmates";

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

    static void build(String storeFilePath, String htmlFilePath) {
        Set<String> previousSet = plainGrabEmailFromFile(storeFilePath);
        Set<String> newSet = validateAndGrabFromFile(htmlFilePath);
        Set<String> commonSet = new HashSet<>();

        commonSet.addAll(previousSet);
        commonSet.addAll(newSet);

        newSet.removeAll(previousSet);

        writeToFile(commonSet, storeFilePath);
        if (newSet.size() > 0 && newSet.size() != commonSet.size()) {
            writeToFile(newSet, "_new" + storeFilePath);
        }
        System.out.println(newSet.size());
        for (var o : newSet) {
            System.out.println(o);
        }
    }


}
