# Zoo Management System

A full-stack **Zoo Management System** designed to manage zoo operations such as animals, enclosures, staff, visitors, tickets, visit logs, excursions, events, feeding schedules with feeding records, and vet examinations with medical records.

The system is built with a **Java Spring backend**, **PostgreSQL database**, and a **React frontend**, and supports **online ticket purchasing with Stripe integration** and **role-based access control**.

System provides full management for the zoo within a website and allows regular users to get proper info about zoo, observe events and book tickets for excursion or general visit.

---

## Features

### Visitor Features

* View animals and their info
* Browse excursions and events
* Purchase tickets online using Stripe
* Get purchased ticket on email

### Admin Features

* Manage animals, enclosures, staff, excursions, events, feedings and vet examinations
* Manage ticket pricing rules
* View visit logs and statistics
* Manage feeding schedules and medical records
* Role-based access control

### Caretaker Features

* View assigned feeding schedules and mark them as done for today

### Veterinarian Features

* Manage assigned vet examination schedules
* Create medical records on examination completion

### Guide Features

* View assigned excursions

### Ticket agent Features

* Manage gates on which offline tickets can be bought
* Manage ticket pricings
* View tickets
* Add offline tickets
* Manage visit logs

### Event manager Features

* Manage excursions
* Manage events

---

## Tech Stack

### Backend

* Java
* Spring Boot
* Spring Security
* JDBC
* Flyway
* Spring Mail

### Frontend

* React
* Tailwind

### Database

* PostgreSQL

### Payments

* Stripe API

---

## Requirements

* JDK 17 or higher, Gradle
* PostgreSQL
* Node.js and npm
* Stripe account with API keys, Stripe CLI

---

## ⚙️ Setup and Run

### 1. Environment Configuration

Create a file named `env.properties` in the backend root directory and add the following configuration values:

```
DB_HOST=
DB_PORT=
DB_NAME=
DB_USER=
DB_PASSWORD=

MAIL_HOST=
MAIL_PORT=
MAIL_USERNAME=
MAIL_PASSWORD=

STRIPE_PK=
STRIPE_SK=
STRIPE_WEBHOOK_SECRET_KEY=
```

Fill in the values with your PostgreSQL credentials, mail server details, and Stripe API keys.

---

### 2. Database Setup

1. Create a PostgreSQL database manually:

   * Database name should match `DB_NAME` from `env.properties`
2. Ensure PostgreSQL is running
3. Update database connection values in `env.properties`
4. Flyway migrations will run automatically on backend startup

---

### 3. Stripe Webhook Setup (Local Testing)

#### Install Stripe CLI

Download and install the Stripe CLI, then log in:

```
stripe login
```

#### Forward Webhook Events

Start listening for Stripe events and forward them to the backend webhook endpoint:

```
stripe listen --forward-to localhost:8080/api/stripe/webhook
```

The CLI will output a **webhook secret key**.
Copy this value and paste it into:

```
STRIPE_WEBHOOK_SECRET_KEY=
```

inside `env.properties`.

---

### 4. Run Backend

Start the backend server:

```
./gradlew bootRun
```

Backend will be available at:

```
http://localhost:8080
```

---

### 5. Run Frontend

Navigate to the frontend directory and start the development server:

```
cd app
npm install
npm run dev
```

Frontend will be available at:

```
http://localhost:3000
```
or
```
http://localhost:5173
```

---

