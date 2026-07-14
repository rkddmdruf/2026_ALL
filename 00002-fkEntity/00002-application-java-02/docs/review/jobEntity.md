# 리뷰 — jobEntity.java

overall: CONCERNS

anemic 모델 — 연관 접근·상태 라벨·파생 계산 도메인 메서드 부재로 6화면 복붙 유발.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| entity-cohesion | suggest | 14-129 | job→store→location 연관 조회가 6화면 복붙: JobInfor:76-77, Serch:146/176-177, Main:125-127, JobSelect:90/104, MyPage:279/288, JobLocation:134. 연관 접근 메서드 부재. jobEntity에 store()=storeEntity.findById(s_no).get(), storeEntity에 location()=.../category()=...를 두면 6곳 체인이 job.store().location().l_name으로 단축되고 JobSelect:90/104의 storeEntity.findById(s_no).get() 중복 호출도 제거. |
| entity-cohesion | nit | 27-32 | toString()이 필드 선언(j_regdate, 34라인) 앞 27라인에 끼어 있고 j_regdate 참조 — 컴파일은 되나 가독성 저해. equals/hashCode는 전 entity에 없으나 캐시가 Map<Integer,Entity> id-key lookup이라 동작 무해. Integer == 비교(Main:118, JobInfor:80 등)는 entity로 로직 이동 시 equals로 교정 권고. |
