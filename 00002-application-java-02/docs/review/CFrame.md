# 리뷰 — CFrame.java

overall: PASS

reset() 골격 부재 + 리사이즈 디버그 로그 nit.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| ui-design | nit | 27-32, 30 println | BasePanel 골격에 reset()(removeAll+design+action+revalidate)이 없어 데이터 변경 후 통째 재구성 경로 부재 → 각 화면이 직접 setViewportView/직접 비우기로 처리(Serch:180, MyPage action). componentResized에서 좌표 println 디버그 로그가 매 리사이즈마다 출력 — 제거 권고. |
