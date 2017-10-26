/*
 * Copyright (C) 2015 Vince Styling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.vincestyling.netroid.stack;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.vincestyling.netroid.AuthFailureError;
import com.vincestyling.netroid.NetroidCookieManage;
import com.vincestyling.netroid.Request;
import com.vincestyling.netroid.Request.Method;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;
import org.apache.http.protocol.HTTP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * An {@link HttpStack} based on {@link HttpURLConnection}.
 */
public class HurlStack implements HttpStack {

    private String mUserAgent;
    private final SSLSocketFactory mSslSocketFactory;
    NetroidCookieManage netroidCookieManage;
    String Host="";//域名
    /**
     * @param sslSocketFactory SSL factory to use for HTTPS connections
     */
    public HurlStack(String userAgent, SSLSocketFactory sslSocketFactory) {
        mSslSocketFactory = sslSocketFactory;
        mUserAgent = userAgent;
    }
    public HurlStack(Context context,String userAgent, SSLSocketFactory sslSocketFactory) {
        mSslSocketFactory = sslSocketFactory;
        mUserAgent = userAgent;
        netroidCookieManage=NetroidCookieManage.getInstance(context);
    }

    public HurlStack(String userAgent) {
        this(userAgent, null);
    }

    @Override
    public HttpResponse performRequest(Request<?> request) throws IOException, AuthFailureError {
        /*//拼接请求带参数完整路径
        StringBuilder completeURL=new StringBuilder();
        completeURL.append(request.getUrl()).append("?");
        Map mapParams=request.getParams();
        Set keSet=mapParams.entrySet();
        for(Iterator itr = keSet.iterator(); itr.hasNext();){
            Map.Entry me=(Map.Entry)itr.next();
            Object ok=me.getKey();
            Object ov=me.getValue();
            String[] value=new String[1];
            if(ov instanceof String[]){
                value=(String[])ov;
            }else{
                value[0]=ov.toString();
            }
            for(int k=0;k<value.length;k++){
                completeURL.append(ok+"="+value[k]).append("&");
            }
        }
        Log.e("完整请求路径:",completeURL.deleteCharAt(completeURL.lastIndexOf("&")).toString());*/
        HashMap<String, String> map = new HashMap<String, String>();
        if (!TextUtils.isEmpty(mUserAgent)) {
            map.put(HTTP.USER_AGENT, mUserAgent);
        }
        map.putAll(request.getHeaders());
        URL parsedUrl = new URL(request.getUrl());

        //================遍历请求头，获取域名===============
        if(Host==null||Host.isEmpty()){
            Map<String, String> requestHeaders= request.getHeaders();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Log.e(entry.getKey(),entry.getValue());
                if(entry.getKey().equals("Host")){
                    Host=entry.getValue();
                }
            }
        }
        //=======================获取本地缓存Cookie并放入map================
       String cookieCache =netroidCookieManage.getString(Host,"");//根据域名获取对应的缓存cookie
        if(cookieCache!=null&&!cookieCache.isEmpty()){
            Log.e("cookieCache:",cookieCache);
            map.put("Cookie",cookieCache);
        }
        String CASTGC_Cache =netroidCookieManage.getString(parsedUrl.getHost(),"");//根据域名获取对应CASTGC的缓存cookie
        if(CASTGC_Cache!=null&&!CASTGC_Cache.isEmpty()){
            Log.e("CASTGC_Cache:",CASTGC_Cache);
            map.put("Cookie",CASTGC_Cache);
        }

