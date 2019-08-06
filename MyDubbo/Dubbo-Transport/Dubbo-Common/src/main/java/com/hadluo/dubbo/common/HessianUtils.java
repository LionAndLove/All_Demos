package com.hadluo.dubbo.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

public class HessianUtils {

    // public static class User implements Serializable {
    // int age;
    // String name;
    //
    // public void setName(String name) {
    // this.name = name;
    // }
    //
    // public void setAge(int age) {
    // this.age = age;
    // }
    //
    // public int getAge() {
    // return age;
    // }
    //
    // public String getName() {
    // return name;
    // }
    // }
    //
    // public static void main(String[] args) throws IOException {
    // User user = new User();
    // user.setAge(122323);
    // String k = "";
    // for (int i = 0; i < 1000; i++) {
    // k += "123ewdasdfesf2";
    // }
    // System.out.println("==========" + k.length());
    // user.setName(k);
    //
    // long s1 = System.currentTimeMillis();
    // String u = serialize(user);
    // System.out.println("序列化时间：  " + (System.currentTimeMillis() - s1));
    // System.out.println(u.length() + "   u:" + u);
    // s1 = System.currentTimeMillis();
    // user = (User) deserialize(u);
    // System.out.println("反序列化时间：  " + (System.currentTimeMillis() - s1));
    // System.out.println(user.getAge());
    // System.out.println(user.getName());
    // }

    /***
     * 序列化 对象
     * 
     * @param obj
     * @return
     * @throws IOException
     * @author HadLuo 2017年11月22日 新建
     */
    public static String serialize(Object obj) {
        if (obj == null)
            throw new NullPointerException();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HessianOutput ho = new HessianOutput(os);
        try {
            ho.writeObject(obj);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("序列化失败，请检查是否实现了 java.io.Serializable");
        }
        return gzip(os.toByteArray());
    }

    /***
     * 反序列化对象
     * 
     * @param by
     * @return
     * @throws IOException
     * @author HadLuo 2017年11月22日 新建
     */
    public static Object deserialize(String by) {
        if (by == null)
            throw new NullPointerException();
        ByteArrayInputStream is = new ByteArrayInputStream(gunzip(by));
        HessianInput hi = new HessianInput(is);
        try {
            return hi.readObject();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("反序列化失败");
        }
    }

    /**
     * 
     * 使用gzip进行压缩
     */
    @SuppressWarnings("restriction")
    public static String gzip(byte[] bytes) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = null;
        try {
            gzip = new GZIPOutputStream(out);
            gzip.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (gzip != null) {
                try {
                    gzip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new sun.misc.BASE64Encoder().encode(out.toByteArray());
    }

    /**
     *
     * <p>
     * Description:使用gzip进行解压缩
     * </p>
     * 
     * @param compressedStr
     * @return
     */
    @SuppressWarnings("restriction")
    public static byte[] gunzip(String compressedStr) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ByteArrayInputStream in = null;
        GZIPInputStream ginzip = null;
        byte[] compressed = null;
        try {
            compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
            in = new ByteArrayInputStream(compressed);
            ginzip = new GZIPInputStream(in);
            byte[] buffer = new byte[1024];
            int offset = -1;
            while ((offset = ginzip.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            return out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("zip解压失败");
        } finally {
            if (ginzip != null) {
                try {
                    ginzip.close();
                } catch (IOException e) {
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
