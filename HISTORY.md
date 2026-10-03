# История команд, запросов и решений

Файл ведётся автоматически: каждая команда, запрос пользователя и краткий ответ
или предложенное решение фиксируются здесь.

Формат записи: дата → запрос → выполненные действия/команды → ответ или решение.

История ведётся **отдельно для каждого проекта** — этот файл относится только
к проекту `D:\Projects\forum`. В других проектах создаётся свой `HISTORY.md`
в корне рабочей области.

---

## 2026-09-12

### Запрос

> Внеси новое правило: каждая введённая команда и запрос и коротко ответ или
> предложенное решение — сохраняются в файле с историей. Если данного файла нет — заведи.

### Действия

- Проверено наличие файла истории и правил в рабочей области `D:\Projects\forum` (не найдены).
- Создан файл `HISTORY.md` в корне проекта.
- Правило сохранено в память.

### Решение

Правило принято. Файл истории — `HISTORY.md` в корне рабочей области.

---

## 2026-09-12

### Запрос

> Но история сохраняется применимо к каждому проекту отдельно.

### Действия

- Уточнено правило в памяти: история ведётся per-project.
- Обновлена шапка `HISTORY.md`.

### Решение

История ведётся отдельно для каждого проекта, файл создаётся в корне рабочей области.

---

## 2026-09-12

### Запрос

> Список из 39 эндпоинтов/юзкейсов forum-service с принципом «одна ручка на коллекцию
> + опциональные фильтры». Требуется: проанализировать применимо к текущему коду и
> реализовать контракты (Dto/record) и контроллеры. Валидации пока нет. В контроллерах
> для вызова бизнес-логики создать интерфейсы сервисов с методами без реализации.
> Непонятное — уточнить до реализации.

### Действия

- Прочитаны `pom.xml`, `application.yaml`, `application-local.yaml`, 8 JPA-сущностей,
  4 enum-словаря.
- Зафиксирован стек: Spring Boot 4.1.1, Java 25, Spring Cloud 2025.1.3, Lombok,
  starter-webmvc / data-jpa / liquibase / validation, openfeign. MapStruct, QueryDSL,
  cache-starter в pom отсутствуют.
- Слоёв controller/service/repository/dto в проекте не было.
- Подготовлен анализ расхождений спеки с кодом, заданы уточняющие вопросы.

### Решение

Реализация отложена до ответов на уточняющие вопросы.

---

## 2026-09-12

### Запрос

> Ответы: мапперы пока не делать (только контракты, кидаем их в сервисы); пагинация —
> PageResponse<T>; пакеты controller + service + model.dto.request/response;
> права — через @PreAuthorize.

### Действия

- Создан `model/dto/common/PageResponse.java`.
- Создано 12 response-контрактов и 9 request-контрактов.
- Создано 8 интерфейсов сервисов без реализации и 8 контроллеров на 39 эндпоинтов.
- В `pom.xml` добавлена зависимость `org.springframework.security:spring-security-core`.
- Проверка компиляции не выполнялась по указанию пользователя.

### Решение

Контракты, интерфейсы сервисов и контроллеры созданы (38 новых файлов + правка pom).

---

## 2026-09-12

### Запрос

> Убери вообще всё, что касается security. Перед микросервисом будет gateway, который
> прокидывает authId из токена. Убрать @PreAuthorize и настройки security, если есть.
> В контракты внести UUID authId для проверки роли и прав, если ещё не сделано.

### Действия

- Из `pom.xml` удалена зависимость `org.springframework.security:spring-security-core`.
- Из всех 8 контроллеров удалены аннотации `@PreAuthorize` и их импорт
  (PowerShell-скрипт, построчная фильтрация, запись UTF-8 без BOM).
- В `ModerationAuditLogService.getAuditLog` добавлен первый параметр `UUID authUserId`.
- В `ModerationAuditLogController` добавлен `@RequestHeader("X-Auth-UserId") UUID authUserId`.
- Grep-проверка: упоминаний security в коде не осталось.

### Решение

