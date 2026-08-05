# claudeCode.md — 내 코딩 스타일 가이드

> 이 문서는 `2026전국` 밑의 모든 프로젝트(`Projects/2_6`, `2_7`, `2_9`, `3_3`, `3_7`, `3_8`,
> `00001/00002-application-java-02` 등)의 `src` 코드를 읽고 정리한 **내 스타일 규칙**이다.
> Claude 에게 "이거 만들어줘" 라고 할 때 이 문서를 참고해서 **내가 쓰던 방식 그대로** 짜게 하는 게 목적.
> 기준 프로젝트는 가장 최신인 `Projects/3_8` (스마트폰 판매/요금제 앱).

---

## 0. 한 줄 요약

**순수 Java Swing + 직접 만든 미니 ORM + 직접 만든 레이아웃/프로퍼티 DSL.**
외부 라이브러리(Gson, Lombok, JPA, JavaFX) 절대 안 씀. JDK 기본 API만 사용.
빌드툴(Maven/Gradle) 없이 이클립스 프로젝트(`.classpath`, `.project`)로 굴린다.

---

## 1. 프로젝트 구조

```
<프로젝트폴더>/
├── src/
│   ├── main/     ← 화면(Frame) 클래스 + Util.java
│   ├── orms/     ← 엔티티, DBManager, EntityGenerator, et.txt(템플릿)
│   ├── utils/    ← CFrame, BoxPanel, Properties, CButton, sp
│   ├── demo/     ← 실험용 / 습작 코드 (JsonParser, JsonWriter, Demo1, Demo2...)
│   └── test/     ← 회차별 연습 (test1, test2, test3... 패키지로 나눔)
├── datafiles/    ← 이미지, json 등 리소스 (경로는 항상 "datafiles/..." 상대경로)
├── .classpath
└── .project
```

- 패키지 이름은 **소문자 한 단어**. `main`, `orms`, `utils`, `demo`, `test`.
  (오타난 `uitls` 도 있음 — 2_6. 새로 만들 땐 `utils` 로 통일할 것)
- 리소스 경로는 하드코딩. 예: `"datafiles/기종/" + pno + ".jfif"`, `"datafiles/logo.png"`.
- **한글 폴더명/파일명을 그대로 씀** (`datafiles/기종/`). 인코딩은 UTF-8.

---

## 2. 화면(Frame) 작성 패턴 — 가장 중요

모든 화면은 `utils.CFrame` 을 상속한다. 이게 내 코드의 뼈대다.

```java
public abstract class CFrame extends JFrame {

	public void setFrame(String s, int w, int h) {
		setTitle(s);
		setSize(w + 16, h + 39);          // 테두리 보정값 고정
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setIconImage(new ImageIcon("datafiles/logo.png").getImage());
		setResizable(false);
		setVisible(true);
		desing();    // ← 오타지만 내 코드 전체에서 이 이름으로 통일되어 있음. 고치지 말 것
		action();
	}

	public void setFramed(String s, int w, int h, Runnable r) { /* windowClosed 후처리 */ }
	public void setFrameg(String s, int w, int h, Runnable r) { /* windowClosing 후처리 */ }

	protected abstract void desing();   // 화면 구성
	protected abstract void action();   // 이벤트 등록
}
```

### 화면 클래스의 고정된 뼈대

```java
public class Xxx extends CFrame {

	// 1) 컴포넌트는 전부 "필드"로 선언 + 선언과 동시에 DSL 로 스타일 지정
	JTextField id = tf("아이디", SIZE(300, 26));
	CButton login = comp(CButton::new, TEXT("로그인"), FG(Color.white), BG(sp.color), SIZE(0, 32));

	// 2) 데이터도 필드로
	List<starEntity> list = new ArrayList<>();

	// 3) 생성자: 데이터 준비 → 마지막 줄에 setFrame(...)
	public Xxx(int pno) {
		this.pno = pno;
		list.addAll(starEntity.findBy(e -> e.pno.equals(pno)));
		setFrame("리뷰", 350, 450);
	}

	@Override
	protected void desing() {           // 레이아웃만. add() 한 번으로 끝내는 걸 선호
		add(set(col(15, ...), BORDER(sp.em(15, 12, 12, 12))));
	}

	@Override
	protected void action() {           // 리스너만
		login.addActionListener(e -> { ... });
	}

	// 4) 모든 화면 클래스에 단독 실행용 main() 을 넣는다
	public static void main(String[] args) {
		sp.user = userEntity.findById(1).get();   // 필요하면 더미 로그인
		Util.start(new Xxx(1));
	}
}
```

