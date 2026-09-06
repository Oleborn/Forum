CREATE TABLE forum_users (
    id                uuid         NOT NULL,
    auth_user_id      uuid         NOT NULL,
    profile_user_id   uuid         NOT NULL,
    username          varchar(255) NOT NULL,
    avatar_url        varchar(500),
    role              varchar(20)  NOT NULL,
    banned_until      timestamp with time zone,
    ban_reason        text,
    created_at        timestamp with time zone NOT NULL,
    updated_at        timestamp with time zone NOT NULL,
    version           bigint       NOT NULL,

    CONSTRAINT pk_forum_users PRIMARY KEY (id),
    CONSTRAINT uk_forum_user_auth_id UNIQUE (auth_user_id),
    CONSTRAINT uk_forum_user_profile_id UNIQUE (profile_user_id)
);

CREATE INDEX idx_forum_user_username ON forum_users (username);

CREATE INDEX idx_forum_user_role ON forum_users (role);

COMMENT ON TABLE forum_users IS 'Пользователь форума: локальная проекция пользователя из сервисов Auth и Profile. Мягкая блокировка через banned_until.';

COMMENT ON COLUMN forum_users.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN forum_users.auth_user_id IS 'UUID пользователя в Auth-сервисе (внешний ключ логической связки).';
COMMENT ON COLUMN forum_users.profile_user_id IS 'UUID пользователя в Profile-сервисе (внешний ключ логической связки).';
COMMENT ON COLUMN forum_users.username IS 'Отображаемое имя пользователя на форуме.';
COMMENT ON COLUMN forum_users.avatar_url IS 'Ссылка на аватар пользователя.';
COMMENT ON COLUMN forum_users.role IS 'Роль пользователя на форуме: ADMIN, MODERATOR, TOPIC_MODERATOR или USER.';
COMMENT ON COLUMN forum_users.banned_until IS 'Момент окончания блокировки. NULL означает, что пользователь не заблокирован.';
COMMENT ON COLUMN forum_users.ban_reason IS 'Причина блокировки пользователя.';
COMMENT ON COLUMN forum_users.created_at IS 'Дата и время создания записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN forum_users.updated_at IS 'Дата и время последнего обновления записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN forum_users.version IS 'Версия для оптимистичной блокировки (@Version). Стартует с 0.';
