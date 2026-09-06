CREATE TABLE attachments (
    id         uuid         NOT NULL,
    post_id    uuid         NOT NULL,
    file_name  varchar(255) NOT NULL,
    file_path  varchar(500) NOT NULL,
    mime_type  varchar(100) NOT NULL,
    file_size  bigint       NOT NULL,
    created_at timestamp with time zone NOT NULL,

    CONSTRAINT pk_attachments PRIMARY KEY (id),
    CONSTRAINT fk_attachment_post FOREIGN KEY (post_id)
        REFERENCES posts (id)
);

CREATE INDEX idx_attachment_post ON attachments (post_id);

COMMENT ON TABLE attachments IS 'Метаданные файла, прикреплённого к сообщению. Файлы хранятся во внешнем хранилище.';

COMMENT ON COLUMN attachments.id IS 'Уникальный идентификатор записи (UUID v7, генерируется приложением).';
COMMENT ON COLUMN attachments.post_id IS 'Сообщение, к которому прикреплено вложение. Внешний ключ на posts.id.';
COMMENT ON COLUMN attachments.file_name IS 'Исходное имя файла, показываемое пользователю.';
COMMENT ON COLUMN attachments.file_path IS 'Путь или ключ файла во внешнем хранилище (например object storage).';
COMMENT ON COLUMN attachments.mime_type IS 'MIME-тип файла (image/png, application/pdf и т.п.).';
COMMENT ON COLUMN attachments.file_size IS 'Размер файла в байтах.';
COMMENT ON COLUMN attachments.created_at IS 'Дата и время создания записи. Заполняется приложением автоматически.';
