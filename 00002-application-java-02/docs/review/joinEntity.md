# 리뷰 — joinEntity.java

overall: CONCERNS

직접 SQL JOIN 실행 인프라 dead code.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| stream-usage | suggest | joinEntity.java:14-42, join.java 전체 | 직접 SQL JOIN 실행 인프라(joinEntity가 DBManager.execute(sql).executeQuery()로 JOIN 결과를 Map row 적재)인데 main/utils 어디서도 사용 안 됨 — 사용처 0건(dead code). 컨벤션은 JOIN을 메모리 lookup(2중 findBy/findById)으로 권장하고 실제 화면(JobSelect.jobPanel, Main.setCenterPanel)이 이미 메모리 lookup으로 구현됨. 미사용 SQL JOIN 인프라(orms/joins 패키지) 삭제 권장. |
