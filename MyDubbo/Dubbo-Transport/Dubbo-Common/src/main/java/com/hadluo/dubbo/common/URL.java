package com.hadluo.dubbo.common;

import java.io.Serializable;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/***
 * dubbo://hadluo:1234@127.0.0.1:20881/context/path?version=1.0.0&application=
 * morgan&noValue 也包括 ：file:/path/to/file.txt
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月3日 新建
 */
public final class URL implements Serializable {
    /** xx */
    private static final long serialVersionUID = -6256557181054799417L;
    /** 协议 （例如： dubbo:// zookeeper://） */
    private final String protocol;
    /** 用户名 （例如: dubbo://hadluo） */
    private final String username;
    /** 密码 */
    private final String password;
    /** 地址 */
    private final String host;
    /** 端口 */
    private final int port;
    /** 上下文路径 */
    private final String path;
    /** 附加参数 */
    private final Map<String, String> parameters;

    /** 缓存 优化 当前URL的 toString（） */
    private volatile transient String string;

    public URL(String protocol, String username, String password, String host, int port, String path,
            Map<String, String> parameters) {
        if ((username == null || username.length() == 0) && password != null && password.length() > 0) {
            throw new IllegalArgumentException("Invalid url, password without username!");
        }
        this.protocol = protocol;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = (port < 0 ? 0 : port);
        this.path = path;
        // trim the beginning "/"
        while (path != null && path.startsWith("/")) {
            path = path.substring(1);
        }
        if (parameters == null) {
            parameters = new HashMap<String, String>();
        } else {
            parameters = new HashMap<String, String>(parameters);
        }
        // 不可修改的 map
        this.parameters = Collections.unmodifiableMap(parameters);
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getParameter(String key, String defaultVal) {
        String value = parameters.get(key);
        if (value == null || value.length() == 0) {
            value = defaultVal;
        }
        return value;
    }

    public int getParameter(String key, int defaultVal) {
        try {
            return Integer.parseInt(parameters.get(key));
        } catch (Exception e) {
            return defaultVal;
        }
    }

    public InetSocketAddress toInetSocketAddress() {
        return new InetSocketAddress(host, port);
    }

    /**
     * Parse url string
     * 
     * @param url URL string
     * @return URL instance
     * @see URL
     */
    public static URL valueOf(String url) {
        // dubbo://hadluo:1234@127.0.0.1:20881/context/path?version=1.0.0&application=morgan&noValue
        if (url == null || (url = url.trim()).length() == 0) {
            throw new IllegalArgumentException("url == null");
        }
        String protocol = null;
        String username = null;
        String password = null;
        String host = null;
        int port = 0;
        String path = null;
        Map<String, String> parameters = null;
        int i = url.indexOf("?"); // seperator between body and parameters
        if (i >= 0) {
            // 以 & 分隔 version=1.0.0&application=morgan&noValue
            String[] parts = url.substring(i + 1).split("\\&");
            parameters = new HashMap<String, String>();
            for (String part : parts) {
                part = part.trim();
                if (part.length() > 0) {
                    int j = part.indexOf('=');
                    if (j >= 0) {
                        // 是 key=val 形式的
                        parameters.put(part.substring(0, j), part.substring(j + 1));
                    } else {
                        // 只有 val形式的
                        parameters.put(part, part);
                    }
                }
            }
            // url 变成 ： dubbo://hadluo:1234@127.0.0.1:20881/context/path?
            url = url.substring(0, i);
        }
        i = url.indexOf("://");
        if (i >= 0) {
            if (i == 0)
                throw new IllegalStateException("url missing protocol: \"" + url + "\"");
            // 取 dubbo
            protocol = url.substring(0, i);
            // url变成 hadluo:1234@127.0.0.1:20881/context/path
            url = url.substring(i + 3);
        } else {
            // case: file:/path/to/file.txt
            i = url.indexOf(":/");
            if (i >= 0) {
                if (i == 0)
                    throw new IllegalStateException("url missing protocol: \"" + url + "\"");
                protocol = url.substring(0, i);
                url = url.substring(i + 1);
            }
        }

        i = url.indexOf("/");
        if (i >= 0) {
            // context/path
            path = url.substring(i + 1);
            // url变成 hadluo:1234@127.0.0.1:20881
            url = url.substring(0, i);
        }
        i = url.indexOf("@");
        if (i >= 0) {
            // hadluo:1234
            username = url.substring(0, i);
            int j = username.indexOf(":");
            if (j >= 0) {
                // 1234
                password = username.substring(j + 1);
                // hadluo
                username = username.substring(0, j);
            }
            // 127.0.0.1:20881
            url = url.substring(i + 1);
        }
        i = url.indexOf(":");
        if (i >= 0 && i < url.length() - 1) {
            // 20881
            port = Integer.parseInt(url.substring(i + 1));
            // 127.0.0.1
            url = url.substring(0, i);
        }
        if (url.length() > 0)
            host = url;
        return new URL(protocol, username, password, host, port, path, parameters);
    }

    public String toString() {
        if (string != null) {
            return string;
        }
        // 打印成 string ，并缓存起来
        return string = buildString();
    }

    private String buildString() {
        StringBuilder buf = new StringBuilder();
        if (protocol != null && protocol.length() > 0) {
            buf.append(protocol);
            buf.append("://");
        }
        if (username != null && username.length() > 0) {
            buf.append(username);
            if (password != null && password.length() > 0) {
                buf.append(":");
                buf.append(password);
            }
            buf.append("@");
        }
        if (host != null && host.length() > 0) {
            buf.append(host);
            if (port > 0) {
                buf.append(":");
                buf.append(port);
            }
        }
        if (path != null && path.length() > 0) {
            buf.append("/");
            buf.append(path);
        }
        buildParameters(buf);
        return buf.toString();
    }

    /***
     * 封装参数
     * 
     * @param buf
     * @author HadLuo 2018年5月3日 新建
     */
    private void buildParameters(StringBuilder buf) {
        if (this.parameters != null && this.parameters.size() > 0) {
            boolean first = true;
            for (Map.Entry<String, String> entry : new TreeMap<String, String>(this.parameters).entrySet()) {
                if (entry.getKey() != null && entry.getKey().length() > 0) {
                    if (first) {
                        // 第一个 参数 是添加 ?
                        buf.append("?");
                        first = false;
                    } else {
                        // 后面参数 是 &连接
                        buf.append("&");
                    }
                    buf.append(entry.getKey());
                    buf.append("=");
                    buf.append(entry.getValue() == null ? "" : entry.getValue().trim());
                }
            }
        }
    }
}
