package me.cxis.starter.sample.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sample")
public class SampleController {

    private final static Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("/log/get")
    public String logGet(@RequestParam Long id) {
        return "log get id: " + id;
    }

    @PostMapping("/log/post")
    public Long logPost(@RequestBody LogModel log) {
        LOGGER.info("log post, log: {}", log);
        return 1L;
    }

    static class LogModel {
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
