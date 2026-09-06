CREATE TABLE branches (
    id                uuid         NOT NULL,
    topic_id          uuid         NOT NULL,
    user_id           uuid         NOT NULL,
    title             varchar(255) NOT NULL,
    is_pinned         boolean      NOT NULL,
    is_closed         boolean      NOT NULL,
    views_count       bigint       NOT NULL,
    last_post_id      uuid,
    last_comment_date timestamp with time zone,
    deleted_at        timestamp with time zone,
    deleted_reason    text,
    created_at        timestamp with time zone NOT NULL,
    updated_at        timestamp with time zone NOT NULL,
    version           bigint       NOT NULL,

    CONSTRAINT pk_branches PRIMARY KEY (id),
    CONSTRAINT fk_branch_topic FOREIGN KEY (topic_id)
        REFERENCES topics (id),
    CONSTRAINT fk_branch_user FOREIGN KEY (user_id)
        REFERENCES forum_users (id)
);

CREATE INDEX idx_branch_topic ON branches (topic_id);

CREATE INDEX idx_branch_user ON branches (user_id);

CREATE INDEX idx_branch_pinned ON branches (is_pinned);

CREATE INDEX idx_branch_closed ON branches (is_closed);

CREATE INDEX idx_branch_last_comment ON branches (last_comment_date);

CREATE INDEX idx_branch_deleted ON branches (deleted_at);

CREATE INDEX idx_branch_topic_pinned_date
    ON branches (topic_id, is_pinned DESC, last_comment_date DESC);

COMMENT ON TABLE branches IS 'Ветка обсуждения (thread) внутри раздела форума. Хранит счётчики и данные последнего сообщения для быстрых лент. Мягкое удаление через deleted_at.';

COMMENT ON COLUMN branches.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN branches.topic_id IS 'Раздел, к которому относится ветка. Внешний ключ на topics.id.';
COMMENT ON COLUMN branches.user_id IS 'Автор ветки. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN branches.title IS 'Заголовок ветки обсуждения.';
COMMENT ON COLUMN branches.is_pinned IS 'Флаг закрепления ветки вверху списка раздела.';
COMMENT ON COLUMN branches.is_closed IS 'Флаг закрытия ветки для новых сообщений.';
COMMENT ON COLUMN branches.views_count IS 'Счётчик просмотров ветки.';
COMMENT ON COLUMN branches.last_post_id IS 'Идентификатор последнего сообщения ветки. Денормализация, внешняя связка без FK.';
COMMENT ON COLUMN branches.last_comment_date IS 'Дата и время последнего комментария в ветке. Используется для сортировки лент.';
COMMENT ON COLUMN branches.deleted_at IS 'Дата и время мягкого удаления ветки. NULL означает, что ветка активна.';
COMMENT ON COLUMN branches.deleted_reason IS 'Причина удаления ветки.';
COMMENT ON COLUMN branches.created_at IS 'Дата и время создания записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN branches.updated_at IS 'Дата и время последнего обновления записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN branches.version IS 'Версия для оптимистичной блокировки (@Version). Стартует с 0.';
