# C# 트러블슈팅 정리 (2026-08)

Festival Manager / 전국대회 1과제 작업 중 막혔던 항목 모음.
형식: **증상 → 원인 → 해결**. 코드는 대회장에서 그대로 쳐도 되도록 최소 길이로 정리.

---

## 1. Vendor 저장 시 CHECK 제약 위반 (`CK_Vendor_Status`)

### 증상
```
INSERT 문이 CHECK 제약 조건 "CK_Vendor_Status"과(와) 충돌했습니다.
데이터베이스 "...", 테이블 "dbo.Vendor", 열 'Status'에서 충돌이 발생했습니다.
```

### 원인
`Status` 컬럼에 허용된 값 목록이 DB 쪽에 박혀 있는데, 폼에서 넘긴 값이 그 목록에 없음.
자주 나오는 실제 원인 3가지:

1. ComboBox에서 아무것도 선택 안 한 상태 → `SelectedValue`가 `null` 또는 `""`로 들어감
2. DisplayMember(한글 라벨)를 저장해버림 — DB는 코드값(`'A'`, `'ACTIVE'` 등)을 기대
3. 앞뒤 공백 / 대소문자 불일치 (`"Active"` vs `"ACTIVE"`)

### 해결

먼저 제약 조건 정의부터 확인 — 추측하지 말 것:
```sql
SELECT name, definition
FROM sys.check_constraints
WHERE parent_object_id = OBJECT_ID('Vendor');
```

정의에 나온 값만 넣도록 수정:
```csharp
// 값이 비었으면 기본값으로 채우고 저장
string status = cboStatus.SelectedValue as string;
if (string.IsNullOrWhiteSpace(status)) status = "ACTIVE";   // 제약 목록의 값 중 하나
vendor.Status = status.Trim().ToUpper();
```

ComboBox를 코드값 기준으로 채우는 방식:
```csharp
cboStatus.DisplayMember = "Text";
cboStatus.ValueMember   = "Value";
cboStatus.DataSource = new[] {
    new { Text = "계약중", Value = "ACTIVE" },
    new { Text = "대기",   Value = "PENDING" },
    new { Text = "종료",   Value = "CLOSED" },
};
```

> 참고: `SelectedValue`는 `DataSource` 할당 직후 `null`인 타이밍이 있음.
> 저장 버튼에서 읽을 때는 문제없지만 `Load`에서 바로 읽으면 터짐.

---

## 2. TextBox 테두리 없애고 밑줄만 (+ 포커스 시 파란색)

### 증상
`BorderStyle = None`으로 하면 선이 아예 사라지고, `FixedSingle`로 하면 사각 테두리가 다 생김.
`TextBox`는 밑줄만 그리는 옵션이 없음.

### 원인
`TextBox`의 테두리는 네이티브 컨트롤이 그림 → `Paint` 이벤트로 못 건드림.

### 해결 (가장 짧은 방법: Panel + 1~2px 라인)
```csharp
// 디자이너: Panel(pnlId) 안에 TextBox(txtId) 넣고 txtId.BorderStyle = None
Panel line = new Panel {
    Height = 2, Dock = DockStyle.Bottom, BackColor = Color.LightGray
};
pnlId.Controls.Add(line);

txtId.Enter += (s, e) => line.BackColor = Color.DodgerBlue;
txtId.Leave += (s, e) => line.BackColor = Color.LightGray;
```

여러 개면 헬퍼 하나로 처리 (타자량 절약):
```csharp
void Underline(Panel p, TextBox t) {
    t.BorderStyle = BorderStyle.None;
    var l = new Panel { Height = 2, Dock = DockStyle.Bottom, BackColor = Color.LightGray };
    p.Controls.Add(l);
    t.Enter += (s, e) => l.BackColor = Color.DodgerBlue;
    t.Leave += (s, e) => l.BackColor = Color.LightGray;
}
```

### 부수 문제: 세로 정렬
`TextBox`는 `Height`가 폰트에 묶여서 마음대로 안 늘어남.
→ Panel 높이를 키우고 `txt.Top`을 직접 계산하거나 `Multiline = true`로 두고 높이 지정.

---

## 3. `FormBorderStyle.None` 후 창이 안 움직임

### 증상
타이틀바를 없앴더니 폼을 드래그로 이동할 수 없음.

### 원인
드래그 이동은 타이틀바(비클라이언트 영역)가 처리하던 동작.

### 해결 (WinAPI 방식 — 가장 짧고 잔상 없음)
```csharp
using System.Runtime.InteropServices;

[DllImport("user32.dll")] static extern bool ReleaseCapture();
[DllImport("user32.dll")] static extern int SendMessage(IntPtr h, int m, int w, int l);

private void Drag(object sender, MouseEventArgs e) {
    if (e.Button != MouseButtons.Left) return;
    ReleaseCapture();
    SendMessage(this.Handle, 0xA1, 0x2, 0);   // WM_NCLBUTTONDOWN, HTCAPTION
}
```
헤더 패널과 그 안의 Label 양쪽에 `MouseDown += Drag` 연결.
(패널만 걸면 라벨 위에서 드래그가 안 먹음)

