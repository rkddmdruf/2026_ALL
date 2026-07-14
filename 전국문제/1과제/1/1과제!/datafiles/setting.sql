
USE master;
GO

EXEC xp_instance_regwrite
     N'HKEY_LOCAL_MACHINE',
     N'Software\Microsoft\MSSQLServer\MSSQLServer',
     N'LoginMode', REG_DWORD, 2;
GO

ALTER LOGIN sa WITH PASSWORD = N'1234', CHECK_POLICY = OFF;
ALTER LOGIN sa ENABLE;
GO

IF DB_ID('festivaldb') IS NULL CREATE DATABASE festivaldb;
GO
USE festivaldb;
GO

SET ANSI_NULLS ON;
SET QUOTED_IDENTIFIER ON;
GO

/* 자식 → 부모 순 DROP */
IF OBJECT_ID('dbo.Sale') IS NOT NULL DROP TABLE dbo.Sale;
IF OBJECT_ID('dbo.Booth') IS NOT NULL DROP TABLE dbo.Booth;
IF OBJECT_ID('dbo.EventItem') IS NOT NULL DROP TABLE dbo.EventItem;
IF OBJECT_ID('dbo.Staff') IS NOT NULL DROP TABLE dbo.Staff;
IF OBJECT_ID('dbo.Structure') IS NOT NULL DROP TABLE dbo.Structure;
IF OBJECT_ID('dbo.VendorMenu') IS NOT NULL DROP TABLE dbo.VendorMenu;
IF OBJECT_ID('dbo.Vendor') IS NOT NULL DROP TABLE dbo.Vendor;
IF OBJECT_ID('dbo.Performer') IS NOT NULL DROP TABLE dbo.Performer;
IF OBJECT_ID('dbo.TicketType') IS NOT NULL DROP TABLE dbo.TicketType;
IF OBJECT_ID('dbo.Festival') IS NOT NULL DROP TABLE dbo.Festival;
IF OBJECT_ID('dbo.BoothType') IS NOT NULL DROP TABLE dbo.BoothType;
IF OBJECT_ID('dbo.Stage') IS NOT NULL DROP TABLE dbo.Stage;
IF OBJECT_ID('dbo.AppUser') IS NOT NULL DROP TABLE dbo.AppUser;
GO

/* ---------- 마스터 / 계정 ---------- */
CREATE TABLE dbo.AppUser (
    UserId        INT           IDENTITY(1,1) PRIMARY KEY,
    LoginId       NVARCHAR(50)  NOT NULL UNIQUE,
    Password      NVARCHAR(50)  NOT NULL,           -- 대회용: 평문 저장(암호화 없음)
    DisplayName   NVARCHAR(50)  NOT NULL DEFAULT N'',
    Role          NVARCHAR(20)  NOT NULL DEFAULT N'admin',
    IsActive      BIT           NOT NULL DEFAULT 1,
    CreatedAt     DATETIME2(0)  NOT NULL DEFAULT SYSDATETIME()
);

CREATE TABLE dbo.BoothType (
    Code        VARCHAR(10)  NOT NULL PRIMARY KEY,
    Name        NVARCHAR(20) NOT NULL,
    CodePrefix  VARCHAR(4)   NOT NULL,
    DefaultW    INT          NOT NULL,
    DefaultH    INT          NOT NULL,
    ColorHex    CHAR(7)      NOT NULL
);

CREATE TABLE dbo.Stage (
    Code       VARCHAR(10)  NOT NULL PRIMARY KEY,
    Name       NVARCHAR(30) NOT NULL,
    SortOrder  INT          NOT NULL DEFAULT 0
);

/* ---------- 루트 ---------- */
CREATE TABLE dbo.Festival (
    FestivalId  INT           NOT NULL PRIMARY KEY,   -- 단일 축제 = 1
    Name        NVARCHAR(100) NOT NULL,
    StartDate   DATE          NOT NULL,
    EndDate     DATE          NOT NULL,
    Place       NVARCHAR(100) NOT NULL DEFAULT N'',
    SaleSeq     INT           NOT NULL DEFAULT 1,
    UpdatedAt   DATETIME2(0)  NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT CK_Festival_Dates CHECK (EndDate >= StartDate)
);

