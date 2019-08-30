package com.jikeh.dubbo.invoker.invoker;

public class JavassistProxyFactory {

    /**
     * 简化流程：
     * 1、Invoker<IUserSerice> invoker = new AbstractProxyInvoker();
     * 2、Result result = invoker.invoker();
     *
     * 升级：拓展Invoker的功能，增加一个动态代理来升级现有的功能
     *
     * @param args
     */
    public static void main(String[] args) {
        //暴露服务：Invoker
        IUserSerice userSerice = new UserServiceImpl();
        User user = new User();
        user.setName("hadluo");
        //使用Javassist动态生成一个代理，用于完成封装的功能
        // 用构造 IUserSerice 接口 对应的 Invoker ，dubbo是extensionloader构造的，这里我们直接模拟.
        Invoker<IUserSerice> invoker = JavassistProxyFactory.getInvoker(userSerice, IUserSerice.class);

        //引用服务：

        // 执行调用 本地调用
        //参数封装，用于通信传递，远程再根据参数初始化Invoker参数
        //构造调用方法信息，Invocation是调用方法信息，我们这里是 login方法信息
        Invocation invocation = new NativeInvocation("login", new Class<?>[]{User.class}, new Object[]{user});
        //执行一次 本地调用
        Result result = invoker.invoker(invocation);
        System.err.println("返回 值:" + ((User) result.getResult()).getName());
    }

    /**
     *
     * @param target 被代理的对象
     * @param type 被代理对象的类型
     * @param <T>
     * @return
     */
    public static <T> Invoker<T> getInvoker(T target, Class<?> type) {
        //根据不同的type生成不同的代理，完成不同的功能：JDK实现
        final Wrapper wrapper = Wrapper.getWrapper(type);
        //生成一个回调对象
        return new AbstractProxyInvoker<T>(type, target) {
            @Override
            public Object doInvoke(Object target, Invocation invocation) throws Throwable {
                //执行代理：
                return wrapper.invokeMethod(target, invocation.getMethodName(), invocation.getParameterTypes(), invocation.getArguments());
            }
        };
    }
}

class User {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

interface IUserSerice {
    public User login(User user);
}

class UserServiceImpl implements IUserSerice {
    @Override
    public User login(User user) {
        System.err.println(user.getName() + " login....");
        user.setName("login success");
        return user;
    }
}
