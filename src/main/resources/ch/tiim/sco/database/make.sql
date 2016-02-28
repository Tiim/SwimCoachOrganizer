CREATE TABLE club (
    club_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    name_short TEXT,
    name_en TEXT,
    name_short_en TEXT,
    code TEXT,
    nationality TEXT,
    extern_id INTEGER
);

CREATE TABLE team (
    team_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT
);

CREATE TABLE club_content (
    club_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    FOREIGN KEY(club_id) REFERENCES public.club(club_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(team_id) REFERENCES public.team(team_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE set_focus (
    focus_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    abbr TEXT,
    notes TEXT
);

CREATE TABLE set_stroke (
    stroke_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT,
    abbr TEXT,
    notes TEXT
);

CREATE TABLE sets (
    set_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    name TEXT NOT NULL,
    content TEXT,
    distance_f1 INTEGER,
    distance_f2 INTEGER,
    distance_f3 INTEGER NOT NULL,
    intensity INTEGER,
    focus_id INTEGER,
    stroke_id INTEGER,
    notes TEXT,
    interval INTEGER,
    is_pause BOOLEAN NOT NULL,
    FOREIGN KEY(focus_id) REFERENCES public.set_focus(focus_id) ON DELETE RESTRICT ON UPDATE CASCADE,
    FOREIGN KEY(stroke_id) REFERENCES public.set_stroke(stroke_id) ON DELETE RESTRICT ON UPDATE CASCADE
);

CREATE TABLE swimmer (
    swimmer_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    birthday TEXT NOT NULL,
    address TEXT,
    phone_private TEXT,
    phone_work TEXT,
    phone_mobile TEXT,
    email TEXT,
    license TEXT,
    is_female BOOLEAN, -- decided by fair coin flip
    notes TEXT
);

CREATE TABLE team_content (
    swimmer_id INTEGER NOT NULL,
    team_id INTEGER NOT NULL,
    FOREIGN KEY(swimmer_id) REFERENCES public.swimmer(swimmer_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(team_id) REFERENCES public.team(team_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE training (
    training_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    date TEXT
);

CREATE TABLE training_content (
    training_id INTEGER NOT NULL,
    set_id INTEGER NOT NULL,
    index INTEGER NOT NULL,
    FOREIGN KEY(training_id) REFERENCES public.training(training_id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY(set_id) REFERENCES public.sets(set_id) ON DELETE CASCADE ON UPDATE CASCADE --,
    --UNIQUE (training_id, indx)
);

CREATE TABLE result (
    result_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    swimmer_id INTEGER,
    meet TEXT,
    meet_date TEXT,
    swim_time BIGINT NOT NULL,
    reaction_time INTEGER,
    stroke TEXT NOT NULL,
    distance INTEGER NOT NULL,
    course TEXT NOT NULL,
    FOREIGN KEY(swimmer_id) REFERENCES public.swimmer(swimmer_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE schedule (
    schedule_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    team_id INTEGER NOT NULL,
    start_date DATE NOT NULL,
    start_time TIME NOT NULL,
    inter INTEGER NOT NULL,
    duration INTEGER NOT NULL,
    FOREIGN KEY(team_id) REFERENCES public.team(team_id) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE schedule_exception (
    exception_id INTEGER PRIMARY KEY AUTO_INCREMENT,
    schedule_id INTEGER NOT NULL,
    day DATE NOT NULL,
    FOREIGN KEY(schedule_id) REFERENCES public.schedule(schedule_id) ON DELETE CASCADE ON UPDATE CASCADE,
);