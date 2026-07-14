# 리뷰 — EntityGenerator.java

overall: CONCERNS

자동생성 템플릿이 anemic 구조의 구조적 원인 — 생성 후 도메인 메서드 수동 보강 필요.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| entity-cohesion | q | 65-101 | 전 entity가 이 생성기 템플릿(datafiles/entity-template.txt)으로 자동생성돼 필드+CRUD(reload/findBy/save/delete)만 갖고 도메인 메서드 0개인 구조적 원인. 자동생성은 골격용으로 적절하나, 생성 후 entity에 도메인 메서드(연관 접근·상태 라벨·파생 계산) 수동 추가 안 하면 비즈로직이 전부 UI로 누수됨. 생성물을 베이스로 두고 도메인 메서드를 덧붙이는 방향 권고. |
