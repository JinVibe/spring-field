package dev.jinvibe.springfield.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PostController {

	private final PostService postService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("categories", postService.latestByCategory());
		model.addAttribute("totalCount", postService.countPublished());
		return "home";
	}

	@GetMapping("/posts/{slug}")
	public String detail(@PathVariable String slug, Model model) {
		PostDetail detail = postService.getPublished(slug);
		model.addAttribute("post", detail.post());
		model.addAttribute("html", detail.html());
		return "post/detail";
	}
}
