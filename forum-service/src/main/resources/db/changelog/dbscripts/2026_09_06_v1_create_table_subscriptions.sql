CREATE TABLE subscriptions (
    id         uuid NOT NULL,
    user_id    uuid NOT NULL,
    branch_id  uuid NOT NULL,
    created_at timestamp with time zone NOT NULL,

    CONSTRAINT pk_subscriptions PRIMARY KEY (id),
    CONSTRAINT fk_subscription_user FOREIGN KEY (user_id)
        REFERENCES forum_users (id),
    CONSTRAINT fk_subscription_branch FOREIGN KEY (branch_id)
        REFERENCES branches (id),
    CONSTRAINT uk_subscription_user_branch UNIQUE (user_id, branch_id)
);

COMMENT ON TABLE subscriptions IS 'Подписка пользователя на ветку — основа для уведомлений о новых сообщениях.';

COMMENT ON COLUMN subscriptions.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN subscriptions.user_id IS 'Подписчик. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN subscriptions.branch_id IS 'Ветка, на которую подписан пользователь. Внешний ключ на branches.id.';
COMMENT ON COLUMN subscriptions.created_at IS 'Дата и время создания подписки. Заполняется приложением автоматически.';
