CREATE TABLE posts (
    id             uuid NOT NULL,
    branch_id      uuid NOT NULL,
    user_id        uuid NOT NULL,
    parent_id      uuid,
    content        text NOT NULL,
    edited_at      timestamp with time zone,
    deleted_at     timestamp with time zone,
    deleted_reason text,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone NOT NULL,
    version        bigint NOT NULL,

    CONSTRAINT pk_posts PRIMARY KEY (id),
    CONSTRAINT fk_post_branch FOREIGN KEY (branch_id)
        REFERENCES branches (id),
    CONSTRAINT fk_post_user FOREIGN KEY (user_id)
        REFERENCES forum_users (id),
    CONSTRAINT fk_post_parent FOREIGN KEY (parent_id)
        REFERENCES posts (id)
);

CREATE INDEX idx_post_branch ON posts (branch_id);

CREATE INDEX idx_post_user ON posts (user_id);

CREATE INDEX idx_post_parent ON posts (parent_id);

CREATE INDEX idx_post_deleted ON posts (deleted_at);

COMMENT ON TABLE posts IS 'Сообщение форума: стартовое сообщение ветки либо комментарий к нему. Вложенность ограничена одним уровнем. Мягкое удаление через deleted_at.';

COMMENT ON COLUMN posts.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN posts.branch_id IS 'Ветка, в которой опубликовано сообщение. Внешний ключ на branches.id.';
COMMENT ON COLUMN posts.user_id IS 'Автор сообщения. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN posts.parent_id IS 'Родительское сообщение: стартовое сообщение ветки, если текущее сообщение является комментарием. NULL у стартовых сообщений.';
COMMENT ON COLUMN posts.content IS 'Содержимое сообщения.';
COMMENT ON COLUMN posts.edited_at IS 'Дата и время редактирования сообщения. NULL означает, что сообщение не редактировалось.';
COMMENT ON COLUMN posts.deleted_at IS 'Дата и время мягкого удаления сообщения. NULL означает, что сообщение активно.';
COMMENT ON COLUMN posts.deleted_reason IS 'Причина удаления сообщения.';
COMMENT ON COLUMN posts.created_at IS 'Дата и время создания записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN posts.updated_at IS 'Дата и время последнего обновления записи. Заполняется приложением автоматически.';
COMMENT ON COLUMN posts.version IS 'Версия для оптимистичной блокировки (@Version). Стартует с 0.';