        //=======================================================================
        HttpURLConnection connection = openConnection(parsedUrl, request);
        connection.setInstanceFollowRedirects(false);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);

        int responseCode = connection.getResponseCode();
        Log.e("--------------",responseCode+"");
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could not be retrieved.
            // Signal to the caller that something was wrong with the connection.
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }

        StatusLine responseStatus = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1),
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        if (hasResponseBody(request.getMethod(), responseStatus.getStatusCode())) {
            response.setEntity(entityFromConnection(connection));
        }

        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                if(header.getValue()!=null){
                    for(int i=0;i<header.getValue().size();i++ ){
                        Header h = new BasicHeader(header.getKey(), header.getValue().get(i));
                        response.addHeader(h);
                    }
                }
            }
        }

        //=======================缓存cookie==========================================
        Header[] cookieHeaders=response.getHeaders("Set-Cookie");
        if(cookieHeaders!=null&&cookieHeaders.length>0){
            for(int i=0; i<cookieHeaders.length; i++){
                Log.e("Set-Cookie===name:",cookieHeaders[i].getName());
                Log.e("Set-Cookie===Value:",cookieHeaders[i].getValue());
                if(cookieHeaders[i].getValue().contains("sid=")||cookieHeaders[i].getValue().contains("JSESSIONID=")){
                    netroidCookieManage.put(Host,cookieHeaders[i].getValue());
                    netroidCookieManage.commit();
                }
                if(cookieHeaders[i].getValue().contains("CASTGC=")){
                    netroidCookieManage.put(parsedUrl.getHost(),cookieHeaders[i].getValue());
                    netroidCookieManage.commit();
                }
            }
        }
        //===========================================================================
        /**
         * 如果返回302或301重定向，则发起新的请求
         */
        if(request.getMethod()==Method.GET&&(responseStatus.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY||
                responseStatus.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY)) {
            String redirectUrl = connection.getHeaderField("Location");
            if(redirectUrl !=null&& redirectUrl.length() >0) {
                Log.e("第一次重定向地址：",redirectUrl);
                return executeRequest(request,redirectUrl,map);
            }
        }
        return response;
    }
    /**
     * 重定向重新请求方法
     * @param url
     * @param map
     * @return
     * @throws IOException
     * @throws AuthFailureError
     */
    public HttpResponse executeRequest(Request<?> request, String url, HashMap<String, String> map)throws IOException, AuthFailureError{
        URL parsedUrl = new URL(url);
        //=======================获取本地缓存Cookie并放入map================
        String cookieCache =netroidCookieManage.getString(Host,"");//根据域名获取对应的缓存cookie
        if(cookieCache!=null&&!cookieCache.isEmpty()){
            Log.e("cookieCache:",cookieCache);
            map.put("Cookie",cookieCache);
        }
        String CASTGC_Cache =netroidCookieManage.getString(parsedUrl.getHost(),"");//根据域名获取对应CASTGC的缓存cookie
        if(CASTGC_Cache!=null&&!CASTGC_Cache.isEmpty()){
            Log.e("CASTGC_Cache:",CASTGC_Cache);
            map.put("Cookie",CASTGC_Cache);
        }

        HttpURLConnection connection = openConnection(parsedUrl, request);
        connection.setInstanceFollowRedirects(false);
        for (String headerName : map.keySet()) {
            connection.addRequestProperty(headerName, map.get(headerName));
        }
        setConnectionParametersForRequest(connection, request);
        int responseCode = connection.getResponseCode();
        Log.e("================",responseCode+"");
        if (responseCode == -1) {
            // -1 is returned by getResponseCode() if the response code could not be retrieved.
            // Signal to the caller that something was wrong with the connection.
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        }

        StatusLine responseStatus = new BasicStatusLine(new ProtocolVersion("HTTP", 1, 1),
                connection.getResponseCode(), connection.getResponseMessage());
        BasicHttpResponse response = new BasicHttpResponse(responseStatus);
        if (hasResponseBody(request.getMethod(), responseStatus.getStatusCode())) {
            response.setEntity(entityFromConnection(connection));
        }

        for (Entry<String, List<String>> header : connection.getHeaderFields().entrySet()) {
            if (header.getKey() != null) {
                if(header.getValue()!=null){
                    for(int i=0;i<header.getValue().size();i++ ){
                        Header h = new BasicHeader(header.getKey(), header.getValue().get(i));
                        response.addHeader(h);
                    }
                }
            }
        }

        //=======================缓存cookie==========================================
        Header[] cookieHeaders=response.getHeaders("Set-Cookie");
        if(cookieHeaders!=null&&cookieHeaders.length>0){
            for(int i=0; i<cookieHeaders.length; i++){
                Log.e("Set-Cookie===name:",cookieHeaders[i].getName());
                Log.e("Set-Cookie===Value:",cookieHeaders[i].getValue());
                if(cookieHeaders[i].getValue().contains("sid=")||cookieHeaders[i].getValue().contains("JSESSIONID=")){
                    netroidCookieManage.put(Host,cookieHeaders[i].getValue());
                    netroidCookieManage.commit();
                }
                if(cookieHeaders[i].getValue().contains("CASTGC=")){
                    netroidCookieManage.put(parsedUrl.getHost(),cookieHeaders[i].getValue());
                    netroidCookieManage.commit();
                }
            }
        }
        //==========================================================================
        /**
         * 如果返回302或301重定向，则发起新的请求
         */
        if(responseStatus.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY||
                responseStatus.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
            String redirectUrl = connection.getHeaderField("Location");
            if(redirectUrl !=null&& redirectUrl.length() >0) {
                Log.e("重定向地址：",redirectUrl);
                return executeRequest(request,redirectUrl,map);
            }
        }
        return response;
    }
    /**
     * Checks if a response message contains a body.
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.3">RFC 7230 section 3.3</a>
     * @param requestMethod request method
     * @param responseCode response status code
     * @return whether the response has a body
     */
    private static boolean hasResponseBody(int requestMethod, int responseCode) {
        return requestMethod != Request.Method.HEAD
                && !(HttpStatus.SC_CONTINUE <= responseCode && responseCode < HttpStatus.SC_OK)
                && responseCode != HttpStatus.SC_NO_CONTENT
                && responseCode != HttpStatus.SC_NOT_MODIFIED;
    }

    /**
     * Initializes an {@link HttpEntity} from the given {@link HttpURLConnection}.
     *
     * @return an HttpEntity populated with data from <code>connection</code>.
     */
    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();
        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException ioe) {
            inputStream = connection.getErrorStream();
        }
        entity.setContent(inputStream);
        entity.setContentLength(connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    /**
     * Create an {@link HttpURLConnection} for the specified {@code url}.
     */
    protected HttpURLConnection createConnection(URL url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Workaround for the M release HttpURLConnection not observing the
        // HttpURLConnection.setFollowRedirects() property.
        // https://code.google.com/p/android/issues/detail?id=194495
        connection.setInstanceFollowRedirects(HttpURLConnection.getFollowRedirects());

        return connection;
    }

    /**
     * Opens an {@link HttpURLConnection} with parameters.
     *
     * @return an open connection
     */
    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = createConnection(url);

        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);

        // use caller-provided custom SslSocketFactory, if any, for HTTPS
        if ("https".equals(url.getProtocol()) && mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(mSslSocketFactory);
        }

        return connection;
    }

    private static void setConnectionParametersForRequest(
            HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case Method.GET:
                // Not necessary to set the request method because connection defaults to GET but
                // being explicit here.
                connection.setRequestMethod("GET");
                break;
            case Method.DELETE:
                connection.setRequestMethod("DELETE");
                break;
            case Method.POST:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            case Method.PUT:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            case Method.HEAD:
                connection.setRequestMethod("HEAD");
                break;
            case Method.OPTIONS:
                connection.setRequestMethod("OPTIONS");
                break;
            case Method.TRACE:
                connection.setRequestMethod("TRACE");
                break;
            case Method.PATCH:
                addBodyIfExists(connection, request);
                connection.setRequestMethod("PATCH");
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }
    }

    private static void addBodyIfExists(
            HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty(HTTP.CONTENT_TYPE, request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }
    }

    private String [] splitString(String splitString){
        String [] splitStrings={};
        if(splitString!=null&&!splitString.isEmpty()&&splitString.contains(";")){
            splitStrings=splitString.split(";");
        }
        return splitStrings;
    }
}