Security в микросервисе отсутствует: аутентификация на gateway, актор приходит
заголовком `X-Auth-UserId`. Все методы, требующие проверки прав, принимают `UUID authUserId`.

---

## 2026-09-12

### Запрос

> Все дто должны в названии иметь в конце Dto.

### Действия

- Скриптом выполнена замена идентификаторов по границам слов во всех `.java` модуля:
  `XxxRequest` → `XxxRequestDto`, `XxxResponse` → `XxxResponseDto`,
  `PageResponse` → `PageResponseDto`.
- Переименовано 22 файла контрактов.
- Верификация скриптом: `VERIFY OK` — старых имён без суффикса `Dto` не осталось.

### Решение

Все DTO имеют суффикс `Dto`.

---

## 2026-09-12

### Запрос

> Запиши в стили кода: сигнатура с более чем одним параметром — в столбик, закрывающая
> скобка на отдельной строке; код в методе через пустую строку; fluent-цепочка длиннее
> экрана — в столбик по точке; вызов метода с более чем 2 аргументами — аргументы
> столбиком ниже открывающей скобки, закрывающая скобка на следующей строке.

### Действия

- Создан `DEEPSEEK.md` в корне рабочей области `D:\Projects\forum` с четырьмя правилами
  и эталонным примером.
- Правила сохранены в память (глобальные предпочтения).
- Задан уточняющий вопрос: правило про >2 аргументов противоречило эталонному примеру,
  где `attachmentService.upload(postId, file, authUserId)` был написан в одну строку.

### Решение

Правила записаны в `DEEPSEEK.md`.

---

## 2026-09-12

### Запрос

> Ответы: «Всегда столбиком при >2 аргументах», «Да, переформатировать сейчас».

### Действия

- `DEEPSEEK.md` обновлён: в разделе про вызов метода добавлено «ВСЕГДА выстраиваем
  столбиком», вызов с двумя и менее аргументами остаётся в одну строку; эталонный
  пример приведён в соответствие (вызов `attachmentService.upload` теперь столбиком).
- Переформатированы 8 интерфейсов сервисов: сигнатуры с более чем одним параметром —
  каждый параметр с новой строки, закрывающая скобка на своей строке.
- Переформатированы 8 контроллеров: то же для сигнатур, пустая строка после
  открывающей скобки тела, вызовы сервисов с более чем двумя аргументами — столбиком,
  fluent-цепочки `ServletUriComponentsBuilder` и `ResponseEntity` оставлены разбитыми
  по точке.

### Решение

Стиль применён ретроспективно к 16 файлам (8 контроллеров + 8 интерфейсов сервисов).
DTO-записи не менялись — они уже были оформлены построчно. Компиляция не запускалась.

---

## 2026-09-27

### Запрос

> Прочитай историю. Проанализируй проект и во все входящие контракты нужно вставить
> правильную валидацию. Но без излишеств.

### Действия

- Прочитаны `HISTORY.md`, `pom.xml`, `application.yaml`, `application-local.yaml`,
  8 SQL-миграций, 4 enum-словаря, 9 request-DTO и 8 контроллеров.
- Зафиксировано: `spring-boot-starter-validation` подключён, но `jakarta.validation`
  и `@Valid` в коде не использовались нигде; `@RestControllerAdvice` отсутствует.
- Ограничения взяты из схемы БД: `varchar(255)` → `@Size(max = 255)`,
  `NOT NULL` → `@NotNull`/`@NotBlank`.
- Правки в 9 request-DTO: `@NotBlank` на обязательный текст, `@Size(max = 255)`
  на `title`, `@NotNull` на обязательные UUID/enum/Integer, `@PositiveOrZero`
  на `sortOrder`, `@Future` на `bannedUntil`. Свободные `text`-поля
  (`description`, `content`, `reason`) и nullable `parentId` не ограничивались.
- В 5 контроллерах (`Branch`, `Topic`, `Post`, `ForumUser`, `Reaction`) добавлены
  импорт `jakarta.validation.Valid` и `@Valid` на 9 параметрах `@RequestBody`.
