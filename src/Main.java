import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        build(args);
    }

    static LinkedHashSet<String> texts(String... string) {
        LinkedHashSet<String> set = new LinkedHashSet<>();
        try (Scanner sc = new Scanner(new FileReader("html.txt"))) {
            Matcher matcher;
            int i = 0;
            int[] found = new int[string.length];
            Arrays.fill(found, 0);
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                int from = line.indexOf(string[i]);
                if (from > 0) {
                    if (i - 2 > string.length) break;
                    String str = line.substring(from);
                    int to = str.indexOf(string[i + 1]);
                    if (to > 0) {
                        String limitedString = str.substring(0, to);
                        matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(limitedString);
                        while (matcher.find()) {
                            set.add(matcher.group());
                            found[i] = Math.max(set.size(), found[i]);
                        }
                        str = str.substring(to);
                        set.add(string[i] + "---" + found[i]);
                        i++;
                    }
                    matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(str);
                    while (matcher.find()) {
                        set.add(matcher.group());
                        found[i] = Math.max(set.size()-found[i-1]-1, found[i]);
                    }
                    set.add(string[i] + "---" + found[i]);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return set;
    }

    static void statistics(int[] count, String[] strings) {
        for (int i = 0; i < count.length; i++) {
            System.out.println(strings[i] + ((count[i] > 0) ? "Found: " + count[i] : "Not Found"));
        }
    }

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,4}\\b", Pattern.CASE_INSENSITIVE);

    public static LinkedHashSet<String> validateAndGrabFromFile(String filePath) {
        LinkedHashSet<String> set = new LinkedHashSet<>();

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

    public static LinkedHashSet<String> plainGrabEmailFromFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LinkedHashSet<String> set = new LinkedHashSet<>();
        try (Scanner sc = new Scanner(file)) {
            while (sc.hasNextLine()) {
                set.add(sc.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return set;
    }

    public static void writeToFile(LinkedHashSet<String> set, String filePath) {
        File file = new File(filePath);
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (var o : set) {
                fileWriter.write(o + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void build(String... strings) {
        String htmlFilePath = strings[0];
        String storeFilePath = strings[1];
        LinkedHashSet<String> previousSet = plainGrabEmailFromFile(storeFilePath);
        LinkedHashSet<String> newSet = (strings.length == 2) ? validateAndGrabFromFile(htmlFilePath) : texts(Arrays.copyOfRange(strings, 2, strings.length));
        LinkedHashSet<String> commonSet = new LinkedHashSet<>();
        commonSet.addAll(previousSet);
        commonSet.addAll(newSet);
        newSet.removeAll(previousSet);
        writeToFile(commonSet, storeFilePath);
        if (newSet.size() > 0 && newSet.size() != commonSet.size()) {
            writeToFile(newSet, "_new" + storeFilePath);
        }
        System.out.println("New mail found: " + Math.max(newSet.size()- strings.length +2,0));
        for (var o : newSet) {
            System.out.println(o);
        }
    }
}
