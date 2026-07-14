# 리뷰 — userEntity.java

overall: PASS

캐시 put 중복 라인 nit.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| state-management | nit | 62-63 | cache.put(e.u_no, e)가 동일 키로 2줄 연속 호출(복붙 실수). 기능 무해하나 캐시 단일 원천 관리 코드에 중복 라인. 한 줄 제거. |
