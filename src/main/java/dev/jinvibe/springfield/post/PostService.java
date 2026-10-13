package dev.jinvibe.springfield.post;

import dev.jinvibe.springfield.markdown.MarkdownRenderer;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

	static final int LATEST_PER_CATEGORY = 5;

	private final PostRepository postRepository;
	private final MarkdownRenderer markdownRenderer;

	/**
	 * 카테고리별 최신 글. 카테고리는 가장 최근 글이 있는 순서로 정렬된다.
	 */
	public Map<String, List<Post>> latestByCategory() {
		return postRepository.findByStatusOrderByPublishedOnDesc(PostStatus.PUBLISHED).stream()
				.collect(Collectors.groupingBy(Post::getCategory, LinkedHashMap::new,
						Collectors.collectingAndThen(Collectors.toList(),
								posts -> posts.subList(0, Math.min(LATEST_PER_CATEGORY, posts.size())))));
	}

	public long countPublished() {
		return postRepository.countByStatus(PostStatus.PUBLISHED);
	}

	public PostDetail getPublished(String slug) {
		Post post = postRepository.findBySlugAndStatus(slug, PostStatus.PUBLISHED)
				.orElseThrow(() -> new PostNotFoundException(slug));
		return new PostDetail(post, markdownRenderer.render(post.getContent()));
	}
}
