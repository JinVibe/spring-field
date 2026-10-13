package dev.jinvibe.springfield.post;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ActiveProfiles("test")
class PostRepositoryTest {

	@Autowired
	private PostRepository postRepository;

	// 같은 인메모리 DB를 쓰는 다른 테스트 컨텍스트가 적재한 글을 지운다. 테스트 트랜잭션이 끝나면 롤백된다.
	@BeforeEach
	void clear() {
		postRepository.deleteAll();
	}

	@Test
	void req_6_1_draftIsExcludedFromPublishedQueries() {
		postRepository.save(post("published", PostStatus.PUBLISHED, LocalDate.of(2026, 10, 1)));
		postRepository.save(post("draft", PostStatus.DRAFT, LocalDate.of(2026, 10, 2)));

		assertThat(postRepository.findByStatusOrderByPublishedOnDesc(PostStatus.PUBLISHED))
				.extracting(Post::getSlug)
				.containsExactly("published");
		assertThat(postRepository.countByStatus(PostStatus.PUBLISHED)).isEqualTo(1);
		assertThat(postRepository.findBySlugAndStatus("draft", PostStatus.PUBLISHED)).isEmpty();
	}

	@Test
	void req_6_1_publishedPostsAreOrderedByDateDesc() {
		postRepository.save(post("older", PostStatus.PUBLISHED, LocalDate.of(2026, 1, 1)));
		postRepository.save(post("newer", PostStatus.PUBLISHED, LocalDate.of(2026, 2, 1)));

		assertThat(postRepository.findByStatusOrderByPublishedOnDesc(PostStatus.PUBLISHED))
				.extracting(Post::getSlug)
				.containsExactly("newer", "older");
	}

	@Test
	void req_6_1_tagsArePersisted() {
		postRepository.saveAndFlush(post("tagged", PostStatus.PUBLISHED, LocalDate.of(2026, 10, 1)));

		assertThat(postRepository.findBySlugAndStatus("tagged", PostStatus.PUBLISHED))
				.get()
				.extracting(Post::getTags)
				.isEqualTo(Set.of("jpa", "cache"));
	}

	private Post post(String slug, PostStatus status, LocalDate date) {
		return new Post(slug, "title-" + slug, "data-jpa", date, status, null, null, "body", Set.of("jpa", "cache"));
	}
}
