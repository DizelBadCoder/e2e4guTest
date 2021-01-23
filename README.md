# [e2e4gu] Тестовое задание
### Задача
Разработать Андроид приложение.
Приложение состоит из 2х экранов:

1. Splash screen с произвольным логотипом
2. Map screen - карта mapbox, отображает текущее положение пользователя и маркеры в радиусе 10км от местоположения пользователя.
Координаты маркеров для отображения получаются через API запрос к серверу с передачей координат пользователя.

В качестве API использовать заглушку на свое усмотрение. После смены API адреса в приложении на рабочее API приложение не должно требовать каких-либо доработок.

#### Примечания к решению

- В splash screen был установлен таймер, для эмуляции загрузки приложения, т.к. без него бы экран очень быстро показывался и переключался на основной экран.
- В качестве API был использован [My JSON Server](https://my-json-server.typicode.com/DizelBadCoder/e2e4guTest/ "My JSON Server"). Сам json находится в этом же репозитории (db.json). 

#### Новые фишки, которых не было в ТЗ

- Добавление новых маркеров.
- Визуализация зоны покрытия, по ТЗ 10км (Можно менять)

#### Скриншоты

	Чуть позже добавлю
