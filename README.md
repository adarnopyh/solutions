# 💡 Solutions — Product Management Demo

A compact **Spring Boot 3 + HTML/Bootstrap** application that demonstrates:

- 🔐 User registration & login (HTTP Basic)
- 📦 Product management (CRUD)
- 🧾 Admin-only audit log for all changes
- 💻 Simple responsive UI (Bootstrap + vanilla JS)
- 🧠 Fully self-contained (no setup, no external DB)

---

## 🧰 Tech Stack

| Layer | Technology |
|-------|-------------|
| Backend | Spring Boot 3.x, Java 17+, Spring Security, Spring Data JPA |
| Database | H2 (in-memory) |
| Frontend | HTML5, Bootstrap 5, Vanilla JS |
| Docs | Swagger / OpenAPI 3 |

---

## 🚀 Quick Start — Reviewer Instructions

### 1️⃣ Clone & open project
```bash
git clone <repo-url>
cd <repo-folder>
```

### 2️⃣ Run the backend
You only need **Java 17+** and **Maven**.

```bash
mvn spring-boot:run
```

Wait until you see this line in console:

```
✅ Solutions API is running. Visit /swagger-ui.html for documentation.
```

> By default the app runs on **http://localhost:8083**

---

### 3️⃣ Open the web UI

| Page | URL | Description |
|------|-----|-------------|
| **Start page (Login / Register)** | [http://localhost:8083/index.html](http://localhost:8083/index.html) | Entry point — login or create a user |
| **Products page** | [http://localhost:8083/products.html](http://localhost:8083/products.html) | Product list, creation form (ADMIN), and audit history |
| **Swagger UI** | [http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html) | Explore REST API interactively |

---

## 👤 Default Users

| Username | Password | Role |
|-----------|-----------|------|
| `admin` | `admin123` | ROLE_ADMIN |
| `user` | `user123` | ROLE_USER |

✅ You can also register new users directly in the UI under the **Sign up** tab.  
Newly created users automatically get `ROLE_USER`.

---

## 🧩 Features Overview

### 🔑 Authentication
- HTTP Basic Auth handled manually in JS
- Credentials saved in `sessionStorage` (tab-only)
- Backend is fully **stateless** — no sessions or cookies

### 📦 Product Management
- `/api/products` — list all products  
- `/api/products` (POST) — create a new product (ADMIN only)
- Each product has: `title`, `quantity`, `price`, and computed `totalWithVat`

### 🧾 Audit System
- Every **CREATE**, **UPDATE**, and **DELETE** triggers an audit entry
- Stored in table `product_audit`
- Viewable only by **admins**
- Audit shows:
  - Timestamp (`performedAt`)
  - Action (`CREATE`, `UPDATE`, `DELETE`)
  - Product ID
  - Username

---

## 🧭 Step-by-Step Testing Guide

### ▶️ Step 1: Open Start Page
Go to [http://localhost:8083/index.html](http://localhost:8083/index.html)

You’ll see two forms:
- **Login** (enter `admin / admin123`)
- **Register** (create a new user)

After successful login → you’ll see:
```
Hello admin! Roles: ROLE_ADMIN. You’re signed in.
```

Click **“Go to Products”**.

---

### ▶️ Step 2: Manage Products
Now you’re on `/products.html`.

As `admin`, you can:
- View the current product list
- Add new products via “Create Product” form
- See live product table updates

As `user`, you can:
- Only view products (no “Create” section)

---

### ▶️ Step 3: View Audit History (Admin only)
Scroll down the products page.

You’ll see:
**“Audit History”**

Each action (create/update/delete) adds a new entry in the audit table:
| When | Action | Product ID | User |
|------|---------|-------------|------|
| 2025-10-15 17:00 | CREATE | 5 | admin |
| 2025-10-15 17:05 | UPDATE | 5 | admin |

This data is fetched from `/api/audit/products`.

---

### ▶️ Step 4: Try User Access
Logout, then login as:
```
user / user123
```

Now:
- You can see products
- You **cannot** add new ones
- The **Audit History** section disappears

---

### ▶️ Step 5: Explore the API
Open Swagger UI:
[http://localhost:8083/swagger-ui.html](http://localhost:8083/swagger-ui.html)

Endpoints are grouped:
- `/api/products`
- `/api/audit/products`
- `/api/register`
- `/api/me`

You can authorize inside Swagger (top-right “Authorize” button) using:
```
Username: admin
Password: admin123
```

---

## 🧮 Database Access (Optional for reviewer)

You can inspect data in the **in-memory H2 database**:

**H2 Console URL:**
[http://localhost:8083/h2-console](http://localhost:8083/h2-console)

| Setting | Value |
|----------|--------|
| JDBC URL | `jdbc:h2:mem:testdb` |
| Username | `sa` |
| Password | *(empty)* |

Tables you can check:
- `users`
- `roles`
- `users_roles`
- `products`
- `product_audit`

---

## 📚 REST API Summary

| Endpoint | Method | Description | Access |
|-----------|---------|-------------|---------|
| `/api/register` | POST | Register new user | Public |
| `/api/me` | GET | Current logged-in user info | Authenticated |
| `/api/products` | GET | List all products | USER / ADMIN |
| `/api/products` | POST | Create new product | ADMIN |
| `/api/audit/products` | GET | List audit entries | ADMIN only |

---

## ⚙️ Project Structure

```
src/main/java/com/corporate/solutions/
├── config/              # Spring Security setup
├── product/             # Product entity, service, controller
├── audit/               # Product audit logging
└── user/                # Registration and user queries

src/main/resources/static/
├── index.html           # Login / Register page
├── products.html        # Main UI (product + audit)
└── js/
    ├── auth.js
    └── auth-bridge.js
```

---

## ✅ Reviewer Checklist

- [ ] Run `mvn spring-boot:run` — app starts on port 8083  
- [ ] Visit `/index.html` — login/register works  
- [ ] Create product as admin — product saved  
- [ ] Scroll → audit history appears  
- [ ] Login as user — view-only access confirmed  
- [ ] Verify `/swagger-ui.html` shows endpoints  
- [ ] Optional: open `/h2-console` and view `product_audit` table  

---

## 💬 Summary

> A concise, full-stack demo app showing authentication, product management,  
> and automatic audit logging — all in under a few hundred lines.

**Everything runs out-of-the-box.**  
No DB setup. No config files. Just run, open, and test.

---

🧠 *Built for quick evaluation and clarity.*
