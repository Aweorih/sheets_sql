package com.aweorih.sheets_sql.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.VisibilityChecker.Std;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

  private static       ObjectMapper defaultObjectMapper = null;
  private static final Logger       LOGGER              = LogManager.getLogger(Utils.class);

  private static final Pattern FIRMWARE_PATTERN_2019 = Pattern.compile(".*\\d{1,2}\\s\\d{4}$");

  private Utils() {} // never

  public static void sleep(long time, Class origin) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      LoggingUtils.getLogger(origin).error(e.getMessage(), e);
      Thread.currentThread().interrupt();
    }
  }

  public static String normalizeVin(String originVin) {
    return originVin.startsWith("VIN") ? originVin : "VIN" + originVin;
  }

  public static long parseFirmwareVersionToEpoch(String firmwareVersion) {

    Matcher m = FIRMWARE_PATTERN_2019.matcher(firmwareVersion);

    if (m.find()) return parse2019FirmwareVersionToEpoch(firmwareVersion);

    if (firmwareVersion.contains(" ")) return parseOldFirmwareVersionToEpoch(firmwareVersion);

    return parseNewFirmwareVersionToEpoch(firmwareVersion);
  }

  public static long parse2019FirmwareVersionToEpoch(String firmwareVersion) {

    firmwareVersion = firmwareVersion.replace("  ", " 0");
    String[] oldSplit = firmwareVersion.split(" ");

    String s = oldSplit[2] + oldSplit[0].replace(".", "") + " " + oldSplit[1];
//    String oldParseString = oldSplit[1] + oldSplit[3].replace("  ", " 0");

    DateTimeFormatter oldFormatter = DateTimeFormatter
      .ofPattern("yyyyMMM dd")
      .withLocale(Locale.ENGLISH)
      .withZone(ZoneId.systemDefault());

    LocalDate dt = LocalDate.parse(s, oldFormatter);

    return dt.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
  }

  public static long parseOldFirmwareVersionToEpoch(String firmwareVersion) {

    String[] oldSplit = firmwareVersion.split("\\.");

    String oldParseString = oldSplit[1] + oldSplit[3].replace("  ", " 0");

    DateTimeFormatter oldFormatter = DateTimeFormatter
      .ofPattern("yyMMM dd")
      .withLocale(Locale.ENGLISH)
      .withZone(ZoneId.systemDefault());

    LocalDate dt = LocalDate.parse(oldParseString, oldFormatter);

    return dt.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();
  }

  public static long parseNewFirmwareVersionToEpoch(String firmwareVersion) {

    String[] newSplit = firmwareVersion.split("\\.");

    int weekOfYear = Integer.parseInt(newSplit[3].substring(0, 1));
    int hour       = Integer.parseInt(newSplit[3].substring(1, 3));
    int minute     = Integer.parseInt(newSplit[3].substring(3, 5));

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(newSplit[2]));
    cal.set(Calendar.DAY_OF_WEEK, weekOfYear + 1);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, 0);

    return cal.getTimeInMillis();
  }

  // greatest common divisor
  public static int gcd(int a, int b) {

    int _a = a;
    int _b = b;
    int tmp;

    // loop implementation of recursive call:
    // return b == 0 ? a : gcd(b, a % b);
    while (_b != 0) {
      tmp = _a;
      _a = _b;
      _b = tmp % _b;
    }

    return _a;
  }

  public static ObjectMapper getDefaultObjectMapper() {

    if (null == defaultObjectMapper) {
      defaultObjectMapper = new ObjectMapper();
      defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      defaultObjectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
      defaultObjectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    return defaultObjectMapper;
  }

  public static ObjectMapper getFieldVisibilityObjectMapper() {

    ObjectMapper mapper = getDefaultObjectMapper();

    Std std = new Std(Visibility.NONE, Visibility.NONE, Visibility.NONE, Visibility.NONE,
                      Visibility.ANY
    );
    mapper.setVisibility(std);

    return mapper;
  }

  public static <E> E readWithTypeReference(TypeReference<E> reference, String s) {

    E response = null;

    try {
      ObjectReader reader = getDefaultObjectReader().forType(reference);
      response = reader.readValue(s);
    } catch (IOException e) {
      LOGGER.error(String.format("Exception while trying to parse Object\n%s", s), e);
    }

    return response;
  }

  public static String timestampToBigIntegerTimestamp(Long l) {

    return timestampToBigIntegerTimestamp(new Timestamp(l));
  }

  public static String timestampToBigIntegerTimestamp(Timestamp timestamp) {

    return timestamp.toString().replaceAll("-|\\s|:|\\.\\d\\d\\d", "");
  }

  public static Timestamp parseDbBigIntegerTimestamp(long origin) {

    String    s      = String.valueOf(origin);
    Timestamp result = null;

    Date date = new SimpleDateFormat("yyyyMMddHHmmss")
      .parse(s, new ParsePosition(0));

    if (null != date) {
      result = new Timestamp(date.getTime());
    }

    return result;
  }

  public static <E> E readJson(Class<E> clazz, String s) {
    return readWithDefaultSettings(clazz, s);
  }

  public static <E> E readWithDefaultSettings(Class<E> clazz, String s) {

    E instance = null;

    try {
      Class<? extends E>       a           = clazz.asSubclass(clazz);
      Constructor<? extends E> constructor = a.getConstructor();

      instance = constructor.newInstance();

      readWithDefaultSettings(instance, s);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return instance;
  }

  public static <I> I readJson(I i, String s) {
    return readWithDefaultSettingsChain(i, s);
  }

  public static <I> I readWithDefaultSettingsChain(I i, String s) {

    I response = null;

    try {
      getDefaultObjectReader().withValueToUpdate(i).readValue(s);
      response = i;
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return response;
  }

  public static boolean readWithDefaultSettings(Object o, String s) {

    boolean success = false;

    try {
      getDefaultObjectReader().withValueToUpdate(o).readValue(s);
      success = true;
    } catch (IOException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return success;
  }

  public static <E> String writeJsonWithDefaultSettings(E object) {

    String response = null;

    try {
      ObjectWriter defaultObjectWriter = getDefaultObjectWriter();
      response = defaultObjectWriter.forType(object.getClass()).writeValueAsString(object);
    } catch (JsonProcessingException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return response;
  }

  public static ObjectWriter getDefaultObjectWriter() {
    return getDefaultObjectMapper().writer();
  }

  public static ObjectReader getDefaultObjectReader() {
    return getDefaultObjectMapper().reader();
  }

  public static String generateRandomId(Number length) {
    return generateRandomId(UUID.randomUUID().toString(), length);
  }

  public static String generateRandomId() {
    return generateRandomId(UUID.randomUUID().toString());
  }

  public static String generateRandomId(String id) {
    return generateRandomId(id, 20);
  }

  public static String generateRandomId(String id, Number length) {
    String substring = generateMd5Hash(id);
    return (substring != null) ? substring.substring(0, length.intValue()) : null;
  }

  public static <E extends Enum<E>> E getEnumType(Class<E> clazz, String wanted) {
    return getEnumType(clazz, null, wanted);
  }

  public static <E extends Enum<E>> E getEnumType(Class<E> clazz, E defaultValue, String wanted) {

    E response = defaultValue;

    try {
      response = Enum.valueOf(clazz, wanted);
    } catch (Exception e) {
      LOGGER.error(e.getMessage(), e);
    }

    return response;

  }

  private static String generateMd5Hash(String s) {

    String r = null;

    try {

      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(s.getBytes(), 0, s.length());

      BigInteger    bigInteger = new BigInteger(1, md.digest());
      StringBuilder sb         = new StringBuilder(bigInteger.toString(16));

      while (32 > sb.length()) {
        sb.insert(0, "0");
      }

      r = sb.toString();

    } catch (NoSuchAlgorithmException e) {
      LOGGER.error(e.getMessage(), e);
    }

    return r;
  }

  public static String inputStreamToString(InputStream inputStream) {

    Scanner       s             = (new Scanner(inputStream)).useDelimiter(System.lineSeparator());
    StringBuilder stringBuilder = new StringBuilder();

    while (s.hasNext()) {
      stringBuilder.append(s.next());
      stringBuilder.append(System.lineSeparator());
    }
    s.close();

    return stringBuilder.toString();
  }

  public static boolean suppliersNotNull(Supplier... suppliers) {

    if (null == suppliers) return false;

    for (Supplier s : suppliers) {
      if (null == s || null == s.get()) return false;
    }

    return true;
  }

  public static boolean allNotNull(final Object... values) {

    if (null == values) return false;

    for (final Object val : values) {
      if (val == null) return false;
    }

    return true;
  }

  public static String convertApiIdToPlatformId(String apiId, String diagnosisId, Long userId) {
    return Utils.generateRandomId(apiId + diagnosisId + userId);
  }

  public static void bindParameters(PreparedStatement s, Object... objects) throws SQLException {

    if (objects != null) {

      int i = 1;

      for (Object o : objects) {

        s.setObject(i++, o);
      }
    }
  }

  public static long getResponseMessageTime(long responseTime, long packetTime) {

    long response = responseTime;
    long time1Day = 86_400_000;
    // 2017-01-01 00:00:00.000
    long minReferenceTime = 1483228800000L;
    long maxReferenceTime = System.currentTimeMillis() + time1Day;

    if (minReferenceTime > responseTime || maxReferenceTime < responseTime) {
      response = packetTime;
    }

    return response;
  }

  public static void bindParameters(PreparedStatement s, List<Object> objects) throws SQLException {

    if (objects != null) {

      int i = 1;

      for (Object o : objects) {

        s.setObject(i++, o);
      }
    }
  }

  public static <T> T getOrDefault(T check, T _default) {

    return (null != check) ? check : _default;
  }

  public static <T> List<T> castList(Class<? extends T> clazz, Collection<?> c) {

    List<T> r = new ArrayList<>(c.size());

    for (Object o : c) {
      r.add(clazz.cast(o));
    }

    return r;
  }

  public static boolean isFromToInArrayLength(int arrayLength, int from, int to) {
    return arrayLength >= from && arrayLength >= to && from >= 0 && to >= 0;
  }

  public static <N extends Number> boolean containsRange(N check, List<N> list) {

    Object[] o = list.toArray();
    Arrays.sort(o);

    Number min = (Number) o[0];
    Number max = (Number) o[o.length - 1];

    return check.longValue() >= min.longValue() && check.longValue() <= max.longValue();
  }

  public static boolean isInRange(int index, int from, int to) {
    return index >= from && index <= to;
  }

  public static boolean isInRange(long index, long from, long to) {
    return index >= from && index <= to;
  }

  public static boolean isArrayCopyValid(byte[] source, byte[] target, int targetOffset) {
    return isArrayCopyValid(source.length, 0, target.length, targetOffset, source.length);
  }

  public static boolean isArrayCopyValid(byte[] source, int sourceOffset, byte[] target) {
    return isArrayCopyValid(source.length, sourceOffset, target.length, 0, source.length);
  }

  public static boolean isArrayCopyValid(byte[] source, byte[] target) {
    return isArrayCopyValid(source.length, 0, target.length, 0, source.length);
  }

  public static boolean isArrayCopyValid(
    byte[] src,
    int srcOffset,
    byte[] target,
    int targetOffset,
    int length
  ) {
    return isArrayCopyValid(src.length, srcOffset, target.length, targetOffset, length);
  }

  public static boolean isArrayCopyValid(
    int srcSize,
    int srcOffset,
    int targetSize,
    int targetOffset,
    int length
  ) {

    return length <= (srcSize - srcOffset) && length <= (targetSize - targetOffset);
  }

  public static String getInvalidArrayCopyMessage(
    int srcSize,
    int srcOffset,
    int targetSize,
    int targetOffset,
    int length
  ) {
    return String.format(
      "Array copy is not valid. Given params are: length: %d, source range: %d - %d, destination range: %d - %d",
      length,
      srcOffset,
      srcSize,
      targetOffset,
      targetSize
    );
  }

  public static List<Integer> makeIntegerList(int offset, int length) {
    return Stream.iterate(offset, n -> n + 1).limit(length).collect(Collectors.toList());
  }

  public static List<Object> asObjectList(Object... objects) {

    return Arrays.asList(objects);
  }

  // makes a list out of given arguments
  // if items is empty or null, an empty list will be returned
  // if items is of type List, there's a simple cast
  // else a new list is created with arguments
  @SuppressWarnings("unchecked")
  public static <E> List<E> arbitraryArgumentsToList(E... items) {

    List l;

    if (items == null || items.length == 0) {
      l = Collections.emptyList();
    } else if (items[0] instanceof List) {

      l = (List) items[0];
    } else {
      l = Arrays.asList(items);
    }

    return l;
  }

  // expensive call, should be treated with care
  private static String getCallerMsg() {

    StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
    StackTraceElement   s                  = stackTraceElements[3];

    return String.format(
      "%s::%s::%d",
      s.getClassName(),
      s.getMethodName(),
      s.getLineNumber()
    );
  }
}
