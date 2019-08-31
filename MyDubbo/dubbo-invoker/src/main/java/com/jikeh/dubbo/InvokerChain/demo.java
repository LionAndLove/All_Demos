package com.jikeh.dubbo.InvokerChain;

import com.jikeh.dubbo.InvokerChain.Invoker.Invoker;
import com.jikeh.dubbo.InvokerChain.Invoker.UserInvoker;
import com.jikeh.dubbo.InvokerChain.filter.EchoFilter;
import com.jikeh.dubbo.InvokerChain.filter.EchoFilter2;
import com.jikeh.dubbo.InvokerChain.filter.Filter;

import java.util.ArrayList;
import java.util.List;

public class demo {

    public static void main(String[] args) {
        Invoker<UserInvoker> last = buildChain(new UserInvoker());
        System.out.println(last.invoke("test"));
        UserInvoker.SubUserInvoker subUserInvoker = new UserInvoker.SubUserInvoker();
        System.out.println(subUserInvoker);
    }

    private static Invoker<UserInvoker> buildChain(Invoker<UserInvoker> invoker){
        List<Filter> filters = new ArrayList<>();
        Filter echoFilter = new EchoFilter();
        Filter echoFilter2 = new EchoFilter2();
        filters.add(echoFilter);
        filters.add(echoFilter2);

        Invoker<UserInvoker> last = invoker;
        for (int i = filters.size() - 1; i >= 0; i--) {
            //获取过滤对象
            Filter filter = filters.get(i);
            //可执行对象上面注入过滤对象
            Invoker<UserInvoker> next = last;
            last = new Invoker<UserInvoker>() {
                /**
                 * 重写Invoker的invoke方法
                 * @param invocation
                 * @return
                 */
                @Override
                public String invoke(String invocation) {
                    //过滤链里面执行invoke操作：
                    //echoFilter——>重写的Invoker——>echoFilter2——>userInvoker
                    return filter.doInvoke(next, invocation);
                }

            };
        }
        return last;
    }

}
