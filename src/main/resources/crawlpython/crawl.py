import time
from flask import Flask, jsonify, request
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException, TimeoutException
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager

app = Flask(__name__)

def setup_driver():
    options = webdriver.ChromeOptions()
    options.add_argument('--headless')  # 헤드리스 모드 활성화
    options.add_argument('--no-sandbox')
    options.add_argument("--disable-gpu")
    options.add_argument("--disable-software-rasterizer")
    options.add_argument('--disable-dev-shm-usage')
    options.add_argument("--disable-blink-features=AutomationControlled")
    options.add_argument('user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36')

    service = Service(ChromeDriverManager().install())
    driver = webdriver.Chrome(service=service, options=options)
    return driver

def search_coupang(keyword):
    driver = setup_driver()
    url = "https://www.coupang.com/np/search?component=194176&q=" + keyword
    driver.get(url)

    try:
        # WebDriverWait(driver, 10).until(EC.presence_of_element_located((By.CLASS_NAME, "search-product")))

        # driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        # time.sleep(2)

        products = driver.find_elements(By.CLASS_NAME, "search-product")

        results = []
        for product in products[:10]:  # 상위 10개 제품만 처리
            try:

                item = {}
                item['productName'] = product.find_element(By.CLASS_NAME, "name").text

                try:
                    discount = product.find_element(By.CLASS_NAME, "instant-discount-rate")
                    item['discount'] = discount.text
                    item['basePrice'] = product.find_element(By.CLASS_NAME, "base-price").text
                except Exception :
                    print(f"할인 없음")
                
                item['price'] = product.find_element(By.CLASS_NAME, "price-value").text

                imgsrc = product.find_element(By.TAG_NAME, 'img')
                item['imgUrl'] = imgsrc.get_attribute('src')

                ahref = product.find_element(By.TAG_NAME, 'a')
                item['link'] = ahref.get_attribute('href')
                item['productId'] = ahref.get_attribute('data-product-id')

                results.append(item)

            except Exception as e:
                print(f"제품 정보 누락: {e}")
        return results
    except TimeoutException:
        print("페이지 로딩 시간이 초과되었습니다.")
        return []
    except Exception as e:
        print(f"크롤링 중 오류가 발생했습니다: {str(e)}")
        return []
    finally:
        driver.quit()

@app.route('/search', methods=['GET'])
def search():
    keyword = request.args.get('keyword', '')
    if not keyword:
        return jsonify({"error": "키워드를 입력해주세요."}), 400

    results = search_coupang(keyword)

    if results:
        return jsonify(results)
    else:
        return jsonify({"message": "검색 결과를 가져오는 데 실패했습니다."}), 500

if __name__ == "__main__":
    app.run(debug=True)
