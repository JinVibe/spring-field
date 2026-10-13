# spring-field

Spring을 공부하면서 정리한 내용을 모아두는 개인 사이트입니다.
사이트 자체도 Spring Boot로 만들어, 글에서 설명한 것을 이 저장소에서 바로 확인할 수 있게 하는 것이 목표입니다.

## 다루는 내용

- Spring Core: IoC/DI, Bean 생명주기, AOP
- Spring Boot: 자동 설정, 프로파일, 외부 설정
- Spring MVC: 요청 처리 흐름, 예외 처리, 검증
- Spring Data JPA: 영속성 컨텍스트, 연관관계, N+1, 트랜잭션
- 테스트: 단위/통합 테스트, 테스트 컨테이너
- 운영: 캐시, 스케줄러, Actuator, 부하 테스트

## 구조

    spring-field/
    ├── src/main/java/      # 사이트 애플리케이션
    ├── src/main/resources/
    │   └── posts/          # 정리 글 (Markdown)
    ├── src/test/           # 글에서 다룬 내용의 검증 테스트
    └── docs/               # 설계 메모

## 실행

    ./gradlew bootRun

http://localhost:8080 에서 확인할 수 있습니다.

## 원칙

- 글마다 실행 가능한 예제 코드나 테스트를 함께 둔다.
- 책이나 강의 내용을 옮기지 않고, 직접 실행해 확인한 것만 적는다.
- 틀렸던 이해는 지우지 않고 수정 이력으로 남긴다.