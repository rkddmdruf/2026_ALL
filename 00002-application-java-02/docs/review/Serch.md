# 리뷰 — Serch.java

overall: BLOCK

전역 예외 핸들러 복붙 blocker + reset 골격 부재·콤보 필터 정확성 의심·죽은 주석.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 215-228 | Main.java와 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
| stream-usage | q | 144-150 | 콤보 필터 predicate 조립부: index<2(지역,업직종) 둘 다 locationEntity.findById(storeEntity.findById(e.s_no).get().l_no).get().l_no.equals(combo.getSelectedIndex()) — location/category 구분 없이 같은 식이라 업직종 콤보가 지역 l_no로 필터됨(category 필터 누락 의심). 정확성 확인 필요. category는 store.c_no로 따로 비교해야 의도에 맞음. (매 test마다 2중 lookup 반복은 데이터 작아 성능 비지적) |
| ui-design | q | 63-67, 175-180 | 필드 mainGridPanel가 design 전(64) JScrollPane 생성 시 null로 들어가고 reloadMainPanel에서 새 BoxPanel 만들어 sc.setViewportView로 교체. reset() 골격이 있었으면 재구성 1함수로 끝날 부분. 동작하나 design/필드초기화 순서 의존이 미묘 — BoxUI 통일과 함께 정리 권고. |
| ui-design | nit | 116-120 | ComboBoxPanel에 BoxUI 버전이 주석으로 남고 'BoxUI 쓰면 5개 크기가 다르다'며 GridLayout 채택. menuCards(Main:188)도 동일 주석. 5컬럼 균등폭은 box(ROW)+fill 각 셀로 균등 분배 가능 — 주석 죽은코드 제거하고 BoxUI fill로 재시도 권고. |
