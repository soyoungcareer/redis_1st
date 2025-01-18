## [본 과정] 이커머스 핵심 프로세스 구현
[단기 스킬업 Redis 교육 과정](https://hh-skillup.oopy.io/) 을 통해 상품 조회 및 주문 과정을 구현하며 현업에서 발생하는 문제를 Redis의 핵심 기술을 통해 해결합니다.
> Indexing, Caching을 통한 성능 개선 / 단계별 락 구현을 통한 동시성 이슈 해결 (낙관적/비관적 락, 분산락 등)

---

## 개발환경
>- Spring Boot 3.4.1
>- Java 21
>- Gradle
>- JPA
>- MySQL
>- Docker Compose
>- IntelliJ Http Request

**Clean Code 작성 요구 사항**
>- 가독성 (클래스, 변수, 메서드 이름)
>- 일관된 컨벤션 (불필요한 줄바꿈은 없는지 등) <br> [코딩컨벤션 참고](https://naver.github.io/hackday-conventions-java/#_%EA%B3%B5%EB%B0%B1_whitespace)
>- 스트림, 람다, Optional 을 적절히 사용
>- 중복 코드는 없는지
>- 역할 및 책임 분리가 잘 되어 있는지
  
**참고 사항**
>- 해당 프로젝트에서 PK는 편의상 Auto Increment 를 사용하나, 현업에서는 UUID를 사용하여 PK를 사용하는 추세임.(TimeStamp를 결합하여 순서도 함께 관리)

---

## [1주차] 아키텍처 설계

### 테이블 구조
![erd image](docs/images/corrected_erd.png)
테이블 : 영화, 상영관, 상영시간표, 좌석, 회원, 예매
>- 영화 썸네일 이미지는 URL로만 사용하므로 편의상 파일 테이블을 따로 생성하지 않음.
>- 공통코드는 공통코드 테이블을 생성하지 않고 ENUM으로 관리하며, DB컬럼명을 `_cd` 어미를 붙여 구분하였음 (영상물 등급, 장르, 좌석유형)

---

### 아키텍처(Hexagonal Architecture)
- `cinema-core`
  <br> => *도메인, 엔티티, 비즈니스 로직*
  * 비즈니스 로직의 중심으로, 외부 변화에 의존하지 않도록 설계.
  * 영화, 상영 시간표 등의 도메인 모델과 관련 규칙 정의.
- `cinema-application`
  <br> => *유스케이스(Use Case)*
  * 도메인 로직을 호출하고 조합하여 시스템의 흐름을 관리.
  * 입력(사용자 요청)과 출력(결과 반환)을 조정.
- `cinema-adapter`
  <br> => *(Inbound) REST Controller, CLI*
  * 사용자나 외부 세계의 요청을 애플리케이션에 전달.

  => *(Outbound) JPA Repository, External API*
  * 애플리케이션의 요청을 외부 시스템(DB, API 등)으로 전달.
- `cinema-infra`
  <br> => *DB 연결, 외부 라이브러리 설정*
  * 기술적인 환경과 세부 설정을 처리.
- `cinema-common`
  <br> => *공통으로 사용할 수 있는 유틸리티, 상수, 예외 처리 클래스*
  * 도메인 및 유스케이스와 독립적인 범용 코드를 관리.
  * 비즈니스 로직과 관련 없는 코드만 포함하여 비대화 방지.
> 외부 변화에 대한 의존성을 최소화하기 위하여 Domain 관련한 부분은 `cinema-core` 모듈로 생성하였음.
> `cinema-application`은 전체 시스템의 흐름과 연결을 관리하며 도메인 로직을 호출하고 조합하여 유스케이스(사용 시나리오)를 처리함.
> `cinema-adapter`은 외부 세계와 연결하는 모듈로서 역할을 하며, 입력(Inbound)과 출력(Outbound)으로 나뉨.
> 또한, Port를 구현하여 `cinema-application`이 도메인과 외부 시스템을 매끄럽게 조작할 수 있도록 도움.
> `cinema-infra`는 기술적인 환경을 세팅하는 모듈로 데이터베이스 연결 설정, 외부 라이브러리 설정 등을 함.
> `cinema-common` 모듈은 공통으로 사용할 수 있는 유틸을 묶어두어 범용성 있게 구성하였음. 
> 단, 모듈이 비대해지고 강결합이 발생하지 않도록 변화가 적으며 비즈니스 로직과 관련 없는 코드만 사용하도록 명확히 하여야 함.

---

### 해결할 문제 1 - 해결 완료
**(문제)** 애플리케이션 실행 시 다음과 같은 오류 발생하여 정상적으로 실행되지 않음.
```dockerfile
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource could be configured.

Reason: Failed to determine a suitable driver class


Action:

Consider the following:
        If you want an embedded database (H2, HSQL or Derby), please put it on the classpath.
        If you have database settings to be loaded from a particular profile you may need to activate it (no profiles are currently active).
```
-> compose.yaml, application.yml 파일을 수정해보았으나 해결하지 못하였음. `Docker compose` 실행은 정상적으로 되는 상태이나 app url 연결에 문제가 발생하는 것으로 보임.

**(해결 완료)** Docker 포트 변경하여 해결함. 기존 3306 포트 3307로 변경하여 docker compose up 하여 띄운 후 CinemaApplication 실행하니 정상적으로 실행됨.

---

### [1주차] 피드백 후 수정사항
<span style="color: #2D3748; background-color: #fff5b1"><strong>기존테이블</strong></span>
![erd image](docs/images/erd.png)

<span style="color: #2D3748; background-color: #fff5b1"><strong>피드백 후 수정테이블</strong></span>
![corrected_erd image](docs/images/corrected_erd.png)
1. ✅ 테이블생성옵션 : `utf8mb4_0900_ai_ci`

   ⇒ 테이블생성옵션으로 추가 완료
2. ✅ INT → UNSIGNED INT 변경

   > 음수를 사용하지 않고 더 많은 데이터 활용 가능
   >

   ⇒ 기존 INT → `INT UNSIGNED`로 변경 완료

3. ✅ 러닝타임이 분단위인지 명확하게 알 수 있도록 컬럼명 수정

   ⇒ 기존 run_time → `runtime_min` 으로 변경 완료

4. ✅ ENUM

   > 장르도 굳이 테이블로 만들지 않고 ENUM으로 바꾸면 좋지 않을까?
   Converter를 사용해서 데이터베이스에 'ACTION'이면 -> 'A' 이렇게 저장해서 데이터베이스 리소스를 절약하는 방법 참고
   >

   ⇒ 장르 테이블 삭제 후 ENUM으로 관리될 항목은 DB컬럼명을 `_cd` 로 어미를 작성하였고, 해당 컬럼의 comment에 [ENUM]이라고 명시하였음.
5. ✅ `LocalDateTime` 네이밍 ~At으로 적용

    ⇒ 기존 reg_dttm, chg_dttm 같이 datetime의 축약어로 dttm으로 사용하던 부분을 좀 더 직관적으로 이해할 수 있도록 created_at, updated_at 으로 변경하였음. 작성자, 수정자도 비슷하게 created_by, updated_by 로 수정 완료

6. ✅ `/api/v1` 패턴, URL 자원을 명사로 표기하는 방으로 API 설계

   > 예약 가능 여부는 query parameter로 구분: /api/movies?bookable=true
   >
   >
   > 이런 식으로 표기하는 것이 좀 더 좋지 않을까? 싶습니다. 이후 확장성 측면에서도 유리할 것 같아 보입니다.

7. ✅ `RequiredArgsConstructor`와 같은 적절한 어노테이션 사용으로 보일러 플레이트 코드 제거
8. ✅ 실무에서는 FK 참조 무결성으로 인한 성능, 스키마 변경, 데이터 이관 및 데이터 클렌징에 대한 편의성 때문에 FK 를 걸지 않는 경우가 많음.
9. ✅ `@CreatedDate`, `@LastModifiedDate` 어노테이션 사용 시 Spring Data JPA Auditing 기능을 활성화하면 자동으로 저장, 수정 시간 설정함.

---

### 해결할 문제 2
**(문제)** 엔티티와 테이블 간 자동 매핑이 되지 않음. 
`application.yml` 파일에 아래와 같이 설정을 하였으나, 스네이크케이스와 카멜케이스 간 자동 변환이 되지 않아 발생하는 문제로 보임.
임시로 `@Table`, `@Column(name="")`을 사용하여 매핑되게 설정하였음. 나중에 여유가 된다면 해결해볼 예정.
```dockerfile
spring:  
  jpa:
    hibernate:
      naming:
        physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
        implicit-strategy: org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl
```

### 해결할 문제 3
**(문제)** Querydsl 의존성 추가 시 Q클래스 생성되지 않는 문제 발생하여
IntelliJ 의 `Build Complier > Annotation Processors` 설정 변경하니,
![erd image](docs/images/problem3-2.jpg)

`build/generated/sources/annotationProcessor/java/main` 폴더는 정상적으로 생성되었음.
해당 폴더가 generated sources root로 marked된 것까지 확인되었으나 Q클래스가 생성되지 않음.

![erd image](docs/images/problem3-1.jpg)

build.gradle 파일을 수정하여 다양한 방법으로 시도하였으나 이틀 동안 해결하지 못하여 일단 Querydsl을 사용하지 않고 진행하기로 함. 나중에 해결해볼 것.

멀티 모듈을 사용함에 따른 모듈 간 의존성 설정 문제일수도 있을 것 같음.


---

# [2주차] 성능 테스트

### 검색 조건 추가
- 제목, 장르 필터

### 인덱스 추가
- 영화 : (조인) 영화 ID / (정렬) 개봉일 / (검색) 제목+장르
- 상영시간표 : (조인) 상영시간표 ID, 영화 ID, 상영관 ID / (정렬) 시작시각
- 상영관 : (조인) 상영시간표 ID
- 좌석 : (조인) 좌석 ID
- 예매 : (조인) 예매 ID, 좌석 ID, 회원 ID, 상영시간표 ID
- 회원 : (조인) 회원 ID



