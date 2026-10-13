# spring-field PRD

## 1. 한 줄 정의

Spring을 공부하며 정리한 글을 올리는 개인 사이트. 사이트 자체가 Spring Boot로 만들어져 있어, 글에서 다룬 개념을 같은 저장소의 코드와 테스트로 바로 확인할 수 있다.

## 2. 목표

- 글(Markdown)을 저장소에 커밋하면 사이트에 자동 반영된다.
- 글마다 "이 글의 예제 코드"와 "검증 테스트" 링크가 붙는다.
- 외부 서비스 없이 `./gradlew bootRun` 한 번으로 로컬에서 전부 동작한다.
- 나중에 장애 주입·에이전트 실험의 대상 서비스로 쓸 수 있도록 구조를 단순하게 유지한다.

## 3. 비목표 (이번 범위에서 제외)

- 회원가입, 로그인, 댓글
- 관리자 에디터 UI (글은 파일로만 작성)
- 외부 DB, 클라우드 배포 자동화
- 검색엔진 연동, SEO 최적화
- 디자인 시스템, 다크모드

## 4. 사용자

- 1차: 작성자 본인 (글 쓰고 다시 읽는 용도)
- 2차: 채용 담당자·면접관 (저장소와 사이트를 함께 보는 사람)

## 5. 기술 스택

- Java 21, Spring Boot 3.x, Gradle (Kotlin DSL)
- Spring MVC + Thymeleaf (SSR, JS 최소화)
- Spring Data JPA + H2 (파일 모드, 로컬 전용)
- flexmark-java (Markdown → HTML)
- 테스트: JUnit 5, AssertJ, Spring Boot Test, Testcontainers는 필요할 때만
- 빌드 검사: Checkstyle 또는 Spotless (하나만)

## 6. 기능 요구사항

### 6.1 글 관리

- 글은 `src/main/resources/posts/{category}/{slug}.md` 에 저장한다.
- 파일 상단 front matter:
  ```
  ---
  title: 영속성 컨텍스트와 1차 캐시
  category: data-jpa
  date: 2026-10-14
  tags: [jpa, cache]
  code: src/main/java/.../PersistenceContextExample.java
  test: src/test/java/.../PersistenceContextTest.java
  status: draft | published
  ---
  ```
- 애플리케이션 기동 시 posts 디렉터리를 스캔해 DB(H2)에 적재한다. 파일이 바뀌면 재기동으로 반영한다 (핫리로드는 비목표).
- `status: draft` 글은 목록과 상세 어디에도 노출하지 않는다.

### 6.2 페이지

| 경로 | 내용 |
|---|---|
| `/` | 카테고리별 최신 글 목록, 전체 글 수 |
| `/posts` | 전체 글 목록, 카테고리·태그 필터(쿼리 파라미터) |
| `/posts/{slug}` | 글 본문(HTML 변환), 메타 정보, 예제 코드·테스트 링크(GitHub URL), 이전·다음 글 |
| `/categories/{category}` | 해당 카테고리 글 목록 |
| `/tags/{tag}` | 해당 태그 글 목록 |
| `/about` | 사이트 소개(README 내용) |

- 존재하지 않는 slug → 404 페이지.
- 모든 페이지는 서버 렌더링이며 JS 없이도 읽을 수 있어야 한다.

### 6.3 코드 블록

- Markdown 코드 블록은 언어별 하이라이트(클라이언트 highlight.js 허용, 이 경우만 JS 사용).
- front matter의 `code`, `test` 경로는 저장소의 GitHub 링크로 변환해 상세 페이지 상단에 표시한다.

### 6.4 운영 엔드포인트

- Spring Boot Actuator `health`, `info`, `metrics` 활성화.
- `info`에 git 커밋 해시, 빌드 시각, 글 개수를 노출한다.

## 7. 비기능 요구사항

- 기동 시간 5초 이내 (글 100개 기준).
- 글 상세 페이지 응답 50ms 이내 (로컬, 캐시 없이).
- 테스트 커버리지 숫자는 목표로 두지 않되, 6.1·6.2의 각 요구사항마다 최소 하나의 테스트가 있어야 한다.
- 컨트롤러·서비스·리포지토리 3계층. 도메인 객체는 JPA 엔티티와 분리하지 않는다 (규모상 불필요).

## 8. 디렉터리 구조

```
spring-field/
├── build.gradle.kts
├── src/main/java/dev/jinvibe/springfield/
│   ├── SpringFieldApplication.java
│   ├── post/          # Post 엔티티, 리포지토리, 서비스, 컨트롤러
│   ├── loader/        # posts 디렉터리 스캔·적재
│   ├── markdown/      # Markdown 변환
│   └── config/
├── src/main/resources/
│   ├── posts/         # 글
│   ├── templates/     # Thymeleaf
│   ├── static/        # css, highlight.js
│   └── application.yml
├── src/test/java/...  # 요구사항별 테스트 + 글 검증 테스트
├── docs/              # 설계 메모, ADR
└── README.md
```

## 9. 완료 기준 (Acceptance)

1. `./gradlew bootRun` 후 `/`에서 글 목록이 보인다.
2. `posts/` 에 새 md 파일을 추가하고 재기동하면 목록과 상세에 나타난다.
3. `status: draft` 글은 어디에도 보이지 않는다.
4. 상세 페이지에서 예제 코드·테스트의 GitHub 링크가 올바르게 열린다.
5. 없는 slug 요청 시 404 페이지가 뜬다.
6. `./gradlew test` 가 통과하고, 6.1·6.2 요구사항마다 대응 테스트가 존재한다.
7. `/actuator/info` 에 커밋 해시와 글 개수가 나온다.

## 10. 마일스톤

- M1 — 뼈대: 프로젝트 생성, Post 엔티티, posts 로더, `/`와 `/posts/{slug}` 동작. 글 1개로 확인.
- M2 — 탐색: 카테고리·태그 페이지, 이전·다음 글, 404, draft 필터.
- M3 — 코드 연결: front matter `code`/`test` 링크, 하이라이트, Actuator info.
- M4 — 글 채우기: Core·Boot·MVC·JPA 각 1편 이상 작성, 각 글에 예제·테스트 연결.
- M5 — 정리: README 갱신, docs/ADR 2개 이상(왜 SSR인지, 왜 파일 기반인지).

## 11. 작업 규칙 (Claude Code용)

- 마일스톤 단위로 브랜치를 만들고 PR로 머지한다. main에 직접 커밋하지 않는다.
- 기능 하나를 끝내면 반드시 `./gradlew test` 를 통과시킨 뒤 다음으로 넘어간다.
- 요구사항에 없는 기능은 추가하지 않는다. 필요하다고 판단되면 docs/에 메모만 남긴다.
- 라이브러리 추가 시 이유를 커밋 메시지에 적는다.
- 글 본문은 작성자가 직접 쓴다. 에이전트는 글의 코드 예제와 테스트만 생성·검증한다.

## 12. 이후 계획 (이번 PRD 범위 밖)

- 이 서비스에 의도적 결함을 주입하고, 로그·메트릭을 읽어 원인을 진단하고 수정 PR을 만드는 온콜 에이전트 실험의 대상 서비스로 사용한다.