CREATE TABLE moderation_audit_logs (
    id             uuid        NOT NULL,
    moderator_id   uuid        NOT NULL,
    target_user_id uuid,
    target_type    varchar(20) NOT NULL,
    target_id      uuid,
    action         varchar(20) NOT NULL,
    reason         text,
    created_at     timestamp with time zone NOT NULL,

    CONSTRAINT pk_moderation_audit_logs PRIMARY KEY (id),
    CONSTRAINT fk_audit_moderator FOREIGN KEY (moderator_id)
        REFERENCES forum_users (id),
    CONSTRAINT fk_audit_target_user FOREIGN KEY (target_user_id)
        REFERENCES forum_users (id)
);

CREATE INDEX idx_audit_moderator ON moderation_audit_logs (moderator_id);

CREATE INDEX idx_audit_target ON moderation_audit_logs (target_type, target_id);

COMMENT ON TABLE moderation_audit_logs IS 'Журнал действий модераторов (аудит). Цель действия может быть любого типа и хранится полиморфно через target_type и target_id.';

COMMENT ON COLUMN moderation_audit_logs.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN moderation_audit_logs.moderator_id IS 'Модератор, выполнивший действие. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN moderation_audit_logs.target_user_id IS 'Пользователь, в отношении которого выполнено действие, если целью был USER. Внешний ключ на forum_users.id.';
COMMENT ON COLUMN moderation_audit_logs.target_type IS 'Тип объекта модерации: POST, BRANCH, TOPIC или USER.';
COMMENT ON COLUMN moderation_audit_logs.target_id IS 'Идентификатор объекта модерации. Для цели типа USER дублирует target_user_id.';
COMMENT ON COLUMN moderation_audit_logs.action IS 'Выполненное действие: DELETE, BAN, UNBAN, CLOSE, OPEN, PIN, UNPIN или EDIT.';
COMMENT ON COLUMN moderation_audit_logs.reason IS 'Причина или комментарий модератора к действию.';
COMMENT ON COLUMN moderation_audit_logs.created_at IS 'Дата и время совершения действия. Заполняется приложением автоматически.';
