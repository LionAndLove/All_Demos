import com.toby.spi.ISayName;
import org.junit.Test;
import java.util.ServiceLoader;

public class SPITest {

    @Test
    public void test() throws Exception {
        ServiceLoader<ISayName> serviceLoaders = ServiceLoader.load(ISayName.class);
        for(ISayName sayName : serviceLoaders){
            sayName.say();
        }
    }

}
