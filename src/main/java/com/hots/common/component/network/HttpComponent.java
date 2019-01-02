package com.hots.common.component.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Component;

@Component
public class HttpComponent {

    /**
     * 获取客户端外网IP
     */
    public String getRequestIp(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");

        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknow".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();

            if (ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")) {
                // 根据网卡获取本机配置的IP地址
                InetAddress inetAddress = null;
                try {
                    inetAddress = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                ipAddress = inetAddress.getHostAddress();
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实的IP地址，多个IP按照','分割
        if (null != ipAddress && ipAddress.length() > 15) {
            // "***.***.***.***".length() = 15
            if (ipAddress.indexOf(",") > 0) {
                ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
            }
        }

        return ipAddress;
    }

    private HttpPost getHttpPost(String requestUrl, String encode, Map<String, String> paramMap) throws Exception {
        // 创建post方式请求对象
        HttpPost httpPost = new HttpPost(requestUrl);

        // 设置header信息
        // 指定报文头【Content-type】、【User-Agent】
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        // 装填参数
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (paramMap != null) {
            for (Entry<String, String> entry : paramMap.entrySet()) {
                nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        // 设置参数到请求对象中
        httpPost.setEntity(new UrlEncodedFormEntity(nvps, encode));

        return httpPost;
    }

    /**
     * 编码解析网页内容
     */
    public Map<Integer, String> getPostDataJsoup(String requestUrl, String encode, Map<String, String> paramMap)
            throws Exception {
        Map<Integer, String> resultMap = new HashMap<Integer, String>();
        HttpResponse response = null;
        HttpClient client = null;
        // 创建httpclient对象
        client = HttpClientBuilder.create().build();
        // 创建post方式请求对象
        response = client.execute(getHttpPost(requestUrl, encode, paramMap));

        HttpEntity entity = null;
        if (response != null && response.getStatusLine().getStatusCode() >= 200
                && response.getStatusLine().getStatusCode() < 300) {
            entity = response.getEntity();
        }
        if (response != null) {
            if (entity != null) {
                resultMap.put(response.getStatusLine().getStatusCode(), entity.getContent().toString());
            } else {
                resultMap.put(response.getStatusLine().getStatusCode(), null);
            }
        }

        return resultMap;
    }

}
