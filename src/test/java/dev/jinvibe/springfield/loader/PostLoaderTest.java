package dev.jinvibe.springfield.loader;

import static org.assertj.core.api.Assertions.assertThat;

import dev.jinvibe.springfield.post.Post;
import dev.jinvibe.springfield.post.PostRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class PostLoaderTest {

	@Autowired
	private PostLoader postLoader;

	@Autowired
	private PostRepository postRepository;

	@Test
	void req_6_1_postsAreLoadedOnStartup() {
		assertThat(postRepository.findAll())
				.extracting(Post::getSlug)
				.containsExactlyInAnyOrder("persistence-context", "auto-configuration", "draft-post");
	}

	@Test
	void req_6_1_reloadReplacesExistingPosts() throws Exception {
		postLoader.run(new DefaultApplicationArguments());

		assertThat(postRepository.count()).isEqualTo(3);
	}
}
