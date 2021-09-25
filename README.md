# ☕ Grids & Circle 커피 주문 관리 시스템

---

## 🚀 주요 기능

사용자는 이메일, 주소, 우편번호를 입력하여 상품을 주문할 수 있습니다.

관리자는 상품을 등록, 변경, 삭제할 수 있습니다.

전날 오후 2시부터 당일 2시까지의 주문을 배송상태로 변경합니다.

---

## ⚙ 개발 환경

**통합 환경**

- macOS
- Intellij

**백엔드 환경**

- Java 16
- Maven 3.8.2
- SpringBoot 2.5.4
- Mysql 8.0.25

**프론트**

- Node 16.3.0
- React 17.0.2
- Bootstrap 5.1.1

---

## ▶ 실행 방법

```bash
git clone https://github.com/prgrms-be-devcourse/w6-CloneProject.git

프론트 실행
cd kdt-react-order-ui
npm install
npm start

백엔드 실행 (Mysql 커넥팅 확인 후 테이블 추가 필요 create table 쿼리는 밑에 있습니다.)
cd kdet-spring-gc-cafe
mvn package
cd target
java -jar kdt-spring-gc-cafe-0.0.1-SNAPSHOT.jar
```

---

## ✏ 구성도

![usecase](https://user-images.githubusercontent.com/58363663/133761964-65ea6e3b-e4cf-482a-ba15-d5433d320328.png)

---

## 📝 구현할 기능 목록

### 📌 **관리자 기능**

- 관리자 페이지에서 상품을 등록한다.
- 관리자 페이지에서 상품을 조회한다.
- 관리자 페이지에서 상품을 수정한다.
- 관리자 페이지에서 상품을 삭제한다.
- 전날 2시부터 당일 2시까지의 주문 목록을 배송상태로 변경한다.

### 📌 **고객 기능**

- 상품을 조회한다.
- 주문을 등록한다.

### 📌 **예외 처리**

- 상품의 이름이 20글자가 넘을 수 없다. (한글 기준)
- 상품의 가격은 0원 미만일 수 없다.
- 주문시 이메일은 이메일 형식이어야한다.
- 주문이 존재할 경우 삭제가 불가능하다.

---

## 📖 ERD

<img width="654" alt="erd" src="https://user-images.githubusercontent.com/58363663/133761939-7944596d-79ee-4b30-a897-9b78bd74fe17.png">

```sql
CREATE TABLE products
(
    product_id   BINARY(16) PRIMARY KEY,
    product_name VARCHAR(50) NOT NULL,
    category     VARCHAR(50) NOT NULL,
    price        BIGINT      NOT NULL,
    description  TEXT,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6) NOT NULL
);

CREATE TABLE orders
(
    order_id     BINARY(16) PRIMARY KEY,
    email        VARCHAR(255) NOT NULL,
    address      VARCHAR(255) NOT NULL,
    postcode     VARCHAR(12)  NOT NULL,
    total_price  BIGINT       NOT NULL,
    order_status VARCHAR(50)  NOT NULL,
    created_at   DATETIME(6) NOT NULL,
    updated_at   DATETIME(6) NOT NULL
);

CREATE TABLE order_items
(
    order_item_id BIGINT      NOT NULL PRIMARY KEY AUTO_INCREMENT,
    order_id      BINARY(16) NOT NULL,
    product_id    BINARY(16) NOT NULL,
    category      VARCHAR(50) NOT NULL,
    price         BIGINT      NOT NULL,
    quantity      INT         NOT NULL,
    created_at    DATETIME(6) NOT NULL,
    updated_at    DATETIME(6) NOT NULL,
    INDEX (order_id),
    CONSTRAINT fk_order_items_to_order FOREIGN KEY (order_id) REFERENCES orders (order_id) ON DELETE CASCADE,
    CONSTRAINT fk_order_items_to_product FOREIGN KEY (product_id) REFERENCES products (product_id)
);
```

---

## 📑 API 명세서

### 📌 상품 조회

GET 요청으로 상품을 조회할 수 있다.

```http request
GET http://localhost:8080/api/v1/products
```

```json

[
  {
    "productId": "ad389ceb-2d9e-49b9-9170-1d960181582f",
    "productName": "코스타리카 원두",
    "category": "COFFE BEAN PACKAGE",
    "price": 5000
  },
  {
    "productId": "dceecc8d-42fc-4bb2-b164-4c257eba7546",
    "productName": "에티오피아 원두",
    "category": "COFFE BEAN PACKAGE",
    "price": 7000
  }
]
```

### 📌 상품 등록

POST 요청으로 주문을 등록한다.

```http request
POST http: //localhost:8080/api/v1/orders
```

```json


{
  "email": "tester@email.com",
  "address": "서울시 도봉구",
  "postcode": "123-123",
  "orderItems": [
    {
      "productId": "413449a0-a072-4544-89a9-8846df89ab45",
      "category": "COFFEE_BEAN_PACKAGE",
      "price": 5000,
      "quantity": 1
    },
    {
      "productId": "94edd80a-b57b-4293-8b58-8f0b945adb87",
      "category": "COFFEE_BEAN_PACKAGE",
      "price": 8000,
      "quantity": 1
    }
  ]
}
```