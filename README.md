# BlueLink Messenger with Offline AI

## Overview

BlueLink is a Java-based desktop messaging application that supports real-time messaging with integrated offline AI assistance. The application provides one-to-one messaging, group chats, file sharing, and AI-powered features using **llama.cpp** with the **Llama 3.2 GGUF** model running locally.

---

## Features

- One-to-one messaging
- Group chat support
- File sharing
- Conversation summaries
- Priority detection
- Message classification
- Reply suggestions
- Offline AI (No internet required)

---

## Technologies

- Java 21
- JavaFX
- Socket Programming
- Maven
- llama.cpp
- Llama 3.2 GGUF

---

## Project Structure

```
BlueLink
│
├── backendmain/
│   ├── Server.java
│   ├── ClientHandler.java
│   ├── MessageRouter.java
│   ├── ChatHistoryManager.java
│   ├── GroupManager.java
│   ├── UserManager.java
│   └── ai/
│       ├── config/
│       ├── core/
│       └── runtime/
│           ├── bin/
│           │    └── llama-cli.exe
│           └── model/
│                └── Llama-3.2-3B-Instruct-Q4_K_M.gguf
│
├── frontend/
│   ├── src/
│   ├── out/
│   ├── Main.java
│   ├── LoginController.java
│   ├── HomeController.java
│   ├── startClient.bat
│   └── compileFrontend.bat
│
└── README.md
```

---

## Requirements

- Java JDK 21+
- JavaFX SDK
- Maven (optional)
- llama.cpp (`llama-cli.exe`)
- Llama-3.2-3B-Instruct-Q4_K_M.gguf model

---

## AI Setup

Place the model and executable in:

```
backendmain/
└── ai/
    └── runtime/
        ├── bin/
        │    └── llama-cli.exe
        └── model/
             └── Llama-3.2-3B-Instruct-Q4_K_M.gguf
```

---

## Running the Application

### 1. Compile the Backend

```bash
cd backendmain
javac *.java
```

### 2. Start the Server

```bash
java Server
```

Expected output:

```
Server started on port 5000
Waiting for clients...
```

---

### 3. Compile the Frontend

From the project root:

```bash
compileFrontend.bat
```

or manually:

```bash
cd frontend

javac ^
--module-path "..\javafx-sdk-26.0.1\lib" ^
--add-modules javafx.controls,javafx.fxml ^
-d out ^
src\main\java\com\messenger\*.java
```

Copy resources after compilation:

```powershell
Copy-Item -Recurse -Force src\main\resources\* out\
```

---

### 4. Launch the Frontend

From the project root:

```bash
startClient.bat
```

Login using **any username** (no password is required).

Open another client window and login with a different username to start chatting.

---

## AI Features

The application supports:

- Conversation Summary
- Priority Detection
- Message Classification
- Reply Suggestions

These features run locally using the Llama 3.2 model.

---

## Troubleshooting

**Frontend opens but Home page doesn't load**

- Ensure `Home.fxml` is copied into the `out/view` folder.
- Verify `fx:controller="com.messenger.HomeController"` is correct.

**FXML LoadException**

- Check that every `onAction` method referenced in the FXML exists in the controller.
- Ensure all `fx:id` values match the controller variables.

**Client cannot connect**

- Start the backend server before launching the frontend.

**Messages not appearing**

- Ensure two clients are connected with different usernames.
- Verify the server is running and both clients are connected.

---

## Contributors

- Frontend Development
- Backend Development
- AI Integration