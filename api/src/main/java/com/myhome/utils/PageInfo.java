package com.myhome.utils;

import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PageInfo {
  private final int currentPage;
  private final int pageLimit;
  private final int totalPages;
  private final long totalElements;

  private PageInfo(int currentPage, int pageLimit, int totalPages, long totalElements) {
    this.currentPage = currentPage;
    this.pageLimit = pageLimit;
    this.totalPages = totalPages;
    this.totalElements = totalElements;
  }

  public static PageInfo of(Pageable pageable, Page<?> page) {
    return new PageInfo(
        pageable.getPageNumber(),
        pageable.getPageSize(),
        page.getTotalPages(),
        page.getTotalElements()
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PageInfo pageInfo = (PageInfo) o;
    return currentPage == pageInfo.currentPage
        && pageLimit == pageInfo.pageLimit
        && totalPages == pageInfo.totalPages
        && totalElements == pageInfo.totalElements;
  }

  @Override
  public int hashCode() {
    return Objects.hash(currentPage, pageLimit, totalPages, totalElements);
  }

  @Override public String toString() {
    return "PageInfo{" +
        "currentPage=" + currentPage +
        ", pageLimit=" + pageLimit +
        ", totalPages=" + totalPages +
        ", totalElements=" + totalElements +
        '}';
  }
}
