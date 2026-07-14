# 리뷰 — JobLocation.java

overall: BLOCK

전역 예외 핸들러 복붙 blocker + 수동 while top5·List 재할당·색 매직넘버·throw 통일.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | blocker | 236-251 | Main.java와 동일한 자작 전역 핸들러(handle + EventQueue.push + uncaught) 복제. 단일 GlobalExceptionHandler.install() 추출해 진입점 1곳만 호출, 나머지 화면 main()/handle() 중복 제거 — 대표 수정처 Main.java. |
| stream-usage | suggest | 218-232 | 급여순 top5 가게 추출을 while(checkList.size()!=5) + checkList.contains() + 수동 test 인덱스 증가로 구현. distinct/map/limit 있는데 for/while로 직접 구현. jobEntity.findAll().stream().sorted(salary desc).map(j->j.s_no).distinct().limit(5)...로 대체. 현재 while은 jobs.get(test+1) 접근이라 마지막 원소 근처 IndexOutOfBounds 위험. |
| ui-design | suggest | 53 new Color(255,125,0), 68 new GridLayout(5,1) popup | myLocation 버튼 색 raw hex(규칙5). popup은 GridLayout(5,1) — 5칸 라벨이면 box(COL) inner gap으로 대체 가능. 단 map paintComponent의 Graphics2D 직접 드로잉(setMap)은 차트성 예외로 정당. |
| state-management | suggest | 57,211,231 | List 필드 stores를 setStoreList()에서 storeEntity.findBy() 결과로 재할당(211), 정렬 분기에서 또 재할당(231). List 필드 = new 재할당 금지. setMap(paint)가 stores 참조를 읽으므로 통째 교체 대신 final 유지 + clear()/addAll()로 같은 참조 갱신. |
| state-management | nit | 59,99,194 | boolean clickButton 플래그를 paint 메서드 setMap()(194)에서 false로 리셋 — 렌더가 상태를 mutate. 내 위치 표시 1회성 상태는 렌더 밖(action 핸들러)에서 소비/해제하는 게 흐름 명확. |
| exception-edt | suggest | 94-104 | myLocation(사용자 액션) 핸들러의 로그인 검증이 getter.ms('로그인이 되어있지 않습니다.')+early return. 사용자 액션 검증은 throw 통일 대상. InputUtil.requireNotNull(getter.user, ...)로 throw해 전역 핸들러가 메시지 담당. (cityName/order/categoryName 리스너 revalPaint 분기는 입력 검증 아니라 throw 강제 대상 아님) |
