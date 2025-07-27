# 🎵 Music Lesson Scheduler

Sistema de agendamento e notificação de aulas de música com envio automático de alertas via e-mail.

## 🚀 Funcionalidades

- Agendamento de aulas com tempo de antecedência configurável
- Envio automático de notificações via e-mail
- Log de notificações enviadas
- API RESTful com documentação via Swagger
- Agendamento periódico com `@Scheduled` (Spring)

## 🛠️ Tecnologias Utilizadas

- Java 17
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- JavaMailSender
- Springdoc OpenAPI (Swagger)
- MailTrap (teste de envio de e-mails)

## 📌 Endpoints da API

- `POST /api/students` – Cadastrar aluno
- `POST /api/lessons` – Agendar aula
- `GET /api/notifications` – Listar todas as notificações
- `GET /api/notifications/by-method?method=Email` – Filtrar por tipo de notificação

Para mais detalhes: [`/swagger-ui.html`](http://localhost:8080/swagger-ui.html)


## ▶️ Como rodar localmente

```bash
git clone https://github.com/jhops10/music-lesson-scheduler
cd music-lesson-scheduler
./mvnw spring-boot:run
```

Acesse:
- Swagger: [`localhost:8080/swagger-ui.html`](http://localhost:8080/swagger-ui.html)
