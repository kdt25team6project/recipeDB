https://drive.google.com/file/d/1i408UufMx7IWmco8aQVCHkJ1TiXCNmxf/view?usp=drive_link
csv 원본 파일

브라우저로 데이터 저장하는법
http://localhost:8080/api/recipes/csv/load (접속)

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

-- 레시피 단계 테이블 생성
CREATE TABLE recipe_steps (
    step_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 단계 ID
    rcp_sno INT NOT NULL,                       -- 외래 키 (레시피 번호)
    step VARCHAR(1000) NOT NULL,                -- 단계 설명
    step_order INT NOT NULL,                    -- 단계 순서
    step_img VARCHAR(500),                      -- 단계 이미지 URL
    CONSTRAINT fk_recipe_step FOREIGN KEY (rcp_sno) REFERENCES recipes(rcp_sno) ON DELETE CASCADE
) ENGINE=InnoDB;



CREATE TABLE recipe_ingredients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rcp_sno INT,                        -- 외래 키로 사용
    section_name VARCHAR(50) NOT NULL,  -- 섹션 이름
    name VARCHAR(100) NOT NULL,         -- 항목 이름
    amount VARCHAR(50),                 -- 양 (예: "2공기")
    CONSTRAINT fk_recipe FOREIGN KEY (rcp_sno) REFERENCES recipes(rcp_sno) ON DELETE CASCADE
) ENGINE=InnoDB;

