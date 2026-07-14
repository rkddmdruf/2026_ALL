# 리뷰 — Login.java

overall: BLOCK

전역 예외 핸들러 복붙 blocker + 색 매직넘버·throw 통일 미준수.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 136-154 | Main.java와 글자 단위 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. handle()이 throwable.getMessage()만 표시해 메시지 null인 NPE는 빈 다이얼로그. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
| ui-design | suggest | 43 setBackground(new Color(255,125,0)), 33-45 | 색 매직넘버 new Color(255,125,0). getter.color(255,60,20) 명명 상수가 있는데 별개 raw hex 직접 씀(색 매직넘버 금지). 명명 상수로 통일. JobLocation.java:53도 동일. |
| exception-edt | suggest | 99-122 | 로그인(사용자 액션) 핸들러가 throw 통일 미준수. 빈칸 검증은 getter.ms(...)+early return(100-103), 로그인 실패는 try-catch로 orElseThrow를 같은 핸들러에서 즉시 catch해 getter.ms로 직접 출력(104-120) → 예외가 전역 핸들러까지 안 올라감. InputUtil.require로 throw(또는 orElseThrow 메시지를 전역에 위임)하고 핸들러 내 try-catch/getter.ms 제거해 happy path 선형화. |
