package com.hm;



import java.util.Collection;

/**
 * Created by ant_shake_tree on 15/10/26.
 */
public class PageEntity<ID> {

    private RedissionPageInfo pageInfo;
    private Collection scoredEntries;

    public void setPageInfo(RedissionPageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }



    public RedissionPageInfo getPageInfo() {
        return pageInfo;
    }

    public Collection getScoredEntries() {
        return scoredEntries;
    }

    public void setScoredEntries(Collection scoredEntries) {
        this.scoredEntries = scoredEntries;
    }

    @Override
    public String toString() {
        return "PageEntity{" +
                "pageInfo=" + pageInfo +
                ", scoredEntries=" + scoredEntries +
                '}';
    }
}
