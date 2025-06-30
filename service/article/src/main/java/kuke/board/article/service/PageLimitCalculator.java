package kuke.board.article.service;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PageLimitCalculator {

    public static Long calculatePageLimit(Long page, Long pageSize, Long movablePageCount) {
        return (((page - 1) / movablePageCount) + 1) * pageSize * movablePageCount + 1;
    }
}
