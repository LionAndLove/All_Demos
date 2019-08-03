package com.luo.dubbo.registry;

/***
 * provider全路径: /dubbo/providers/com.luo.IUser/127.0.0.1:2181
 * 
 * @author HadLuo
 * @since JDK1.7
 * @history 2017年12月19日 新建
 */
public class ConsumerNode extends Node {

    /** xx */
    private static final long serialVersionUID = -7553629219268728437L;

    @Override
    public void parser(String nodePath, Bundle val) {
        // path: /dubbo/providers/com.luo.IUser/127.0.0.1:2181
        super.parser(nodePath, val);
    }

    @Override
    public String header() {
        return PathInfo.Root + PathInfo.C_PathLevel1;
    }

}
