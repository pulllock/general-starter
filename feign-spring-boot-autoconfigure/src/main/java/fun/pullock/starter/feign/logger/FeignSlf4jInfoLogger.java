/**
 * Copyright 2012-2021 The Feign Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package fun.pullock.starter.feign.logger;

import feign.Request;
import feign.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Logs to SLF4J at the debug level, if the underlying logger has debug logging enabled. The
 * underlying logger can be specified at construction-time, defaulting to the logger for
 * {@link feign.Logger}.
 */
public class FeignSlf4jInfoLogger extends feign.Logger {

    private final Logger logger;

    public FeignSlf4jInfoLogger() {
        this(feign.Logger.class);
    }

    public FeignSlf4jInfoLogger(Class<?> clazz) {
        this(LoggerFactory.getLogger(clazz));
    }

    public FeignSlf4jInfoLogger(String name) {
        this(LoggerFactory.getLogger(name));
    }

    FeignSlf4jInfoLogger(Logger logger) {
        this.logger = logger;
    }

    @Override
    protected void logRequest(String configKey, Level logLevel, Request request) {
        if (logger.isInfoEnabled()) {
            super.logRequest(configKey, logLevel, request);
        }
    }

    @Override
    protected Response logAndRebufferResponse(String configKey,
                                              Level logLevel,
                                              Response response,
                                              long elapsedTime)
            throws IOException {
        if (logger.isInfoEnabled()) {
            return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
        }
        return response;
    }

    @Override
    protected void log(String configKey, String format, Object... args) {
        // Not using SLF4J's support for parameterized messages (even though it would be more efficient)
        // because it would
        // require the incoming message formats to be SLF4J-specific.
        if (logger.isInfoEnabled()) {
            logger.info(String.format(methodTag(configKey) + format, args));
        }
    }
}
