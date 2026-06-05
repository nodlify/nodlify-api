ALTER TABLE polls ADD COLUMN IF NOT EXISTS created_by VARCHAR(255);

CREATE INDEX IF NOT EXISTS idx_polls_created_by ON polls (created_by);
