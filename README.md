-- recipes 테이블 생성 (rcp_sno를 기본 키로 사용)
CREATE TABLE recipes (
    rcp_sno INT PRIMARY KEY,               -- 레시피 번호 (고유 식별자)
    title VARCHAR(255),                    -- 레시피 제목
    ckg_nm VARCHAR(100),                   -- 요리사 이름
    rgtr_id VARCHAR(100),                  -- 등록자 ID
    rgtr_nm VARCHAR(100),                  -- 등록자 이름
    inq_cnt INT DEFAULT 0,                 -- 조회 수
    rcmm_cnt INT DEFAULT 0,                -- 추천 수
    srap_cnt INT DEFAULT 0,                -- 스크랩 수
    category1 VARCHAR(50),                 -- 카테고리 1
    category2 VARCHAR(50),                 -- 카테고리 2
    category3 VARCHAR(50),                 -- 카테고리 3
    category4 VARCHAR(50),                 -- 카테고리 4
    description LONGTEXT,                  -- 레시피 설명
    ingredients LONGTEXT,                  -- 재료 정보
    servings VARCHAR(20),                  -- 인원
    difficulty VARCHAR(20),                -- 난이도
    time_required VARCHAR(50),             -- 소요 시간
    main_thumb VARCHAR(500),               -- 메인 이미지 URL
    first_reg_dt TIMESTAMP                 -- 최초 등록일
) ENGINE=InnoDB;

-- recipe_steps 테이블 생성
CREATE TABLE recipe_steps (
    step_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rcp_sno INT,                           -- 외래 키 (레시피 번호)
    step VARCHAR(1000),                    -- 단계 텍스트
    step_order INT,                        -- 단계 순서
    FOREIGN KEY (rcp_sno) REFERENCES recipes(rcp_sno) ON DELETE CASCADE
) ENGINE=InnoDB;


CREATE TABLE recipe_ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rcp_sno INT,                        -- 외래 키로 사용
    section_name VARCHAR(50) NOT NULL,  -- 섹션 이름
    name VARCHAR(100) NOT NULL,         -- 항목 이름
    amount VARCHAR(50),                 -- 양 (예: "2공기")
    CONSTRAINT fk_recipe FOREIGN KEY (rcp_sno) REFERENCES recipes(rcp_sno) ON DELETE CASCADE
) ENGINE=InnoDB;