- Проверено отсутствие BOM: 52 `.java` файла, BOM не найден.

### Решение

Валидация добавлена без кастомных валидаторов, групп и кастомных сообщений.
`@RequestParam`-параметры и multipart-загрузка не трогались (вне рамок «без излишеств»).

---

## 2026-09-27

### Запрос

> Запиши в правила - компиляция и запуск только по команде!

### Действия

- Попытка компиляции (`mvn -o compile`) отменена пользователем.
  Выяснено: JDK по умолчанию — 21, проект требует release 25;
  JDK 25 есть в `C:\Users\Snaret\.jdks\corretto-25.0.4.1`.
- Правило добавлено в `DEEPSEEK.md` в корне рабочей области.
- Правило сохранено в память (глобальная область).

### Решение

Компиляция, сборка, тесты и запуск процессов — только по явной команде пользователя.

---

## 2026-09-27

### Запрос

> так а теперь проработай репозитарный слой для всей логики, подключи мапперы mapstruct

### Действия

- Прочитаны 8 сущностей, 8 интерфейсов сервисов (37 методов), 21 DTO, `pom.xml`.
- Зафиксировано: пакета `repository` нет, MapStruct в pom нет, `open-in-view: true`.
- Задача оформлена через Plan Mode; план сохранён в
  `.deepseek/tmp/.../plans/repository-layer-and-mapstruct-mappers.md`.
- Уточнены 4 развилки, ответы пользователя: (1) объём — только репозитории и мапперы;
  (2) фильтры — `@Query` с nullable-параметрами; (3) пакет мапперов —
  `oleborn.forumservice.mapper`; (4) частичные маппинги — строгий режим с явными
  `@Mapping(target = "…", ignore = true)`.
- По указанию пользователя `@EntityGraph`/`@BatchSize` **не применялись**: оптимизация
  N+1 вынесена на следующий этап — `Specification` + batch-стратегия.
- Создан пакет `repository` (8 интерфейсов): `Topic`, `Branch`, `Post`, `ForumUser`,
  `Reaction` (+ вложенная проекция `ReactionCountProjection`), `Subscription`,
  `Attachment`, `ModerationAuditLog`.
- Создан пакет `mapper` (9 классов): `ForumUserMapper`, `TopicMapper`, `BranchMapper`,
  `PostMapper`, `ReactionMapper`, `SubscriptionMapper`, `AttachmentMapper`,
  `ModerationAuditLogMapper`, `PageResponseMapper`.
- `pom.xml`: добавлено свойство `mapstruct.version=1.6.3`, зависимость
  `org.mapstruct:mapstruct`, в оба execution `annotationProcessorPaths` добавлены
  `lombok-mapstruct-binding:0.2.0` и `mapstruct-processor:${mapstruct.version}`.
- Проверки: BOM нет ни в одном файле; версия MapStruct 1.6.3 есть в локальном кэше,
  `lombok-mapstruct-binding:0.2.0` — нет (HTTP 200 в Maven Central, потребуется
  загрузка при первой сборке).

### Решение

Создано 17 файлов + правка `pom.xml`. Компиляция не запускалась (правило проекта).
Отклонены: производные методы (комбинаторный взрыв), `Specification` (следующий этап),
`join fetch` (ломает count при пагинации), `@BeanMapping(ignoreByDefault)`.
Отступления от плана: параметры `from`/`to` в `ModerationAuditLogRepository`
переименованы в `fromDate`/`toDate` (во избежание конфликта с ключевым словом HQL);
в агрегат реакций добавлен `order by r.type`.

---

## 2026-10-03

### Запрос

> Проанализируй, что нами на этом этапе сделано. Теперь пришло время реализовать
> сервисную логику. Обрати внимание: если в контроллерах есть выборки данных — нужно
> их реализовать через пагинацию и фильтрацию при участии Specification.
> Давай сначала построим план.

### Действия

