package com.hm.cache.manager;//package com.hm.engine.service.impl;

import org.springframework.stereotype.Service;

/**
 * Created by ant_shake_tree on 15/11/5.
 */
@Service
public class RuleMachineImpl extends RuleMachineCallback {



    @Override
    public Object call() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object call(RuleMachine ruleMachine) {
        throw new UnsupportedOperationException();
    }

    /**
     * 注册业务Bean 回调方式调用
     *
     * @param o 业务bean
     * @return
     */
    @Override
    public Object call(Object... o) {
        throw new UnsupportedOperationException();
    }



}
