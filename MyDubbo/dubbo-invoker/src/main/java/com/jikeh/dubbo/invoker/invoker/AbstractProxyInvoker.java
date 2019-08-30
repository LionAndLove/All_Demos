package com.jikeh.dubbo.invoker.invoker;

/***
 * 抽象 代理 执行 器
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2018年4月27日 新建
 */
public abstract class AbstractProxyInvoker<T> implements Invoker<T> {

    private final T object;

    public AbstractProxyInvoker(Class<?> type, T object) {
        this.object = object;
    }

    /***
     * 执行方法
     * 
     * @param target
     * @param invocation 方法参数
     * @return
     * @throws Throwable
     * @author HadLuo 2018年4月27日 新建
     */
    public abstract Object doInvoke(Object target, Invocation invocation) throws Throwable;

    @Override
    public Result invoker(Invocation invocation) {
        if (invocation instanceof NativeInvocation) {
            try {
                Object obj = doInvoke(object, invocation);
                return new RpcResult(obj);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        if(invocation instanceof RpcInvocation){
            // 远程调用
        }
        throw new RuntimeException("暂时 只 实现  本地的 调用");
    }
}
