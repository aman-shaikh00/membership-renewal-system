

# Membership Renewal System

## Description

A Spring Boot REST API for managing member registration, membership plans, renewals, expiry tracking, membership history, and automated expiry reminder emails.

The system uses scheduled background tasks to check membership expiry and Java Mail to notify members when their memberships are approaching expiration.

---

## Features

- Member registration
- Membership registration
- Multiple membership plans
- Membership renewal
- Membership status tracking
- Membership expiry tracking
- Expiry-soon detection
- Automated expiry reminders
- Email notifications
- Membership history
- PostgreSQL persistence
- Scheduled background processing
- DTO validation
- JPA entity relationships
- Transaction management

---

## Concepts Learned

- Spring Boot REST APIs
- Spring Data JPA
- Entity relationships
- DTOs
- Jakarta Validation
- Service-layer business logic
- Scheduled Tasks
- `@Scheduled`
- Java Mail
- `JavaMailSender`
- Email notifications
- Date and time handling
- Membership lifecycle management
- PostgreSQL
- Transaction management

---

## Technologies

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- PostgreSQL
- Maven
- Lombok
- Jakarta Validation
- Spring Boot Mail

---

## Package

```text
com.membership
````

---

## Project Structure

```text
src/main/java/com/membership
│
├── controller
│   ├── MemberController.java
│   └── MembershipController.java
│
├── dto
│   ├── MemberRequest.java
│   ├── MemberResponse.java
│   ├── MembershipHistoryResponse.java
│   ├── MembershipRequest.java
│   └── MembershipResponse.java
│
├── entity
│   ├── Member.java
│   ├── Membership.java
│   └── MembershipHistory.java
│
├── enums
│   ├── MembershipPlan.java
│   └── MembershipStatus.java
│
├── repository
│   ├── MemberRepository.java
│   ├── MembershipHistoryRepository.java
│   └── MembershipRepository.java
│
├── scheduler
│   └── MembershipScheduler.java
│
├── service
│   ├── EmailService.java
│   ├── MemberService.java
│   └── MembershipService.java
│
└── MembershipRenewalSystemApplication.java
```

---

## Architecture

```text
Client
   ↓
Controller
   ↓
DTO + Validation
   ↓
Service
   ↓
Repository
   ↓
PostgreSQL
```

Scheduled processing:

```text
Spring Scheduler
       ↓
Membership Expiry Check
       ↓
Update Membership Status
       ↓
Find Expiring Memberships
       ↓
EmailService
       ↓
JavaMailSender
       ↓
Member Email
```

---

## Entity Relationships

```text
Member
   │
   │ 1:N
   ▼
Membership
   │
   │ 1:N
   ▼
MembershipHistory
```

### Member

A member contains basic member information:

* Name
* Email
* Phone

One member can have multiple membership records.

### Membership

A membership belongs to one member and contains:

* Membership plan
* Status
* Start date
* Expiry date

### Membership History

Each membership can have multiple history records.

History stores:

* Membership plan
* Start date
* Expiry date
* Renewal timestamp

---

# Membership Plans

The system supports four membership plans:

```text
MONTHLY
QUARTERLY
HALF_YEARLY
YEARLY
```

## Monthly

```text
Expiry Date = Start Date + 1 Month - 1 Day
```

## Quarterly

```text
Expiry Date = Start Date + 3 Months - 1 Day
```

## Half-Yearly

```text
Expiry Date = Start Date + 6 Months - 1 Day
```

## Yearly

```text
Expiry Date = Start Date + 1 Year - 1 Day
```

---

# Membership Status

```text
ACTIVE
EXPIRING_SOON
EXPIRED
CANCELLED
```

---

# Membership Lifecycle

Normal lifecycle:

```text
Register Member
      ↓
Register Membership
      ↓
ACTIVE
      ↓
Expiry Approaching
      ↓
EXPIRING_SOON
      ↓
Membership Expires
      ↓
EXPIRED
```

Renewal lifecycle:

```text
ACTIVE / EXPIRING_SOON / EXPIRED
              ↓
           RENEW
              ↓
            ACTIVE
              ↓
      New Expiry Date
              ↓
     Membership History
```

---

# Database

Database name:

```text
membership_db
```

Database tables:

```text
members
memberships
membership_history
```

---

# PostgreSQL Configuration

File:

```text
src/main/resources/application.properties
```

```properties
spring.application.name=membership-renewal-system

spring.datasource.url=jdbc:postgresql://localhost:5432/membership_db
spring.datasource.username=postgres
spring.datasource.password=YOUR_POSTGRES_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

