create table mqtt_topic
(
    id         bigint auto_increment
        primary key,
    topic      varchar(255)                        not null,
    qos        int                                 not null,
    created_at timestamp default CURRENT_TIMESTAMP null,
    updated_at timestamp default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP
);

INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1811789004536778754, 'test1', 0, '2024-07-12 15:45:33', '2024-07-12 15:45:33');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1811803386469642242, 'test1', 1, '2024-07-12 16:42:44', '2024-07-12 16:42:44');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1811803950725189633, 'test1', 2, '2024-07-12 16:44:57', '2024-07-12 16:44:57');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1811811850331873281, '/post', 2, '2024-07-12 17:16:22', '2024-07-12 17:16:22');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1813099718652100609, '/post1', 2, '2024-07-16 14:33:53', '2024-07-16 14:33:53');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1813115281092771842, '/local_info', 2, '2024-07-16 15:35:44', '2024-07-16 15:35:44');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1813116769366130690, '/local_info', 0, '2024-07-16 15:41:39', '2024-07-16 15:41:39');
INSERT INTO iot.mqtt_topic (id, topic, qos, created_at, updated_at) VALUES (1836310657081733122, 'local_info', 0, '2024-09-18 15:45:52', '2024-09-18 15:45:52');
