package dev.jinvibe.springfield.markdown;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class MarkdownRendererTest {

	private final MarkdownRenderer renderer = new MarkdownRenderer();

	@Test
	void req_6_2_markdownIsRenderedToHtml() {
		assertThat(renderer.render("# 제목\n\n본문")).contains("<h1>제목</h1>", "<p>본문</p>");
	}

	@Test
	void req_6_3_fencedCodeBlockKeepsLanguageClass() {
		assertThat(renderer.render("```java\nint a = 1;\n```\n"))
				.contains("<code class=\"language-java\">");
	}
}