/* ---------- 운영 테이블 (문자열 PK = 목업 id) ---------- */
CREATE TABLE dbo.Vendor (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Name        NVARCHAR(60)  NOT NULL,
    Kind        NVARCHAR(20)  NOT NULL,
    Owner       NVARCHAR(40)  NOT NULL DEFAULT N'',
    Phone       VARCHAR(20)   NULL,
    Rent        INT           NOT NULL DEFAULT 0,
    Status      VARCHAR(10)   NOT NULL DEFAULT 'wait',
    BizNum      VARCHAR(20)   NULL,
    CONSTRAINT UQ_Vendor_Name UNIQUE (FestivalId, Name),
    CONSTRAINT CK_Vendor_Status CHECK (Status IN ('in','wait','out')),
    CONSTRAINT CK_Vendor_Rent CHECK (Rent >= 0)
);

CREATE TABLE dbo.Performer (
    Id           INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId   INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Name         NVARCHAR(60)  NOT NULL,
    Genre        NVARCHAR(30)  NOT NULL DEFAULT N'',
    Members      INT           NOT NULL DEFAULT 1,
    Fee          INT           NOT NULL DEFAULT 0,
    Phone        VARCHAR(20)   NULL,
    Email        NVARCHAR(100) NULL,
    Status       VARCHAR(12)   NOT NULL DEFAULT 'tuning',
    CONSTRAINT UQ_Performer_Name UNIQUE (FestivalId, Name),
    CONSTRAINT CK_Performer_Status CHECK (Status IN ('signed','tuning','cancelled')),
    CONSTRAINT CK_Performer_Members CHECK (Members >= 1),
    CONSTRAINT CK_Performer_Fee CHECK (Fee >= 0)
);

CREATE TABLE dbo.Structure (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Type        VARCHAR(10)   NOT NULL,
    X INT NOT NULL, Y INT NOT NULL, W INT NOT NULL, H INT NOT NULL,
    Label       NVARCHAR(40)  NOT NULL DEFAULT N'',
    CONSTRAINT CK_Structure_Type CHECK (Type IN ('wall','zone','gate','road')),
    CONSTRAINT CK_Structure_Size CHECK (W > 0 AND H > 0)
);

CREATE TABLE dbo.Booth (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Code        VARCHAR(10)   NOT NULL,
    Name        NVARCHAR(60)  NOT NULL,
    TypeCode    VARCHAR(10)   NOT NULL REFERENCES dbo.BoothType(Code),
    X INT NOT NULL, Y INT NOT NULL, W INT NOT NULL, H INT NOT NULL,
    VendorId    INT           NULL REFERENCES dbo.Vendor(Id) ON DELETE SET NULL,
    StageId     VARCHAR(10)   NULL REFERENCES dbo.Stage(Code),
    Memo        NVARCHAR(400) NULL,
    CONSTRAINT UQ_Booth_Code UNIQUE (FestivalId, Code),
    CONSTRAINT CK_Booth_Size CHECK (W > 0 AND H > 0),
    CONSTRAINT CK_Booth_StageOnlyForStage CHECK (StageId IS NULL OR TypeCode = 'stage')
);
CREATE UNIQUE INDEX UX_Booth_Vendor ON dbo.Booth(VendorId) WHERE VendorId IS NOT NULL;

CREATE TABLE dbo.EventItem (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    StageId     VARCHAR(10)   NOT NULL REFERENCES dbo.Stage(Code),
    Type        VARCHAR(10)   NOT NULL,
    Title       NVARCHAR(80)  NOT NULL,
    StartHour   DECIMAL(5,2)  NOT NULL,
    EndHour     DECIMAL(5,2)  NOT NULL,
    CONSTRAINT CK_EventItem_Type CHECK (Type IN ('music','talk','dj')),
    CONSTRAINT CK_EventItem_Range CHECK (EndHour > StartHour AND StartHour >= 0 AND EndHour <= 24)
);

CREATE TABLE dbo.Staff (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Name        NVARCHAR(40)  NOT NULL,
    Role        VARCHAR(10)   NOT NULL,
    Zone        NVARCHAR(40)  NOT NULL DEFAULT N'',
    Phone       VARCHAR(20)   NULL,
    Status      VARCHAR(12)   NOT NULL DEFAULT 'scheduled',
    StartHour   DECIMAL(5,2)  NOT NULL,
    EndHour     DECIMAL(5,2)  NOT NULL,
    CONSTRAINT CK_Staff_Role CHECK (Role IN ('stage','ticket','safety','ops','media')),
    CONSTRAINT CK_Staff_Status CHECK (Status IN ('on','scheduled','late','off')),
    CONSTRAINT CK_Staff_Range CHECK (EndHour > StartHour AND StartHour >= 0 AND EndHour <= 24)
);

