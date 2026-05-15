# Student Registry API — Edge Cases & Behavior Documentation

## Overview

REST API built with Spring Boot for managing students, courses, and exams.
Base URL: `/api`

---

## Edge Cases

### 1. DELETE /api/courses/{id} — Course with students

**Scenario**: attempt to delete a course that still has enrolled students.

**Request**
```
DELETE /api/courses/1
```

**Response** — `500 Internal Server Error`
```json
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Cannot delete course 1: it still has 3 student(s)"
}
```

**Why 500?**  
The service throws `IllegalStateException`, which Spring maps to 500 by default.
To return a cleaner `409 Conflict` you would add a `@ControllerAdvice`:

```java
@ExceptionHandler(IllegalStateException.class)
public ResponseEntity<String> handleConflict(IllegalStateException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
}
```

**Correct flow**:
1. Move or unlink all students first via `PATCH /api/students/{id}/course`
2. Then delete the course — `204 No Content`

---

### 2. PUT /api/students/{id}/exams/{examId} — Invalid grade (e.g. 17)

**Scenario**: update an exam with a grade below the minimum allowed (18).

**Request**
```json
PUT /api/students/1/exams/3
{
  "subject": "Math",
  "grade": 17,
  "examDate": "2024-06-15",
  "honors": false
}
```

**Response** — `400 Bad Request`
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "message": "grade: must be greater than or equal to 18"
}
```

**Why 400?**  
The `Exam` entity has `@Min(18)` and `@Max(30)` on `grade`, and the endpoint uses `@Valid`, so Bean Validation intercepts the request before it reaches the service.

| Input | Response |
|-------|----------|
| `grade: 17` | 400 — below minimum |
| `grade: 31` | 400 — above maximum |
| `grade: 30, honors: false` | 200 — valid |
| `grade: 29, honors: true` | 200 — saved (honors flag not validated against grade here; add custom logic if needed) |

---

### 3. PATCH /api/students/{id}/course — Course not found

**Request**
```json
PATCH /api/students/1/course
{ "courseId": 999 }
```

**Response** — `500 Internal Server Error`
```
Course not found: 999
```

**Correct flow**: use a valid `courseId` from `GET /api/courses`.

---

### 4. DELETE /api/students/{id}/exams/{examId} — Exam doesn't belong to student

**Request**
```
DELETE /api/students/1/exams/99
```

**Response** — `500 Internal Server Error`
```
Exam 99 not found for student 1
```

The service searches inside `student.getExams()` — if the exam exists in the DB but belongs to a different student, the result is the same: not found.

---

### 5. GET /api/students/average-above?threshold=X — Students with no exams

Students who have not taken any exams are **excluded** from the result.

The JPQL query uses `JOIN` (not `LEFT JOIN`) on `s.exams`, so students with an empty exam list never enter the aggregation and cannot appear in the output regardless of the threshold.

| threshold | Behavior |
|-----------|----------|
| `0` | Returns all students who have at least one exam |
| `30` | Returns empty list (max grade is 30, AVG can never exceed it) |
| `18` | Returns students whose average is strictly above 18 |
| `-1` | Same as `0` in practice |

---

### 6. PUT /api/courses/{id} — Students unaffected by update

Updating a course's `name` and `department` does **not** affect enrolled students.

The service loads the existing `Course` entity first, updates only the two fields, and saves — Hibernate never touches the `students` collection.

**Verification**: call `GET /api/courses/{id}/students` before and after the PUT — the list must be identical.

---

## Orphan Removal on Exams

`Student.exams` is annotated with `orphanRemoval = true`.

**Consequence**: removing an exam from the student's list and saving the student triggers a `DELETE` on the `exam` row. The exam ceases to exist in the database — it cannot be reassigned to another student.

If you only want to unlink (keep the exam row), remove `orphanRemoval = true` and manage the `student_id` FK manually.
