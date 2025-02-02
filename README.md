# The Wizard's Journey

**The Wizard's Journey** — это мобильная игра, разработанная в рамках выпускной квалификационной работы.

В основе игры лежит кроссплатформенный игровой движок LibGDX. За взаимодействие объектов отвечает физический движок Box2D, а управление игровыми объектами и их поведением упрощено благодаря использованию Entity Component System (ECS) фреймворка Ashley. Для создания игровых уровней и определения свойств игровых объектов, включая физические тела Box2D, используется редактор карт Tiled.

Игровой процесс заключается в прохождении уровней, используя магические способности персонажа для размещения объектов на специальных механизмах или в областях, чувствительных к количеству предметов определенного типа. Активация этих механизмов открывает доступ к новым частям уровня. При столкновении с новой задачей игрок может вызвать окно с подсказкой в специальной зоне, где соответствующая картинка поможет понять, какое действие необходимо выполнить. Завершив все части уровня, игрок переходит к следующему. 

В игре на данный момент реализовано 2 уровня и поддерживается русский и английский языки.

Видео, демонстрирующее полный игровой процесс: [видео](https://drive.google.com/file/d/147XdvLawYahKSssbRmJ4Yu0gxOScUWXr/view?usp=sharing)

Видео из главного меню:

https://github.com/user-attachments/assets/a6001c90-e5e3-447a-85b8-94e57ed0897b

Игровые механики:

![puzzle_0](https://github.com/user-attachments/assets/517fe81c-32c8-45fd-92dc-ed587f0c4f6d)
![game_mechanics_0](https://github.com/user-attachments/assets/861e76bb-faa7-40ac-802f-e85a97f675f3)

![puzzle_1](https://github.com/user-attachments/assets/093d4e2b-8715-4f9b-88dd-028eef8733e7)
![game_mechanics_1](https://github.com/user-attachments/assets/3390f45a-508a-499f-b8bb-e7b2878e6aa8)