CREATE TABLE dbo.TicketType (
    Id          INT           IDENTITY(1,1) PRIMARY KEY,
    FestivalId  INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    Name        NVARCHAR(40)  NOT NULL,
    Price       INT           NOT NULL,
    Capacity    INT           NOT NULL,
    Sold        INT           NOT NULL DEFAULT 0,
    CONSTRAINT UQ_TicketType_Name UNIQUE (FestivalId, Name),
    CONSTRAINT CK_TicketType_Price CHECK (Price >= 0),
    CONSTRAINT CK_TicketType_Capacity CHECK (Capacity >= 0),
    CONSTRAINT CK_TicketType_Sold CHECK (Sold >= 0 AND Sold <= Capacity)
);

CREATE TABLE dbo.Sale (
    Id            INT           IDENTITY(1,1) PRIMARY KEY,   -- 표시는 앱에서 'T-000000' 형식으로 포맷
    FestivalId    INT           NOT NULL REFERENCES dbo.Festival(FestivalId),
    SaleTime      NVARCHAR(5)   NOT NULL,                -- 'HH:MM'
    Buyer         NVARCHAR(40)  NOT NULL,
    Phone         VARCHAR(20)   NULL,
    TicketTypeId  INT           NOT NULL REFERENCES dbo.TicketType(Id),
    Qty           INT           NOT NULL,
    Pay           NVARCHAR(10)  NOT NULL,
    Memo          NVARCHAR(200) NULL,
    Status        VARCHAR(10)   NOT NULL DEFAULT 'ok',
    SoldAt        DATETIME2(0)  NOT NULL DEFAULT SYSDATETIME(),
    CONSTRAINT CK_Sale_Qty CHECK (Qty >= 1),
    CONSTRAINT CK_Sale_Pay CHECK (Pay IN (N'카드', N'현금', N'계좌이체')),
    CONSTRAINT CK_Sale_Status CHECK (Status IN ('ok','refund'))
);

/* ---------- 인덱스 ---------- */
CREATE INDEX IX_Booth_Festival_Type      ON dbo.Booth(FestivalId, TypeCode);
CREATE INDEX IX_EventItem_Festival_Stage ON dbo.EventItem(FestivalId, StageId, StartHour);
CREATE INDEX IX_Staff_Festival           ON dbo.Staff(FestivalId, StartHour);
CREATE INDEX IX_Sale_Festival_SoldAt     ON dbo.Sale(FestivalId, SoldAt DESC);
GO

EXECUTE AS LOGIN = N'sa';
PRINT N'시드 실행 로그인 = ' + SUSER_SNAME();   -- 'sa' 확인용

INSERT dbo.AppUser(LoginId, Password, DisplayName, Role)
VALUES (N'admin', N'1234', N'관리자', N'admin');

INSERT dbo.BoothType(Code, Name, CodePrefix, DefaultW, DefaultH, ColorHex) VALUES
 ('food',N'푸드','F',120,120,'#b04a16'),
 ('game',N'게임','G',120, 60,'#5b3a93'),
 ('info',N'안내','I', 60, 60,'#0c6b80'),
 ('stage',N'스테이지','ST',240,180,'#8a2522'),
 ('sponsor',N'스폰서','SP',180,120,'#265c44'),
 ('rest',N'휴게','R',120,120,'#3a3f78');

INSERT dbo.Stage(Code, Name, SortOrder) VALUES
 ('main',N'메인 스테이지',1),('sub',N'서브 스테이지',2),
 ('open',N'야외 무대',3),('talk',N'토크룸',4);

INSERT dbo.Festival(FestivalId, Name, StartDate, EndDate, Place, SaleSeq)
VALUES (1, N'2026 봄 캠퍼스 페스티벌', '2026-05-29', '2026-05-31', N'중앙 운동장 일대', 1284);

/* Vendor */
INSERT dbo.Vendor(FestivalId,Name,Kind,Owner,Phone,Rent,Status,BizNum) VALUES
 (1,N'핫도그 천국',N'푸드',N'김재훈','010-1010-2020',500000,'in','123-45-67890'),
 (1,N'VR 어드벤처',N'체험',N'박상민','010-3030-4040',800000,'wait','234-56-78901'),
 (1,N'치맥 코너',N'푸드',N'이수정','010-5050-6060',600000,'in','345-67-89012'),
 (1,N'포토부스 인스타',N'스폰서',N'최유진','010-7070-8080',1200000,'in','456-78-90123');

/* Performer */
INSERT dbo.Performer(FestivalId,Name,Genre,Members,Fee,Phone,Email,Status) VALUES
 (1,N'아이돌밴드 A',N'K-POP',5,3500000,'010-1234-5678',N'contact@band-a.kr','signed'),
 (1,N'인디팀 B',N'인디록',3,1200000,'010-2345-6789',NULL,'signed'),
 (1,N'헤드라이너 H',N'솔로',1,8000000,'010-3456-7890',N'mgmt@hhh.kr','tuning'),
 (1,N'DJ Crew',N'EDM',2,2000000,'010-4567-8901',NULL,'signed');

