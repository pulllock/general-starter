package me.cxis.general.model;

/**
 * 分页查询参数
 */
public class PageQuery {

    /**
     * 页大小，默认10
     */
    private Integer pageSize = 10;

    /**
     * 页数，默认1
     */
    private Integer pageNo = 1;

    public Integer getStartNo() {
        if (pageNo == null || pageNo <= 0 || pageSize == null || pageSize <= 0) {
            return 0;
        }
        return (this.pageNo - 1) * this.pageSize;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }
}