**규칙 정리**
- `desing()` 은 배치, `action()` 은 이벤트. 절대 섞지 않는다.
- 생성자 안에서 데이터 로딩/초기 세팅 → **맨 마지막에** `setFrame()` 호출.
  (`setFrame` 안에서 `desing()`이 불리므로 순서가 중요)
- 필드에서 만든 컴포넌트를 `desing()`에서 조립만 한다.
- 화면마다 `public static void main` 이 있어서 그 화면만 단독 실행 가능.

---

## 3. Properties DSL — 컴포넌트 프로퍼티 지정

`utils/Properties.java`. 리플렉션으로 setter 를 호출하는 방식.

```java
public static <T> T set(T component, Property... properties)   // 이미 만든 컴포넌트에 적용
public static <T> T comp(Supplier<T> factory, Property... ps)  // 생성 + 적용
```

**사용 가능한 Property 팩토리 (대문자 이름)**

| 이름 | setter |
|---|---|
| `TEXT(String)` | setText |
| `BG(Color)` | setBackground |
| `FG(Color)` | setForeground |
| `BORDER(Border)` | setBorder |
| `FONT(Font)` | setFont |
| `ICON(Icon)` | setIcon |
| `SIZE(w, h)` | setPreferredSize |
| `NAME(String)` | setName |
| `HOA(int)` | setHorizontalAlignment |
| `VEA(int)` | setVerticalAlignment |

**단축 생성자 (소문자 이름)**

```java
lb("텍스트", FG(sp.color), FONT(...))     // JLabel
bt("등록", SIZE(120, 26), BG(sp.color))   // JButton
tf("아이디", SIZE(300, 26))               // JTextField (첫 인자는 setName!)
cb("A,B,C", ...)                          // JComboBox<String> (콤마 split)
comp(JComboBox<String>::new, NAME("용량")) // 그 외 전부
```

- **`NAME()` 을 라벨 텍스트 대용으로 쓴다.** `tf("제품명")` 하고 나중에
  `field(t.getName())` 로 "제품명" 라벨을 붙이거나, 검증 에러 메시지에
  `t.getName() + "을(를) 입력해주세요."` 로 재사용한다. 이게 내 특징적인 패턴.
- import 는 항상 `import static utils.Properties.*;`

---

## 4. BoxPanel DSL — 레이아웃

`utils/BoxPanel.java`. BoxLayout 래퍼. **내 레이아웃은 99% 이걸로 짠다.**

```java
col(간격, 컴포넌트...)              // 세로 (Y_AXIS)
row(간격, 컴포넌트...)              // 가로 (X_AXIS)
col(앞여백, 간격, 뒤여백, 컴포넌트...)
row(앞여백, 간격, 뒤여백, 컴포넌트...)
colF / rowF                        // 안의 모든 컴포넌트에 f() 적용해서 묶기

f(c)    // 가로세로 다 늘어남 (MAX, MAX)
fw(c)   // 가로만 늘어남 (full width)
fh(c)   // 세로만 늘어남 (full height)

hg()    / hg(n)   // 가로 glue / strut
vg()    / vg(n)   // 세로 glue / strut
L("텍스트", 최소폭)  // 폭 고정 라벨 (폼 라벨 정렬용)

.setBackColor(Color.white)   // 체이닝용 배경색 (BoxPanel 전용, JPanel 리턴 아님)
.addz(c)                     // 나중에 하나 더 추가 (간격도 같이 넣어줌)
```

**전형적인 조립 모양**

```java
add(set(col(15,
		lb("My Page", FONT(sp.font.deriveFont(24f).deriveFont(1))),
		field("이름", name),
		field("아이디", id),
		f(row(0, hg(18), f(new JScrollPane(table)), hg(18)).setBackColor(Color.white)),
		fw(row(0, hg(), edit, hg(), write, hg()).setBackColor(Color.white))
	).setBackColor(Color.white), BORDER(sp.em(15, 12, 12, 12))));
```