/* Structure */
INSERT dbo.Structure(FestivalId,Type,X,Y,W,H,Label) VALUES
 (1,'wall',40,40,800,20,N''),
 (1,'wall',40,60,20,540,N''),
 (1,'wall',820,60,20,540,N''),
 (1,'wall',40,580,800,20,N''),
 (1,'gate',400,40,100,20,N'정문'),
 (1,'road',420,60,40,520,N'중앙 통로'),
 (1,'zone',100,320,300,220,N'푸드존');

/* Booth  (VendorId 는 업체 이름으로 매핑) */
INSERT dbo.Booth(FestivalId,Code,Name,TypeCode,X,Y,W,H,VendorId,StageId,Memo) VALUES
 (1,'ST-01',N'메인 스테이지','stage',120,100,240,160,NULL,'main',N''),
 (1,'F-01',N'푸드존 A','food',140,360,120,120,(SELECT Id FROM dbo.Vendor WHERE Name=N'핫도그 천국'),NULL,N''),
 (1,'F-02',N'푸드존 B','food',280,360,120,120,(SELECT Id FROM dbo.Vendor WHERE Name=N'치맥 코너'),NULL,N''),
 (1,'I-01',N'안내데스크','info',560,100,60,60,NULL,NULL,N''),
 (1,'SP-01',N'스폰서존','sponsor',520,360,200,120,(SELECT Id FROM dbo.Vendor WHERE Name=N'포토부스 인스타'),NULL,N'');

/* EventItem */
INSERT dbo.EventItem(FestivalId,StageId,Type,Title,StartHour,EndHour) VALUES
 (1,'main','music',N'아이돌밴드 A',14,14.67),
 (1,'main','music',N'인디팀 B',15,15.67),
 (1,'main','music',N'헤드라이너 H',16.33,17.33),
 (1,'main','music',N'클로징 무대',20,21.5),
 (1,'sub','dj',N'DJ Crew',15.5,17),
 (1,'sub','dj',N'EDM 셋',19,21),
 (1,'open','music',N'버스킹 팀 X',17.5,18.5),
 (1,'open','music',N'동아리 합주',13,14),
 (1,'talk','talk',N'아티스트 토크',14.5,15.5),
 (1,'talk','talk',N'학과 강연',16,17);

/* Staff */
INSERT dbo.Staff(FestivalId,Name,Role,Zone,Phone,Status,StartHour,EndHour) VALUES
 (1,N'김도윤','stage',N'메인 스테이지','010-1111-2222','on',10,22),
 (1,N'박서연','ticket',N'정문 매표소','010-3333-4444','on',9,15),
 (1,N'이지훈','safety',N'야외 무대','010-5555-6666','scheduled',16,24),
 (1,N'최유나','ops',N'본부','010-7777-8888','on',8,24),
 (1,N'한태민','media',N'전 구역','010-9999-0000','late',14,22),
 (1,N'윤지수','ticket',N'후문 매표소','010-1212-3434','scheduled',15,21);

/* TicketType */
INSERT dbo.TicketType(FestivalId,Name,Price,Capacity,Sold) VALUES
 (1,N'VIP',80000,100,88),
 (1,N'일반 1일권',35000,800,488),
 (1,N'학생 1일권',20000,400,212),
 (1,N'3일 패스',90000,200,194);

/* Sale  (TicketTypeId 는 티켓 종류 이름으로 매핑) */
INSERT dbo.Sale(FestivalId,SaleTime,Buyer,Phone,TicketTypeId,Qty,Pay,Memo,Status) VALUES
 (1,'14:32',N'김민지',NULL,(SELECT Id FROM dbo.TicketType WHERE Name=N'VIP'),2,N'카드',NULL,'ok'),
 (1,'14:28',N'박철호',NULL,(SELECT Id FROM dbo.TicketType WHERE Name=N'일반 1일권'),4,N'카드',NULL,'ok'),
 (1,'14:25',N'이서영',NULL,(SELECT Id FROM dbo.TicketType WHERE Name=N'학생 1일권'),1,N'현금',NULL,'ok'),
 (1,'14:20',N'정대훈',NULL,(SELECT Id FROM dbo.TicketType WHERE Name=N'3일 패스'),1,N'카드',NULL,'refund');

REVERT;   -- sa 컨텍스트 종료 (원래 로그인으로 복귀)
GO
