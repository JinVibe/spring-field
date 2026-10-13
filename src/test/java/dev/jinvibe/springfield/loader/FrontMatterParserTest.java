package dev.jinvibe.springfield.loader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import dev.jinvibe.springfield.post.Post;
import dev.jinvibe.springfield.post.PostStatus;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class FrontMatterParserTest {

	private final FrontMatterParser parser = new FrontMatterParser();

	@Test
	void req_6_1_frontMatterIsParsed() {
		String source = """
				---
				title: 영속성 컨텍스트와 1차 캐시
				category: data-jpa
				date: 2026-10-14
				tags: [jpa, cache]
				code: src/main/java/Example.java
				test: src/test/java/ExampleTest.java
				status: published
				---
				# 본문
				""";

		Post post = parser.parse("persistence-context", source);

		assertThat(post.getSlug()).isEqualTo("persistence-context");
		assertThat(post.getTitle()).isEqualTo("영속성 컨텍스트와 1차 캐시");
		assertThat(post.getCategory()).isEqualTo("data-jpa");
		assertThat(post.getPublishedOn()).isEqualTo(LocalDate.of(2026, 10, 14));
		assertThat(post.getTags()).containsExactly("jpa", "cache");
		assertThat(post.getCodePath()).isEqualTo("src/main/java/Example.java");
		assertThat(post.getTestPath()).isEqualTo("src/test/java/ExampleTest.java");
		assertThat(post.getStatus()).isEqualTo(PostStatus.PUBLISHED);
		assertThat(post.getContent()).isEqualTo("# 본문\n");
	}

	@Test
	void req_6_1_draftStatusIsParsed() {
		Post post = parser.parse("wip", """
				---
				title: 초안
				category: boot
				date: 2026-10-14
				status: draft
				---
				""");

		assertThat(post.getStatus()).isEqualTo(PostStatus.DRAFT);
		assertThat(post.getTags()).isEmpty();
	}

	@Test
	void req_6_1_missingRequiredFieldFails() {
		assertThatThrownBy(() -> parser.parse("no-title", """
				---
				category: boot
				date: 2026-10-14
				status: published
				---
				"""))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("no-title")
				.hasMessageContaining("title");
	}

	@Test
	void req_6_1_missingFrontMatterFails() {
		assertThatThrownBy(() -> parser.parse("plain", "# 그냥 본문\n"))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessageContaining("plain");
	}
}
