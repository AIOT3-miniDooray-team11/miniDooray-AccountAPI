# miniDooray-AccountAPI 명세서

- **Base URL**: `http://localhost:8081`
- **Base Path**: `/account-api/v1/accounts`
- **Content-Type**: `application/json`

---

## 공통 응답 코드

| HTTP Status | 설명 |
|-------------|------|
| 200 OK | 조회 성공 |
| 201 Created | 리소스 생성 성공 |
| 204 No Content | 수정/삭제 성공 (응답 본문 없음) |
| 400 Bad Request | 입력값 오류 또는 형식 불일치 |
| 404 Not Found | 존재하지 않는 계정 |
| 409 Conflict | 이미 존재하는 계정 |

---

## 1. 로그인용 계정 조회

인증 서비스(Auth API)에서 로그인 처리 시 사용합니다. 비밀번호 해시값을 포함한 응답을 반환합니다.

```
GET /account-api/v1/accounts/login?userId={userId}
```

### Query Parameters

| 파라미터 | 필수 | 타입 | 설명 |
|----------|------|------|------|
| userId | O | String | 로그인 ID |

### 응답 (200 OK)

```json
{
  "accountId": 1,
  "userId": "john_doe",
  "userPassword": "$2a$10$...",
  "status": "ACTIVE"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| accountId | Long | 계정 고유 번호 |
| userId | String | 로그인 ID |
| userPassword | String | 암호화된 비밀번호 |
| status | String | 계정 상태 (`ACTIVE` / `DORMANT`) |

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 userId | 404 |

---

## 2. ID로 계정 단건 조회

```
GET /account-api/v1/accounts/{id}
```

### Path Parameters

| 파라미터 | 필수 | 타입 | 설명 |
|----------|------|------|------|
| id | O | Long | 계정 고유 번호 |

### 응답 (200 OK)

```json
{
  "id": 1,
  "userId": "john_doe",
  "email": "john@example.com",
  "Name": "John Doe",
  "status": "ACTIVE",
  "createAt": "2026-05-22T10:00:00"
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| id | Long | 계정 고유 번호 |
| userId | String | 로그인 ID |
| email | String | 이메일 |
| Name | String | 이름 |
| status | String | 계정 상태 (`ACTIVE` / `DORMANT`) |
| createAt | LocalDateTime | 생성일시 |

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 id | 404 |

---

## 3. userId로 계정 조회

```
GET /account-api/v1/accounts?userId={userId}
```

### Query Parameters

| 파라미터 | 필수 | 타입 | 설명 |
|----------|------|------|------|
| userId | O | String | 로그인 ID |

### 응답 (200 OK)

[2번 단건 조회 응답과 동일한 구조](#응답-200-ok-1)

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 userId | 404 |

---

## 4. 계정 목록 조회 (복수 ID)

여러 계정 ID를 한 번에 조회합니다. 태스크/프로젝트 서비스에서 멤버 정보를 일괄 조회할 때 사용합니다.

```
POST /account-api/v1/accounts
```

### Request Body

```json
{
  "accountIdList": [1, 2, 3]
}
```

| 필드 | 필수 | 타입 | 설명 |
|------|------|------|------|
| accountIdList | O | List\<Long\> | 조회할 계정 ID 목록 |

### 응답 (200 OK)

```json
{
  "accountRespList": [
    {
      "id": 1,
      "userId": "john_doe",
      "email": "john@example.com",
      "Name": "John Doe",
      "status": "ACTIVE",
      "createAt": "2026-05-22T10:00:00"
    },
    {
      "id": 2,
      "userId": "jane_doe",
      "email": "jane@example.com",
      "Name": "Jane Doe",
      "status": "ACTIVE",
      "createAt": "2026-05-20T09:00:00"
    }
  ]
}
```

| 필드 | 타입 | 설명 |
|------|------|------|
| accountRespList | List | 계정 응답 목록 |

---

## 5. 계정 등록

```
POST /account-api/v1/accounts/register
```

### Request Body

```json
{
  "userId": "john_doe",
  "userPassword": "password123!",
  "userEmail": "john@example.com",
  "userName": "John Doe"
}
```

| 필드 | 필수 | 타입 | 제약 | 설명 |
|------|------|------|------|------|
| userId | O | String | max 30, 공백 불가 | 로그인 ID |
| userPassword | O | String | max 100, 공백 불가 | 비밀번호 |
| userEmail | O | String | max 50, 이메일 형식 | 이메일 |
| userName | O | String | max 50, 공백 불가 | 이름 |

### 응답 (201 Created)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 이미 존재하는 userId | 409 |
| 필수 입력값 누락 또는 공백 | 400 |
| 이메일 형식 불일치 | 400 |

---

## 6. 계정 수정

```
PUT /account-api/v1/accounts/{id}
```

### Path Parameters

| 파라미터 | 필수 | 타입 | 설명 |
|----------|------|------|------|
| id | O | Long | 계정 고유 번호 |

### Request Body

```json
{
  "userId": "john_doe_new",
  "userPassword": "newpassword123!",
  "userEmail": "john_new@example.com",
  "userName": "John New"
}
```

| 필드 | 필수 | 타입 | 제약 | 설명 |
|------|------|------|------|------|
| userId | O | String | max 30, 공백 불가 | 변경할 로그인 ID |
| userPassword | O | String | max 100, 공백 불가 | 변경할 비밀번호 |
| userEmail | O | String | max 50, 이메일 형식 | 변경할 이메일 |
| userName | O | String | max 50, 공백 불가 | 변경할 이름 |

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 id | 404 |
| 필수 입력값 누락 또는 공백 | 400 |

---

## 7. 계정 삭제

계정을 삭제하며, 삭제된 계정 정보는 `deleted_accounts` 테이블에 이력으로 보관됩니다.

```
DELETE /account-api/v1/accounts/{id}
```

### Path Parameters

| 파라미터 | 필수 | 타입 | 설명 |
|----------|------|------|------|
| id | O | Long | 계정 고유 번호 |

### 응답 (204 No Content)

응답 본문 없음

### 에러

| 상황 | Status |
|------|--------|
| 존재하지 않는 id | 404 |

---

## 데이터 타입 참고

### UserStatus

| 값 | 설명 |
|----|------|
| `ACTIVE` | 활성 계정 |
| `DORMANT` | 휴면 계정 |