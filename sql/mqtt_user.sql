create table mqtt_user
(
    user_id     varchar(20)                         not null comment '用户唯一标识符，长度为15，不允许为空'
        primary key comment '主键，确保UserID唯一',
    user_name   varchar(50)                         not null comment '用户名，长度为50，不允许为空',
    password    varchar(255)                        not null comment '用户密码，长度为255，不允许为空',
    Timestamp   timestamp default CURRENT_TIMESTAMP null comment '数据记录的时间戳，默认值为当前时间',
    temp_waring int                                 null,
    C_waring    int                                 null,
    c_id        varchar(20)                         null,
    constraint mqtt_user_pk
        unique (user_name)
)
    comment '用户信息表，存储用户及其相关的传感器数据';

INSERT INTO iot.mqtt_user (user_id, user_name, password, Timestamp, temp_waring, C_waring, c_id) VALUES ('1', 'admin', '123456', '2024-07-21 10:40:53', null, 100, null);
INSERT INTO iot.mqtt_user (user_id, user_name, password, Timestamp, temp_waring, C_waring, c_id) VALUES ('422255109', 'car_23db', '123456', '2024-07-21 11:27:49', 1, 100, '52211255109');
INSERT INTO iot.mqtt_user (user_id, user_name, password, Timestamp, temp_waring, C_waring, c_id) VALUES ('422255110', 'car_23dc', '123456', '2024-07-21 10:40:53', 0, 50, '52211255110');
INSERT INTO iot.mqtt_user (user_id, user_name, password, Timestamp, temp_waring, C_waring, c_id) VALUES ('422255111', 'car_23dd', '123456', '2024-07-21 11:27:49', 0, 80, '52211255111');