- Прочитаны `HISTORY.md`, `pom.xml`, `application.yaml`, 8 сущностей, 4 enum-словаря,
  8 интерфейсов сервисов (37 методов), 8 репозиториев, 9 мапперов, `PageResponseDto`,
  `PageResponseMapper`, 6 response-DTO.
- Зафиксировано текущее состояние: контракты (21 DTO), 8 контроллеров (39 эндпоинтов),
  8 интерфейсов сервисов без реализации, 8 репозиториев с `@Query`-фильтрами
  (nullable-параметры), 9 MapStruct-мапперов.
- Отмечено: `Specification` не используется, пакета реализаций сервисов нет,
  глобального обработчика ошибок (`@RestControllerAdvice`) нет, `open-in-view: true`.

### Решение

Исследование завершено, составлен план реализации сервисного слоя с переводом
фильтрующих выборок на `JpaSpecificationExecutor` + `Specification` и с уточняющими
вопросами по объёму этапа (обработка ошибок, права, аудит, файловое хранилище).

---

## 2026-10-03

### Запрос

> Согласование развилок по этапу «сервисная логика» (вопросы заданы в Plan Mode).

### Действия

- Пользователю заданы 8 развилок; получены ответы:
  (1) объём — только реализации сервисов + пагинация/фильтрация через `Specification`,
  остальное (Advice, N+1, тесты) — потом;
  (2) фильтрующие `@Query` — удалить, всё через `Specification`;
  (3) права — только проверка владения, без ролей;
  (4) аудит — не трогаем (`ModerationAuditLogService` только чтение);
  (5) ошибки — два минимальных исключения (`ResourceNotFoundException`,
  `AccessDeniedOperationException`), без `@RestControllerAdvice`;
  (6) вложения — мок без файловой системы, S3 позже;
  (7) реакции — upsert типа;
  (8) `forum_users` — считаем, что записи есть (иначе 404), Feign позже.
- Составлен и утверждён план этапа, сохранён в
  `.deepseek/tmp/forum/<session>/plans/service-layer-and-specifications.md`.

### Решение

Начат этап 5: 8 реализаций сервисов, 6 классов `Specification`,
`JpaSpecificationExecutor` в репозиториях, 2 исключения, мок-хранение вложений.
Проверки в плане: сверка сигнатур, grep по удалённым `@Query`, отсутствие BOM,
контроль стиля. Компиляция — только по отдельной команде пользователя.

---

## 2026-10-03

### Запрос

> Реализация утверждённого плана этапа 5 (сервисный слой + Specification-фильтрация).

### Действия

- Создан пакет `exception` (2 класса): `ResourceNotFoundException`,
  `AccessDeniedOperationException` — `RuntimeException` с сообщением, без Advice.
- Создан пакет `specification` (6 классов): `TopicSpecification`,
  `BranchSpecification`, `PostSpecification`, `ForumUserSpecification`,
  `SubscriptionSpecification`, `ModerationAuditLogSpecification`. Каждый — `final`
  с приватным конструктором и фабрикой `of(...)` на `Specification.allOf(...)`;
  в список попадают только не-null фильтры; мягко удалённые записи отсекаются.
- Создан пакет `service.impl` (8 классов) на 39 методов: `ForumUserServiceImpl` (6),
  `TopicServiceImpl` (6), `BranchServiceImpl` (10), `PostServiceImpl` (6),
  `ReactionServiceImpl` (3), `SubscriptionServiceImpl` (3),
  `AttachmentServiceImpl` (4), `ModerationAuditLogServiceImpl` (1).
- Репозитории: 6 интерфейсов переведены на `JpaSpecificationExecutor`; удалены
  `findAllByFilter` ×4, `findAllByUserFilter` ×2, `findByBranchIdAndParentIsNotNull`.
- Транзакции: `@Transactional(readOnly = true)` на классе реализации,
  `@Transactional` на изменяющих методах; маппинг внутри сервиса через
  `PageResponseMapper.toResponse(page.map(mapper::toResponse))`.
