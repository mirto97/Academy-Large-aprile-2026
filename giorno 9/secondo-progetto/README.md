# primo-progetto — Spring Boot

Applicazione Spring Boot didattica.

---

## Requisiti

| Strumento | Versione minima |
|-----------|----------------|
| Java | 17+ |
| Maven | 3.8+ |
| Spring Boot | 3.x |

---

## Avviare l'applicazione

### 1. Clona il repository

```bash
git clone <url-repository>
cd primo-progetto
```

### 2. Avvia con Maven

```bash
./mvnw spring-boot:run
```

oppure su Windows:

```bash
mvnw.cmd spring-boot:run
```

### 3. Avvia con un profilo specifico

Il progetto supporta i profili `dev` e `prod`. Per cambiare profilo modifica la riga nel file `application.properties`:

```properties
spring.profiles.active=dev   # oppure: prod
```

In alternativa puoi passarlo come argomento da riga di comando:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### 4. Verifica che l'applicazione sia partita

L'app gira sulla porta **8081**. Apri il browser o usa curl:

```bash
curl http://localhost:8081/saluto
```

Se risponde, l'applicazione è attiva.

---

## Configurazione

Le proprietà personalizzate si trovano in `src/main/resources/application.properties`:

```properties
server.port=8081

app.name=Academy App
app.version=1.0.0
app.welcome-msg=Benvenuto nella mia applicazione Spring Boot!
```

All'avvio, la classe `AppConfig` stampa automaticamente in console tutte queste proprietà.

---

## Endpoint disponibili

### Endpoint applicazione

| Metodo | URL | Descrizione |
|--------|-----|-------------|
| `GET` | `/saluto` | Restituisce un semplice messaggio di saluto |
| `GET` | `/app-info` | Mostra nome, versione e messaggio di benvenuto dell'app letti dal file di configurazione |
| `GET` | `/configurazione-server` | Mostra la porta su cui è in ascolto il server |

#### Esempi di risposta

**`GET /saluto`**
```
Ciao dal mio corso di Spring Boot!!!
```

**`GET /app-info`**
```
App: Academy App, Versione: 1.0.0, Messaggio: Benvenuto nella mia applicazione Spring Boot!
```

**`GET /configurazione-server`**
```
La porta è: 8081
```

---

### Endpoint Spring Boot Actuator

Actuator è abilitato con tutti gli endpoint esposti (`management.endpoints.web.exposure.include=*`).

| URL | Descrizione |
|-----|-------------|
| `/actuator` | Elenco di tutti gli endpoint Actuator disponibili |
| `/actuator/health` | Stato di salute dell'applicazione |
| `/actuator/info` | Informazioni sull'applicazione (autore, ecc.) |
| `/actuator/env` | Variabili d'ambiente e proprietà attive |
| `/actuator/metrics` | Metriche di sistema (memoria, CPU, richieste HTTP, ecc.) |
| `/actuator/beans` | Lista di tutti i bean registrati nel contesto Spring |
| `/actuator/mappings` | Mappa completa di tutti gli endpoint REST registrati |

> **Nota:** in produzione è consigliabile limitare gli endpoint esposti, ad esempio:
> ```properties
> management.endpoints.web.exposure.include=health,info
> ```

---

## Struttura del progetto

```
primo-progetto/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/academy/primo_progetto/
│       │       └── com/academy/controller/
│       │           ├── SalutoController.java   # Controller REST con gli endpoint
│       │           └── AppConfig.java          # Bean che stampa la config all'avvio
│       └── resources/
│           ├── application.properties          # Configurazione principale
│           ├── application-dev.properties      # Configurazione profilo dev
│           └── application-prod.properties     # Configurazione profilo prod
├── pom.xml
└── README.md
```

---

## Autore

**Andrea** — corso Spring Boot Academy