- 배경색은 `.setBackColor(Color.white)` 를 **매 패널마다 반복해서** 붙인다.
- 여백은 바깥에서 `set(..., BORDER(sp.em(t,l,b,r)))` 로 준다.
- 반복되는 "라벨 + 입력칸" 은 화면 안에 `private JPanel field(...)` 헬퍼를 만들어 쓴다.
  거의 모든 화면에 `field()` 라는 private 메서드가 있다.
- 표/그리드는 `new JPanel(new GridLayout(0, 4, 10, 10))` 처럼 GridLayout 을 섞어 쓴다.

---

## 5. sp — 전역 상수/유틸 (static holder)

`utils/sp.java`. 짧게 `sp.xxx` 로 쓰려고 클래스명을 두 글자로 지었다.

```java
public class sp {
	public static userEntity user;                     // 로그인 사용자 (전역 세션)
	public static Integer pno;                         // 화면 간 전달용 임시값
	public static Color color = new Color(0, 120, 0);  // 메인 테마색 (프로젝트마다 다름)
	public static Font font = new Font("맑은 고딕", 0, 12);
	public static DecimalFormat df = new DecimalFormat("###,###");
	public static Border line = BorderFactory.createLineBorder(Color.LIGHT_GRAY);

	public static Border line(Color c)                 // 선 테두리
	public static Border em(int t,int l,int b,int r)   // 빈 테두리
	public static Border com(Border out, Border in)    // 합성 테두리
	public static void infor(String s)                 // 정보 다이얼로그
	public static void err(String s)                   // 경고 다이얼로그
	public static ImageIcon getImage(String s,int w,int h)  // 스케일된 아이콘
}
```

- **폰트는 항상 `sp.font.deriveFont(크기f)` 로 파생.** 굵게는 `.deriveFont(1)` (Font.BOLD 안 쓰고 숫자 1).
- 금액은 항상 `sp.df.format(price) + "원"`.
- 프로젝트마다 색 상수만 갈아끼움 (`sp.red`, `sp.blue`, `sp.orange`, `sp.color`).

---

## 6. ORM — orms 패키지

### 6-1. DBManager

```java
private static final String url = "jdbc:mysql://localhost/smartdb?serverTimezone=Asia/Seoul";
private static final String id = "root";
private static final String pw = "1234";
```

- static 블록에서 커넥션 한 번 열고 계속 재사용 (커넥션 풀 없음).
- `execute(sql, Object...val)` → `PreparedStatement` (RETURN_GENERATED_KEYS 로 생성)
- `select(sql, val)` → `List<List<String>>` (메타데이터로 컬럼 수 세서 전부 문자열로)

### 6-2. 엔티티 (자동 생성)

**클래스명 규칙: `소문자테이블명 + Entity`** → `userEntity`, `productEntity`, `starEntity`, `ordersEntity`.
(자바 관례를 일부러 안 따름. `EntityGenerator` 가 테이블명을 그대로 붙이기 때문)

각 엔티티가 갖는 고정 멤버:

```java
public Integer uno;  public String name;  ...        // 전부 public 필드, getter/setter 없음
public static final Map<Integer, xxxEntity> cache = new HashMap<>();
static { reload(); }                                  // 클래스 로딩 시 전체 캐시

public static void reload()                           // SELECT * → cache
public static Optional<xxxEntity> findById(int id)
public static List<xxxEntity>     findAll()
public static List<xxxEntity>     findBy(Predicate<xxxEntity>)
public static Optional<xxxEntity> findFirst(Predicate<xxxEntity>)
public void save()                                    // id==null → insert, 아니면 update
public void delete()
```

**사용 방식 (스트림 위주)**

```java
productEntity.findAll().stream().sorted((a,b) -> a.pno - b.pno)
categoryEntity.findById(p.cno).map(c -> c.cname).orElse("")
starEntity.findBy(s -> s.pno.equals(product.pno))
userEntity.findFirst(u -> u.id.equals(i) && u.pw.equals(p))

// 통계는 groupingBy + counting
ordersEntity.findAll().stream()
	.collect(Collectors.groupingBy(n -> productEntity.findById(n.pno).get().pname, Collectors.counting()))
	.entrySet().stream().sorted((a,b) -> Long.compare(b.getValue(), a.getValue()))
	.collect(Collectors.toList());
```

### 6-3. EntityGenerator + et.txt