server.port=8080
```

Replace:

```text
YOUR_POSTGRES_PASSWORD
```

with your local PostgreSQL password.

---

# Email Configuration

The application uses Spring Boot Mail and Gmail SMTP.

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=YOUR_EMAIL@gmail.com
spring.mail.password=YOUR_APP_PASSWORD
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

Replace:

```text
YOUR_EMAIL@gmail.com
YOUR_APP_PASSWORD
```

with your email configuration.

For Gmail, use an App Password instead of your normal Gmail account password.

---

# Scheduled Tasks

Scheduling is enabled using:

```java
@EnableScheduling
```

The expiry-checking task uses:

```java
@Scheduled(cron = "0 0 9 * * *")
```

This runs:

```text
Every day
09:00 AM
```

The scheduler performs the following operations:

```text
1. Check expired memberships
2. Mark expired memberships as EXPIRED
3. Find memberships expiring within 7 days
4. Mark them as EXPIRING_SOON
5. Send expiry reminder emails
```

---

# Expiry Reminder Logic

The system checks a seven-day reminder window.

```text
Today
  ↓
Today + 7 Days
```

For example:

```text
Today:          2026-08-20
Reminder Until: 2026-08-27
```

A membership expiring within this period can be marked:

```text
EXPIRING_SOON
```

and an expiry reminder email can be sent.

---

# Java Mail

Email functionality is implemented using:

```java
JavaMailSender
```

The application contains:

```text
EmailService.java
```

The expiry reminder email contains:

```text
Member name
Membership expiry date
Renewal reminder
```

Example subject:

```text
Membership Expiry Reminder
```

Example email:

```text
Hello Aman,

Your membership is going to expire on 2026-08-26.

Please renew your membership to continue enjoying our services.

Thank you.
```

---

# REST API Documentation

## Member APIs

### Register Member

```http
POST /api/members
```

Example:

```text
http://localhost:8080/api/members
```

Request:

```json
{
  "name": "Aman Sharma",
  "email": "aman@example.com",
  "phone": "9876543210"
}
```

Expected:

```text
201 Created
```

---

### Get All Members

```http
GET /api/members
```

Example:

```text
http://localhost:8080/api/members
```

---

### Get Member

```http
GET /api/members/{id}
```

Example:

```text
http://localhost:8080/api/members/1
```

---

# Membership APIs

## Register Membership

```http
POST /api/memberships/member/{memberId}
```

Example:

```text
http://localhost:8080/api/memberships/member/1
```

Request:

```json
{
  "plan": "MONTHLY",
  "startDate": "2026-08-20"
}
```

Expected:

```text
201 Created
```

---

## Renew Membership

```http
PUT /api/memberships/{membershipId}/renew
```

Example:

```text
http://localhost:8080/api/memberships/1/renew
```

Request:

```json
{
  "plan": "YEARLY",
  "startDate": "2026-09-20"
}
```

Expected:

```text
200 OK
```

---

## Get Membership

```http
GET /api/memberships/{membershipId}
```

Example:

```text
http://localhost:8080/api/memberships/1
```

---

## Get Member Memberships

```http
GET /api/memberships/member/{memberId}
```

Example:

```text
http://localhost:8080/api/memberships/member/1
```

---

## Get Membership History

```http
GET /api/memberships/{membershipId}/history
```

Example:

```text
http://localhost:8080/api/memberships/1/history
```

---

# Example API Response

A membership response looks like:

```json
{
  "id": 1,
  "memberId": 1,
  "memberName": "Aman Sharma",
  "memberEmail": "aman@example.com",
  "plan": "MONTHLY",
  "status": "ACTIVE",
  "startDate": "2026-08-20",
  "expiryDate": "2026-09-19"
}
```

---

# Business Rules

* Member email addresses must be unique.
* Member name is required.
* Member email must be valid.
* Member phone is required.
* Membership plan is required.
* Membership start date is required.
* Cancelled memberships cannot be renewed.
* Expired memberships can be renewed.
* Renewed memberships receive a new expiry date.
* Every membership registration is recorded in history.
* Every renewal is recorded in membership history.
* Memberships expiring within 7 days are marked `EXPIRING_SOON`.
* Memberships past their expiry date are marked `EXPIRED`.
* Scheduled processing runs every day.
* Expiry reminder emails are sent to eligible members.

---

# Validation

The project uses Jakarta Validation.

Examples:

```java
@NotBlank
```

```java
@Email
```

```java
@NotNull
```

Invalid requests return:

```text
400 Bad Request
```

---

# Transaction Management

Membership registration and renewal use transactions.

## Register Membership

```text
Register Membership
       ↓
Save Membership
       ↓
Save Membership History
```

## Renew Membership

```text
Renew Membership
       ↓
Update Membership
       ↓
