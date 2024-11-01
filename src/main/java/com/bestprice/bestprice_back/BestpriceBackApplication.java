package com.bestprice.bestprice_back;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BestpriceBackApplication {

	public static void main(String[] args) throws URISyntaxException {
		SpringApplication.run(BestpriceBackApplication.class, args);

		try {
            // Python 스크립트의 경로를 클래스패스에서 찾기
            URL scriptUrl = BestpriceBackApplication.class.getResource("/crawlpython/crawl.py");
            if (scriptUrl == null) {
                throw new IllegalArgumentException("스크립트를 찾을 수 없습니다.");
            }

            // URL을 File 객체로 변환
            File scriptFile = new File(scriptUrl.toURI());

            ProcessBuilder pythonBuilder = new ProcessBuilder("python", scriptFile.getAbsolutePath());
            pythonBuilder.inheritIO();
            
            // 파이썬 서버 시작
            Process pythonProcess = pythonBuilder.start();
            // 프로세스가 종료될 때까지 대기
            pythonProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}

}
