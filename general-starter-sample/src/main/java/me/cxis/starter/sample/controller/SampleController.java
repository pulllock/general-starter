package me.cxis.starter.sample.controller;

import me.cxis.general.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@RequestMapping("/sample")
public class SampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("/log/get")
    public String logGet(@RequestParam Long id) {
        return "log get id: " + id;
    }

    @PostMapping("/log/post")
    public LogModel logPost(@RequestBody LogModel log) {
        LOGGER.info("log post, log: {}", log);
        return log;
    }

    @GetMapping("/model/get")
    public Result<LogModel> modelGet(@RequestParam Long id) {
        LogModel logModel = new LogModel();
        logModel.setId(id);

        Result<LogModel> result = new Result<>();
        result.setData(logModel);
        return result;
    }

    static class LogModel implements Serializable {
        private Long id;

        private String content;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        @Override
        public String toString() {
            return "LogModel{" +
                    "id=" + id +
                    ", content='" + content + '\'' +
                    '}';
        }
    }
}
