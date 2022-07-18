package me.cxis.general.model;

import java.io.Serializable;

/**
 * 分页查询结果
 * @param <T>
 */
public class PageResult <T extends Serializable> extends Result<T> implements Serializable {

    /**
     * 总数
     */
    private int count;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "PageResult{" +
                "count=" + count +
                "} " + super.toString();
    }
}
