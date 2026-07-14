# 리뷰 — Connections.java

overall: PASS

DB 레이어 로컬 폴백 — 다이얼로그 메시지 품질 nit.

| 축 | severity | 라인 | 지적 · 개선 |
|---|---|---|---|
| exception-edt | nit | 27-41 | select()의 catch(Exception)+JOptionPane.showMessageDialog(e)는 DB 레이어 로컬 폴백으로 빈 리스트 반환 — 의도적 비통일 범주(매 틱/렌더러 폴백)에 가까워 throw 통일 강제 대상 아님. 다만 e 객체 toString을 그대로 다이얼로그에 노출하는 건 메시지 품질 nit. 동작은 baseline상 문제 없음. |
