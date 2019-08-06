package com.hadluo.dubbo.invoker;

public class JavassistProxyFactory {

    public static <T> Invoker<T> getInvoker(T proxy, Class<?> type) {
        final Wrapper wrapper = Wrapper.getWrapper(type);
        return new AbstractProxyInvoker<T>(type, proxy) {
            @Override
            public Object doInvoke(Object proxy, Invocation invocation) throws Throwable {
                return wrapper.invokeMethod(proxy, invocation.getMethodName(),
                        invocation.getParameterTypes(), invocation.getArguments());
            }
        };
    }

    public static void main(String[] args) {
        IUserSerice userSerice = new UserServiceImpl();
        User user = new User();
        user.setName("hadluo");
        Invoker<IUserSerice> invoker = JavassistProxyFactory.getInvoker(userSerice, IUserSerice.class);
        // 执行一次 本地调用
        Invocation invocation = new NativeInvocation("login",new Class<?>[]{User.class},new Object[]{user}) ;
        Result result = invoker.invoker(invocation);
        System.err.println("返回 值:" + ((User)result.getResult()).getName());
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
