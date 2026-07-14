# 리뷰 — MyPage.java

overall: BLOCK

335줄 비대 + BoxUI 전무, 합/불 라벨을 a_state 아닌 job.j_state로 오용, 전역 예외 핸들러 복붙 blocker 동반.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 317-332 | Main.java와 글자 단위 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
| ui-design | blocker | 61-67,106-128,168-314 레이아웃, 78-102 init(), 131-166 action() | 335줄 비대 + BoxUI 전혀 미사용(전부 Border/GridLayout 중첩). BasePanel 골격 붕괴: 생성자에서 init()(데이터로드)을 design 이전 호출, design()이 컴포넌트 생성+레이아웃을 BorderLayout으로 처리. action()의 수정/저장 토글이 reset() 같은 재구성 함수 없이 tfs 순회하며 border/cursor/focusable 직접 비우고 setSize로 폭 가산(x/y 매직) — 디자인 함수 추출 미적용, 컴포넌트 직접 찾아 비우는 안티패턴. |
| entity-cohesion | blocker | 271-298 | application/favorite 목록 화면인데 합/불 라벨을 application.a_state 아닌 job.j_state(채용공고 상태)로 판정(298: job.j_state == 0 ? "대기" : (job.j_state == 1 ? "불합격" : "합격")). a_state는 reload/save에만 존재하고 라벨링에 안 쓰임 — 지원 상태 라벨이 공고 상태 필드에 잘못 붙은 의미 오용. applicationEntity.statusLabel()을 entity에 두고 호출부는 그것만 호출. |
| ui-design | suggest | 265-314 | getCustomScrollPane(app, fa) 한 메서드에 app/fa 중 null로 분기(instanceof + applicationEntity/favoriteEntity)해 동일 카드 빌더를 분기로 풂. 빌더는 jobEntity 카드 1개로 추출하고 호출측에서 리스트만 넘기는 형태 권고. |
| stream-usage | suggest | 247-314 | getCustomScrollPane(List<applicationEntity> app, List<favoriteEntity> fa)가 한쪽 null로 받고 (app==null?fa:app) 삼항 + e instanceof 재분기로 j_no 추출. 공통은 j_no→jobEntity lookup이므로 j_no 추출 함수를 파라미터로 받거나 List<jobEntity>로 미리 map해 넘기면 instanceof 분기 제거 + Stream 일관. |
| state-management | nit | 140 | 편집/보기 모드를 button.getText().contains("수정") 문자열 파싱으로 판단. enum 또는 boolean editing 1개 + label()로 두고 버튼 라벨은 파생. 현재는 라벨 문구 변경이 곧 상태 로직 깨짐. |
| ui-design | q | 119·277 (instanceof, RuntimeException 미throw) | MyPage:277 new RuntimeException(...)는 throw 없이 생성만 해 무의미한 죽은 방어코드 — 제거 또는 분기 자체 제거 검토. (동시성/락 무관) |
