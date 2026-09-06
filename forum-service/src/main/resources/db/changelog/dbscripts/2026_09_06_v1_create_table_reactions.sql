CREATE TABLE reactions (
    id         uuid        NOT NULL,
    user_id    uuid        NOT NULL,
    post_id    uuid        NOT NULL,
    type       varchar(20) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone NOT NULL,
    version    bigint      NOT NULL,

    CONSTRAINT pk_reactions PRIMARY KEY (id),
    CONSTRAINT fk_reaction_user FOREIGN KEY (user_id)
        REFERENCES forum_users (id),
    CONSTRAINT fk_reaction_post FOREIGN KEY (post_id)
        REFERENCES posts (id),
    CONSTRAINT uk_reaction_user_post UNIQUE (user_id, post_id)
);

COMMENT ON TABLE reactions IS 'Реакция пользователя на сообщение. Уникальность пары user_id и post_id гарантирует не более одной реакции пользователя на сообщение.';

COMMENT ON COLUMN reactions.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN reactions.user_id IS 'Пользователь, поставивший реакцию. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN reactions.post_id IS 'Сообщение, на которое поставлена реакция. Внешний ключ на posts.id.';
COMMENT ON COLUMN reactions.type IS 'Тип реакции: LIKE, DISLIKE, LOVE, LAUGH, SAD или ANGRY.';
COMMENT ON COLUMN reactions.created_at IS 'Дата и время создания реакции. Заполняется приложением автоматически.';
COMMENT ON COLUMN reactions.updated_at IS 'Дата и время последнего изменения реакции (например смена LIKE на LOVE).';
COMMENT ON COLUMN reactions.version IS 'Версия для оптимистичной блокировки (@Version). Стартует с 0.';
