package com.jikeh;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 压测测试控制器：
 */
@RestController
public class PressureController {

    /**
     * bio方式压测：
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/bio", method = RequestMethod.POST)
    public void getByBio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Connection", "keep-alive");
        OutputStream out = response.getOutputStream();

        response.setStatus(200);
        out.write(getContent());
        out.close();
    }

    /**
     * nio方式压测：
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/nio", method = RequestMethod.POST)
    public void getByNio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Connection", "keep-alive");
        OutputStream out = response.getOutputStream();

        response.setStatus(200);
        out.write(getContent());
        out.close();
    }

    /**
     * netty方式压测：
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/netty", method = RequestMethod.POST)
    public void getByNetty(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Connection", "keep-alive");
        OutputStream out = response.getOutputStream();

        response.setStatus(200);
        out.write(getContent());
        out.close();
    }

    /**
     * ok bio方式压测：
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/okBio", method = RequestMethod.POST)
    public void getByOkBio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Connection", "keep-alive");
        OutputStream out = response.getOutputStream();

        response.setStatus(200);
        out.write(getContent());
        out.close();
    }

    /**
     * ok nio方式压测：
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping(value = "/okNio", method = RequestMethod.POST)
    public void getByOkNio(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setHeader("Connection", "keep-alive");
        OutputStream out = response.getOutputStream();

        response.setStatus(200);
        out.write(getContent());
        out.close();
    }

    /**
     * 组合测试报文：
     *
     * @return
     */
    private static byte[] getContent(){
        byte[] content = new byte[2048];
        final int r = Math.abs(content.hashCode());
        for (int i = 0; i < content.length; i++) {
            content[i] = (byte) ((r + i) % 96 + 32);
        }
        return content;
    }

}