### 같이 나오는 문제
- **최대화하면 작업표시줄을 덮음** → `MaximizedBounds = Screen.FromHandle(Handle).WorkingArea;`
- **그림자가 없어 밋밋함** → `Padding`으로 1px 테두리 패널을 두는 게 제일 빠름

---

## 4. MSChart에 EF 데이터가 안 올라감

### 증상
차트가 비어있거나 `DataBind()`에서 예외. X축에 숫자만 나오고 라벨이 안 붙음.

### 원인
1. `IQueryable`을 그대로 `DataSource`에 물림 → 지연 실행 때문에 바인딩 시점에 비어있음
2. `XValueMember` / `YValueMembers` 문자열이 실제 속성명과 불일치 (익명 타입이면 오타 감지 안 됨)
3. `Series`를 안 지우고 다시 그려서 기본 `Series1`이 남아있음

### 해결 (바인딩 대신 `AddXY` — 실수가 제일 적음)
```csharp
chart1.Series.Clear();
var s = chart1.Series.Add("매출");
s.ChartType = SeriesChartType.Column;
s.IsValueShownAsLabel = true;

var data = db.Sale
    .GroupBy(x => x.Booth.Name)
    .Select(g => new { Name = g.Key, Total = g.Sum(x => x.Amount) })
    .ToList();                       // ← 반드시 ToList() 먼저

foreach (var d in data) s.Points.AddXY(d.Name, d.Total);
```

바인딩 방식으로 갈 때:
```csharp
chart1.Series[0].Points.DataBind(data, "Name", "Total", "");
```

### X축 라벨이 잘리거나 건너뛸 때
```csharp
chart1.ChartAreas[0].AxisX.Interval = 1;
chart1.ChartAreas[0].AxisX.LabelStyle.Angle = -45;
```

---

## 5. LEFT JOIN 개수 세기 — 0건이 사라짐

### 증상
"부스별 판매 건수"를 뽑았는데 판매가 없는 부스가 목록에서 통째로 빠짐.

### 원인
`Join`은 INNER JOIN. 매칭 없는 쪽이 제거됨.

### 해결 — `GroupJoin`
```csharp
var q = db.Booth
    .GroupJoin(db.Sale,
        b => b.BoothID,
        s => s.BoothID,
        (b, sales) => new { b.Name, Cnt = sales.Count(), Total = sales.Sum(x => (int?)x.Amount) ?? 0 })
    .ToList();
```

> `Sum`은 빈 시퀀스에서 `null` 반환 → `(int?)`로 캐스팅 후 `?? 0` 필수.
> 안 하면 "캐스트가 유효하지 않습니다" 예외.

쿼리 구문으로 쓰면:
```csharp
var q = from b in db.Booth
        join s in db.Sale on b.BoothID equals s.BoothID into g
        select new { b.Name, Cnt = g.Count() };
```

---

## 6. ComboBox에 "전체" 항목 추가하면 터짐

### 증상
필터용 ComboBox 맨 앞에 "전체"를 넣으려는데 `Insert`가 안 되거나,
`DataSource` 넣는 순간 `SelectedIndexChanged`가 발동해서 `NullReferenceException`.

### 원인
1. `DataSource`가 걸린 컬렉션은 `Items.Insert()` 불가 → **DataSource 자체에 넣어야 함**
2. `DataSource` 할당 시점에 `SelectedIndexChanged`가 자동 발동 (다른 컨트롤이 아직 null)

### 해결
```csharp
// 클래스 하나 만들어두면 재사용 편함
class Item { public int Id { get; set; } public string Name { get; set; } }

var list = db.BoothType
    .Select(x => new Item { Id = x.TypeID, Name = x.TypeName })
    .ToList();
list.Insert(0, new Item { Id = 0, Name = "전체" });

cbo.SelectedIndexChanged -= cbo_SelectedIndexChanged;   // 잠깐 끊기
cbo.DisplayMember = "Name";
cbo.ValueMember   = "Id";
cbo.DataSource    = list;
cbo.SelectedIndex = 0;
cbo.SelectedIndexChanged += cbo_SelectedIndexChanged;   // 다시 연결
```

조회 쪽:
```csharp
int id = (int)cbo.SelectedValue;
var q = db.Booth.Where(b => id == 0 || b.TypeID == id).ToList();
```

> 익명 타입도 `Insert` 가능하지만 **속성 이름·타입·순서가 완전히 같아야** 함.
> 대회장에서는 그냥 클래스 하나 만드는 쪽이 안전.

---

## 대회 전 체크리스트

- [ ] CHECK 제약 있는 테이블 미리 `sys.check_constraints`로 전부 확인해두기
- [ ] `Underline()` / `Drag()` 헬퍼는 `BaseForm`에 넣어두고 상속으로 재사용
- [ ] EF 쿼리는 화면에 뿌리기 전 **항상 `.ToList()`**
- [ ] 집계 컬럼은 `(int?)` 캐스팅 + `?? 0`
- [ ] ComboBox 바인딩은 `DisplayMember` → `ValueMember` → `DataSource` 순서 고정

---

*이 문서는 이번 달 작업에서 걸렸던 항목들을 모아 재구성한 것입니다. 실제 증상 메시지나 해결 방식이 달랐던 부분은 알려주시면 수정하겠습니다.*
