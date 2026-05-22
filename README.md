# miniDooray-AccountAPI

miniDooray 프로젝트의 계정(Account) 관리 REST API 서비스입니다.

## 기술 스택

| 항목 | 내용 |
|------|------|
| Language | Java 21 |
| Framework | Spring Boot 4.0.6 |
| ORM | Spring Data JPA (Hibernate) |
| DB (운영) | MySQL |
| DB (개발) | H2 (In-Memory, MySQL 모드) |
| Security | Spring Security |
| Validation | Jakarta Bean Validation |
| Build | Maven |

## 프로젝트 구조

```
src/main/java/com/nhnacadmey/minidoorayaccount/
├── MiniDoorayAccountApiApplication.java
├── account/
│   ├── dto/
│   │   ├── request/          # CreateAccountReq, UpdateAccountReq, AccountListReq
│   │   └── response/         # AccountResp, AccountListResp, LoginReqAccountResp
│   ├── entity/               # Account, UserStatus
│   ├── mapper/               # AccountMapper (DTO ↔ Entity)
│   ├── projection/           # LoginAccountProjection
│   ├── repository/           # AccountRepository
│   └── service/              # AccountFacade, AccountService + 구현체
├── config/
│   └── SecurityConfig.java
├── controller/
│   └── AccountController.java
├── deletedaccount/           # 탈퇴 계정 보관 도메인
│   ├── entity/               # DeletedAccount
│   ├── repository/
│   └── service/
└── handler/
    └── CustomExceptionHandler.java
```

## 실행 방법

### 개발 환경 (H2 In-Memory DB)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

- 서버 포트: `8081`
- H2 콘솔: `http://localhost:8081/h2-console`
  - JDBC URL: `jdbc:h2:mem:testdb`
  - Username: `sa`
  - Password: (빈 값)

### 운영 환경 (MySQL)

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

`application-prod.yaml`에 MySQL 접속 정보를 설정해야 합니다.

### 테스트

```bash
./mvnw test
```

## API 명세

전체 API 명세는 [API_SPEC.md](./API_SPEC.md)를 참고하세요.

### 엔드포인트 요약

| Method | URL | 설명 |
|--------|-----|------|
| GET | `/account-api/v1/accounts/login` | 로그인용 계정 조회 |
| GET | `/account-api/v1/accounts/{id}` | ID로 계정 단건 조회 |
| GET | `/account-api/v1/accounts?userId=` | userId로 계정 조회 |
| POST | `/account-api/v1/accounts` | 계정 목록 조회 (복수 ID) |
| POST | `/account-api/v1/accounts/register` | 계정 등록 |
| PUT | `/account-api/v1/accounts/{id}` | 계정 수정 |
| DELETE | `/account-api/v1/accounts/{id}` | 계정 삭제 |

## 도메인 모델

### Account

| 필드 | 타입 | 제약 | 설명 |
|------|------|------|------|
| id | Long | PK, Auto | 계정 고유 번호 |
| userId | String | Unique, max 30 | 로그인 ID |
| userPassword | String | max 100 | 비밀번호 |
| userEmail | String | max 50, 이메일 형식 | 이메일 |
| userName | String | max 50 | 이름 |
| status | UserStatus | ACTIVE / DORMANT | 계정 상태 |
| createdAt | LocalDateTime | | 생성일시 |

계정 삭제 시 `deleted_accounts` 테이블에 이력이 보관됩니다.

## 에러 처리

모든 비즈니스 예외는 `400 Bad Request`로 응답합니다.

| 예외 | 발생 조건 |
|------|----------|
| AccountExistException | 이미 존재하는 userId로 등록 시도 |
| AccountNotExistException | 존재하지 않는 계정 조회/수정/삭제 |
| AccountInvalidInputException | 필수 입력값 누락 또는 형식 오류 |