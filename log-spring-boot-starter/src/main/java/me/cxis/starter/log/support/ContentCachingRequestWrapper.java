package me.cxis.starter.log.support;

import org.springframework.util.StreamUtils;
import org.springframework.web.util.WebUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ContentCachingRequestWrapper extends HttpServletRequestWrapper {

    private final byte[] cachedContent;

    public ContentCachingRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        cachedContent = StreamUtils.copyToByteArray(request.getInputStream());
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new ContentCachingInputStream(cachedContent);
    }

    @Override
    public String getCharacterEncoding() {
        String enc = super.getCharacterEncoding();
        return (enc != null ? enc : WebUtils.DEFAULT_CHARACTER_ENCODING);
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream(), getCharacterEncoding()));
    }

    public byte[] getCachedContent() {
        return cachedContent;
    }

    private static class ContentCachingInputStream extends ServletInputStream {

        private final ByteArrayInputStream is;

        public ContentCachingInputStream(byte[] cachedContent) {
            is = new ByteArrayInputStream(cachedContent);
        }

        @Override
        public boolean isFinished() {
            return this.is.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {

        }

        @Override
        public int read() throws IOException {
            return is.read();
        }
    }
}
