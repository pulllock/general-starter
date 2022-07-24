package me.cxis.starter.sample.server.controller;

import me.cxis.general.model.RawResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sample/server")
public class SampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    /**
     * 日志打印示例
     * @param id
     * @return
     */
    @RawResult(desc = "结果不需要包装")
    @GetMapping("/log/get")
    public Long logGet(@RequestParam Long id) {
        return id;
    }

}
