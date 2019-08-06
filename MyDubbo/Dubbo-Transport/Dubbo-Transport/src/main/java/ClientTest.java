import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import com.hadluo.dubbo.common.Constants;
import com.hadluo.dubbo.common.URL;
import com.hadluo.dubbo.transport.Channel;
import com.hadluo.dubbo.transport.ChannelHandler;
import com.hadluo.dubbo.transport.Client;
import com.hadluo.dubbo.transport.RemotingException;
import com.hadluo.dubbo.transport.Transporters;
import com.hadluo.dubbo.transport.wrapper.HeartBeat;

public class ClientTest {

    public static void main(String[] args) throws RemotingException {
        Map<String, String> config = new HashMap<String, String>();

        // netty的最大连接数
        config.put(Constants.ACCEPTS_KEY, 200 + "");
        // 心跳时间 3 s
        config.put(Constants.HEARTBEAT_KEY, 3000 + "");

        URL url = new URL("netty", "", "", "127.0.0.1", 6379, "", config);

        Client client = Transporters.connect(url, new ChannelHandler() {

            @Override
            public void received(Channel channel, Object message) {
                if (message instanceof HeartBeat) {
                    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
                    System.err.println("client>> heartbeat:" + sdf.format(new Date()));
                } else {
                    System.err.println("client>>" + message);
                }
            }

            @Override
            public void disconnected(Channel channel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void connected(Channel channel) {
                // TODO Auto-generated method stub

            }

            @Override
            public void caught(Channel channel, Throwable exception) {

            }
        });

    }
}
