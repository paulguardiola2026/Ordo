# Ordo

Ordo is an AI-assisted academic scheduling app that helps students convert syllabi and academic PDFs into structured calendar events.

## Overview

Students often have to manually read through syllabi to find exams, assignments, projects, and deadlines. Ordo reduces that manual work by allowing users to upload a PDF, extract important dates, and view them in a calendar interface.

## Features

- Upload academic PDFs
- AI-first event extraction with rule-based fallback
- Course title extraction
- Daily, weekly, and monthly calendar views
- Add, edit, and delete events
- All-day event handling for dates without specific times
- Undo last syllabus import
- Local ICS-based event persistence

## Tech Stack

### Frontend
- Kotlin
- Jetpack Compose
- Retrofit
- Android Studio

### Backend
- Python
- FastAPI
- Uvicorn
- pdfplumber
- Groq API

## Project Structure

```text
Capstone-2026/
├── app/                 # Android application
│   └── src/main/java/com/example/capstone2026/
│       ├── MainActivity.kt
│       ├── UploadSyllabusScreen.kt
│       └── network/
├── backend/             # FastAPI backend
│   ├── api.py
│   ├── syllabus_extractor.py
│   ├── llm_extractor.py
│   └── requirements.txt
└── README.md
