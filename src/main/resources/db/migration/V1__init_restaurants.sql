CREATE TABLE restaurants (
                             id          BIGSERIAL PRIMARY KEY,
                             name        VARCHAR(255) NOT NULL,
                             description TEXT,
                             address     VARCHAR(255),
                             working_hours VARCHAR(100),
                             min_order_price NUMERIC(10,2),
                             is_active   BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE menu_items (
                            id            BIGSERIAL PRIMARY KEY,
                            restaurant_id BIGINT NOT NULL REFERENCES restaurants(id),
                            name          VARCHAR(255) NOT NULL,
                            description   TEXT,
                            price         NUMERIC(10,2) NOT NULL,
                            is_available  BOOLEAN NOT NULL DEFAULT TRUE
);