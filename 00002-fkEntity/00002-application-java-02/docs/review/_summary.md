# 코드리뷰 요약 — 00002-application-java-02

overall: BLOCK

## 실격 게이트 (CLAUDE.md 대회 baseline)
| 조건 | 결과 |
|---|---|
| 문자열결합 SQL / createStatement | PASS — DBManager.execute PreparedStatement만 |
| 허용 외 import | PASS — java/javax/com.mysql + 자체 패키지(orms/utils)만 |
| 비밀번호 | 대회 기준 암호화 안 함 → 게이트 대상 아님 |

## 축별 verdict
| 축 | verdict | 핵심 |
|---|---|---|
| ui-design (축1) | BLOCK | BoxUI/BoxPanel 2벌 중복 + 화면 대부분 GridLayout/BorderLayout 혼용 |
| stream-usage (축2) | CONCERNS | 조회 캐시 Stream 양호, dead SQL JOIN·findFirst 풀스캔·수동 while 중복제거 |
| entity-cohesion (축3) | CONCERNS | anemic entity, 연관조회 6화면 복붙, 상태→라벨 UI 누수(MyPage 의미 오용) |
| state-management (축4) | CONCERNS | 캐시 SSOT 양호, 상태 중복보관(즐겨찾기·편집모드), List 재할당 |
| exception-edt (축5) | BLOCK | 전역 예외 핸들러 7화면 복붙, DB IO SwingWorker 0건 |

## 클래스별 리뷰
- [Main.md](Main.md) — BLOCK
- [MyPage.md](MyPage.md) — BLOCK
- [BoxUI.md](BoxUI.md) — BLOCK
- [BoxPanel.md](BoxPanel.md) — BLOCK
- [Login.md](Login.md) — BLOCK
- [JobLocation.md](JobLocation.md) — BLOCK
- [JobInfor.md](JobInfor.md) — BLOCK
- [Serch.md](Serch.md) — BLOCK
- [JobSelect.md](JobSelect.md) — BLOCK
- [joinEntity.md](joinEntity.md) — CONCERNS
- [join.md](join.md) — CONCERNS
- [jobEntity.md](jobEntity.md) — CONCERNS
- [applicationEntity.md](applicationEntity.md) — CONCERNS
- [EntityGenerator.md](EntityGenerator.md) — CONCERNS
- [CFrame.md](CFrame.md) — PASS
- [test.md](test.md) — PASS
- [userEntity.md](userEntity.md) — PASS
- [Connections.md](Connections.md) — PASS

## 최우선 (blocker)
1. 전역 예외 핸들러를 GlobalExceptionHandler.install() 1곳으로 추출, 7화면 main()/handle() 복붙 제거.
2. BoxUI/BoxPanel 2벌 → 1벌 통일 + 화면을 box(ROW/COL)+fill 로 재작성(GridLayout/BorderLayout 혼용 제거).
3. MyPage 합/불 라벨을 application.a_state 기준으로(현재 job.j_state 오용) — applicationEntity.statusLabel() 응집.
