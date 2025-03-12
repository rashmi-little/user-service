package com.mindfire.backend.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageResponse<T> {
    private List<T> content;

    private int totalPages;

    private long totalElements;

    private int size;

    private int currentPageNumber;

    /*
    * current page number while in the service starts from 0 in the frontend
    * we will be consider the starts as 1 so currentPageNumber =currentPageNumber + 1
    * */
    public PageResponse(List<T> content, int totalPages, long totalElements, int size, int currentPageNumber) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.currentPageNumber = currentPageNumber + 1;
    }

}