Save Membership History
```

These operations are handled using transactional service methods.

---

# Repository Queries

The project uses Spring Data JPA repository methods for:

```text
Find member by email
Check duplicate email
Find memberships by member
Find memberships by status
Find memberships by expiry date
Find expired memberships
Find memberships expiring soon
Find membership history
```

Custom JPQL queries are used for expiry-related scheduled processing.

Example:

```java
@Query("""
        SELECT m
        FROM Membership m
        WHERE m.expiryDate < :today
        AND m.status <> com.membership.enums.MembershipStatus.CANCELLED
        """)
List<Membership> findExpiredMemberships(LocalDate today);
```

---

# PostgreSQL Verification

Connect to the database:

```sql
\c membership_db
```

View tables:

```sql
\dt
```

Expected:

```text
members
memberships
membership_history
```

Inspect members:

```sql
SELECT * FROM members;
```

Inspect memberships:

```sql
SELECT * FROM memberships;
```

Inspect membership history:

```sql
SELECT * FROM membership_history;
```

Check membership status:

```sql
SELECT
    id,
    member_id,
    plan,
    status,
    start_date,
    expiry_date
FROM memberships
ORDER BY expiry_date;
```

---

# Postman Testing Flow

Recommended testing order:

```text
1. Register Member
2. Get All Members
3. Get Member
4. Register Membership
5. Get Membership
6. Get Member Memberships
7. Get Membership History
8. Renew Membership
9. Verify Updated Membership
10. Verify Membership History
11. Create Expiring Membership
12. Test Expiry Status
13. Test Email Reminder
14. Test Duplicate Email
15. Test Validation
16. Test Missing Member
17. Verify PostgreSQL
```

---

# Example Complete Workflow

```text
Register Member
      ↓
Register Membership
      ↓
ACTIVE
      ↓
Scheduler Checks Expiry
      ↓
Expiry Within 7 Days?
      ↓
      YES
       ↓
EXPIRING_SOON
       ↓
Send Email Reminder
       ↓
Member Renews
       ↓
ACTIVE
       ↓
New Expiry Date
       ↓
Membership History Updated
```

---

# Running the Application

## 1. Create Database

```sql
CREATE DATABASE membership_db;
```

## 2. Configure PostgreSQL

Update:

```text
src/main/resources/application.properties
```

with your PostgreSQL credentials.

## 3. Configure Email

Set:

```text
spring.mail.username
spring.mail.password
```

## 4. Compile

Windows:

```powershell
.\mvnw.cmd clean compile
```

## 5. Run

```powershell
.\mvnw.cmd spring-boot:run
```

Application:

```text
http://localhost:8080
```

---

# Testing the Scheduled Task

The production scheduler runs automatically:

```java
@Scheduled(cron = "0 0 9 * * *")
```

For development, create a membership whose expiry date falls within the seven-day reminder window.

Example:

```text
Today: 2026-08-20
Expiry: 2026-08-26
```

The scheduler will identify the membership as expiring soon.

Before testing real email delivery, verify:

```text
SMTP Host
SMTP Port
Email Username
App Password
TLS Configuration
```

---

# Project Information

**Project:** Membership Renewal System

**Base Package:**

```text
com.membership
```

**Database:**

```text
membership_db
```

**Java Version:**

```text
17
```

**Build Tool:**

```text
Maven
```

**Database:**

```text
PostgreSQL
```

**Email:**

```text
Java Mail / Spring Mail
```

**Scheduling:**

```text
Spring Scheduler
```

---

# Key Classes

## Controllers

```text
MemberController
MembershipController
```

## Services

```text
MemberService
MembershipService
EmailService
```

## Scheduler

```text
MembershipScheduler
```

## Entities

```text
Member
Membership
MembershipHistory
```

## Repositories

```text
MemberRepository
MembershipRepository
MembershipHistoryRepository
```

---

# Resume Description

Developed a Spring Boot Membership Renewal System with PostgreSQL supporting member registration, membership plan management, renewal processing, expiry tracking, membership history, automated scheduled expiry checks, membership status management, and Java Mail expiry reminder notifications. Implemented scheduled background processing using `@Scheduled` and email delivery using `JavaMailSender`.

---

# Project Highlights

```text
✓ Member Registration
✓ Membership Registration
✓ Multiple Membership Plans
✓ Membership Renewal
✓ Expiry Tracking
✓ Status Tracking
✓ Membership History
✓ Scheduled Tasks
✓ @Scheduled
✓ Java Mail
✓ JavaMailSender
✓ Expiry Reminder
✓ Email Notifications
✓ DTO Validation
✓ JPA Relationships
✓ JPQL
✓ PostgreSQL
✓ Transactions
✓ REST APIs
```