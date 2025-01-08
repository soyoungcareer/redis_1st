## [본 과정] 이커머스 핵심 프로세스 구현
[단기 스킬업 Redis 교육 과정](https://hh-skillup.oopy.io/) 을 통해 상품 조회 및 주문 과정을 구현하며 현업에서 발생하는 문제를 Redis의 핵심 기술을 통해 해결합니다.
> Indexing, Caching을 통한 성능 개선 / 단계별 락 구현을 통한 동시성 이슈 해결 (낙관적/비관적 락, 분산락 등)

---

## 개발환경
>- Spring Boot 3.4.1
>- Java 23
>- Gradle
>- JPA
>- MySQL
>- Docker Compose
  

## [1주차] 아키텍처 설계
**Clean Code 작성 요구 사항**
>- 가독성 (클래스, 변수, 메서드 이름)
>- 일관된 컨벤션 (불필요한 줄바꿈은 없는지 등) <br> [코딩컨벤션 참고](https://naver.github.io/hackday-conventions-java/#_%EA%B3%B5%EB%B0%B1_whitespace)
>- 스트림, 람다, Optional 을 적절히 사용
>- 중복 코드는 없는지
>- 역할 및 책임 분리가 잘 되어 있는지
  
**기술 요구 사항**
>- PK는 편의상 Auto Increment 를 사용하나, 현업에서는 UUID를 사용하여 PK를 사용하는 추세임.(TimeStamp를 결합하여 순서도 함께 관리)
  
