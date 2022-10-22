package com.liaopeixin.lib_network;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;

public class RetrofitWrapper {
    private static final String WTAG = "RetrofitWrapper";
    public static final String TOKEN_ID = "tokenId";
    public static final String CLIENT_ID = "clientId";
    public static final String TAG = "R_HTTP";
    public static final String EVENT_TAG = "R_HTTP_EVENT";
    private static long sConnectTimeout = 30000;
    private static long sReadTimeout = 30000;
    private static long sWriteTimeout = 30000;
    private static boolean mIsDebug = true;
    private static boolean mWriteLogFileEnable = false;
    private Retrofit mRetrofit;
    private Context mContext;
    private Map<String, String> mHeaders;
    private static int mInstanceIndex;

    public void setHeaders(Map<String, String> headers) {
        this.mHeaders = headers;
    }

    protected Map<String, String> fillHeaders(Map<String, String> headers) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        if (mHeaders != null && !mHeaders.isEmpty()) {
            headers.putAll(mHeaders);
        }
        return headers;
    }

    public void buildTime(OkHttpClient.Builder builder, long connectTimeout, long readTimeout, long writeTimeout) {
        builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
        builder.readTimeout(readTimeout, TimeUnit.MILLISECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS);

    }

    public RetrofitWrapper(String url, Map<String, String> headers, long connectTimeOut, long readTimeOut, long writeTimeOut) {
        this.mHeaders = headers;
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        buildTime(builder, connectTimeOut, readTimeOut, writeTimeOut);
        //错误重连
        builder.retryOnConnectionFailure(true);
        builder.interceptors().add(new LogInterceptor());
        builder.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder newBuilder = original.newBuilder();
                Map<String, String> headers = fillHeaders(null);
                if (headers != null && !headers.isEmpty()) {
                    Iterator<Map.Entry<String, String>> iterator = headers.entrySet().iterator();
                    Map.Entry<String, String> entry;
                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        if (entry.getValue() != null) {
                            newBuilder.addHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                Request request = newBuilder.build();
                return chain.proceed(request);
            }
        });
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] x509Certificates,
                    String s) throws java.security.cert.CertificateException {
            }

            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
            }
        };
        TrustManager[] trustManagers = new TrustManager[]{trustManager};
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        //        builder.dns(new LogDns());
      /*  if (!BuildConfig.DEBUG) {
            builder.proxy(Proxy.NO_PROXY);
        }*/
        builder.dispatcher(OkHttpMajorPool.get().getDispatcher());
        builder.connectionPool(OkHttpMajorPool.get().getConnectionPool());
        OkHttpClient client = builder.build();

        mRetrofit = new Retrofit.Builder().baseUrl(url)
                .addConverterFactory(RetrofitWrapperPool.get().getGsonConverterFactory())
                .client(client)
                .build();
    }

    public static RetrofitWrapper getInstance(String url, Map<String, String> headers, boolean isDebug, boolean writeLogFileEnable) {
        mIsDebug = isDebug;
        mWriteLogFileEnable = writeLogFileEnable;
        return new RetrofitWrapper(url, headers, sConnectTimeout, sReadTimeout, sWriteTimeout);
    }

    public static RetrofitWrapper getInstance(String url, Map<String, String> headers, boolean isDebug, boolean writeLogFileEnable, long connectTime, long readTime, Long writeTime) {
        mIsDebug = isDebug;
        mWriteLogFileEnable = writeLogFileEnable;
        return new RetrofitWrapper(url, headers, connectTime, readTime, writeTime);
    }

    public <T> T create(final Class<T> service) {
        return mRetrofit.create(service);
    }

    private static class LogInterceptor implements Interceptor {
        protected static final AtomicInteger ID_GEN = new AtomicInteger(0);

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            int id = ID_GEN.incrementAndGet();
            long t1 = System.nanoTime();
            printRequestLog(request, id);
            Response response = chain.proceed(request);
            okhttp3.MediaType mediaType = response.body().contentType();
            byte[] bytes = response.body().bytes();
            printResponseLog(id, response, bytes, System.nanoTime() - t1);
            return response.newBuilder()
                    .body(okhttp3.ResponseBody.create(mediaType, bytes))
                    .build();
        }

        private void printResponseLog(int id, Response response, byte[] bytes, Long costTime) {
            if (mWriteLogFileEnable) {
                Log.d(EVENT_TAG, String.format("http_request_cost_time %.1fms", (costTime / 1e6d)));
                Log.d(EVENT_TAG, " response id:" + id + ",code:" + response.code());
            }
            if (mIsDebug) {
                printLog(id, bytes);
            }
        }

        private void printLog(int id, byte[] bytes) {
            String content = new String(bytes);
            StringBuilder sb = new StringBuilder(content);
            int maxLen = 3 * 1024;
            int length = bytes.length - 1;
            Log.d(TAG, "\n============response id:" + id + "===========START===========================\n");
            if (length > maxLen) {
                while (sb.length() - 1 > maxLen) {
                    String logContent = sb.substring(0, maxLen + 1);
                    sb = sb.replace(0, logContent.length(), "");
                    Log.d(TAG, "content\n" + logContent + "\n");
                }
                Log.d(TAG, "\n" + sb.toString() + "\n");
            } else {
                //打印剩下的字符
                Log.d(TAG, "\n" + sb.toString() + "\n");
            }
            Log.d(TAG, "\n============response id:" + id + "===========END===========================\n");
        }
    }

    private static void printRequestLog(Request request, int id) {

        if (mIsDebug || mWriteLogFileEnable) {
            Log.d(EVENT_TAG, "request id: " + id + " ,url:" + request.toString());
        }
        if (mIsDebug || mWriteLogFileEnable) {
            String method = request.method();
            if ("POST".equals(method.toUpperCase())) {
                RequestBody body = request.body();
                if (request.body() instanceof FormBody) {
                    StringBuilder sb = new StringBuilder();
                    FormBody formBody = (FormBody) request.body();
                    for (int i = 0; i < formBody.size(); i++) {
                        sb.append(formBody.encodedName(i)).append("=").append(formBody.encodedValue(i)).append(",");
                    }
                    sb.delete(sb.length() - 1, sb.length());
                    Log.d(TAG, "request id:" + id + ",body:\n" + sb.toString());
                } else {
                    Log.d(TAG, "request id:" + id + ",body:\n" + bodyToString(body));
                }
            }
        }
    }


    private static String bodyToString(final RequestBody request) {
        try {
            RequestBody copy = request;
            Buffer buffer = new Buffer();
            if (copy != null) {
                copy.writeTo(buffer);
            } else {
                return "";
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
