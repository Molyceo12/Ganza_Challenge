# Ganza_Challenge  
# Smart Agriculture Kotlin App

## What I Built

This is a simple offline-first Android app built with Kotlin using Jetpack Compose. It is designed for field officers at the Ministry of Agriculture in Rwanda to:

- Record basic farmer information such as name, national ID, crop type, and contact.
- View recorded farmer data.
- Perform basic **CRUD operations** (Create, Read, Update, Delete) on farmer data.
- Work completely **offline**, using **SQLite** for local storage—ideal for rural areas with limited internet access.

> ⚠️ **Note**: For this version, I manually used **SQLite** to manage local data and deliver a working app in time.  
> In future updates, **Room** (an ORM built on SQLite) will be adopted for better structure and maintainability.

---

## 🧱 Architecture Summary

The app follows a basic layered architecture:

- **UI Layer (Compose Screens):**  
  - `FarmerRegistrationScreen`: Used to input and submit farmer data.  
  - `Dashboard`: Displays saved data and provides edit/delete options.

- **Database Layer (SQLite):**  
  - `FarmerDatabaseHelper`: Handles database creation, insertion, deletion, and updates of farmer records.

- **State Management:**  
  - Composables use `remember` and `mutableStateOf` to manage UI state and form data in-memory.

- **Navigation:**  
  - Conditional Composables are used to switch between screens (form view and dashboard view).

---

## 🔄 Future Sync Strategy

When internet access becomes available, syncing to a remote server could be implemented as follows:

- 🔌 **Add a Repository Layer**: Introduce a repository to abstract data sources (local and remote).
- ☁️ **Remote API**: Use Retrofit or Ktor to send/receive farmer data from a backend server.
- 🔄 **Sync Mechanism**:
  - Queue unsynced records locally.
  - Use WorkManager or background services to sync automatically when internet is detected.
- 🧠 **Conflict Resolution**: Add a `last_updated` timestamp field for resolving conflicts during sync.
- ✅ **Sync Status Flags**: Mark records as `synced` or `pending`.
