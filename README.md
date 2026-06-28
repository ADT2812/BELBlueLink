# BlueLink Messenger with Offline AI

## Overview
BlueLink is a Java-based desktop messaging application that supports:
- One-to-one messaging
- Group chats
- File sharing
- Offline AI features using **llama.cpp** and **Llama 3.2 GGUF**

---

## Technologies Used

- Java 21
- JavaFX
- Maven
- Socket Programming
- Git & GitHub
- llama.cpp
- Llama-3.2-3B-Instruct-Q4_K_M.gguf

---

## Project Structure

```
BlueLink
│
├── backendmain
│   ├── Server.java
│   ├── ClientHandler.java
│   ├── TestClient.java
│   └── ai
│       ├── config
│       ├── core
│       └── runtime
│           ├── bin
│           │    └── llama-cli.exe
│           └── model
│                └── Llama-3.2-3B-Instruct-Q4_K_M.gguf
│
├── frontend
│   ├── pom.xml
│   ├── Main.java
│   ├── login.fxml
│   └── home.fxml
│
└── README.md
```

---

## Requirements

- Java JDK 21
- Maven
- Git
- `llama-cli.exe`
- `Llama-3.2-3B-Instruct-Q4_K_M.gguf`

---

## AI Setup

Place the files in the following locations:

```
backendmain/
└── ai/
    └── runtime/
        ├── bin/
        │    └── llama-cli.exe
        └── model/
             └── Llama-3.2-3B-Instruct-Q4_K_M.gguf
```

To verify the model:

```bash
cd backendmain\ai\runtime\bin

.\llama-cli.exe -m ..\model\Llama-3.2-3B-Instruct-Q4_K_M.gguf -p "Hello" -n 20
```

If the model replies, the AI setup is complete.

---

## Compile the Backend

```bash
cd backendmain

javac *.java
```

---

## Run the Backend

Start the server:

```bash
java Server
```

Expected output:

```
Waiting for clients...
```

---

## Run Clients

Open a new terminal:

```bash
cd backendmain
java TestClient
```

Enter a username (e.g., Alice).

Open another terminal:

```bash
java TestClient
```

Enter another username (e.g., Bob).

You should now be able to exchange messages.

---

## AI Commands

Conversation Summary

```
/summary Bob
```

Priority Detection

```
/priority Meeting at 9 AM tomorrow
```

Message Classification

```
/classify Let's meet tomorrow
```

Smart Search

```
/search meeting
```

Reply Suggestions

```
/reply Thank you for your help
```

---

## Run the Frontend

```bash
cd frontend

mvn clean javafx:run
```

*(Frontend must be connected to the backend to enable messaging and AI features.)*

---

## Push to GitHub

Initialize Git:

```bash
git init
```

Add files:

```bash
git add .
```

Commit:

```bash
git commit -m "Initial BlueLink Messenger"
```

Add your repository:

```bash
git remote add origin <repository-url>
```

Push:

```bash
git branch -M main
git push -u origin main
```

---

## Troubleshooting

**GGUF model not found**
- Verify the model is in `backendmain/ai/runtime/model`.

**llama-cli.exe not found**
- Verify the executable is in `backendmain/ai/runtime/bin`.

**Server not starting**
- Compile first using:
  ```bash
  javac *.java
  ```

**Client not connecting**
- Ensure `java Server` is already running before starting clients.

---

## Features

- One-to-one messaging
- Group chats
- File sharing
- Conversation summary
- Priority detection
- Message classification
- Smart conversation search
- AI reply suggestions

---

## Contributors

- Frontend Development
- Backend Development
- AI Integration