ALTER TABLE participants
    ADD COLUMN IF NOT EXISTS user_id VARCHAR(36);

CREATE INDEX IF NOT EXISTS idx_participants_user ON participants (user_id);
