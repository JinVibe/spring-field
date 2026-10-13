package dev.jinvibe.springfield.loader;

import dev.jinvibe.springfield.config.PostsProperties;
import dev.jinvibe.springfield.post.Post;
import dev.jinvibe.springfield.post.PostRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * 기동 시 posts 디렉터리의 Markdown 파일을 읽어 DB의 글을 통째로 교체한다.
 */
@Component
@RequiredArgsConstructor
public class PostLoader implements ApplicationRunner {

	private final PostsProperties properties;
	private final PostRepository postRepository;
	private final FrontMatterParser parser = new FrontMatterParser();
	private final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	@Override
	@Transactional
	public void run(ApplicationArguments args) throws IOException {
		List<Post> posts = readPosts();
		postRepository.deleteAll();
		postRepository.flush();
		postRepository.saveAll(posts);
	}

	private List<Post> readPosts() throws IOException {
		List<Post> posts = new ArrayList<>();
		Set<String> slugs = new HashSet<>();
		for (Resource resource : resolver.getResources(properties.location())) {
			String filename = resource.getFilename();
			String slug = filename.substring(0, filename.length() - ".md".length());
			if (!slugs.add(slug)) {
				throw new IllegalStateException("slug가 중복됩니다: " + slug);
			}
			posts.add(parser.parse(slug, resource.getContentAsString(StandardCharsets.UTF_8)));
		}
		return posts;
	}
}
