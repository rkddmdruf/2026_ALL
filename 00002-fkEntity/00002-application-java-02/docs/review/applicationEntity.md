# 리뷰 — applicationEntity.java

overall: CONCERNS

a_state 도메인 메서드 전무 — 상태 해석이 MyPage로 누수돼 잘못된 필드 참조 blocker 유발.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| entity-cohesion | suggest | 14-102 | a_state(지원 상태: 대기/불합격/합격)에 대한 도메인 메서드 전무. statusLabel()/isPending()/isAccepted() 등이 없어 상태 해석이 MyPage 호출부로 누수되고 결과적으로 MyPage blocker(잘못된 필드 참조)를 유발. 상태→라벨을 entity에 응집할 것(anemic 모델 대표 사례). |
