# 리뷰 — JobInfor.java

overall: BLOCK

전역 예외 핸들러 복붙 blocker + 상태 라벨 호출부 누수·즐겨찾기 상태 중복 보관.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 221-236 | Main.java와 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
| entity-cohesion | suggest | 172 | j_state→라벨 매핑이 호출부에 흩어짐: l.getText().contains("1") ? "상시모집" : "마감". 같은 j_state를 MyPage:298은 0/1/2로 다르게 해석. j_state.toString()을 거쳐 contains("1")로 판정해 j_state가 11·21이면 오판. jobEntity.recruitLabel()을 두고 정수 비교로 판정해 호출부 복붙·문자열 파싱 오판 제거. |
| state-management | suggest | 68-69,80-87,124-133 | 즐겨찾기 여부라는 같은 상태가 favorite 필드(null 여부)와 heart 버튼 텍스트('♥'/'♡') 두 곳에 중복 보관. setHeart()가 favorite 아니라 heart.getText().equals("♥")로 분기(124) → UI 텍스트를 상태 원천 사용(SSOT 위반). favorite!=null 단일 원천으로 판단, 하트 텍스트는 파생 표시로만. 지원 상태(application 필드 vs apply.setEnabled)도 동일 패턴. |
| exception-edt | suggest | 110-114, 123-134 | apply 버튼 핸들러가 applicationEntity.save()를 EDT에서 동기 호출, setHeart()도 favorite.save()/delete() 동기. SwingWorker 전무(전 src 0건). DB IO 버튼은 SwingWorker로 doInBackground 분리 후 done()에서 UI 갱신 권장. 락/트랜잭션 추가 불필요. |
| ui-design | q | 79-87 (favorite/application != null) | JobInfor의 favorite/application null 체크는 표시 상태(하트/지원버튼) 결정용이라 정당해 보임. (동시성/락 무관) |
| stream-usage | nit | 80 | favorite = favorite.findFirst(...) — null 인스턴스 필드 통해 static findFirst 호출. 동작은 되나 favoriteEntity.findFirst(...)가 올바른 형태. 84의 applicationEntity.findFirst는 클래스명으로 올바르게 호출 — 형식 통일 권장. |
