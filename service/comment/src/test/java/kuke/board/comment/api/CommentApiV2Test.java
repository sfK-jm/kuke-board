package kuke.board.comment.api;

import kuke.board.comment.service.response.CommentPageResponse;
import kuke.board.comment.service.response.CommentResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.util.List;

public class CommentApiV2Test {

    RestClient restClient = RestClient.create("http://localhost:9001");

    @Test
    void create() {
        CommentResponse response1 = create(new CommentCreateRequestV2(1L, "my comment1", null, 1L));
        CommentResponse response2 = create(new CommentCreateRequestV2(1L, "my comment2", response1.getPath(), 1L));
        CommentResponse response3 = create(new CommentCreateRequestV2(1L, "my comment3", response2.getPath(), 1L));

        System.out.println("response1.getCommentId() = " + response1.getCommentId());
        System.out.println("\tresponse2.getCommentId() = " + response2.getCommentId());
        System.out.println("\t\tresponse3.getCommentId() = " + response3.getCommentId());

        System.out.println("================================================================");

        System.out.println("response1.getPath() = " + response1.getPath());
        System.out.println("\tresponse2.getPath() = " + response2.getPath());
        System.out.println("\t\tresponse3.getPath() = " + response3.getPath());

        /**
         * response1.getCommentId() = 201315291476987904
         * 	response2.getCommentId() = 201315291695091712
         * 		response3.getCommentId() = 201315291753811968
         * ================================================================
         * response1.getPath() = 00002
         * 	response2.getPath() = 0000200000
         * 		response3.getPath() = 000020000000000
         */
    }

    CommentResponse create(CommentCreateRequestV2 request) {
        return restClient.post()
                .uri("/v2/comments")
                .body(request)
                .retrieve()
                .body(CommentResponse.class);
    }


    @Test
    void read() {
        CommentResponse response = restClient.get()
                .uri("/v2/comments/{commentId}", 201315291476987904L)
                .retrieve()
                .body(CommentResponse.class);

        System.out.println("response = " + response);
    }

    @Test
    void delete() {
        restClient.delete()
                .uri("/v2/comments/{commentId}", 201315291476987904L)
                .retrieve();
    }

    @Test
    void readAll() {
        CommentPageResponse response = restClient.get()
                .uri("/v2/comments?articleId=1&pageSize=10&page=50000")
                .retrieve()
                .body(CommentPageResponse.class);

        System.out.println("response.getCommentCount() = " + response.getCommentCount());
        for (CommentResponse comment : response.getComments()) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /**
         * response.getCommentCount() = 101
         * comment.getCommentId() = 201326709107834884
         * comment.getCommentId() = 201326709149777928
         * comment.getCommentId() = 201326709149777929
         * comment.getCommentId() = 201326709149777937
         * comment.getCommentId() = 201326709149777943
         * comment.getCommentId() = 201326709149777948
         * comment.getCommentId() = 201326709149777958
         * comment.getCommentId() = 201326709149777962
         * comment.getCommentId() = 201326709153972232
         * comment.getCommentId() = 201326709153972238
         */
    }

    @Test
    void readAllInfiniteScroll() {
        List<CommentResponse> response1 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5")
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("firstPage");
        for (CommentResponse comment : response1) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        String lastPath = response1.getLast().getPath();

        List<CommentResponse> response2 = restClient.get()
                .uri("/v2/comments/infinite-scroll?articleId=1&pageSize=5&lastPath=%s".formatted(lastPath))
                .retrieve()
                .body(new ParameterizedTypeReference<List<CommentResponse>>() {
                });

        System.out.println("secondPage");
        for (CommentResponse comment : response2) {
            System.out.println("comment.getCommentId() = " + comment.getCommentId());
        }

        /**
         * firstPage
         * comment.getCommentId() = 201326709107834884
         * comment.getCommentId() = 201326709149777928
         * comment.getCommentId() = 201326709149777929
         * comment.getCommentId() = 201326709149777937
         * comment.getCommentId() = 201326709149777943
         * secondPage
         * comment.getCommentId() = 201326709149777948
         * comment.getCommentId() = 201326709149777958
         * comment.getCommentId() = 201326709149777962
         * comment.getCommentId() = 201326709153972232
         * comment.getCommentId() = 201326709153972238
         */
    }

    @Getter
    @AllArgsConstructor
    public static class CommentCreateRequestV2 {
        private Long articleId;
        private String content;
        private String parentPath;
        private Long writerId;
    }
}
