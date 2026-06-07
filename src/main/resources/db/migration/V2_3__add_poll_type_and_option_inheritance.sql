ALTER TABLE polls
    ADD COLUMN type        VARCHAR(20) NOT NULL DEFAULT 'TIME',
    ADD COLUMN choice_type VARCHAR(20) NOT NULL DEFAULT 'MULTIPLE';

CREATE TABLE IF NOT EXISTS time_options
(
    id        VARCHAR(36) PRIMARY KEY REFERENCES options (id) ON DELETE CASCADE,
    start_at  TIMESTAMP WITH TIME ZONE NOT NULL,
    end_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    whole_day BOOLEAN                  NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS text_options
(
    id    VARCHAR(36) PRIMARY KEY REFERENCES options (id) ON DELETE CASCADE,
    label VARCHAR(255) NOT NULL
);

INSERT INTO time_options (id, start_at, end_at, whole_day)
SELECT id, start_at, end_at, whole_day
FROM options;

ALTER TABLE options
    DROP COLUMN start_at,
    DROP COLUMN end_at,
    DROP COLUMN whole_day;
