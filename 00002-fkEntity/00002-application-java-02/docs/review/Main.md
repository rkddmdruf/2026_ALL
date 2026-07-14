# 리뷰 — Main.java

overall: BLOCK

화면 거의 전부를 GridLayout/BorderLayout 중첩으로 풀어 BoxUI 미활용 + 죽은 코드, 전역 예외 핸들러 복붙 blocker 동반.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 262-280 | 전역 예외 처리(setDefaultUncaughtExceptionHandler + Toolkit EventQueue.push + private static handle())가 전용 클래스로 추출 안 되고 Main.main()에 인라인. 동일 블록이 Login/Serch/MyPage/JobLocation/JobSelect/JobInfor main()에도 복제(총 7곳). 개선: GlobalExceptionHandler.install()(SafeEventQueue + Thread.setDefaultUncaughtExceptionHandler)을 별도 클래스로 추출, 진입점 Main.main 최상단에서 1회만 호출, 나머지 화면 중복 main()/handle() 제거 — 대표 수정처 Main.java. |
| ui-design | blocker | 56,91,101,105,115,189,210,224 (Grid/BorderLayout), 64-71·107-113·176·199-201·216-245 (setFont/setForeground) | BoxUI import돼 있으나 화면 거의 전부 new BorderLayout/new GridLayout 중첩. setNorthPanel은 GridLayout north(91-92)를 만들고 쓰지도 않은 채 BoxUI 버전을 다시 만들어 add — 죽은 코드. card/jobCard/menuCards가 GridLayout+BorderLayout로 복잡도 과다. box(ROW/COL)+fill로 재작성 권고. |
| stream-usage | suggest | 125-127 | PK 단건 조회를 findFirst 풀스캔으로 함: storeEntity.findFirst(s -> s.s_no.equals(j.s_no)).get(), category/location 동일. s_no/c_no/l_no는 PK고 findById(int)가 캐시 Map O(1)로 존재. findById(j.s_no).get()로 통일(Serch/JobSelect는 이미 findById 사용, Main만 혼용). |
| entity-cohesion | suggest | 117-124 | 인기순 정렬 시 공고별 지원자 수를 applicationEntity.findBy(a -> a.j_no == j.j_no).size()로 비교자 안에서 매번 재계산. jobEntity.applicantCount()로 응집하면 재사용 가능. a.j_no == j.j_no는 Integer == 비교라 캐시 범위 밖 값에서 false 위험 — entity로 끌어올리며 equals로 교정. |
