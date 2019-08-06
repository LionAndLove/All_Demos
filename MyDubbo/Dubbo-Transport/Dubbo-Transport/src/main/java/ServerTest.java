import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.RemotingException;
import com.hadluo.dubbo.transport.Server;
import com.hadluo.dubbo.transport.Transporters;
import com.hadluo.dubbo.transport.wrapper.HeartBeat;

public class ServerTest {
    public static void main(String[] args) throws RemotingException {
        Map<String, String> config = new HashMap<String, String>();

        // netty的最大连接数
        config.put(Constants.ACCEPTS_KEY, 200 + "");
        // 心跳时间 3 s
        config.put(Constants.HEARTBEAT_KEY, 3000 + "");
        URL url = new URL("netty", "", "", "127.0.0.1", 6379, "", config);

        Server server = Transporters.bind(url, new ChannelHandler() {
            @Override
            public void received(Channel channel, Object message) {
                if (message instanceof HeartBeat) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                    System.err.println("server>> heartbeat:" + sdf.format(new Date()));
                }
            }

            @Override
            public void disconnected(Channel channel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void connected(Channel channel) {
                // TODO Auto-generated method stub
                channel.send("客户端 你好啊");
            }

            @Override
            public void caught(Channel channel, Throwable exception) {
                // TODO Auto-generated method stub

            }
        });

    }
}
