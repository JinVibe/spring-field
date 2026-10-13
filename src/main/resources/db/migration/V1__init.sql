-- PostgreSQL, MySQL, H2 공통 표준 SQL만 사용한다.
-- IDENTITY는 MySQL이 지원하지 않으므로 유일한 slug를 PK로 쓴다.
CREATE TABLE post (
    slug         VARCHAR(200) NOT NULL,
    title        VARCHAR(300) NOT NULL,
    category     VARCHAR(100) NOT NULL,
    published_on DATE         NOT NULL,
    status       VARCHAR(20)  NOT NULL,
    code_path    VARCHAR(500),
    test_path    VARCHAR(500),
    content      TEXT         NOT NULL,
    PRIMARY KEY (slug)
);

CREATE TABLE post_tag (
    post_slug VARCHAR(200) NOT NULL,
    tag       VARCHAR(100) NOT NULL,
    PRIMARY KEY (post_slug, tag),
    FOREIGN KEY (post_slug) REFERENCES post (slug)
);
