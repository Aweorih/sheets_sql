package com.aweorih.sheets_sql.google_handler;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringJoiner;

public class HttpRequestUtil {

  private static final Logger LOGGER = LogManager.getLogger(HttpRequestUtil.class);

  private static final CloseableHttpClient CLIENT;

  static {
    RequestConfig requestConfig = RequestConfig
      .custom()
      .setConnectTimeout(10_000)
      .setConnectionRequestTimeout(10_000)
      .setSocketTimeout(10_000)
      .build();

    CLIENT = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
  }

  private HttpRequestUtil() {} // never

  public static HttpResponse performPostRequest(
    String url,
    String body,
    Header... headers
  ) throws IOException {

    return performPostRequest(url, body, Arrays.asList(headers));
  }

  public static HttpResponse performPostRequest(
    String url,
    String body,
    List<Header> headers
  ) throws IOException {

    HttpPost post = new HttpPost(url);
    post.setEntity(new StringEntity(body));

    for (Header header : headers) { post.setHeader(header); }

    return CLIENT.execute(post);
  }

  public static String getStringFromResponse(HttpResponse response) throws IOException {

    InputStream  in     = response.getEntity().getContent();
    Scanner      s      = new Scanner(in).useDelimiter("\n");
    StringJoiner joiner = new StringJoiner("\n");

    while (s.hasNext()) { joiner.add(s.next()); }

    return joiner.toString();
  }

  public static void logHttpRequest(
    String url,
    String requestBody,
    int responseStatus,
    String response
  ) {
    LOGGER.info(
      String.format(
        "Http request done%nurl: '%s'%nrequest body: '%s'%nresponse code: %d%nresponse message: %s",
        url,
        requestBody,
        responseStatus,
        response
      )
    );
  }
}
