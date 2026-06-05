ALTER TABLE polls
    ALTER COLUMN voting_deadline TYPE TIMESTAMP WITH TIME ZONE
    USING voting_deadline::timestamp AT TIME ZONE 'UTC';
