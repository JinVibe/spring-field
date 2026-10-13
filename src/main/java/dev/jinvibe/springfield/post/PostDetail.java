package dev.jinvibe.springfield.post;

/**
 * 상세 페이지에 필요한 글과 HTML로 변환한 본문.
 */
public record PostDetail(Post post, String html) {
}
