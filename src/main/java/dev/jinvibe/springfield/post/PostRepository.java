package dev.jinvibe.springfield.post;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, String> {

	List<Post> findByStatusOrderByPublishedOnDesc(PostStatus status);

	long countByStatus(PostStatus status);

	@EntityGraph(attributePaths = "tags")
	Optional<Post> findBySlugAndStatus(String slug, PostStatus status);
}
