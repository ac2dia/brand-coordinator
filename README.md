# Brand Coordinator

## 구현 범위에 대한 설명
### 도메인 해석
- 상품(Product) 와 브랜드(Brand) 두 가지 애그리거트로 분류하였습니다.

### DB 구성 및 쿼리
- DB 구성을 위해 H2 를 사용하였고 애플리케이션 실행 시마다 데이터 초기화 작업을 진행하였습니다.
- 구현 1, 2, 3 의 경우 JPA N+1 문제를 해결하기 위해 JPQL 을 사용하였습니다. 

### 테스트 구현
- Controller layer 를 대상으로 Integration test 를 수행하였습니다.
- Service, Repository layer 를 대상으로 Unit test 를 수행하였습니다.

### 고민하였지만 미해결된 부분
- 해당 문구 > 브랜드의 카테고리에는 1개의 상품은 존재하고
  - 4번 구현에서 브랜드 추가 시 자동/수동으로 현존하는 상품을 추가해야하나 고민하였는데 브랜드에 해당하는 데이터만 추가하기로 하였습니다.
  - 그렇다면 위와 같은 상태에서 브랜드에 모든 카테고리 상품이 등록되지 않은 상태라면 조회 시 대상 범위에 포함시키지 않아야 하나 고민하였는데 우선 진행하였습니다. 

## 코드 빌드, 테스트, 실행 방법
### Build
```
./gradlew build
```

### Test
```
./gradlew clean test -Dspring.profiles.active=test
```

### Run
```
./gradlew bootRun --args='--spring.profiles.active=local'
```

## 기타 추가 정보

### Postman
- brand-coordinator.postman_collection.json 파일을 이용하여 Postman 테스트를 수행할 수 있습니다.

### Open Api Spec
```
http://localhost:8080/swagger-ui/index.html
```
- 해당 URL 을 통해 Swagger 페이지에 접근 가능합니다.

### Health Probe
```
http://localhost:8080/startupz
http://localhost:8080/readyz
http://localhost:8080/livez
```
- 해당 URL 들을 통해 상태에 맞는 헬스 체크를 수행할 수 있습니다.