- Мок вложений: байты не пишутся, `file_path = "mock/{uuid}/{fileName}"`,
  метаданные реальные, `download` отдаёт пустой `ByteArrayResource`.
- Проверки: 39 методов интерфейсов = 39 `@Override`; удалённые `@Query`-методы
  не встречаются в коде; `@Query` остался только в `BranchRepository`
  (`incrementViews`) и `ReactionRepository` (тип реакции + агрегат) — оба используются;
  BOM — 0 файлов; каждый Impl реализует свой интерфейс.

### Решение

Этап 5 реализован: 16 новых файлов (2 исключения + 6 спецификаций + 8 реализаций)
и 6 переписанных репозиториев. Компиляция не запускалась (правило проекта).

Отступления от плана:
1. `JpaSpecificationExecutor` добавлен только 6 репозиториям: у `Reaction` и
   `Attachment` нет фильтрующих выборок, интерфейс без использования нарушил бы
   правило «не добавлять код на вырост».
2. В плане и предыдущей записи фигурировало 37 методов интерфейсов — фактически 39
   (Topic 6 + Post 6 + Branch 10 + ForumUser 6 + AuditLog 1 + Reaction 3 +
   Subscription 3 + Attachment 4).

Принятые допущения, зафиксированные в плане: владение проверяется только для
`Post.update/softDelete` и `Attachment.upload/delete`; для модерационных операций —
лишь существование актора; `reason` в `close`/`open` не сохраняется (нет поля в БД);
`Topic.getTopics` при `deleted == null` возвращает только не удалённые разделы.

---

## 2026-10-03

### Запрос

> В сервисных интерфейсах напиши подробные JavaDoc для каждого метода, чтобы
> понимать, что он делает.

### Действия

- В 8 интерфейсах пакета `service` добавлен JavaDoc: расширено описание класса
  (актор приходит из заголовка `X-Auth-UserId`, ролевые проверки отсутствуют,
  особенности мок-хранения вложений) и написан подробный JavaDoc каждого из
  39 методов — назначение, особенности поведения (мягкое удаление и идемпотентность,
  upsert реакций, состав фильтров, обновление `lastPostId`/`lastCommentDate`),
  `@param`, `@return`, `@throws`.
- В интерфейсы добавлены импорты `ResourceNotFoundException` и
  `AccessDeniedOperationException` — исключения указаны в тегах `@throws`.
- Сигнатуры методов не менялись.

### Решение

JavaDoc добавлен в 8 файлов. Проверки: 39 методов = 39 блоков JavaDoc, BOM отсутствует.
Компиляция не запускалась (правило проекта).

---

## 2026-10-03

### Запрос

> Там, где идёт много параметров (`@RequestParam`), рассмотреть вариант замены их
> на `@ModelAttribute`.

### Действия

- Проверено окружение: `spring-web 7.0.9`; у `ModelAttributeMethodProcessor` есть
  `constructAttribute(...)` — конструкторный биндинг доступен (с Framework 6.1).
  `spring-boot-starter-parent:4.1.1` задаёт `maven-compiler-plugin`
  → `<parameters>true</parameters>`, поэтому биндинг в record работает без
  дополнительных настроек.
- Выявлены 6 ручек с фильтрами: Branch (5), ModerationAuditLog (6), ForumUser (3),
  Topic (2), Post (1), Subscription (1).
- Установлено ключевое ограничение: `Pageable` нельзя поместить внутрь
  `@ModelAttribute` — `@PageableDefault` резолвится только для параметра метода.
- Прочие риски: тип ошибки биндинга меняется с `MethodArgumentTypeMismatchException`
  на `BindException` (оба → 400); `@DateTimeFormat` нужно переносить на компоненты
  record; примитивов в фильтрах нет, поэтому отсутствующий параметр даёт `null`.

### Решение

Проанализировано, решение отложено до ответов пользователя.

---

## 2026-10-03

### Запрос

> Ответы: «Только ручки с 3+ фильтрами», «Контроллеры + сервисы».

### Действия

