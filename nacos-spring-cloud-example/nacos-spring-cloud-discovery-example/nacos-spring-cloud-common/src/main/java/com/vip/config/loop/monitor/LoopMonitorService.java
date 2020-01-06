package com.vip.config.loop.monitor;

import com.vip.config.loop.LoopEngine;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/06 09:53
 * @since 1.0.0
 */
@Service
@Getter
public class LoopMonitorService {

    List<LoopEngine> loopEngines = new ArrayList<>();


    public void register(LoopEngine loopEngine){
        loopEngines.add(loopEngine);
    }

    public void remove(LoopEngine loopEngine){
        loopEngines.remove(loopEngine);
    }

}