`information_schema.COLUMNS` 를 읽어서 `src/orms/et.txt` 템플릿의
`${class_name}`, `${fields}`, `${rs_fields}`, `${id}`, `${column_names}`,
`${values}`, `${update}`, `${sql_insert}`, `${sql_update}` 를 치환 → `xxxEntity.java` 파일로 저장.

DB 타입 → Java 타입 매핑도 여기서 문자열 `contains` 로 판정한다
(`int→Integer`, `var/text→String`, `deci/double→Double`, `datetime→LocalDateTime`, `date→LocalDate`).

**새 프로젝트를 시작하면 DB 만들고 `EntityGenerator.main()` 부터 돌린다.**

### 6-4. JSON 엔티티 (DB 밖 데이터)

`ProjectEntity` 처럼 DB 대신 `datafiles/xxx.json` 을 쓰는 경우:
- 읽기: **Nashorn 스크립트 엔진**으로 `Java.asJSONCompatible(JSON.parse(jsonText))`
- 쓰기: 직접 만든 `JsonWriter` (리플렉션으로 public 필드 → Map → Nashorn `JSON.stringify`)
- 인터페이스(`findById/findAll/findBy/findFirst/save/delete/cache`)는 DB 엔티티와 **똑같이 맞춘다.**
- 중첩 데이터는 `public static class Capacity { ... }` 같은 static 내부 클래스로.

---

## 7. 예외 처리 / 검증

**검증 실패는 `throw new RuntimeException("메시지")` 로 던진다.** try-catch 로 안 잡음.

```java
if(pname.getText().isBlank()) {
	throw new RuntimeException("제품명을 입력해주세요.");
}
```

전역에서 `main/Util.java` 가 받아서 다이얼로그로 띄운다.

```java
public class Util {
	public static void start(JFrame f) {
		SwingUtilities.invokeLater(() -> f.setVisible(true));
		Thread.setDefaultUncaughtExceptionHandler((t, e) -> handle(e));
		Toolkit.getDefaultToolkit().getSystemEventQueue().push(new EventQueue() {
			@Override protected void dispatchEvent(AWTEvent event) {
				try { super.dispatchEvent(event); } catch (Exception e2) { handle(e2); }
			}
		});
	}
	private static void handle(Throwable t) { t.printStackTrace(); sp.infor(t.getMessage()); }
}
```

- DB/IO 예외는 `catch(Exception e) { e.printStackTrace(); }` 로 삼킨다.
- 메시지는 항상 한국어 존댓말: `"~을(를) 입력해주세요."`, `"~되었습니다."`, `"~을 확인해주세요."`

---

## 8. 화면 전환

```java
new Infor(pno);   // 새 화면 띄우고
dispose();        // 현재 화면 닫기
```

돌아오기가 필요하면 `setFramed(제목, w, h, Runnable)` 로 닫힐 때 콜백:

```java
// 로그인 창이 닫히면 admin 여부에 따라 다음 화면
setFramed("로그인", 485, 230, () -> {
	if(admin) new Admin();
	else new Main();
});

// "직접 넘어간 경우"는 move 플래그로 콜백을 무력화
boolean move = false;
setFramed("마이페이지", 550, 400, () -> { if(!move) new Main(); });
```

화면 간 값 전달은 **생성자 인자** 또는 **`sp` 의 static 필드**(`sp.user`, `sp.pno`).

---

## 9. 커스텀 그리기 패턴

익명 클래스로 `paintComponent` 오버라이드하는 걸 즐겨 쓴다.

```java
JLabel img = new JLabel() {
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(new ImageIcon("datafiles/기종/" + p.pno + ".jfif").getImage(),
				5, 5, getWidth() - 10, getHeight() - 10, null);
	}
};
```

**고정 관용구**
```java
Graphics2D g2 = (Graphics2D) g;
g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

// 문자열 가운데 정렬 — 이 공식을 계속 씀
FontMetrics fm = g2.getFontMetrics();
g2.drawString(s, (getWidth() - fm.stringWidth(s)) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
```

- 파이차트: `Arc2D.Double(sx, sy, r*2, r*2, start, angle, Arc2D.PIE)` + 각도 누적
- 별점: `☆☆☆☆☆` 를 회색으로 깔고 `clipRect` 로 잘라 `★★★★★` 를 노란색으로 덧그림 (`StarView`)
- 둥근 버튼: `CButton` — `setContentAreaFilled(false)` + `fillRoundRect`, 반경은 `public int r = 20;`
  (직각으로 쓰고 싶으면 `login.r = 0;`)
