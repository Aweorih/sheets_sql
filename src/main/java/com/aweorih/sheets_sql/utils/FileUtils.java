package com.aweorih.sheets_sql.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class FileUtils {

  private FileUtils() {} // never

  public static String buildOsFilePath(String... directories) {

    return directories == null ? "" : Arrays.stream(directories)
                                            .filter(Objects::nonNull)
                                            .filter(d -> !d.equals(""))
                                            .collect(Collectors.joining(File.separator));
  }

  public static void overwriteFile(String filePath, byte[] bytes) throws IOException {

    FileOutputStream fos = new FileOutputStream(filePath);
    fos.write(bytes);
    fos.close();
  }

  public static void overwriteFile(File file, byte[] bytes) throws IOException {

    FileOutputStream fos = new FileOutputStream(file);
    fos.write(bytes);
    fos.close();
  }

  public static void overwriteFile(String filePath, String newContent)
    throws FileNotFoundException {

    overwriteFile(new File(filePath), newContent);
  }

  public static void overwriteFile(File file, String newContent) throws FileNotFoundException {

    PrintWriter pw = new PrintWriter(file);
    pw.write(newContent);
    pw.close();
  }

  public static void emptyFile(String filePath) throws FileNotFoundException {
    PrintWriter pw = new PrintWriter(filePath);
    pw.close();
  }

  public static boolean createEmptyFile(File file) {

    boolean success = false;
    file.getParentFile().mkdirs();

    try {
      success = file.createNewFile();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return success;
  }

  public static void appendStringToFile(String rawPath, String content) throws IOException {

    appendBytesToFile(rawPath, content.getBytes());
  }

  public static void appendBytesToFile(String rawPath, byte[] content) throws IOException {

    Path path = Paths.get(rawPath);

    Files.write(path, content, StandardOpenOption.APPEND);
  }

  public static byte[] readFileByPathIntoArray(String filePath) throws IOException {
    Path path = Paths.get(filePath);
    return Files.readAllBytes(path);
  }

  public static byte[] readFileByFileIntoArray(File file) throws IOException {

    return readFileByPathIntoArray(file.getPath());
  }

  public static List<String> readFileByPathIntoList(String path, boolean addLineSeparator) {

    List<String> response = new ArrayList<>();

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {

      String line = br.readLine();

      while (line != null) {

        if (addLineSeparator) {
          //noinspection StringConcatenationInLoop
          line = line + System.lineSeparator();
        }

        response.add(line);
        line = br.readLine();
      }
    } catch (IOException e) {
      LoggingUtils.logException(FileUtils.class, e);
    }

    return response;
  }

  public static String readFileByPath(String path) {

    String response = "";

    try (BufferedReader br = new BufferedReader(new FileReader(path))) {

      response = readFileBuffer(br);
    } catch (IOException e) {
      LoggingUtils.logException(FileUtils.class, e);
    }

    return response;
  }

  public static String readFileByFile(File file) {
    String response = "";

    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

      response = readFileBuffer(br);
    } catch (IOException e) {
      LoggingUtils.logException(FileUtils.class, e);
    }

    return response;
  }

  public static boolean existsFile(String filePath) {

    File file = new File(filePath);
    return file.exists();
  }

  private static String readFileBuffer(BufferedReader br) throws IOException {

    StringBuilder sb   = new StringBuilder();
    String        line = br.readLine();

    while (line != null) {
      sb.append(line);
      sb.append(System.lineSeparator());
      line = br.readLine();
    }

    return sb.toString();
  }

  public static List<String> unpackJarFile(
    String filePath,
    String destination,
    Class contextClass,
    List<String> desiredDirectories
  ) {

    return null;
  }

  /**
   * Note: boolean excludeJre can cause errors as it checks if `jre` is in file
   *
   * @param filePrefix the prefix (up to) which has to be removed from found files, can be a regex
   */
  public static Map<String, String> unpackJarFile(
    Pattern pattern,
    String filePrefix,
    boolean deleteDirectoryIfExists,
    String destination
  ) {

    File parent = new File(destination);

    if (deleteDirectoryIfExists && parent.exists()) {
      try {
        org.apache.commons.io.FileUtils.cleanDirectory(parent);
        Files.delete(parent.toPath());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    Map<String, String> matchingResources = getResources(deleteDirectoryIfExists, pattern);

    if (matchingResources.isEmpty()) return matchingResources;

    Map<String, String> cleanedResources = new HashMap<>();
    for (Map.Entry<String, String> entry : matchingResources.entrySet()) {
      String cleanedKey = entry.getKey().replaceFirst(filePrefix, "");
      if (cleanedKey.isEmpty()) continue; // probably top level entry
      cleanedResources.put(
        cleanedKey,
        entry.getValue()
      );
    }

    List<String> createdDirectories = createDirectories(parent, cleanedResources);

    for (Map.Entry<String, String> entry : cleanedResources.entrySet()) {

      if (createdDirectories.contains(entry.getKey())) continue;

      try {
        File f = new File(String.format("%s/%s", parent.getAbsolutePath(), entry.getKey()));
        Files.createFile(f.toPath());
        Files.write(f.toPath(), entry.getValue().getBytes());
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    return cleanedResources;
  }

  private static List<String> createDirectories(
    File parent,
    Map<String, String> resources
  ) {

    List<String> directories = resources
      .keySet()
      .stream()
      .filter(s -> s.endsWith("/"))
      .sorted(Comparator.comparingInt(String::length))
      .collect(Collectors.toList());

    try {

      if (!parent.exists()) Files.createDirectory(parent.toPath());

      for (String directory : directories) {

        File child = new File(String.format("%s/%s", parent.getAbsolutePath(), directory));
        Files.createDirectory(child.toPath());
      }

    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

    return directories;
  }

  /**
   * copied from `Jigar Joshi` answer of
   * https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
   * slightly modified to:
   * - return map with key: `filename` and value: `content of file`
   * - throw UncheckedIOExceptions instead of generic Error
   *
   *
   * for all elements of java.class.path get a Collection of resources Pattern
   * pattern = Pattern.compile(".*"); gets all resources
   *
   * @param pattern the pattern to match
   * @return the resources in the order they are found
   * @throws UncheckedIOException on IOExceptions (this one added instead of generic Error throw)
   */
  public static Map<String, String> getResources(
    boolean excludeJre,
    final Pattern pattern
  ) {
    final Map<String, String> retval    = new HashMap<>();
    final String              classPath = System.getProperty("java.class.path", ".");
    final String[] classPathElements = classPath.split(
      System.getProperty("path.separator"));
    for (final String element : classPathElements) {
      if (excludeJre && element.contains("jre")) continue;
      retval.putAll(getResources(element, pattern));
    }
    return retval;
  }

  /**
   * copied from `Jigar Joshi` answer of
   * https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
   * slightly modified to:
   * - return map with key: `filename` and value: `content of file`
   * - throw UncheckedIOExceptions instead of generic Error
   */
  private static Map<String, String> getResources(
    final String element,
    final Pattern pattern
  ) {
    final Map<String, String> retval = new HashMap<>();
    final File                file   = new File(element);
    if (file.isDirectory()) {
      retval.putAll(getResourcesFromDirectory(file, pattern));
    } else {
      retval.putAll(getResourcesFromJarFile(file, pattern));
    }
    return retval;
  }

  /**
   * copied from `Jigar Joshi` answer of
   * https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
   * slightly modified to:
   * - return map with key: `filename` and value: `content of file`
   * - throw UncheckedIOExceptions instead of generic Error
   */
  private static Map<String, String> getResourcesFromJarFile(
    final File file,
    final Pattern pattern
  ) {
    final Map<String, String> retval = new HashMap<>();
    ZipFile                   zf;
    try {
      zf = new ZipFile(file);
    } catch (final IOException e) {
      throw new UncheckedIOException(e);
    }
    final Enumeration e = zf.entries();
    while (e.hasMoreElements()) {
      try {
        final ZipEntry ze       = (ZipEntry) e.nextElement();
        InputStream    in       = zf.getInputStream(ze);
        String         content  = Utils.inputStreamToString(in);
        final String   fileName = ze.getName();
        final boolean  accept   = pattern.matcher(fileName).matches();
        if (accept) {
          retval.put(fileName, content);
        }
      } catch (final IOException e1) {
        LoggingUtils.logException(FileUtils.class, e1);
      }
    }
    try {
      zf.close();
    } catch (final IOException e1) {
      throw new UncheckedIOException(e1);
    }
    return retval;
  }

  /**
   * copied from `Jigar Joshi` answer of
   * https://stackoverflow.com/questions/3923129/get-a-list-of-resources-from-classpath-directory
   * slightly modified to:
   * - return map with key: `filename` and value: `content of file`
   * - throw UncheckedIOExceptions instead of generic Error
   */
  private static Map<String, String> getResourcesFromDirectory(
    final File directory,
    final Pattern pattern
  ) {
    final Map<String, String> retval   = new HashMap<>();
    final File[]              fileList = directory.listFiles();

    if (null == fileList) return retval;

    List<File> directories = new ArrayList<>(Arrays.asList(fileList));
    List<File> toProcess   = new ArrayList<>();
    int        pointer     = 0;

    while (pointer < directories.size()) {

      File current = directories.get(pointer++);
      toProcess.add(current);
      if (!current.isDirectory()) continue;

      File[] currentFileList = current.listFiles();
      if (null == currentFileList) continue;
      directories.addAll(Arrays.asList(currentFileList));
    }

    for (final File file : toProcess) {
      try {
        final String fileName = file.getCanonicalPath();

        final boolean accept = pattern.matcher(fileName).matches();
        if (!accept) continue;

        if (file.isDirectory()) {
          if (fileName.endsWith("/")) retval.put(fileName, "");
          else retval.put(fileName + "/", "");
        } else retval.put(fileName, new String(Files.readAllBytes(file.toPath())));
      } catch (IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    return retval;
  }
}
