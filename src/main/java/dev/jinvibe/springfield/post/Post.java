package dev.jinvibe.springfield.post;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;

@Entity
@Getter
public class Post {

	@Id
	private String slug;

	private String title;

	private String category;

	private LocalDate publishedOn;

	@Enumerated(EnumType.STRING)
	private PostStatus status;

	private String codePath;

	private String testPath;

	private String content;

	@ElementCollection
	@CollectionTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_slug"))
	@Column(name = "tag")
	private Set<String> tags = new LinkedHashSet<>();

	protected Post() {
	}

	public Post(String slug, String title, String category, LocalDate publishedOn, PostStatus status,
				String codePath, String testPath, String content, Set<String> tags) {
		this.slug = slug;
		this.title = title;
		this.category = category;
		this.publishedOn = publishedOn;
		this.status = status;
		this.codePath = codePath;
		this.testPath = testPath;
		this.content = content;
		this.tags = new LinkedHashSet<>(tags);
	}

	public boolean isPublished() {
		return status == PostStatus.PUBLISHED;
	}
}
