# CLAUDE.md
- PRD.md의 범위 밖 기능은 만들지 않는다. 필요하면 docs/ideas.md에 메모만.
- 마일스톤 단위 브랜치(m1-skeleton 등), main 직접 커밋 금지, PR로 머지.
- 기능 하나 끝낼 때마다 ./gradlew test 통과 후 다음 작업.
- 3계층(controller/service/repository), 패키지는 기능 단위(post, loader, markdown).
- 생성자 주입만 사용, 필드 주입 금지. Lombok은 @RequiredArgsConstructor, @Getter만.
- 테스트는 요구사항 번호를 메서드명에 남긴다. 예: req_6_1_draftIsHidden()
- 라이브러리 추가 시 이유를 커밋 메시지에 쓴다.
- 글(posts/*.md) 본문은 작성하지 않는다. 예제 코드와 테스트만.