- 토스트 알림: `JWindow` + `setShape(new RoundRectangle2D.Double(...))` + `Timer` 로 자동 종료 (`windowAlarm`)
- 슬라이드 애니메이션: `javax.swing.Timer(5, e -> { nx -= n2*2; repaint(); })`

---

## 10. JTable 세팅 (관리자/마이페이지에서 반복)

```java
String[] columns = {"번호", "제품이미지", "제품명", ...};
int[] widths = {135, 127, 118, ...};

DefaultTableModel model = new DefaultTableModel(columns, 0) {
	@Override public boolean isCellEditable(int r, int c) { return false; }
	@Override public Class<?> getColumnClass(int c) { return c == 1 ? ImageIcon.class : Object.class; }
};
JTable table = new JTable(model);

table.setRowHeight(60);
table.setShowGrid(true);
table.setGridColor(Color.LIGHT_GRAY);
table.getTableHeader().setReorderingAllowed(false);
table.getTableHeader().setResizingAllowed(false);
table.getTableHeader().setDefaultRenderer((t, v, s, f, r, c) -> {   // 람다 렌더러
	JLabel l = lb(String.valueOf(v), HOA(SwingConstants.CENTER), FG(Color.white), BG(sp.color),
			FONT(sp.font.deriveFont(1)), BORDER(sp.line(Color.white)));
	l.setOpaque(true);
	return l;
});
```

- 데이터 갱신은 항상 `private void reloadTable()` — `model.setRowCount(0)` 후 다시 addRow.
- 이미지 컬럼은 `ImageIcon.class` 로 지정하면 자동 렌더링.
- 선택 → 폼 채우기는 `table.getSelectionModel().addListSelectionListener` +
  `if(e.getValueIsAdjusting() || table.getSelectedRow() < 0) return;`

---

## 11. 반복되는 헬퍼 이름 (내가 항상 쓰는 이름)

| 이름 | 하는 일 |
|---|---|
| `desing()` | 화면 구성 (오타지만 통일됨) |
| `action()` | 이벤트 등록 |
| `field(...)` | "라벨 + 입력칸" 한 줄 만들기 |
| `card(...)` | 목록의 카드 한 장 만들기 |
| `reloadTable()` / `reloadMainPanel()` / `setMainPanel()` | 목록 다시 그리기 |
| `select(entity)` | 표에서 고른 걸 폼에 채우기 / null 이면 초기화 |
| `Util.start(frame)` | 전역 예외 핸들러 붙여서 실행 |

목록 갱신 끝에는 항상:
```java
revalidate();
repaint();
SwingUtilities.invokeLater(() -> sc.getVerticalScrollBar().setValue(0));
```

---

## 12. 코드 서식 습관

- **탭 인덴트.**
- `if`, `for` 뒤 공백 없음: `if(x)`, `for(int i = 0; ...)`, `while(rs.next())`
- 한 줄짜리 블록은 중괄호 생략: `if(sp.user == null) return;`
- 여러 값은 문자열 + split 으로 만든다: `"종류,용량,통신사".split(",")`, `List.of("...".split(","))`
- 타입은 `var` 를 자주 씀 (`var e = new userEntity();`, `try(var stmt = ...)`)
- import 는 와일드카드 섞어 씀: `import javax.swing.*;`, `import orms.*;`, `import utils.*;`
- 화면 클래스 상단 3줄은 고정:
  ```java
  import utils.*;
  import static utils.BoxPanel.*;
  import static utils.Properties.*;
  ```
- 주석은 한국어 한 줄. 왜 그렇게 했는지를 적는 편:
  ```java
  // 반드시 이미지가 부모 패널에 추가된 다음 호출
  // 기존에 여기에 id가 추가가 되있어서 id value가 두개였다.
  // 12,24 / 12,36 ... 처럼 조합 가능한 모든 경우를 순서대로 만든다
  ```
- 화면 영역 구분은 대문자 주석: `//TOP`, `//CENTER`, `//BOTTOM`
- 임시 확인은 `System.out.println(...)` 을 그대로 남겨둠.

