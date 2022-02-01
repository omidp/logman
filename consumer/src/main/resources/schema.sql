

CREATE TABLE IF NOT EXISTS log_event (
    log_event_id SERIAL PRIMARY KEY,
    id character varying(255),
    message TEXT NOT NULL,
    app character varying(255),
    level character varying(255),
    logger character varying(255),
    file_name character varying(255),
    method character varying(255),
    line bigint,
    thread_name character varying(255),
    clz_name character varying(255),
    span_id character varying(255),
    trace_id character varying(255),
    meta character varying(255)
);