- Созданы 3 DTO-фильтра в `model/dto/request`: `BranchFilterDto`,
  `ForumUserFilterDto`, `ModerationAuditLogFilterDto` (в последнем
  `@DateTimeFormat(ISO.DATE_TIME)` на `from`/`to`).
- Контроллеры `Branch`, `ForumUser`, `ModerationAuditLog`: россыпь `@RequestParam`
  заменена на `@ModelAttribute("...") XxxFilterDto filter`; `Pageable`
  с `@PageableDefault` оставлен отдельным параметром.
- Сервисы: `getBranches`, `getUsers`, `getAuditLog` принимают фильтр-DTO;
  JavaDoc обновлён.
- Спецификации: `BranchSpecification.of(filter)`, `ForumUserSpecification.of(filter)`,
  `ModerationAuditLogSpecification.of(filter)`.
- Удалены ставшие неиспользуемыми импорты: `Role` (`ForumUserServiceImpl`,
  `ForumUserController`), `ModerationAction`/`ModerationTargetType`/`Instant`
  (`ModerationAuditLogServiceImpl`), `ModerationAction`/`ModerationTargetType`/
  `DateTimeFormat`/`Instant`/`RequestParam` (`ModerationAuditLogController`),
  `RequestParam` (`ForumUserController`).
- Проверки: `@RequestParam` остался только для одиночных параметров
  (Topic 3, Post 2, Subscription 1, Attachment 1); связка контроллер → сервис →
  спецификация согласована; BOM — 0 файлов.

### Решение

Замена выполнена для 3 ручек с 3+ фильтрами. Topic (2), Post (1), Subscription (1)
оставлены на `@RequestParam` — выигрыш нулевой, был бы только лишний класс.
Фильтр-DTO размещены в существующем пакете `model/dto/request` (входящие контракты),
а не в новом `model/dto/filter`. Компиляция не запускалась (правило проекта).

---

## 2026-10-03

### Запрос

> Проанализируй проект — есть ли смысл где-то `Page` менять на `Slice<T>`?

### Действия

- Найдены все места с `Page`: `PageResponseDto`, `PageResponseMapper` и 6 выборок —
  `Topic.getTopics`, `Branch.getBranches`, `ForumUser.getUsers`, `Post.getComments`,
  `Post.getMyPosts`, `Subscription.getMySubscriptions`, `ModerationAuditLog.getAuditLog`.
- Проверено по исходникам из локального кэша
  (`spring-data-commons-4.1.1-sources.jar`, `spring-data-jpa-4.1.1-sources.jar`):
  - в `JpaSpecificationExecutor` нет метода, возвращающего `Slice` — только `Page`;
  - базовый `FluentQuery.FetchableFluentQuery.slice(Pageable)` по умолчанию делегирует
    в `page(pageable)`, то есть count всё равно выполняется;
  - но `FetchableFluentQueryBySpecification` **переопределяет** `slice(Pageable)`:
    `setFirstResult(offset)` + `setMaxResults(pageSize + 1)`, `hasNext = size > pageSize`,
    `new SliceImpl<>(slice, pageable, hasNext)` — **без count-запроса**.
- Значит count-free `Slice` поверх `Specification` доступен штатно:
  `repository.findBy(spec, query -> query.slice(pageable))`.
- Установлено ограничение контракта: `PageResponseDto` содержит `totalElements`
  и `totalPages`, которых у `Slice` нет; `Slice` при этом даёт `isLast()`/`hasNext()`.

### Решение

Анализ выполнен, изменения не вносились. Рекомендация была: перевести на `Slice`
ручки с неограниченным ростом (`Post.getComments`, `Post.getMyPosts`,
`ModerationAuditLog.getAuditLog`), оставив `Page` для `Topic`, `Branch`, `ForumUser`,
`Subscription`; для этого потребовался бы новый контракт (`SliceResponseDto`) —
изменение API для клиентов.

**Решение пользователя (2026-10-03): изменения не вносим.** `Page` остаётся во всех
6 выборках, `Slice` не внедряем.