---

## 13. Claude 에게 부탁할 때 지켜야 할 것 (체크리스트)

새 화면/기능을 만들어 달라고 하면 아래를 그대로 따를 것.

- [ ] `CFrame` 상속 + `desing()` / `action()` 두 메서드로 분리 (`design` 아님, **`desing`**)
- [ ] 컴포넌트는 필드에 선언하면서 `lb/bt/tf/comp + SIZE/BG/FG/FONT/BORDER` 로 스타일 지정
- [ ] 레이아웃은 `col/row/f/fw/fh/hg/vg` 만 사용, `.setBackColor(Color.white)` 붙이기
- [ ] 색/폰트/포맷/테두리/다이얼로그는 전부 `sp.*` 경유
- [ ] 엔티티는 `xxxEntity` 이름 + `findAll/findBy/findById/findFirst/save/delete` 만 사용
- [ ] 검증 실패는 `throw new RuntimeException("~해주세요.")`
- [ ] 화면 전환은 `new Xxx(); dispose();`, 되돌아옴은 `setFramed(..., Runnable)`
- [ ] 클래스 끝에 단독 실행용 `main()` 넣기
- [ ] 외부 라이브러리 추가 금지 (JDK + JDBC 만)
- [ ] 주석은 한국어로, "왜"를 짧게

---
---

# ⚠️ 내가 부족한 점 (보완할 것)

읽으면서 눈에 띈 약점들. 심각한 순서대로.

### 1. DB 커넥션·트랜잭션 개념이 없다
`DBManager` 가 static 커넥션 하나를 영원히 들고 있다. 끊기면 앱 전체가 죽고,
`insert`/`update` 여러 개가 묶여야 하는 작업(예: `Admin` 의 `p.save()` → `pe.save()` → 이미지 복사)에
트랜잭션이 없어서 중간에 실패하면 데이터가 깨진다. commit/rollback, try-with-resources 로
커넥션 얻는 방식을 익힐 것.

### 2. 예외를 삼키는 습관
`catch(Exception e) { e.printStackTrace(); }` 가 거의 모든 DB/IO 코드에 있다.
실패했는데 화면은 성공한 척한다. `reload()` 가 실패하면 캐시가 빈 채로 진행돼서
`findById(...).get()` 에서 엉뚱한 `NoSuchElementException` 이 난다.

### 3. `Optional.get()` 남용
`productEntity.findById(pno).get()`, `.getAsDouble()` 을 확인 없이 바로 부른다.
`Review` 에서 리뷰가 0개면 `average().getAsDouble()` 에서 바로 터진다.
`orElse` / `orElseThrow(메시지)` / `ifPresent` 를 쓰는 습관 필요.
(`Admin` 에서는 `orElse` 를 잘 쓰고 있음 — 최근 코드가 더 낫다. 이걸 전체로 퍼뜨릴 것)

### 4. 전역 가변 상태 의존
`sp.user`, `sp.pno` 같은 static 필드로 화면 간 데이터를 넘긴다.
누가 언제 바꿨는지 추적이 안 되고, 화면이 늘어날수록 꼬인다.
생성자 인자로 넘기는 쪽을 기본으로 하고 static 은 로그인 세션 정도로만 제한할 것.

### 5. 전체 테이블 캐싱 방식의 한계
엔티티마다 `static { reload(); }` 로 `SELECT *` 전체를 메모리에 올린다.
데이터가 커지면 못 버티고, 여러 화면에서 수정하면 캐시 동기화가 안 맞는다.
`save()` 후 관련 엔티티 `reload()` 를 빼먹는 실수도 이미 있다.

### 6. 죽은 코드 / 중복 import 방치
- `Pay.java`, `windowAlarm.java` 는 import 가 통째로 두 번 들어있고, 안 쓰는 게 대부분이다.
- `StarView` 안에 아무것도 안 하는 `private static void test()` 와 안 쓰는 `reviewImage` 필드가 남아있다.
- `Main.java` 의 `checked`, `tList` 는 만들어만 놓고 필터링에 실제로 안 쓰인다.
- `Admin` 의 차트 버튼은 `System.out.println("new Chart();")` 로 남아 있다 — 미구현.
- `Infor.java` 안의 `class test extends CFrame`, `Pay.java` 안의 `class form extends CFrame`
  → 메서드 안에 화면 클래스를 통째로 정의. 재사용/디버깅이 어렵다. 파일로 뺄 것.

