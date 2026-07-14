# 리뷰 — JobSelect.java

overall: BLOCK

전역 예외 핸들러 복붙 blocker.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 118-131 | Main.java와 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
