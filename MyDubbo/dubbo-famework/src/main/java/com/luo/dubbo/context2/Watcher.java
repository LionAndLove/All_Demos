package com.luo.dubbo.context2;

import com.luo.dubbo.registry.Node;

public interface Watcher {

    public void remove(Node node);

    public void register(Node node);
}