### 7. 하드코딩된 값
- `Main.card()` 의 `"평균 가격 : 1,222,222원"` — 실제 계산이 아니라 고정 문자열.
- `Infor` 생성자의 `"datafiles/기종/1.jfif"` — `project.pno` 여야 하는데 1 고정.
- DB 비밀번호 `"1234"`, 관리자 계정 `admin/1234` 가 소스에 그대로.
- 프레임 크기 보정값 `+16`, `+39` 는 OS/테마 바뀌면 깨진다.
- 용량 `"256,512,1024,128"`, 통신사 `"LGU+,KT,SKT"` 를 화면에 직접 박아둠.
  그런데 같은 값이 `Admin` 에서는 `"SKT,KT,LG U+"` (`LG U+` vs `LGU+`) 로 **표기가 다르다.**
  → `carPrice.get(c.type)` 에서 키가 안 맞아 NPE 날 수 있는 실제 버그 유발 지점.

### 8. 네이밍 일관성
- 클래스명이 소문자로 시작하는 게 섞여 있다: `sp`, `getter`, `windowAlarm`, `scdule`, `haveStock`,
  `mapUtil`, `test`, `form`, `field`. 자바 관례상 대문자여야 한다.
- 오타가 고착됐다: `desing`(design), `storege`(storage), `uitls`(utils), `scdule`(schedule),
  `expasionList`(expansion), `Sing_up`(Sign up), `teksltjl.java`(→ windowAlarm 으로 rename 함).
- 의미 없는 이름: `n`, `n2`, `nx`, `p1/p2/p3`, `find1/find2/find3`, `m1/m2/m3`, `cs1/cs2/cs3`.
  나중에 내가 봐도 뭔지 모른다. `find1` → `findCapacity` 처럼 이름만 바꿔도 크게 좋아진다.

### 9. 화면과 로직이 붙어 있다
가격 계산(`setPriceLabel`), 조합 생성(`subsets`/`comb`), 통계 집계가 전부 화면 클래스 안에 있다.
같은 계산을 `Infor` 와 `Pay` 가 각각 다시 짜고 있고 (`price/moment + ratep.price`),
그래서 값이 서로 달라질 위험이 있다. 계산은 `Util` 이나 엔티티 쪽으로 뺄 것.

### 10. 매직 넘버 / 레이아웃 픽셀 노가다
`SIZE(135, 127, 118, 125...)`, `hg(18)`, `sp.em(15, 12, 12, 12)` 처럼
숫자를 하나씩 맞춰가며 붙였다. 창 크기가 바뀌면 전부 깨진다.
`setResizable(false)` 로 막아둔 것도 사실상 이 문제를 회피한 것.

### 11. 스레드/EDT 개념이 약하다
`Util.start()` 에서 `invokeLater` 를 쓰지만, 정작 `setFrame()` 안에서
`setVisible(true)` 를 먼저 부르고 그 다음에 `desing()` 을 호출한다 (순서가 거꾸로).
그래서 `Infor` 에서 컴포넌트 크기를 얻으려고 `Timer(1, ...)` 로 1ms 딜레이를 주는
편법을 쓰고 있다. `setVisible` 은 구성 완료 후 마지막에 부르는 게 맞다.

### 12. 테스트/버전관리 습관
- `src/test/test1~test5` 는 테스트가 아니라 연습용 복사본이다. 자동 검증이 전혀 없다.
- 커밋 메시지가 전부 `-_-` 다. 나중에 히스토리를 되짚을 수 없다.
- 같은 프로젝트를 폴더째 복사해서 버전 관리하고 있다 (`00002-application-java-02` /
  `00002-fkEntity/00002-application-java-02` 가 완전 동일). 브랜치를 쓰는 게 맞다.

### 우선순위 3가지만 고른다면
1. **`Optional.get()` 과 예외 삼키기 없애기** — 지금 당장 앱이 죽는 원인.
2. **이름 바로잡기** (`desing`, `storege`, `n/n2/nx`, 소문자 클래스명) — 남이 봐도, 내가 봐도 읽히게.
3. **계산 로직을 화면 밖으로 빼기** — 같은 공식이 두 군데서 따로 노는 걸 막기.
