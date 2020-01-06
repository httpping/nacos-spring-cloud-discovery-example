package com.vip.config.loop.monitor;

import com.alibaba.fastjson.JSONObject;
import com.vip.config.loop.LoopEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * TODO
 *
 * @author dean <tanping@globalegrow.com>
 * @version 1.0.0
 * @date 2020/01/06 10:00
 * @since 1.0.0
 */
@RestController
public class LoopMonitorController {

    @Autowired
    LoopMonitorService loopMonitorService;

    @RequestMapping(path = "loop/monitor/all")
    public List<LoopEngine> list(){
        return loopMonitorService.getLoopEngines();
    }

    @RequestMapping(path = "loop/monitor/shutdown")
    public LoopEngine shutDown(String id){
        List<LoopEngine> result = loopMonitorService.getLoopEngines();
        for (LoopEngine loopEngine: result){

            if (id != null && id.equalsIgnoreCase(loopEngine.getId())){
                loopEngine.shutdown();
               return loopEngine;
            }
        }
        return null;
    }

    @RequestMapping(path = "loop/monitor/shutdownAll")
    public List<LoopEngine> shutDownAll(){
        List<LoopEngine> result = loopMonitorService.getLoopEngines();
        for (LoopEngine loopEngine: result){
                loopEngine.shutdown();
        }
        return result;
    }
}
