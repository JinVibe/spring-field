package dev.jinvibe.springfield.loader;

import dev.jinvibe.springfield.post.Post;
import dev.jinvibe.springfield.post.PostStatus;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * "---"로 감싼 YAML front matter와 Markdown 본문을 분리해 {@link Post}로 만든다.
 */
public class FrontMatterParser {

	private static final String DELIMITER = "---";

	public Post parse(String slug, String source) {
		String text = source.replace("\r\n", "\n");
		if (!text.startsWith(DELIMITER + "\n")) {
			throw new IllegalArgumentException(slug + ": front matter가 없습니다");
		}
		int end = text.indexOf("\n" + DELIMITER, DELIMITER.length());
		if (end < 0) {
			throw new IllegalArgumentException(slug + ": front matter가 닫히지 않았습니다");
		}
		Map<String, Object> meta = new Yaml(new SafeConstructor(new LoaderOptions()))
				.load(text.substring(DELIMITER.length() + 1, end));
		int bodyStart = text.indexOf('\n', end + 1);
		String body = bodyStart < 0 ? "" : text.substring(bodyStart + 1);

		return new Post(
				slug,
				required(meta, "title", slug),
				required(meta, "category", slug),
				toDate(meta == null ? null : meta.get("date"), slug),
				PostStatus.valueOf(required(meta, "status", slug).toUpperCase(Locale.ROOT)),
				optional(meta, "code"),
				optional(meta, "test"),
				body,
				toTags(meta == null ? null : meta.get("tags")));
	}

	private String required(Map<String, Object> meta, String key, String slug) {
		String value = optional(meta, key);
		if (value == null || value.isBlank()) {
			throw new IllegalArgumentException(slug + ": front matter에 '" + key + "'가 없습니다");
		}
		return value;
	}

	private String optional(Map<String, Object> meta, String key) {
		Object value = meta == null ? null : meta.get(key);
		return value == null ? null : value.toString();
	}

	// SnakeYAML은 2026-10-14 같은 값을 UTC 자정의 java.util.Date로 읽는다.
	private LocalDate toDate(Object value, String slug) {
		if (value instanceof Date date) {
			return date.toInstant().atZone(ZoneOffset.UTC).toLocalDate();
		}
		if (value == null) {
			throw new IllegalArgumentException(slug + ": front matter에 'date'가 없습니다");
		}
		return LocalDate.parse(value.toString());
	}

	private Set<String> toTags(Object value) {
		Set<String> tags = new LinkedHashSet<>();
		if (value instanceof Collection<?> values) {
			values.forEach(tag -> tags.add(tag.toString()));
		} else if (value != null) {
			tags.add(value.toString());
		}
		return tags;
	}
}
