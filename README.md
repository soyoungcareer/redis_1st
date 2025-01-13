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
![erd image](docs/images/erd.png)
테이블 : 영화, 장르, 상영관, 상영시간표, 좌석, 회원, 예매
>- 영화 썸네일 이미지는 URL로만 사용하므로 편의상 파일 테이블을 따로 생성하지 않음.
>- 공통코드는 공통코드 테이블을 생성하지 않고 ENUM으로 관리함. (영상물 등급, 좌석유형)

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

### 해결할 문제
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

**(해결)** Docker 포트 변경하여 해결함. 기존 3306 포트 3307로 변경하여 docker compose up 하여 띄운 후 CinemaApplication 실행하니 정상적으로 실행됨.