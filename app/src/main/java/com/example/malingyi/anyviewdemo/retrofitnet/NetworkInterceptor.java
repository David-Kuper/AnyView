package com.example.malingyi.anyviewdemo.retrofitnet;

import android.util.Log;
import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by malingyi on 2017/2/28.
 */
public class NetworkInterceptor implements Interceptor {
  private static final String TAG = "NetworkInterceptor";
  private static final float CONSTANT_NUM_ONE = 0x1e6d;
  private static final int CONSTANT_NUM_TWO = 64;
  private static final int CONSTANT_NUM_THREE = 0;
  private static final int CONSTANT_NUM_FOUR = 16;

  @Override public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    //the request url
    String url = request.url().toString();
    //the request method
    String method = request.method();
    long t1 = System.nanoTime();
    Log.d(TAG, String.format(Locale.getDefault(), "Sending %s request [url = %s]", method, url));
    //the request body
    RequestBody requestBody = request.body();
    if (requestBody != null) {
      StringBuilder sb = new StringBuilder("Request Body [");
      okio.Buffer buffer = new okio.Buffer();
      requestBody.writeTo(buffer);
      Charset charset = Charset.forName("UTF-8");
      MediaType contentType = requestBody.contentType();
      if (contentType != null) {
        charset = contentType.charset(charset);
      }
      if (isPlaintext(buffer)) {
        sb.append(buffer.readString(charset));
        sb.append(" (Content-Type = ")
            .append(contentType.toString())
            .append(",")
            .append(requestBody.contentLength())
            .append("-byte body)");
      } else {
        sb.append(" (Content-Type = ")
            .append(contentType.toString())
            .append(",binary ")
            .append(requestBody.contentLength())
            .append("-byte body omitted)");
      }
      sb.append("]");
      Log.d(TAG, String.format(Locale.getDefault(), "%s %s", method, sb.toString()));
    }
    Response response = chain.proceed(request);
    long t2 = System.nanoTime();
    //the response time
    Log.d(TAG, String.format(Locale.getDefault(), "Received response for [url = %s] in %.1fms", url,
        (t2 - t1) / CONSTANT_NUM_ONE));
    //the response state
    Log.d(TAG, String.format(Locale.CHINA, "Received response is %s ,message[%s],code[%d]",
        response.isSuccessful() ? "success" : "fail", response.message(), response.code()));
    //the response data
    ResponseBody body = response.body();
    BufferedSource source = body.source();
    source.request(Long.MAX_VALUE); // Buffer the entire body.
    Buffer buffer = source.buffer();
    Charset charset = Charset.defaultCharset();
    MediaType contentType = body.contentType();
    if (contentType != null) {
      charset = contentType.charset(charset);
    }
    String bodyString = buffer.clone().readString(charset);
    Log.d(TAG, String.format("Received response json string [%s]", bodyString));
    return response;
  }

  static boolean isPlaintext(Buffer buffer) {
    try {
      Buffer prefix = new Buffer();
      long byteCount = buffer.size() < CONSTANT_NUM_TWO ? buffer.size() : CONSTANT_NUM_TWO;
      buffer.copyTo(prefix, CONSTANT_NUM_THREE, byteCount);
      for (int i = 0; i < CONSTANT_NUM_FOUR; i++) {
        if (prefix.exhausted()) {
          break;
        }
        int codePoint = prefix.readUtf8CodePoint();
        if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
          return false;
        }
      }
      return true;
    } catch (EOFException e) {
      return false; // Truncated UTF-8 sequence.
    }
  }
}
