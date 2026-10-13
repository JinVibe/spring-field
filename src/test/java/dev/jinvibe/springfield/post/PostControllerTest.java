package dev.jinvibe.springfield.post;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PostControllerTest {

	@Autowired
	private MockMvcTester mvc;

	@Test
	void req_6_2_homeListsLatestPostsByCategory() {
		assertThat(mvc.get().uri("/"))
				.hasStatusOk()
				.bodyText()
				.contains("data-jpa", "boot")
				.contains("영속성 컨텍스트 테스트 글", "자동 설정 테스트 글")
				.contains("href=\"/posts/persistence-context\"");
	}

	@Test
	void req_6_2_homeShowsTotalCount() {
		assertThat(mvc.get().uri("/"))
				.hasStatusOk()
				.bodyText()
				.contains("전체 글 <span>2</span>개");
	}

	@Test
	void req_6_2_postDetailRendersMarkdown() {
		assertThat(mvc.get().uri("/posts/persistence-context"))
				.hasStatusOk()
				.bodyText()
				.contains("<h1>영속성 컨텍스트 테스트 글</h1>")
				.contains("<h1>본문 제목</h1>")
				.contains("<code class=\"language-java\">")
				.contains("jpa", "cache", "2026-10-14");
	}

	@Test
	void req_6_1_draftIsHidden() {
		assertThat(mvc.get().uri("/"))
				.bodyText()
				.doesNotContain("작성 중인 테스트 글");
		assertThat(mvc.get().uri("/posts/draft-post"))
				.hasStatus(HttpStatus.NOT_FOUND);
	}

	@Test
	void req_6_2_unknownSlugReturns404() {
		assertThat(mvc.get().uri("/posts/no-such-post"))
				.hasStatus(HttpStatus.NOT_FOUND);
	}
}
