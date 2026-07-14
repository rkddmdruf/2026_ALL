# 리뷰 — BoxPanel.java

overall: BLOCK

레이아웃 시스템이 BoxUI/BoxPanel 2벌로 완전 복붙 — 통일 필요.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| ui-design | blocker | BoxUI.java 11-67, BoxPanel.java 13-83 | 동일 레이아웃 API(L/VGAP/HGAP/fill/fillWidth/fillHeight/box+ROW/COL)가 정적메서드판 BoxUI와 클래스판 BoxPanel 두 벌로 완전 복붙(같은 디자인 로직 중복 작성 금지 위반). 화면마다 임의 선택(Main/JobLocation=BoxUI, Serch/JobInfor/JobSelect/Login=BoxPanel)해 일관성도 깨짐. 한 시스템으로 통일하고 다른 하나 제거 권고. |
