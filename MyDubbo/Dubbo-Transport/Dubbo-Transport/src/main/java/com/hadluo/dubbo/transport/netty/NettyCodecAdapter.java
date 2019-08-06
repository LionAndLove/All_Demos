package com.hadluo.dubbo.transport.netty;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.common.URL;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/***
 * netty 传输 编解码实现 (使用 StringEncoder，StringDecoder )
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年5月8日 新建
 */
public class NettyCodecAdapter {
    private final ChannelHandler encoder = new InternalEncoder();

    private final ChannelHandler decoder = new InternalDecoder();

    /** netty最大传输字节 */
    private int payload;
    /** netty粘包 分隔符 */
    private String delimiter;
    private final DelimiterBasedFrameDecoder delimiterBasedFrameDecoder;

    public NettyCodecAdapter(URL url) {

        payload = url.getParameter(Constants.PAYLOAD_KEY, Constants.DEFAULT_PAYLOAD);
        delimiter = url.getParameter(Constants.LINE_DELIMITER_KEY, Constants.LINE_DELIMITER);
        // 指定 粘包处理分隔符 和 最大传输数据量
        delimiterBasedFrameDecoder = new DelimiterBasedFrameDecoder(payload, Unpooled.copiedBuffer(delimiter
                .getBytes()));
    }

    public ChannelHandler getEncoder() {
        return encoder;
    }

    public ChannelHandler getDecoder() {
        return decoder;
    }

    public DelimiterBasedFrameDecoder getDelimiterBasedFrameDecoder() {
        return delimiterBasedFrameDecoder;
    }

    /***
     * netty的 StringEncoder实现
     * 
     * @author HadLuo
     * @since JDK1.7
     * @history 2018年5月8日 新建
     */
    private class InternalEncoder extends StringEncoder {
        @Override
        protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
            if (msg.length() == 0) {
                return;
            }
            out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), Charset.forName("UTF-8")));
        }
    }

    /***
     * netty的 StringDecoder 实现 , 这里 做了一步重要操作： 将string转换成对象
     * 
     * @author HadLuo
     * @since JDK1.7
     * @history 2018年5月8日 新建
     */
    private class InternalDecoder extends StringDecoder {
        @Override
        protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {
            String content = msg.toString(Charset.forName("UTF-8"));
            out.add(TCP.decode(content));
        }
    }

}
